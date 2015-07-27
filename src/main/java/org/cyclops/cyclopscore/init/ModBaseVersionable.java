package org.cyclops.cyclopscore.init;

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
        return getVersion() != null && !getReferenceValue(REFKEY_MOD_VERSION).equals(getVersion());
    }

}
