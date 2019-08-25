package org.cyclops.cyclopscore.world.gen;

import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.init.IRegistry;

/**
 * Interface for retrogen registry.
 * @author rubensworks
 */
public interface IRetroGenRegistry extends IRegistry {

    /**
     * Add a new retro-generatable instance.
     * @param retroGen The retrogen instance.
     */
    public void registerRetroGen(IRetroGen retroGen);

    /**
     * Called when a chunk loads.
     * Make sure to annotate this with {@link SubscribeEvent}
     * @param event The chunk load event.
     */
    public void retroGenLoad(ChunkDataEvent.Load event);

    /**
     * Called when a chunk saves.
     * Make sure to annotate this with {@link SubscribeEvent}
     * @param event The chunk save event.
     */
    public void retroGenSave(ChunkDataEvent.Save event);

}
