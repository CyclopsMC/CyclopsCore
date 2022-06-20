package org.cyclops.cyclopscore.client.gui;

import net.minecraft.CrashReport;
import net.minecraft.FileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.GeneralConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class GuiMainMenuExtensionDevWorld {

    private static final String WORLD_NAME_PREFIX = "cyclops-dev";

    @SubscribeEvent
    public static void onMainMenuInit(ScreenEvent.InitScreenEvent event) {
        // Add a button to the main menu if we're in a dev environment
        if (GeneralConfig.devWorldButton && event.getScreen() instanceof TitleScreen) {
            event.addListener(new Button(event.getScreen().width / 2 + 102, event.getScreen().height / 4 + 48,
                    58, 20, Component.translatable("general.cyclopscore.dev_world"), (button) -> {
                Minecraft mc = Minecraft.getInstance();

                // Open the last played dev world
                // If shift is held, create a new world.
                if (!MinecraftHelpers.isShifted()) {
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
                        CyclopsCore.clog(Level.ERROR, "Couldn't load level list" + e.getMessage());
                        mc.setScreen(new ErrorScreen(Component.translatable("selectWorld.unable_to_load"), Component.literal(e.getMessage())));
                    }

                    if (devWorldSummary != null && mc.getLevelSource().levelExists(devWorldSummary.getLevelId())) {
                        mc.forceSetScreen(new GenericDirtMessageScreen(Component.translatable("selectWorld.data_read")));
                        mc.createWorldOpenFlows().loadLevel(event.getScreen(), devWorldSummary.getLevelId());
                        return;
                    }
                }

                // Set rules
                GameRules gameRules = new GameRules();
                gameRules.getRule(GameRules.RULE_DAYLIGHT).set(false, null);
                gameRules.getRule(GameRules.RULE_DO_IMMEDIATE_RESPAWN).set(true, null);
                gameRules.getRule(GameRules.RULE_DO_PATROL_SPAWNING).set(false, null);
                gameRules.getRule(GameRules.RULE_DO_TRADER_SPAWNING).set(false, null);
                LevelSettings worldsettings = new LevelSettings(WORLD_NAME_PREFIX, GameType.CREATIVE,
                        false, Difficulty.PEACEFUL, true, gameRules, DataPackConfig.DEFAULT);

                // Create generator settings, based on GameTestServer
                int seed = new Random().nextInt();
                RegistryAccess.Frozen dynamicRegistries = RegistryAccess.BUILTIN.get();
                WorldGenSettings generatorSettings = dynamicRegistries.registryOrThrow(Registry.WORLD_PRESET_REGISTRY).getHolderOrThrow(WorldPresets.FLAT).value().createWorldGenSettings(seed, false, false);

                // Determine a save name
                String saveName;
                try {
                    saveName = FileUtil.findAvailableName(mc.getLevelSource().getBaseDir(), WORLD_NAME_PREFIX, "");
                } catch (IOException e) {
                    saveName = "World";
                }

                // Create the world
                mc.createWorldOpenFlows().createFreshLevel(saveName, worldsettings, dynamicRegistries, generatorSettings);
            }));
        }
    }

}
