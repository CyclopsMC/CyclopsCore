package org.cyclops.cyclopscore.modcompat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.config.IConfigInitializer;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The loader for {@link IModCompat} instances.
 * @author rubensworks
 *
 */
public class ModCompatLoader implements IConfigInitializer {

    private static final String CONFIG_CATEGORY = "mod compat";

    private final Map<String, ForgeConfigSpec.ConfigValue<Boolean>> propertiesEnabled = Maps.newIdentityHashMap();

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
        if(shouldLoadExternalCompat(modCompat)) {
            modCompat.createInitializer().initialize();
        }
    }

    protected String getId(IExternalCompat compat) {
        if(compat instanceof IModCompat) {
            return ((IModCompat) compat).getModId();
        }
        return null;
    }

    /**
     * If the given compat should be loaded.
     * @param compat The mod compat.
     * @return If it should be loaded.
     */
    public boolean shouldLoadExternalCompat(IExternalCompat compat) {
        return (compat instanceof IModCompat && shouldLoadModCompat((IModCompat) compat));
    }
    
    /**
     * If the given mod compat should be loaded.
     * @param modCompat The mod compat.
     * @return If it should be loaded.
     */
    public boolean shouldLoadModCompat(IModCompat modCompat) {
    	return isModLoaded(modCompat) && isEnabled(modCompat) && isNotCrashed(modCompat.getModId());
    }
    
    private boolean isModLoaded(IModCompat modCompat) {
        return Reference.MOD_VANILLA.equals(modCompat.getModId()) || ModList.get().isLoaded(modCompat.getModId());
    }
    
    private boolean isEnabled(IExternalCompat compat) {
        ForgeConfigSpec.ConfigValue<Boolean> property = propertiesEnabled.get(compat.getId());
        return property != null && property.get();
    }

    private boolean isNotCrashed(String id) {
        return !crashedcompats.contains(id);
    }

    @Override
    public void initializeConfig(Map<ModConfig.Type, ForgeConfigSpec.Builder> configBuilders) {
        ForgeConfigSpec.Builder configBuilder = configBuilders.get(ModConfig.Type.SERVER);
        if (configBuilder == null) {
            configBuilder = new ForgeConfigSpec.Builder();
            configBuilders.put(ModConfig.Type.SERVER, configBuilder);
        }
        for (IExternalCompat compat : compats) {
            ForgeConfigSpec.ConfigValue<Boolean> configProperty = configBuilder
                    .comment(compat.getComment())
                    .translation("config." + mod.getModId() + "." + compat.getId().replaceAll("\\s", ""))
                    .define(compat.getId(), compat.isEnabledDefault());
            propertiesEnabled.put(compat.getId(), configProperty);
        }
    }
}
