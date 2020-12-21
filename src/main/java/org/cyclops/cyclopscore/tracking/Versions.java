package org.cyclops.cyclopscore.tracking;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.Level;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

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
    private static volatile boolean displayed = false;

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
                            String data = IOUtils.toString(url, Charset.forName("UTF-8"));
                            String lines[] = data.split("\\r?\\n");
                            if(lines.length < 3) {
                                triple.getLeft().log(Level.WARN, "Retrieved invalid version data.");
                            } else {
                                String version = lines[0];
                                String info = lines[1];
                                String updateUrl = lines[2];
                                setVersionInfo(triple.getLeft(), triple.getMiddle(), version, info, updateUrl);
                                if(triple.getMiddle().needsUpdate()) {
                                    triple.getLeft().log(Level.INFO, String.format("%s is outdated, version %s can be found at %s.", triple.getLeft().getModName(), version, updateUrl));
                                } else {
                                    triple.getLeft().log(Level.INFO, String.format("%s is up-to-date!", triple.getLeft().getModName()));
                                }
                            }
                        } catch (IOException e) {
                            triple.getLeft().log(Level.WARN, "Could not get version info: " + e.toString());
                            setVersionInfo(triple.getLeft(), triple.getMiddle(), null, null, null);
                        }
                    }
                    allDone = true;
                }
            }).start();
        }
    }

    public static void setVersionInfo(ModBase mod, IModVersion modVersion, String version, String info, String updateUrl) {
        modVersion.setVersionInfo(version, info, updateUrl);
        if (version != null && info != null && updateUrl != null) {
            setForgeVersionInfo(mod, modVersion, version, info, updateUrl);
        }
    }

    public static void setForgeVersionInfo(ModBase mod, IModVersion modVersion, String version, String info, String updateUrl) {
        try {
            Field resultsField = VersionChecker.class.getDeclaredField("results");
            resultsField.setAccessible(true);
            Map<IModInfo, VersionChecker.CheckResult> results = (Map<IModInfo, VersionChecker.CheckResult>) resultsField.get(null);

            Constructor<VersionChecker.CheckResult> constructor = VersionChecker.CheckResult.class.getDeclaredConstructor(VersionChecker.Status.class, ComparableVersion.class, Map.class, String.class);
            constructor.setAccessible(true);
            VersionChecker.Status status = modVersion.needsUpdate() ? VersionChecker.Status.OUTDATED : VersionChecker.Status.UP_TO_DATE;
            ComparableVersion comparableVersion = new ComparableVersion(version);
            VersionChecker.CheckResult checkResult = constructor.newInstance(status, comparableVersion, ImmutableMap.of(comparableVersion, info), updateUrl);
            IModInfo modInfo = ModList.get().getModFileById(mod.getModId()).getMods().get(0);
            results.put(modInfo, checkResult);
        } catch (NoSuchFieldException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            mod.log(Level.ERROR, String.format("Failed to set Forge version information for %s-%s.", mod.getModName(), version));
            e.printStackTrace();
        }
    }

    /**
     * When a player tick event is received.
     * @param event The received event.
     */
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public synchronized void onTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.END && allDone && !displayed) {
            List<Triple<ModBase, IModVersion, String>> versionMods = getVersionMods();
            for (Triple<ModBase, IModVersion, String> triple : versionMods) {
                if (triple.getMiddle().needsUpdate()) {
                    // Chat formatting inspired by CoFH
                    PlayerEntity player = event.player;
                    IFormattableTextComponent chat = new StringTextComponent("");

                    Style modNameStyle = Style.EMPTY;
                    modNameStyle.setColor(Color.fromTextFormatting(TextFormatting.AQUA));

                    Style versionStyle = Style.EMPTY;
                    versionStyle.setColor(Color.fromTextFormatting(TextFormatting.AQUA));

                    Style downloadStyle = Style.EMPTY;
                    downloadStyle.setColor(Color.fromTextFormatting(TextFormatting.BLUE));

                    String currentVersion = MinecraftHelpers.getMinecraftVersion() + "-" + triple.getLeft().getContainer().getModInfo().getVersion().toString();
                    String newVersion = MinecraftHelpers.getMinecraftVersion() + "-" + triple.getMiddle().getVersion();
                    ITextComponent versionTransition = new StringTextComponent(String.format("%s -> %s", currentVersion, newVersion)).setStyle(versionStyle);
                    modNameStyle.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, versionTransition));
                    ITextComponent modNameComponent = new StringTextComponent(String.format("[%s]", triple.getLeft().getModName())).setStyle(modNameStyle);

                    ITextComponent downloadComponent = new TranslationTextComponent(L10NHelpers.localize("general.cyclopscore.version.download")).setStyle(downloadStyle);
                    downloadStyle.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("general.cyclopscore.version.clickToDownload")));
                    downloadStyle.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, triple.getMiddle().getUpdateUrl()));

                    chat.append(modNameComponent);
                    chat.appendString(" ");
                    chat.append(new TranslationTextComponent("general.cyclopscore.version.updateAvailable")
                            .setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.WHITE))));
                    chat.appendString(String.format(": %s ", triple.getMiddle().getVersion()));
                    chat.append(downloadComponent);

                    try {
                    player.sendMessage(chat, Util.DUMMY_UUID);

                    chat = new StringTextComponent("");
                    chat.append(modNameComponent);
                    chat.appendString(TextFormatting.WHITE + " ");
                    chat.appendString(triple.getMiddle().getInfo());
                    player.sendMessage(chat, Util.DUMMY_UUID);
                    } catch (NullPointerException e) {
                        // The player SMP connection can rarely be null at this point,
                        // let's retry in the next tick.
                        return;
                    }
                }
            }
            displayed = true;
            try {
                MinecraftForge.EVENT_BUS.unregister(this);
            } catch (Exception e) {
            }
        }
    }

}
