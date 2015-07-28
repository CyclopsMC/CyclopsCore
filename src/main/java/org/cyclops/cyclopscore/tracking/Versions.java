package org.cyclops.cyclopscore.tracking;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.init.ModBase;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Version checking service.
 * This will check for a regular text file at the given url for each mod.
 * This file must conform to the following format:
 *  [version]
 *  [version info]
 *  [update url]
 * @author rubensworks
 */
public class Versions {

    private static final List<Triple<ModBase, IModVersion, String>> versionMods = Lists.newLinkedList();

    private static volatile boolean checked = false;
    private static volatile boolean allDone = false;

    public static synchronized void registerMod(ModBase mod, IModVersion modVersion, String versionUrl) {
        versionMods.add(Triple.of(mod, modVersion, versionUrl));
    }

    protected static synchronized List<Triple<ModBase, IModVersion, String>> getVersionMods() {
        return Lists.newArrayList(versionMods);
    }

    /**
     * Check the versions for all registered mods.
     * This should and can only be called once.
     */
    public static void checkAll() {
        if(!checked) {
            checked = true;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    List<Triple<ModBase, IModVersion, String>> versionMods = getVersionMods();
                    for (Triple<ModBase, IModVersion, String> triple : versionMods) {
                        try {
                            URL url = new URL(triple.getRight());
                            String data = IOUtils.toString(url);
                            String lines[] = data.split("\\r?\\n");
                            if(lines.length < 3) {
                                triple.getLeft().log(Level.WARN, "Retrieved invalid version data.");
                            } else {
                                String version = lines[0];
                                String info = lines[1];
                                String updateUrl = lines[2];
                                triple.getMiddle().setVersionInfo(version, info, updateUrl);
                                if(triple.getMiddle().needsUpdate()) {
                                    triple.getLeft().log(Level.INFO, String.format("%s is outdated, version %s can be found at %s.", triple.getLeft().getModName(), version, updateUrl));
                                } else {
                                    triple.getLeft().log(Level.INFO, String.format("%s is up-to-date!", triple.getLeft().getModName()));
                                }
                            }
                        } catch (IOException e) {
                            triple.getLeft().log(Level.WARN, "Could not get version info: " + e.toString());
                            triple.getMiddle().setVersionInfo(null, null, null);
                        }
                    }
                    allDone = true;
                }
            }).run();
        }
    }

    /**
     * When a player tick event is received.
     * @param event The received event.
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.END && allDone) {
            List<Triple<ModBase, IModVersion, String>> versionMods = getVersionMods();
            for (Triple<ModBase, IModVersion, String> triple : versionMods) {
                if(triple.getMiddle().needsUpdate()) {
                    // Chat formatting inspired by CoFH
                    EntityPlayer player = event.player;
                    IChatComponent chat = new ChatComponentText("");

                    ChatStyle modNameStyle = new ChatStyle();
                    modNameStyle.setColor(EnumChatFormatting.AQUA);

                    ChatStyle versionStyle = new ChatStyle();
                    versionStyle.setColor(EnumChatFormatting.AQUA);

                    ChatStyle downloadStyle = new ChatStyle();
                    downloadStyle.setColor(EnumChatFormatting.BLUE);

                    String currentVersion = Reference.MOD_MC_VERSION + "-" + triple.getLeft().getReferenceValue(ModBase.REFKEY_MOD_VERSION);
                    String newVersion = Reference.MOD_MC_VERSION + "-" + triple.getMiddle().getVersion();
                    IChatComponent versionTransition = new ChatComponentText(String.format("%s -> %s", currentVersion, newVersion)).setChatStyle(versionStyle);
                    modNameStyle.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, versionTransition));
                    IChatComponent modNameComponent = new ChatComponentText(String.format("[%s]", triple.getLeft().getModName())).setChatStyle(modNameStyle);

                    IChatComponent downloadComponent = new ChatComponentText(String.format("[%s]", L10NHelpers.localize("general.cyclopscore.version.download"))).setChatStyle(downloadStyle);
                    downloadStyle.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentTranslation("general.cyclopscore.version.clickToDownload")));
                    downloadStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, triple.getMiddle().getUpdateUrl()));

                    chat.appendSibling(modNameComponent);
                    chat.appendText(" ");
                    chat.appendSibling(new ChatComponentTranslation("general.cyclopscore.version.updateAvailable").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)));
                    chat.appendText(String.format(": %s ", triple.getMiddle().getVersion()));
                    chat.appendSibling(downloadComponent);

                    player.addChatComponentMessage(chat);

                    chat = new ChatComponentText("");
                    chat.appendSibling(modNameComponent);
                    chat.appendText(EnumChatFormatting.WHITE + " ");
                    chat.appendText(triple.getMiddle().getInfo());
                    player.addChatComponentMessage(chat);
                }
            }
            FMLCommonHandler.instance().bus().unregister(this);
        }
    }

}
