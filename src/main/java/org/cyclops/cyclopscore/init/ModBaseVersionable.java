package org.cyclops.cyclopscore.init;

import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import org.cyclops.cyclopscore.tracking.IModVersion;

/**
 * A {@link ModBase} which is also a {@link org.cyclops.cyclopscore.tracking.IModVersion}.
 * @author rubensworks
 */
public abstract class ModBaseVersionable extends ModBase implements IModVersion {

    private boolean versionInfo = false;
    private String version;
    private String info;
    private String updateUrl;

    public ModBaseVersionable(String modId, String modName, String modVersion) {
        super(modId, modName);
        putGenericReference(REFKEY_MOD_VERSION, modVersion);
    }

    @Override
    public void setVersionInfo(String version, String info, String updateUrl) {
        versionInfo = true;
        this.version = version;
        this.info = info;
        this.updateUrl = updateUrl;
    }

    @Override
    public boolean isVersionInfo() {
        return versionInfo;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public String getUpdateUrl() {
        return updateUrl;
    }

    @Override
    public boolean needsUpdate() {
        String currentVersionString = getReferenceValue(REFKEY_MOD_VERSION);
        String latestVersionString = getVersion();
        if (latestVersionString == null || "@VERSION@".equals(currentVersionString)) {
            return false; // We're running offline or in this mod's dev environment
        }
        ArtifactVersion currentVersion = new DefaultArtifactVersion(currentVersionString);
        ArtifactVersion newVersion = new DefaultArtifactVersion(latestVersionString);
        return getVersion() != null && currentVersion.compareTo(newVersion) < 0;
    }

}
