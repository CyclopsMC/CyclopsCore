package org.cyclops.cyclopscore.modcompat;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.init.IInitListener;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.List;
import java.util.Set;

/**
 * The loader for {@link IModCompat} instances.
 * @author rubensworks
 *
 */
public class ModCompatLoader implements IInitListener {

    private static final String CONFIG_CATEGORY = "mod compat";

    protected final ModBase mod;
    protected final List<IExternalCompat> compats = Lists.newLinkedList();
    protected final Set<String> crashedcompats = Sets.newHashSet();

    public ModCompatLoader(ModBase mod) {
        this.mod = mod;
    }

    /**
     * Register a new mod compatibility.
     * Make sure to call this before any Forge initialization steps are called!
     * @param modCompat The mod compatibility
     */
    public void addModCompat(IModCompat modCompat) {
        this.compats.add(modCompat);
    }

    /**
     * Register a new api compatibility.
     * Make sure to call this before any Forge initialization steps are called!
     * @param apiCompat The api compatibility
     */
    public void addApiCompat(IApiCompat apiCompat) {
        this.compats.add(apiCompat);
    }
    
    @Override
    public void onInit(IInitListener.Step step) {
        if(step == Step.PREINIT && !compats.isEmpty()) {
            mod.getConfigHandler().addCategory(CONFIG_CATEGORY);
        }
        for(IExternalCompat compat : compats) {
            if(shouldLoadExternalCompat(compat)) {
                String id = getId(compat);
                try {
                    compat.onInit(step);
                } catch (RuntimeException e) {
                    mod.log(Level.ERROR, "The compatibility for " + id +
                            " has crashed! Report this crash clog to the mod author or try updating the conflicting mods.");
                    if(mod.getReferenceValue(ModBase.REFKEY_CRASH_ON_MODCOMPAT_CRASH)) throw e;
                    e.printStackTrace();
                    crashedcompats.add(id);
                }
            }
        }
    }

    protected String getId(IExternalCompat compat) {
        if(compat instanceof IModCompat) {
            return ((IModCompat) compat).getModID();
        }
        if(compat instanceof IApiCompat) {
            return ((IApiCompat) compat).getApiID();
        }
        return null;
    }

    /**
     * If the given compat should be loaded.
     * @param compat The mod compat.
     * @return If it should be loaded.
     */
    public boolean shouldLoadExternalCompat(IExternalCompat compat) {
        return (compat instanceof IModCompat && shouldLoadModCompat((IModCompat) compat)
                || (compat instanceof IApiCompat && shouldLoadApiCompat((IApiCompat) compat)));
    }
    
    /**
     * If the given mod compat should be loaded.
     * @param modCompat The mod compat.
     * @return If it should be loaded.
     */
    public boolean shouldLoadModCompat(IModCompat modCompat) {
    	return isModLoaded(modCompat) && isEnabled(modCompat, modCompat.getModID()) && isNotCrashed(modCompat.getModID());
    }

    /**
     * If the given api compat should be loaded.
     * @param apiCompat The api compat.
     * @return If it should be loaded.
     */
    public boolean shouldLoadApiCompat(IApiCompat apiCompat) {
        return ModAPIManager.INSTANCE.hasAPI(apiCompat.getApiID()) && isEnabled(apiCompat, apiCompat.getApiID())
                && isNotCrashed(apiCompat.getApiID());
    }
    
    private boolean isModLoaded(IModCompat modCompat) {
        return Reference.MOD_VANILLA.equals(modCompat.getModID()) || Loader.isModLoaded(modCompat.getModID());
    }
    
    private boolean isEnabled(IExternalCompat compat, String id) {
    	Configuration config = mod.getConfigHandler().getConfig();
    	Property property = config.get(CONFIG_CATEGORY, id,
                compat.isEnabled());
        property.setRequiresMcRestart(true);
        property.setComment(compat.getComment());
        boolean enabled = property.getBoolean(true);
        if(config.hasChanged()) {
        	config.save();
        }
        return enabled;
    }

    private boolean isNotCrashed(String id) {
        return !crashedcompats.contains(id);
    }
    
}
