package org.cyclops.cyclopscore;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.init.DirectObjectReference;
import org.cyclops.cyclopscore.init.ItemCreativeTab;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.ICommonProxy;

/**
 * The main mod class of CyclopsCore.
 * @author rubensworks
 *
 */
@Mod(modid = Reference.MOD_ID,
     name = Reference.MOD_NAME,
     useMetadata = true,
     version = Reference.MOD_VERSION,
     dependencies = Reference.MOD_DEPENDENCIES
)
public class CyclopsCore extends ModBase {

    /**
     * The unique instance of this mod.
     */
    @Mod.Instance(value = Reference.MOD_ID)
    public static CyclopsCore _instance;

    public CyclopsCore() {
        super(Reference.MOD_ID, Reference.MOD_NAME);
    }

    @Mod.EventHandler
    @Override
    public final void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Mod.EventHandler
    @Override
    public final void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Mod.EventHandler
    @Override
    public final void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public CreativeTabs constructDefaultCreativeTab() {
        return new ItemCreativeTab(this, new DirectObjectReference<>(Items.diamond)); // TODO: temp
    }

    @Override
    public ICommonProxy getProxy() {
        return null;
    }

    /**
     * Log a new info message for this mod.
     * @param message The message to show.
     */
    public static void clog(String message) {
        CyclopsCore._instance.log(Level.INFO, message);
    }

    /**
     * Log a new message of the given level for this mod.
     * @param level The level in which the message must be shown.
     * @param message The message to show.
     */
    public static void clog(Level level, String message) {
        CyclopsCore._instance.log(level, message);
    }

}