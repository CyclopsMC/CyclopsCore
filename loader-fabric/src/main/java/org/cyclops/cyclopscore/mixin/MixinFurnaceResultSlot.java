package org.cyclops.cyclopscore.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.events.IItemSmeltedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author rubensworks
 */
@Mixin(FurnaceResultSlot.class)
public class MixinFurnaceResultSlot {

    @Inject(method = "checkTakeAchievements", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void checkTakeAchievements(ItemStack itemStack, CallbackInfo callback) {
        FurnaceResultSlot slot = ((FurnaceResultSlot) (Object) this);
        if (slot.removeCount > 0 && slot.player instanceof ServerPlayer serverPlayer) {
            IItemSmeltedEvent.EVENT.invoker().onCrafted(serverPlayer, itemStack);
        }
    }

}
