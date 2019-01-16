package org.cyclops.cyclopscore;

import com.google.common.collect.Maps;
import net.minecraft.command.ICommand;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacityConfig;
import org.cyclops.cyclopscore.command.CommandDebug;
import org.cyclops.cyclopscore.command.CommandIgnite;
import org.cyclops.cyclopscore.command.CommandMod;
import org.cyclops.cyclopscore.command.CommandRecursion;
import org.cyclops.cyclopscore.command.CommandReloadResources;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.ingredient.recipe.IRecipeInputOutputDefinitionRegistry;
import org.cyclops.cyclopscore.ingredient.recipe.RecipeInputOutputDefinitionHandlers;
import org.cyclops.cyclopscore.ingredient.recipe.RecipeInputOutputDefinitionRegistry;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.modcompat.versionchecker.VersionCheckerModCompat;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.cyclops.cyclopscore.tracking.Analytics;
import org.cyclops.cyclopscore.tracking.ImportantUsers;
import org.cyclops.cyclopscore.tracking.Versions;

import java.util.Map;

/**
 * The main mod class of CyclopsCore.
 * @author rubensworks
 *
 */
@Mod(   modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        useMetadata = true,
        version = Reference.MOD_VERSION,
        dependencies = Reference.MOD_DEPENDENCIES,
        guiFactory = "org.cyclops.cyclopscore.GuiConfigOverview$ExtendedConfigGuiFactory",
        certificateFingerprint = Reference.MOD_FINGERPRINT
)
public class CyclopsCore extends ModBaseVersionable {

    /**
     * The proxy of this mod, depending on 'side' a different proxy will be inside this field.
     * @see net.minecraftforge.fml.common.SidedProxy
     */
    @SidedProxy(clientSide = "org.cyclops.cyclopscore.proxy.ClientProxy", serverSide = "org.cyclops.cyclopscore.proxy.CommonProxy")
    public static ICommonProxy proxy;

    /**
     * The unique instance of this mod.
     */
    @Mod.Instance(value = Reference.MOD_ID)
    public static CyclopsCore _instance;

    public CyclopsCore() {
        super(Reference.MOD_ID, Reference.MOD_NAME, Reference.MOD_VERSION);
        putGenericReference(REFKEY_MOD_VERSION, Reference.MOD_VERSION);
        FluidRegistry.enableUniversalBucket();
    }

    @Override
    protected void loadModCompats(ModCompatLoader modCompatLoader) {
        modCompatLoader.addModCompat(new VersionCheckerModCompat());
    }

    @Override
    protected RecipeHandler constructRecipeHandler() {
        return null;
    }

    @Override
    protected ICommand constructBaseCommand() {
        Map<String, ICommand> commands = Maps.newHashMap();
        commands.put(CommandRecursion.NAME, new CommandRecursion(this));
        commands.put(CommandIgnite.NAME, new CommandIgnite(this));
        commands.put(CommandDebug.NAME, new CommandDebug(this));
        commands.put(CommandReloadResources.NAME, new CommandReloadResources(this));
        CommandMod command =  new CommandMod(this, commands);
        command.addAlias("cyclops");
        return command;
    }

    @Mod.EventHandler
    @Override
    public final void preInit(FMLPreInitializationEvent event) {
        // Registries
        getRegistryManager().addRegistry(IRecipeInputOutputDefinitionRegistry.class, new RecipeInputOutputDefinitionRegistry());

        super.preInit(event);
        Advancements.load();
        if (Loader.isModLoaded(Reference.MOD_COMMONCAPABILITIES)) {
            RecipeInputOutputDefinitionHandlers.load();
        }
    }

    @Mod.EventHandler
    @Override
    public final void init(FMLInitializationEvent event) {
        Analytics.sendAll();
        Versions.checkAll();
        ImportantUsers.checkAll();
        super.init(event);
    }

    @Mod.EventHandler
    @Override
    public final void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Mod.EventHandler
    @Override
    public void onServerStarting(FMLServerStartingEvent event) {
        super.onServerStarting(event);
    }

    @Override
    public CreativeTabs constructDefaultCreativeTab() {
        return null; // We don't need a creative tab for this core mod.
    }

    @Override
    public ICommonProxy getProxy() {
        return proxy;
    }

    @Override
    public void onGeneralConfigsRegister(ConfigHandler configHandler) {
        configHandler.add(new GeneralConfig());
    }

    @Override
    public void onMainConfigsRegister(ConfigHandler configHandler) {
        super.onMainConfigsRegister(configHandler);
        configHandler.add(new FluidHandlerItemCapacityConfig());
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
