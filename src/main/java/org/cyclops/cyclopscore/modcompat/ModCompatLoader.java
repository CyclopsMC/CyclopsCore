package org.cyclops.cyclopscore.modcompat;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraftforge.fml.ModList;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.List;
import java.util.Set;

/**
 * The loader for {@link IModCompat} instances.
 * @author rubensworks
 *
 */
public class ModCompatLoader {

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
    	return isModLoaded(modCompat) && isNotCrashed(modCompat.getModId());
    }

    private boolean isModLoaded(IModCompat modCompat) {
        return Reference.MOD_VANILLA.equals(modCompat.getModId()) || ModList.get().isLoaded(modCompat.getModId());
    }

    private boolean isNotCrashed(String id) {
        return !crashedcompats.contains(id);
    }

}
