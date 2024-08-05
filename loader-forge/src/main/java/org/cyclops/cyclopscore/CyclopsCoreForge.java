package org.cyclops.cyclopscore;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * The main mod class of CyclopsCore.
 * @author rubensworks
 *
 */
@Mod(Reference.MOD_ID)
public class CyclopsCoreForge extends ModBaseForge<CyclopsCoreForge> {

    /**
     * The unique instance of this mod.
     */
    public static CyclopsCoreForge _instance;

    private boolean loaded = false;

    public CyclopsCoreForge() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::loadComplete);
    }

    private void loadComplete(FMLLoadCompleteEvent event) {
        this.loaded = true;
    }

    /**
     * @return If this mod has been fully loaded. (The {@link FMLLoadCompleteEvent} has been called)
     */
    public boolean isLoaded() {
        return loaded;
    }

}
