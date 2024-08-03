package org.cyclops.cyclopscore.client.gui;

import net.minecraft.CrashReport;
import net.minecraft.FileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelSummary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
                        if (!Screen.hasShiftDown()) {
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
                                e.printStackTrace();
                                mc.setScreen(new ErrorScreen(Component.translatable("selectWorld.unable_to_load"), Component.literal(e.getMessage())));
                            }

                            if (devWorldSummary != null && mc.getLevelSource().levelExists(devWorldSummary.getLevelId())) {
                                mc.forceSetScreen(new GenericMessageScreen(Component.translatable("selectWorld.data_read")));
                                mc.createWorldOpenFlows().openWorld(devWorldSummary.getLevelId(), () -> minecraft.setScreen(screen));
                                return;
                            }
                        }

                        // Set rules
                        GameRules gameRules = new GameRules();
                        gameRules.getRule(GameRules.RULE_DAYLIGHT).set(false, null);
                        gameRules.getRule(GameRules.RULE_DO_IMMEDIATE_RESPAWN).set(true, null);
                        gameRules.getRule(GameRules.RULE_DO_PATROL_SPAWNING).set(false, null);
                        gameRules.getRule(GameRules.RULE_DO_TRADER_SPAWNING).set(false, null);
                        WorldDataConfiguration worlddataconfiguration = new WorldDataConfiguration(new DataPackConfig(new ArrayList<>(Minecraft.getInstance().getResourcePackRepository().getAvailableIds()), List.of()), FeatureFlags.REGISTRY.allFlags());
                        LevelSettings worldsettings = new LevelSettings(WORLD_NAME_PREFIX, GameType.CREATIVE,
                                false, Difficulty.PEACEFUL, true, gameRules, worlddataconfiguration);

                        // Create generator settings and world options, based on GameTestServer
                        Function<RegistryAccess, WorldDimensions> generatorSettings = registryAccess -> registryAccess
                                .registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value()
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

                        // Create the world
                        mc.createWorldOpenFlows().createFreshLevel(saveName, worldsettings, worldOptions, generatorSettings, screen);
                    })
                    .pos(screen.width / 2 + 102, screen.height / 4 + 48)
                    .size(58, 20)
                    .build();
            screen.addRenderableWidget(buttonBuilt);
        }
    }

}
