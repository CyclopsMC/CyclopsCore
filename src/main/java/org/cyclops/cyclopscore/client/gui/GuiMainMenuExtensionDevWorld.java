package org.cyclops.cyclopscore.client.gui;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.ErrorScreen;
import net.minecraft.client.gui.screen.FlatPresetsScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.FileUtil;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.datafix.codec.DatapackCodec;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.GeneralConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class GuiMainMenuExtensionDevWorld {

    private static final String WORLD_NAME_PREFIX = "cyclops-dev";
    private static final String PRESET_FLAT_WORLD = "minecraft:bedrock,3*minecraft:stone,52*minecraft:sandstone;minecraft:desert;";
    private static final String PRESET_FLAT_WORLD_JSON = "{\"bonus_chest\":false,\"dimensions\":{\"minecraft:overworld\":{\"type\":\"minecraft:overworld\",\"generator\":{\"settings\":{\"lakes\":false,\"features\":false,\"biome\":\"minecraft:desert\",\"structures\":{\"structures\":{}},\"layers\":[{\"height\":1,\"block\":\"minecraft:bedrock\"},{\"height\":3,\"block\":\"minecraft:stone\"},{\"height\":52,\"block\":\"minecraft:sandstone\"}]},\"type\":\"minecraft:flat\"}},\"minecraft:the_nether\":{\"type\":\"minecraft:the_nether\",\"generator\":{\"biome_source\":{\"preset\":\"minecraft:nether\",\"seed\":-7729799262413108572,\"type\":\"minecraft:multi_noise\"},\"seed\":-7729799262413108572,\"settings\":\"minecraft:nether\",\"type\":\"minecraft:noise\"}},\"minecraft:the_end\":{\"type\":\"minecraft:the_end\",\"generator\":{\"biome_source\":{\"seed\":-7729799262413108572,\"type\":\"minecraft:the_end\"},\"seed\":-7729799262413108572,\"settings\":\"minecraft:end\",\"type\":\"minecraft:noise\"}}},\"seed\":-7729799262413108572,\"generate_features\":false}";

    @SubscribeEvent
    public static void onMainMenuInit(GuiScreenEvent.InitGuiEvent event) {
        // Add a button to the main menu if we're in a dev environment
        if (GeneralConfig.devWorldButton && event.getGui() instanceof MainMenuScreen) {
            event.addWidget(new Button(event.getGui().width / 2 + 102, event.getGui().height / 4 + 48,
                    58, 20, new TranslationTextComponent("general.cyclopscore.dev_world"), (button) -> {
                Minecraft mc = Minecraft.getInstance();

                // Open the last played dev world
                // If shift is held, create a new world.
                if (!MinecraftHelpers.isShifted()) {
                    WorldSummary devWorldSummary = null;
                    mc.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    try {
                        for (WorldSummary worldSummary : mc.getSaveLoader().getSaveList()) {
                            if (worldSummary.getDisplayName().equals(WORLD_NAME_PREFIX)) {
                                if (devWorldSummary == null
                                        || devWorldSummary.getLastTimePlayed() < worldSummary.getLastTimePlayed()) {
                                    devWorldSummary = worldSummary;
                                }
                            }
                        }

                    } catch (AnvilConverterException e) {
                        CyclopsCore.clog(Level.ERROR, "Couldn't load level list" + e.getMessage());
                        mc.displayGuiScreen(new ErrorScreen(new TranslationTextComponent("selectWorld.unable_to_load"), new StringTextComponent(e.getMessage())));
                    }

                    if (devWorldSummary != null && mc.getSaveLoader().canLoadWorld(devWorldSummary.getFileName())) {
                        mc.loadWorld(devWorldSummary.getFileName());
                        return;
                    }
                }

                // Set rules
                GameRules gameRules = new GameRules();
                gameRules.get(GameRules.DO_DAYLIGHT_CYCLE).set(false, null);
                gameRules.get(GameRules.DO_IMMEDIATE_RESPAWN).set(true, null);
                gameRules.get(GameRules.DO_PATROL_SPAWNING).set(false, null);
                gameRules.get(GameRules.DO_TRADER_SPAWNING).set(false, null);
                WorldSettings worldsettings = new WorldSettings(WORLD_NAME_PREFIX, GameType.CREATIVE,
                        false, Difficulty.PEACEFUL, true, gameRules, DatapackCodec.VANILLA_CODEC);

                // Create generator settings, based on a flat world preset
                int seed = new Random().nextInt();
                DynamicRegistries.Impl dynamicRegistries = DynamicRegistries.func_239770_b_();
                Registry<DimensionType> registryDimensionType = dynamicRegistries.getRegistry(Registry.DIMENSION_TYPE_KEY);
                Registry<Biome> registryBiome = dynamicRegistries.getRegistry(Registry.BIOME_KEY);
                Registry<DimensionSettings> registryDimensionSettings = dynamicRegistries.getRegistry(Registry.NOISE_SETTINGS_KEY);
                SimpleRegistry<Dimension> simpleregistry = DimensionType.getDefaultSimpleRegistry(registryDimensionType, registryBiome, registryDimensionSettings, seed);
                FlatGenerationSettings flatgenerationsettings = FlatPresetsScreen.func_243299_a(registryBiome, PRESET_FLAT_WORLD, FlatGenerationSettings.func_242869_a(registryBiome));
                DimensionGeneratorSettings generatorSettings = new DimensionGeneratorSettings(seed, false, false,
                        DimensionGeneratorSettings.func_242749_a(registryDimensionType, simpleregistry, new FlatChunkGenerator(flatgenerationsettings)));

                // Determine a save name
                String saveName;
                try {
                    saveName = FileUtil.findAvailableName(mc.getSaveLoader().getSavesDir(), WORLD_NAME_PREFIX, "");
                } catch (IOException e) {
                    saveName = "World";
                }

                // Create the world
                mc.createWorld(saveName, worldsettings, dynamicRegistries, generatorSettings);
            }));
        }
    }

}
