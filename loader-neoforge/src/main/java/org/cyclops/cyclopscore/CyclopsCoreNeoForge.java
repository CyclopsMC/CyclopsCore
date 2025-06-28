package org.cyclops.cyclopscore;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.advancement.criterion.*;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacityConfig;
import org.cyclops.cyclopscore.client.particle.ParticleBlurConfig;
import org.cyclops.cyclopscore.client.particle.ParticleDropColoredConfig;
import org.cyclops.cyclopscore.command.*;
import org.cyclops.cyclopscore.command.argument.ArgumentTypeConfigPropertyConfig;
import org.cyclops.cyclopscore.command.argument.ArgumentTypeDebugPacketConfig;
import org.cyclops.cyclopscore.command.argument.ArgumentTypeEnumConfig;
import org.cyclops.cyclopscore.component.DataComponentCapacityConfig;
import org.cyclops.cyclopscore.component.DataComponentEnergyStorageConfig;
import org.cyclops.cyclopscore.component.DataComponentFluidContentConfig;
import org.cyclops.cyclopscore.component.DataComponentInventoryConfig;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.gametest.StartupTestNeoForge;
import org.cyclops.cyclopscore.helper.CyclopsCoreInstance;
import org.cyclops.cyclopscore.infobook.IInfoBookRegistry;
import org.cyclops.cyclopscore.infobook.InfoBookRegistry;
import org.cyclops.cyclopscore.infobook.test.ContainerInfoBookTestConfig;
import org.cyclops.cyclopscore.infobook.test.InfoBookTest;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.inventory.IRegistryInventoryLocation;
import org.cyclops.cyclopscore.inventory.RegistryInventoryLocation;
import org.cyclops.cyclopscore.loot.modifier.LootModifierInjectItemConfigNeoForge;
import org.cyclops.cyclopscore.metadata.IRegistryExportableRegistry;
import org.cyclops.cyclopscore.metadata.RegistryExportableRegistry;
import org.cyclops.cyclopscore.metadata.RegistryExportables;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.modcompat.curios.ModCompatCurios;
import org.cyclops.cyclopscore.network.PacketCodecsNeoForge;
import org.cyclops.cyclopscore.persist.nbt.NBTClassTypesNeoForge;
import org.cyclops.cyclopscore.proxy.ClientProxyNeoForge;
import org.cyclops.cyclopscore.proxy.CommonProxyNeoForge;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.cyclops.cyclopscore.tracking.ImportantUsers;

/**
 * The main mod class of CyclopsCore.
 * @author rubensworks
 *
 */
@Mod(Reference.MOD_ID)
public class CyclopsCoreNeoForge extends ModBaseNeoForge<CyclopsCoreNeoForge> {

    /**
     * The unique instance of this mod.
     */
    public static CyclopsCoreNeoForge _instance;

    private boolean loaded = false;

    public CyclopsCoreNeoForge(IEventBus modEventBus) {
        super(Reference.MOD_ID, (instance) -> {
            _instance = instance;
            CyclopsCoreInstance.MOD = instance;
        }, modEventBus);
        modEventBus.addListener(this::loadComplete);
        modEventBus.addListener(new StartupTestNeoForge()::register);

        // Registries
        getRegistryManager().addRegistry(IRegistryExportableRegistry.class, RegistryExportableRegistry.getInstance());
        getRegistryManager().addRegistry(IInfoBookRegistry.class, new InfoBookRegistry());
        getRegistryManager().addRegistry(IRegistryInventoryLocation.class, RegistryInventoryLocation.getInstance());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected IClientProxy constructClientProxy() {
        return new ClientProxyNeoForge();
    }

    @Override
    @OnlyIn(Dist.DEDICATED_SERVER)
    protected ICommonProxy constructCommonProxy() {
        return new CommonProxyNeoForge();
    }

    @Override
    protected ModCompatLoader constructModCompatLoader() {
        ModCompatLoader modCompatLoader = super.constructModCompatLoader();

        modCompatLoader.addModCompat(new ModCompatCurios());

        return modCompatLoader;
    }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> constructBaseCommand(Commands.CommandSelection selection, CommandBuildContext context) {
        LiteralArgumentBuilder<CommandSourceStack> root = super.constructBaseCommand(selection, context);

        root.then(CommandIgnite.make());
        root.then(CommandDebug.make());
        root.then(CommandReloadResources.make());
        root.then(CommandDumpRegistries.make());
        root.then(CommandInfoBookTest.make());

        return root;
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
        super.setup(event);

        // Load core stuff
        NBTClassTypesNeoForge.load();
        PacketCodecsNeoForge.load();

        // Populate registries
        RegistryExportables.load();

        getRegistryManager().getRegistry(IInfoBookRegistry.class).registerInfoBook(
                InfoBookTest.getInstance(), "/data/" + Reference.MOD_ID + "/info/test.xml");

        // Load others
        new GuiContainerOpenTriggerEventHooksNeoForge();
        new ModItemObtainedTriggerEventHooksNeoForge();
        new ItemCraftedTriggerTriggerEventHooksNeoForge();
    }

    @Override
    protected void onServerStarting(ServerStartingEvent event) {
        super.onServerStarting(event);

        // Handle metadata
        ImportantUsers.checkAll();
    }

    @Override
    protected boolean hasDefaultCreativeModeTab() {
        return false;
    }

    @Override
    public void onConfigsRegister(ConfigHandlerCommon configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig(this));

        // Capabilities
        configHandler.addConfigurable(new FluidHandlerItemCapacityConfig());

        // Particles
        configHandler.addConfigurable(new ParticleBlurConfig<>(this));
        configHandler.addConfigurable(new ParticleDropColoredConfig<>(this));

        // Containers
        configHandler.addConfigurable(new ContainerInfoBookTestConfig<>(this));

        // Argument types
        configHandler.addConfigurable(new ArgumentTypeConfigPropertyConfig<>(this));
        configHandler.addConfigurable(new ArgumentTypeDebugPacketConfig<>(this));
        configHandler.addConfigurable(new ArgumentTypeEnumConfig<>(this));

        // Loot modifiers
        configHandler.addConfigurable(new LootModifierInjectItemConfigNeoForge());

        // Triggers
        configHandler.addConfigurable(new GuiContainerOpenTriggerConfig<>(this));
        configHandler.addConfigurable(new ItemCraftedTriggerConfig<>(this));
        configHandler.addConfigurable(new ModItemObtainedTriggerConfig<>(this));

        // Data components
        configHandler.addConfigurable(new DataComponentCapacityConfig(this));
        configHandler.addConfigurable(new DataComponentEnergyStorageConfig(this));
        configHandler.addConfigurable(new DataComponentFluidContentConfig());
        configHandler.addConfigurable(new DataComponentInventoryConfig());
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
        CyclopsCoreNeoForge._instance.log(Level.INFO, message);
    }

    /**
     * Log a new message of the given level for this mod.
     * @param level The level in which the message must be shown.
     * @param message The message to show.
     */
    public static void clog(Level level, String message) {
        CyclopsCoreNeoForge._instance.log(level, message);
    }

}
