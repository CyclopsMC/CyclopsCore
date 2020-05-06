package org.cyclops.cyclopscore;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacityConfig;
import org.cyclops.cyclopscore.client.particle.ParticleBlurConfig;
import org.cyclops.cyclopscore.command.CommandDebug;
import org.cyclops.cyclopscore.command.CommandDumpRegistries;
import org.cyclops.cyclopscore.command.CommandIgnite;
import org.cyclops.cyclopscore.command.CommandInfoBookTest;
import org.cyclops.cyclopscore.command.CommandReloadResources;
import org.cyclops.cyclopscore.command.argument.ArgumentSerializerMod;
import org.cyclops.cyclopscore.command.argument.ArgumentTypeConfigProperty;
import org.cyclops.cyclopscore.command.argument.ArgumentTypeDebugPacket;
import org.cyclops.cyclopscore.command.argument.ArgumentTypeEnum;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.infobook.IInfoBookRegistry;
import org.cyclops.cyclopscore.infobook.InfoBookRegistry;
import org.cyclops.cyclopscore.infobook.test.ContainerInfoBookTestConfig;
import org.cyclops.cyclopscore.infobook.test.InfoBookTest;
import org.cyclops.cyclopscore.ingredient.recipe.IRecipeInputOutputDefinitionRegistry;
import org.cyclops.cyclopscore.ingredient.recipe.RecipeInputOutputDefinitionHandlers;
import org.cyclops.cyclopscore.ingredient.recipe.RecipeInputOutputDefinitionRegistry;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.metadata.IRegistryExportableRegistry;
import org.cyclops.cyclopscore.metadata.RegistryExportableRegistry;
import org.cyclops.cyclopscore.metadata.RegistryExportables;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.modcompat.baubles.ModCompatBaubles;
import org.cyclops.cyclopscore.proxy.ClientProxy;
import org.cyclops.cyclopscore.proxy.CommonProxy;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.cyclops.cyclopscore.tracking.Analytics;
import org.cyclops.cyclopscore.tracking.ImportantUsers;
import org.cyclops.cyclopscore.tracking.Versions;

/**
 * The main mod class of CyclopsCore.
 * @author rubensworks
 *
 */
@Mod(Reference.MOD_ID)
public class CyclopsCore extends ModBaseVersionable<CyclopsCore> {

    /**
     * The unique instance of this mod.
     */
    public static CyclopsCore _instance;

    private boolean loaded = false;

    public CyclopsCore() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);

        getRegistryManager().addRegistry(IInfoBookRegistry.class, new InfoBookRegistry());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected IClientProxy constructClientProxy() {
        return new ClientProxy();
    }

    @Override
    @OnlyIn(Dist.DEDICATED_SERVER)
    protected ICommonProxy constructCommonProxy() {
        return new CommonProxy();
    }

    @Override
    protected ModCompatLoader constructModCompatLoader() {
        ModCompatLoader modCompatLoader = super.constructModCompatLoader();

        modCompatLoader.addModCompat(new ModCompatBaubles());

        return modCompatLoader;
    }

    @Override
    protected LiteralArgumentBuilder<CommandSource> constructBaseCommand() {
        LiteralArgumentBuilder<CommandSource> root = super.constructBaseCommand();

        root.then(CommandIgnite.make());
        root.then(CommandDebug.make());
        root.then(CommandReloadResources.make());
        root.then(CommandDumpRegistries.make());
        root.then(CommandInfoBookTest.make());

        return root;
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
        // Registries
        getRegistryManager().addRegistry(IRecipeInputOutputDefinitionRegistry.class, new RecipeInputOutputDefinitionRegistry());
        getRegistryManager().addRegistry(IRegistryExportableRegistry.class, RegistryExportableRegistry.getInstance());

        super.setup(event);

        // Populate registries
        Advancements.load();
        if (ModList.get().isLoaded(Reference.MOD_COMMONCAPABILITIES)) {
            RecipeInputOutputDefinitionHandlers.load();
        }
        RegistryExportables.load();

        // Register argument types
        ArgumentTypes.register(Reference.MOD_ID + ":" + "enum",
                (Class<ArgumentTypeEnum<?>>) (Class) ArgumentTypeEnum.class,
                new ArgumentTypeEnum.Serializer());
        ArgumentTypes.register(Reference.MOD_ID + ":" + "config_property",
                ArgumentTypeConfigProperty.class, new ArgumentSerializerMod<>(ArgumentTypeConfigProperty::new,
                        ArgumentTypeConfigProperty::getMod));
        ArgumentTypes.register(Reference.MOD_ID + ":" + "debug_packet",
                ArgumentTypeDebugPacket.class,
                new ArgumentSerializer<>(() -> ArgumentTypeDebugPacket.INSTANCE));

        getRegistryManager().getRegistry(IInfoBookRegistry.class).registerInfoBook(
                InfoBookTest.getInstance(), "/data/" + Reference.MOD_ID + "/info/test.xml");
    }

    @Override
    protected void onServerStarting(FMLServerStartingEvent event) {
        super.onServerStarting(event);

        // Handle metadata
        Analytics.sendAll();
        Versions.checkAll();
        ImportantUsers.checkAll();
    }

    @Override
    public ItemGroup constructDefaultItemGroup() {
        return null; // We don't need a creative tab for this core mod.
    }

    @Override
    public void onConfigsRegister(ConfigHandler configHandler) {
        configHandler.addConfigurable(new GeneralConfig());

        // Capabilities
        configHandler.addConfigurable(new FluidHandlerItemCapacityConfig());

        // Particles
        configHandler.addConfigurable(new ParticleBlurConfig());

        // Containers
        configHandler.addConfigurable(new ContainerInfoBookTestConfig());
    }

    private void loadComplete(FMLLoadCompleteEvent event) {
        this.loaded = true;
    }

    /**
     * @return If this mod has been fully loaded. (The {@link FMLLoadCompleteEvent} has been called)
     */
    public boolean isLoaded() {
        return loaded;
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
