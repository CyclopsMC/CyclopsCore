package org.cyclops.cyclopscore.client.gui;

import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FileUtil;
import net.minecraft.util.Util;
import net.minecraft.world.Difficulty;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.*;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.helper.CyclopsCoreInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class GuiMainMenuExtensionDevWorld {

    private static final String WORLD_NAME_PREFIX = "cyclops-dev";

    public static void onMainMenuInit(Minecraft minecraft, Screen screen) {
        if (screen instanceof TitleScreen) {
            Button buttonBuilt = Button.builder(Component.translatable("general.cyclopscore.dev_world"), (button) -> {
                        Minecraft mc = Minecraft.getInstance();

                        // Open the last played dev world
                        // If shift is held, create a new world.
                        if (!Minecraft.getInstance().hasShiftDown()) {
                            LevelSummary devWorldSummary = null;
                            mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                            try {
                                List<LevelSummary> levelList = mc.getLevelSource().loadLevelSummaries(mc.getLevelSource().findLevelCandidates())
                                        .exceptionally((p_233202_) -> {
                                            mc.delayCrash(CrashReport.forThrowable(p_233202_, "Couldn't load level list"));
                                            return List.of();
                                        })
                                        .get(5000, TimeUnit.MILLISECONDS);
                                for (LevelSummary worldSummary : levelList) {
                                    if (worldSummary.getLevelName().equals(WORLD_NAME_PREFIX)) {
                                        if (devWorldSummary == null
                                                || devWorldSummary.getLastPlayed() < worldSummary.getLastPlayed()) {
                                            devWorldSummary = worldSummary;
                                        }
                                    }
                                }

                            } catch (InterruptedException | ExecutionException | TimeoutException | LevelStorageException e) {
                                CyclopsCoreInstance.MOD.getLoggerHelper().log(Level.ERROR, "Couldn't load level list" + e.getMessage());
                                mc.gui.setScreen(new ErrorScreen(Component.translatable("selectWorld.unable_to_load"), Component.literal(e.getMessage())));
                            }

                            if (devWorldSummary != null && mc.getLevelSource().levelExists(devWorldSummary.getLevelId())) {
                                mc.setScreenAndShow(new GenericMessageScreen(Component.translatable("selectWorld.data_read")));
                                mc.createWorldOpenFlows().openWorld(devWorldSummary.getLevelId(), () -> minecraft.gui.setScreen(screen));
                                return;
                            }
                        }

                        // Set rules
                        GameRules gameRules = new GameRules(FeatureFlags.DEFAULT_FLAGS);
                        gameRules.set(GameRules.ADVANCE_TIME, false, null);
                        gameRules.set(GameRules.ADVANCE_WEATHER, false, null);
                        gameRules.set(GameRules.IMMEDIATE_RESPAWN, true, null);
                        gameRules.set(GameRules.SPAWN_PATROLS, false, null);
                        gameRules.set(GameRules.SPAWN_WANDERING_TRADERS, false, null);
                        WorldDataConfiguration worlddataconfiguration = new WorldDataConfiguration(new DataPackConfig(new ArrayList<>(Minecraft.getInstance().getResourcePackRepository().getAvailableIds()), List.of()), FeatureFlags.REGISTRY.allFlags());
                        LevelSettings worldsettings = new LevelSettings(WORLD_NAME_PREFIX, GameType.CREATIVE,
                                new LevelSettings.DifficultySettings(Difficulty.PEACEFUL, false, false), true, worlddataconfiguration);

                        // Create generator settings and world options, based on GameTestServer
                        Function<HolderLookup.Provider, WorldDimensions> generatorSettings = registryAccess -> registryAccess
                                .lookupOrThrow(Registries.WORLD_PRESET).getOrThrow(WorldPresets.FLAT).value()
                                .createWorldDimensions();
                        long seed = new Random().nextLong();
                        WorldOptions worldOptions = new WorldOptions(seed, false, false);

                        // Determine a save name
                        String saveName;
                        try {
                            saveName = FileUtil.findAvailableName(mc.getLevelSource().getBaseDir(), WORLD_NAME_PREFIX, "");
                        } catch (IOException e) {
                            saveName = "World";
                        }

                        // Create the world with custom game rules via doWorldLoad
                        mc.setScreenAndShow(new GenericMessageScreen(Component.translatable("selectWorld.data_read")));
                        LevelStorageSource.LevelStorageAccess levelSourceAccess;
                        try {
                            levelSourceAccess = mc.getLevelSource().validateAndCreateAccess(saveName);
                        } catch (Exception e) {
                            CyclopsCoreInstance.MOD.getLoggerHelper().log(Level.ERROR, "Failed to access world storage: " + e.getMessage());
                            mc.gui.setScreen(screen);
                            return;
                        }
                        PackRepository packRepository = ServerPacksSource.createPackRepository(levelSourceAccess);
                        try {
                            WorldLoader.PackConfig packConfig = new WorldLoader.PackConfig(packRepository, worldsettings.dataConfiguration(), false, false);
                            WorldLoader.InitConfig initConfig = new WorldLoader.InitConfig(packConfig, Commands.CommandSelection.INTEGRATED, LevelBasedPermissionSet.GAMEMASTER);
                            LevelSettings worldsettingsFinal = worldsettings;
                            WorldOptions worldOptionsFinal = worldOptions;
                            Function<HolderLookup.Provider, WorldDimensions> generatorSettingsFinal = generatorSettings;
                            CompletableFuture<WorldStem> resourceLoad = WorldLoader.load(initConfig, context -> {
                                WorldDimensions dimensions = generatorSettingsFinal.apply(context.datapackWorldgen());
                                WorldDimensions.Complete completeDimensions = dimensions.bake(context.datapackDimensions().lookupOrThrow(Registries.LEVEL_STEM));
                                return new WorldLoader.DataLoadOutput<>(
                                        new LevelDataAndDimensions.WorldDataAndGenSettings(
                                                new PrimaryLevelData(worldsettingsFinal, completeDimensions.specialWorldProperty(), completeDimensions.lifecycle()),
                                                new WorldGenSettings(worldOptionsFinal, dimensions)
                                        ),
                                        completeDimensions.dimensionsRegistryAccess()
                                );
                            }, WorldStem::new, Util.backgroundExecutor(), mc);
                            mc.managedBlock(resourceLoad::isDone);
                            WorldStem worldStem = resourceLoad.get();
                            mc.doWorldLoad(levelSourceAccess, packRepository, worldStem, Optional.of(gameRules), true);
                        } catch (Exception e) {
                            CyclopsCoreInstance.MOD.getLoggerHelper().log(Level.ERROR, "Failed to create world: " + e.getMessage());
                            levelSourceAccess.safeClose();
                            mc.gui.setScreen(screen);
                        }
                    })
                    .pos(screen.width / 2 + 102, screen.height / 4 + 56)
                    .size(58, 20)
                    .build();
            screen.addRenderableWidget(buttonBuilt);
        }
    }

}
