package org.cyclops.cyclopscore.modcompat;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.List;
import java.util.Set;

/**
 * The loader for {@link IModCompat} instances.
 * @author rubensworks
 *
 */
public class ModCompatLoader {

    private static final String CONFIG_CATEGORY = "mod compat";

    protected final IModBase mod;
    protected final List<IExternalCompat> compats = Lists.newLinkedList();
    protected final Set<String> crashedcompats = Sets.newHashSet();

    public ModCompatLoader(IModBase mod) {
        this.mod = mod;
    }

    /**
     * Register a new mod compatibility.
     * Make sure to call this before any Forge initialization steps are called!
     * @param modCompat The mod compatibility
     */
    public void addModCompat(IModCompat modCompat) {
        if(shouldLoadExternalCompat(modCompat)) {
            modCompat.createInitializer().initialize(this.mod);
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
        return isModLoaded(modCompat) && isNotCrashed(modCompat.getModId());
    }

    private boolean isModLoaded(IModCompat modCompat) {
        return mod.getModHelpers().getMinecraftHelpers().isModLoaded(modCompat.getModId());
    }

    private boolean isNotCrashed(String id) {
        return !crashedcompats.contains(id);
    }

}
