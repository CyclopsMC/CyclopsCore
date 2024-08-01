package org.cyclops.cyclopscore.tracking;

/**
 * Interface for mods which can be used inside the version checker {@link Versions}.
 * @author rubensworks
 */
public interface IModVersion {

    public void setVersionInfo(String version, String info, String updateUrl);

    public boolean isVersionInfo();

    public String getVersion();

    public String getInfo();

    public String getUpdateUrl();

    public boolean needsUpdate();

}
