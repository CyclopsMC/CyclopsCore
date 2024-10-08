package org.cyclops.cyclopscore.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiConsumer;

/**
 * @author rubensworks
 */
public interface IBlockExplodedEvent {
    Event<IBlockExplodedEvent> EVENT = EventFactory.createArrayBacked(IBlockExplodedEvent.class,
            (listeners) -> (state, level, pos, explosion, dropConsumer) -> {
                for (IBlockExplodedEvent event : listeners) {
                    event.onBlockExploded(state, level, pos, explosion, dropConsumer);
                }
            }
    );

    void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer);
}
