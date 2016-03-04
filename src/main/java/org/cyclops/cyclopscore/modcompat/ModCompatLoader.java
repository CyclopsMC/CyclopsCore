package org.cyclops.cyclopscore.modcompat;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.init.IInitListener;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The loader for {@link IModCompat} instances.
 * @author rubensworks
 *
 */
public class ModCompatLoader implements IInitListener {

    private static final String CONFIG_CATEGORY = "mod compat";

    protected final ModBase mod;
    protected final List<IModCompat> modCompats = Lists.newLinkedList();
    protected final Set<String> crashedModcompats = Sets.newHashSet();
    public static final Multimap<Class<? extends ICapabilityProvider>,
            Pair<ICapabilityCompat.ICapabilityReference<?>, ICapabilityCompat<? extends ICapabilityProvider>>>
            capabilityCompats = HashMultimap.create();

    public ModCompatLoader(ModBase mod) {
        this.mod = mod;
    }

    /**
     * Register a new mod compatibility.
     * Make sure to call this before any Forge initialization steps are called!
     * @param modCompat The mod compatibility
     */
    public void addModCompat(IModCompat modCompat) {
        this.modCompats.add(modCompat);
    }

    /**
     * Register a new capability compatibility.
     * @param providerClazz The capability provider class.
     * @param capabilityReference A reference to the capability.
     * @param capabilityCompat The compatibility instance, nothing in this will be called unless the capability is present.
     * @param <P> The capability provider type.
     * @param <C> The capability.
     */
    public <P extends ICapabilityProvider, C> void addCapabilityCompat(
            Class<P> providerClazz, ICapabilityCompat.ICapabilityReference<C> capabilityReference,
            ICapabilityCompat<P> capabilityCompat) {
        capabilityCompats.put(providerClazz,
                Pair.<ICapabilityCompat.ICapabilityReference<?>, ICapabilityCompat<? extends ICapabilityProvider>>
                        of(capabilityReference, capabilityCompat));
    }
    
    @Override
    public void onInit(IInitListener.Step step) {
        if(step == Step.PREINIT && !modCompats.isEmpty()) {
            mod.getConfigHandler().addCategory(CONFIG_CATEGORY);
        }
        for(IModCompat modCompat : modCompats) {
            if(shouldLoadModCompat(modCompat)) {
                try {
                    modCompat.onInit(step);
                } catch (RuntimeException e) {
                    mod.log(Level.ERROR, "The mod compatibility for " + modCompat.getModID() +
                            " has crashed! Report this crash clog to the mod author or try updating the conflicting mods.");
                    if(mod.getReferenceValue(ModBase.REFKEY_CRASH_ON_MODCOMPAT_CRASH)) throw e;
                    e.printStackTrace();
                    crashedModcompats.add(modCompat.getModID());
                }
            }
        }
    }
    
    /**
     * If the given mod compat should be loaded.
     * @param modCompat The mod compat.
     * @return If it should be loaded.
     */
    public boolean shouldLoadModCompat(IModCompat modCompat) {
    	return isModLoaded(modCompat) && isModEnabled(modCompat) && isModNotCrashed(modCompat);
    }
    
    private boolean isModLoaded(IModCompat modCompat) {
        return Reference.MOD_VANILLA.equals(modCompat.getModID()) || Loader.isModLoaded(modCompat.getModID());
    }
    
    private boolean isModEnabled(IModCompat modCompat) {
    	Configuration config = mod.getConfigHandler().getConfig();
    	Property property = config.get(CONFIG_CATEGORY, modCompat.getModID(),
    			modCompat.isEnabled());
        property.setRequiresMcRestart(true);
        property.comment = modCompat.getComment();
        boolean enabled = property.getBoolean(true);
        if(config.hasChanged()) {
        	config.save();
        }
        return enabled;
    }

    private boolean isModNotCrashed(IModCompat modCompat) {
        return !crashedModcompats.contains(modCompat.getModID());
    }

    protected static void attachCapability(ICapabilityCompat<ICapabilityProvider> compat, ICapabilityProvider provider) {
        compat.attach(provider);
    }

    public static void attachCapability(ICapabilityProvider capabilityProvider) {
        for (Map.Entry<Class<? extends ICapabilityProvider>, Pair<ICapabilityCompat.ICapabilityReference<?>,
                ICapabilityCompat<? extends ICapabilityProvider>>> entry : capabilityCompats.entries()) {
            if(entry.getValue().getLeft().getCapability() != null && entry.getKey().isInstance(capabilityProvider)) {
                attachCapability((ICapabilityCompat<ICapabilityProvider>) entry.getValue().getRight(),
                        capabilityProvider);
            }
        }
    }
    
}
