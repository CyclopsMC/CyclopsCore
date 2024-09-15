package org.cyclops.cyclopscore.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.cyclops.cyclopscore.events.IItemPickupEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author rubensworks
 */
@Mixin(ItemEntity.class)
public class MixinItemEntity {

    @Inject(method = "playerTouch", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void playerTouch(Player player, CallbackInfo callback) {
        ItemEntity itemEntity = ((ItemEntity) (Object) this);
        if (!itemEntity.hasPickUpDelay()) {
            IItemPickupEvent.EVENT.invoker().onPickup(player, itemEntity);
        }
    }

}
