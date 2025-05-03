package org.cyclops.cyclopscore.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

/**
 * @author rubensworks
 */
public class LecternInfoBookHandlerFabric {

    public LecternInfoBookHandlerFabric() {
        UseBlockCallback.EVENT.register(this::onRightClickLectern);
    }

    public InteractionResult onRightClickLectern(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (LecternInfoBookHandler.onRightClickLectern(level, hitResult.getBlockPos(), player)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

}
