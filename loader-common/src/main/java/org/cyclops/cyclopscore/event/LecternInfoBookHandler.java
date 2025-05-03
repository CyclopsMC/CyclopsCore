package org.cyclops.cyclopscore.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.inventory.InventoryLocationPlayer;
import org.cyclops.cyclopscore.item.ItemGui;

/**
 * @author rubensworks
 */
public class LecternInfoBookHandler {

    public static boolean onRightClickLectern(Level level, BlockPos pos, Player entity) {
        BlockState blockState = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!level.isClientSide()
                && blockState.is(Blocks.LECTERN)
                && blockEntity instanceof LecternBlockEntity lecternBlockEntity
                && lecternBlockEntity.getBook().getItem() instanceof ItemGui itemGui) {
            if (entity.isCrouching()) {
                // Remove book from lectern if sneaking
                IModHelpers.get().getItemStackHelpers().spawnItemStack(level, pos.relative(blockState.getValue(LecternBlock.FACING)), lecternBlockEntity.getBook().copy());
                LecternBlock.resetBookState(entity, level, pos, blockState, false);
                lecternBlockEntity.clearContent();
            } else {
                // Read book
                itemGui.openGuiForItemIndex(level, (ServerPlayer) entity, InventoryLocationPlayer.getInstance().handToLocation(entity, InteractionHand.MAIN_HAND, 0));
            }
            return true;
        }
        return false;
    }
}
