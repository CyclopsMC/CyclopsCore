package org.cyclops.cyclopscore.init;

import net.neoforged.bus.api.IEventBus;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.cyclops.cyclopscore.tracking.IModVersion;

import java.util.function.Consumer;

/**
 * A {@link ModBase} which is also a {@link org.cyclops.cyclopscore.tracking.IModVersion}.
 * @author rubensworks
 */
public abstract class ModBaseVersionable<T extends ModBaseVersionable> extends ModBase<T> implements IModVersion {

    private boolean versionInfo = false;
    private String version;
    private String info;
    private String updateUrl;

    public ModBaseVersionable(String modId, Consumer<T> instanceSetter, IEventBus modEventBus) {
        super(modId, instanceSetter, modEventBus);
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
        ArtifactVersion currentVersion = getContainer().getModInfo().getVersion();
        String latestVersionString = getVersion();
        if (latestVersionString == null) {
            return false; // We're running offline or in this mod's dev environment
        }
        ArtifactVersion newVersion = new DefaultArtifactVersion(latestVersionString);
        return getVersion() != null && currentVersion.compareTo(newVersion) < 0;
    }

}
