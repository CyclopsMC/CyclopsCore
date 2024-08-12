package org.cyclops.cyclopscore;

import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.tracking.Analytics;
import org.cyclops.cyclopscore.tracking.Versions;

/**
 * @author rubensworks
 */
public class GeneralConfigNeoForge extends GeneralConfig {
    public GeneralConfigNeoForge() {
        super(CyclopsCore._instance);
    }

    @Override
    public void onRegistered() {
        if(analytics) {
            Analytics.registerMod((ModBase) getMod(), Reference.GA_TRACKING_ID);
        }
        if(versionChecker) {
            Versions.registerMod((ModBase) getMod(), CyclopsCore._instance, "https://raw.githubusercontent.com/CyclopsMC/Versions/master/" + getMod().getModHelpers().getMinecraftHelpers().getMinecraftVersionMajorMinor() + "/CyclopsCore.txt");
        }
    }
}
