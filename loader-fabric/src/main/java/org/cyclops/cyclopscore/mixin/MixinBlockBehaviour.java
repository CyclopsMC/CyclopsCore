package org.cyclops.cyclopscore.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.events.IBlockExplodedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.BiConsumer;

/**
 * @author rubensworks
 */
@Mixin(BlockBehaviour.class)
public class MixinBlockBehaviour {

    @Inject(method = "onExplosionHit", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer, CallbackInfo callback) {
        if (!state.isAir() && explosion.getBlockInteraction() != Explosion.BlockInteraction.TRIGGER_BLOCK) {
            IBlockExplodedEvent.EVENT.invoker().onBlockExploded(state, level, pos, explosion, dropConsumer);
        }
    }

}
