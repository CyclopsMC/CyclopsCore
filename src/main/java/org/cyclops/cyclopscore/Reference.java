package org.cyclops.cyclopscore;

/**
 * Class that can hold basic static things that are better not hard-coded
 * like mod details, texture paths, ID's...
 * @author rubensworks
 */
public final class Reference {

    // Mod info
    public static final String MOD_ID = "cyclopscore";
    public static final String MOD_NAME = "Cyclops Core";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String MOD_BUILD_NUMBER = "@BUILD_NUMBER@";
    public static final String MOD_MC_VERSION = "@MC_VERSION@";
    public static final String GA_TRACKING_ID = "UA-65307010-1";
    public static final String VERSION_URL = "https://raw.githubusercontent.com/CyclopsMC/Versions/master/1.12/CyclopsCore.txt";

    // Mod ID's
    public static final String MOD_VANILLA = "Minecraft";
    public static final String MOD_BAUBLES = "baubles";
    public static final String MOD_VERSION_CHECKER = "VersionChecker";

    // Paths
    public static final String TEXTURE_PATH_PARTICLES = "textures/particles/";

    // MOD ID's
    public static final String MOD_WAILA = "waila";
    public static final String MOD_JEI = "jei";

    // Dependencies
    public static final String MOD_DEPENDENCIES =
              "required-after:forge@[14.21.1.2406,);";

}
