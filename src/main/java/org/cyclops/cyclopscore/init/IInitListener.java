package org.cyclops.cyclopscore.init;

/**
 * Listener that can be given to the mod instance.
 * @author rubensworks
 *
 */
public interface IInitListener {

	/**
     * This will be called in an init step.
	 * @param initStep The init step.
     */
    public void onInit(Step initStep);
    
    /**
     * The possible init steps.
     * @author rubensworks
     *
     */
    public static enum Step {
    	
    	/**
    	 * Mod pre-init, {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent}
    	 */
    	PREINIT,
    	/**
    	 * Mod init, {@link net.minecraftforge.fml.common.event.FMLInitializationEvent}
    	 */
    	INIT,
    	/**
    	 * Mod post-init, {@link net.minecraftforge.fml.common.event.FMLPostInitializationEvent}.
    	 */
    	POSTINIT
    	
    }
	
}
