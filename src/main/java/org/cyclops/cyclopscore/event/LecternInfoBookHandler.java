package org.cyclops.cyclopscore.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.inventory.InventoryLocationPlayer;
import org.cyclops.cyclopscore.item.ItemGui;

/**
 * @author rubensworks
 */
public class LecternInfoBookHandler {

    @SubscribeEvent
    public void onRightClickLectern(PlayerInteractEvent.RightClickBlock event) {
        BlockState blockState = event.getLevel().getBlockState(event.getPos());
        BlockEntity blockEntity = event.getLevel().getBlockEntity(event.getPos());
        if (!event.getLevel().isClientSide()
                && blockState.is(Blocks.LECTERN)
                && blockEntity instanceof LecternBlockEntity lecternBlockEntity
                && lecternBlockEntity.getBook().getItem() instanceof ItemGui itemGui) {
            if (event.getEntity().isSecondaryUseActive()) {
                // Remove book from lectern if sneaking
                ItemStackHelpers.spawnItemStack(event.getLevel(), event.getPos().relative(blockState.getValue(LecternBlock.FACING)), lecternBlockEntity.getBook().copy());
                LecternBlock.resetBookState(event.getEntity(), event.getLevel(), event.getPos(), blockState, false);
                lecternBlockEntity.clearContent();
            } else {
                // Read book
                itemGui.openGuiForItemIndex(event.getLevel(), (ServerPlayer) event.getEntity(), InventoryLocationPlayer.getInstance().handToLocation(event.getEntity(), InteractionHand.MAIN_HAND, 0));
            }
            event.setCanceled(true);
        }
    }
}
