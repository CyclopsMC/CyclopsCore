package org.cyclops.cyclopscore.client.gui;

import net.minecraft.FileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.client.gui.screens.PresetFlatWorldScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
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
import java.util.Random;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class GuiMainMenuExtensionDevWorld {

    private static final String WORLD_NAME_PREFIX = "cyclops-dev";
    private static final String PRESET_FLAT_WORLD = "minecraft:bedrock,3*minecraft:stone,52*minecraft:sandstone;minecraft:desert;";
    private static final String PRESET_FLAT_WORLD_JSON = "{\"bonus_chest\":false,\"dimensions\":{\"minecraft:overworld\":{\"type\":\"minecraft:overworld\",\"generator\":{\"settings\":{\"lakes\":false,\"features\":false,\"biome\":\"minecraft:desert\",\"structures\":{\"structures\":{}},\"layers\":[{\"height\":1,\"block\":\"minecraft:bedrock\"},{\"height\":3,\"block\":\"minecraft:stone\"},{\"height\":52,\"block\":\"minecraft:sandstone\"}]},\"type\":\"minecraft:flat\"}},\"minecraft:the_nether\":{\"type\":\"minecraft:the_nether\",\"generator\":{\"biome_source\":{\"preset\":\"minecraft:nether\",\"seed\":-7729799262413108572,\"type\":\"minecraft:multi_noise\"},\"seed\":-7729799262413108572,\"settings\":\"minecraft:nether\",\"type\":\"minecraft:noise\"}},\"minecraft:the_end\":{\"type\":\"minecraft:the_end\",\"generator\":{\"biome_source\":{\"seed\":-7729799262413108572,\"type\":\"minecraft:the_end\"},\"seed\":-7729799262413108572,\"settings\":\"minecraft:end\",\"type\":\"minecraft:noise\"}}},\"seed\":-7729799262413108572,\"generate_features\":false}";

    @SubscribeEvent
    public static void onMainMenuInit(ScreenEvent.InitScreenEvent event) {
        // Add a button to the main menu if we're in a dev environment
        if (GeneralConfig.devWorldButton && event.getScreen() instanceof TitleScreen) {
            event.addListener(new Button(event.getScreen().width / 2 + 102, event.getScreen().height / 4 + 48,
                    58, 20, new TranslatableComponent("general.cyclopscore.dev_world"), (button) -> {
                Minecraft mc = Minecraft.getInstance();

                // Open the last played dev world
                // If shift is held, create a new world.
                if (!MinecraftHelpers.isShifted()) {
                    LevelSummary devWorldSummary = null;
                    mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    try {
                        for (LevelSummary worldSummary : mc.getLevelSource().getLevelList()) {
                            if (worldSummary.getLevelName().equals(WORLD_NAME_PREFIX)) {
                                if (devWorldSummary == null
                                        || devWorldSummary.getLastPlayed() < worldSummary.getLastPlayed()) {
                                    devWorldSummary = worldSummary;
                                }
                            }
                        }

                    } catch (LevelStorageException e) {
                        CyclopsCore.clog(Level.ERROR, "Couldn't load level list" + e.getMessage());
                        mc.setScreen(new ErrorScreen(new TranslatableComponent("selectWorld.unable_to_load"), new TextComponent(e.getMessage())));
                    }

                    if (devWorldSummary != null && mc.getLevelSource().levelExists(devWorldSummary.getLevelId())) {
                        mc.loadLevel(devWorldSummary.getLevelId());
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

                // Create generator settings, based on a flat world preset
                int seed = new Random().nextInt();
                RegistryAccess.Frozen dynamicRegistries = RegistryAccess.BUILTIN.get();
                Registry<DimensionType> registryDimensionType = dynamicRegistries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
                Registry<Biome> registryBiome = dynamicRegistries.registryOrThrow(Registry.BIOME_REGISTRY);
                Registry<StructureSet> registryStructureSet = dynamicRegistries.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);
                Registry<LevelStem> simpleregistry = DimensionType.defaultDimensions(dynamicRegistries, seed);
                FlatLevelGeneratorSettings flatgenerationsettings = PresetFlatWorldScreen.fromString(registryBiome, registryStructureSet, PRESET_FLAT_WORLD, FlatLevelGeneratorSettings.getDefault(registryBiome, registryStructureSet));
                WorldGenSettings generatorSettings = new WorldGenSettings(seed, false, false,
                        WorldGenSettings.withOverworld(registryDimensionType, simpleregistry, new FlatLevelSource(registryStructureSet, flatgenerationsettings)));

                // Determine a save name
                String saveName;
                try {
                    saveName = FileUtil.findAvailableName(mc.getLevelSource().getBaseDir(), WORLD_NAME_PREFIX, "");
                } catch (IOException e) {
                    saveName = "World";
                }

                // Create the world
                mc.createLevel(saveName, worldsettings, dynamicRegistries, generatorSettings);
            }));
        }
    }

}
