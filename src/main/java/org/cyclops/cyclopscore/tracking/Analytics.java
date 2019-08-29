package org.cyclops.cyclopscore.tracking;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.GeneralConfig;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.init.ModBase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

/**
 * An analytics service that uses the Google Measurement Protocol.
 * This conforms to the Google Measurement Protocol policy: https://developers.google.com/analytics/devguides/collection/protocol/policy
 * No data is collected which can be used to identify a player.
 * This will be disabled when the Minecraft snooper is disabled or if this service is disabled in the config file.
 * @author rubensworks
 */
public class Analytics {

    // Tracking id, Anonymized user id, Mod name, Mod version, Minecraft version, Server/Client, Java Version
    private static final String REQUEST_PATTERN = "http://www.google-analytics.com/collect?v=1&tid=%s&cid=%s&t=screenview&cd=Startup&an=%s&av=%s&cd1=%s&cd2=%s&cd3=%s";
    private static boolean checked = false;

    private static final List<Pair<ModBase, String>> trackingMods = Lists.newLinkedList();

    /**
     * Register a new mod to track.
     * Make sure to call this before the mod init phase.
     * @param mod The mod.
     * @param trackingId The Google Analytics tracking id to use.
     */
    public static synchronized void registerMod(ModBase mod, String trackingId) {
        trackingMods.add(Pair.of(mod, trackingId));
    }

    protected static synchronized List<Pair<ModBase, String>> getTrackingMods() {
        return Lists.newArrayList(trackingMods);
    }

    /**
     * Send tracking info for all registered mods.
     * This should and can only be called once.
     */
    public static void sendAll() {
        if(!checked) {
            checked = true;
            if (FMLEnvironment.dist.isClient()
                    ? Minecraft.getInstance().getSnooper().isSnooperRunning()
                    : ServerLifecycleHooks.getCurrentServer().getSnooper().isSnooperRunning()) {
                new Thread(() -> {
                    List<Pair<ModBase, String>> trackingMods = getTrackingMods();
                    for (Pair<ModBase, String> pair : trackingMods) {
                        try {
                            URL url = new URL(createRequestURL(pair.getLeft(), pair.getRight()));
                            //noinspection ResultOfMethodCallIgnored
                            IOUtils.toString(url, Charset.forName("UTF-8")); // Just do request, we don't need the output (?)
                        } catch (IOException e) {
                            pair.getLeft().log(Level.WARN, "Could not send tracking info: " + e.toString());
                        }
                    }
                }).start();
            }
        }
    }

    protected static String createRequestURL(ModBase<?> mod, String trackingId) throws UnsupportedEncodingException {
        String mcVersion = URLEncoder.encode(Reference.MOD_MC_VERSION, "UTF-8");
        return String.format(REQUEST_PATTERN, trackingId, GeneralConfig.anonymousAnalyticsID,
                URLEncoder.encode(mod.getModName(), "UTF-8"),
                mcVersion + "-" + URLEncoder.encode(mod.getReferenceValue(ModBase.REFKEY_MOD_VERSION), "UTF-8"),
                mcVersion, FMLEnvironment.dist.isClient() ? "client" : "server", System.getProperty("java.version"));
    }

}
