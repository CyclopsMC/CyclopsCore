package org.cyclops.cyclopscore.tracking;

import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.event.PlayerRingOfFire;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.UUID;

/**
 * Loads important users from a remove file,
 * so that they can be whitelisted for the {@link org.cyclops.cyclopscore.event.PlayerRingOfFire}.
 * @author rubensworks
 */
public class ImportantUsers {

    private static final String SOURCE = "https://raw.githubusercontent.com/CyclopsMC/Versions/master/data/users.txt";

    private static volatile boolean checked = false;

    /**
     * Load the users from a remove file.
     * This should and can only be called once.
     */
    public static void checkAll() {
        if(!checked) {
            checked = true;
            new Thread(() -> {
                Set<UUID> uuids = Sets.newHashSet();
                try {
                    URL url = new URL(SOURCE);
                    String data = IOUtils.toString(url, Charset.forName("UTF-8"));
                    String lines[] = data.split("\\r?\\n");
                    for (String line : lines) {
                        String[] segments = line.split(",");
                        if(segments.length >= 1) {
                            try {
                                uuids.add(UUID.fromString(segments[0]));
                            } catch (IllegalArgumentException e) {
                                // Ignore error
                            }
                        }
                    }
                } catch (IOException e) {
                    CyclopsCore.clog(Level.WARN, "Could not get version important users: " + e.toString());
                }

                PlayerRingOfFire.ALLOW_RING.addAll(uuids);
            }).start();
        }
    }

}
