package org.cyclops.cyclopscore.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.cyclops.cyclopscore.events.ILootTableModifyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author rubensworks
 */
@Mixin(LootTable.class)
public class MixinLootTable {

    @Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void getRandomItems(LootContext context, CallbackInfoReturnable<ObjectArrayList<ItemStack>> callback) {
        LootTable lootTable = (LootTable) (Object) this;
        ILootTableModifyEvent.EVENT.invoker().getLootTableItems(lootTable, context, callback.getReturnValue());
    }

}
