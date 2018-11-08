package org.cyclops.cyclopscore.client.gui;

import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.GeneralConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.lwjgl.input.Keyboard;

import java.util.Random;

@Mod.EventBusSubscriber(Side.CLIENT)
public class GuiMainMenuExtensionDevWorld {

    private static final String WORLD_NAME_PREFIX = "cyclops-dev";
    private static final String PRESET_FLAT_WORLD = "3;minecraft:bedrock,3*minecraft:stone,52*minecraft:sandstone;2;";

    @SubscribeEvent
    public static void onMainMenuInit(GuiScreenEvent.InitGuiEvent event) {
        // Add a button to the main menu if we're in a dev environment
        if (GeneralConfig.devWorldButton && event.getGui() instanceof GuiMainMenu) {
            event.getButtonList().add(new GuiButton(666, event.getGui().width / 2 + 102, event.getGui().height / 4 + 48,
                    58, 20, L10NHelpers.localize("general.cyclopscore.dev_world")));
        }
    }

    @SubscribeEvent
    public static void onMainMenuClick(GuiScreenEvent.ActionPerformedEvent event) {
        if (GeneralConfig.devWorldButton && event.getGui() instanceof GuiMainMenu && event.getButton().id == 666) {
            Minecraft mc = Minecraft.getMinecraft();

            // Open the last played dev world
            // If shift is held, create a new world.
            boolean isShift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
            if (!isShift) {
                WorldSummary devWorldSummary = null;
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
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
                    mc.displayGuiScreen(new GuiErrorScreen(I18n.format("selectWorld.unable_to_load"), e.getMessage()));
                }

                if (devWorldSummary != null && mc.getSaveLoader().canLoadWorld(devWorldSummary.getFileName())) {
                    FMLClientHandler.instance().tryLoadExistingWorld(new GuiWorldSelection(new GuiMainMenu()), devWorldSummary);
                    return;
                }
            }

            WorldSettings worldsettings = new WorldSettings(new Random().nextInt(), GameType.CREATIVE,
                    false, false, WorldType.FLAT);
            worldsettings.enableCommands();
            worldsettings.setGeneratorOptions(PRESET_FLAT_WORLD);
            String saveName = GuiCreateWorld.getUncollidingSaveDirName(mc.getSaveLoader(), WORLD_NAME_PREFIX);
            mc.launchIntegratedServer(saveName, WORLD_NAME_PREFIX, worldsettings);
        }
    }

    @SubscribeEvent
    public static void onWorldStart(WorldEvent.Load event) {
        if (!event.getWorld().isRemote && event.getWorld().getWorldInfo().getWorldName().equals(WORLD_NAME_PREFIX)) {
            event.getWorld().getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
        }
    }

}
