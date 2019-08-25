package org.cyclops.cyclopscore.client.gui;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.ErrorScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FileUtil;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.ClientHooks;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.GeneralConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.io.IOException;
import java.util.Random;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class GuiMainMenuExtensionDevWorld {

    private static final String WORLD_NAME_PREFIX = "cyclops-dev";
    private static final String PRESET_FLAT_WORLD = "minecraft:bedrock,3*minecraft:stone,52*minecraft:sandstone;minecraft:desert;";

    @SubscribeEvent
    public static void onMainMenuInit(GuiScreenEvent.InitGuiEvent event) {
        // Add a button to the main menu if we're in a dev environment
        if (GeneralConfig.devWorldButton && event.getGui() instanceof MainMenuScreen) {
            event.addWidget(new Button(event.getGui().width / 2 + 102, event.getGui().height / 4 + 48,
                    58, 20, L10NHelpers.localize("general.cyclopscore.dev_world"), (button) -> {
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
                        mc.displayGuiScreen(new ErrorScreen(new TranslationTextComponent("selectWorld.unable_to_load"), e.getMessage()));
                    }

                    if (devWorldSummary != null && mc.getSaveLoader().canLoadWorld(devWorldSummary.getFileName())) {
                        ClientHooks.tryLoadExistingWorld(new WorldSelectionScreen(new MainMenuScreen()), devWorldSummary);
                        return;
                    }
                }

                WorldSettings worldsettings = new WorldSettings(new Random().nextInt(), GameType.CREATIVE,
                        false, false, WorldType.FLAT);
                worldsettings.enableCommands();
                worldsettings.setGeneratorOptions(Dynamic.convert(NBTDynamicOps.INSTANCE, JsonOps.INSTANCE,
                        FlatGenerationSettings.createFlatGeneratorFromString(PRESET_FLAT_WORLD)
                                .func_210834_a(NBTDynamicOps.INSTANCE).getValue()));
                // MCP: used to be CreateWorldScreen.getUncollidingSaveDirName
                String saveName = null;
                try {
                    saveName = FileUtil.func_214992_a(mc.getSaveLoader().func_215781_c(), WORLD_NAME_PREFIX, "");
                } catch (IOException e) {
                    saveName = "World";
                }
                mc.launchIntegratedServer(saveName, WORLD_NAME_PREFIX, worldsettings);
            }));
        }
    }

    @SubscribeEvent
    public static void onWorldStart(WorldEvent.Load event) {
        if (!event.getWorld().isRemote() && event.getWorld().getWorldInfo().getWorldName().equals(WORLD_NAME_PREFIX)) {
            event.getWorld().getWorldInfo().getGameRulesInstance()
                    .get(GameRules.DO_DAYLIGHT_CYCLE).set(false, null);
        }
    }

}
