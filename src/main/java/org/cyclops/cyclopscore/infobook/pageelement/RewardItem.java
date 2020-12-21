package org.cyclops.cyclopscore.infobook.pageelement;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

/**
 * An item reward.
 * @author rubensworks
 */
public class RewardItem implements IReward {

    protected static final int SLOT_SIZE = 16;

    private final ItemStack itemStack;

    public RewardItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public boolean canObtain(PlayerEntity player) {
        return true;
    }

    @Override
    public void obtain(PlayerEntity player) {
        if (!player.inventory.addItemStackToInventory(itemStack.copy())) {
            ItemStackHelpers.spawnItemStack(player.getEntityWorld(), player.getPosition(), itemStack.copy());
        }
    }

    @Override
    public int getWidth() {
        return SLOT_SIZE + 4;
    }

    @Override
    public int getHeight() {
        return SLOT_SIZE + 4;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AdvancedButton createButton(IInfoBook infoBook) {
        return new RecipeAppendix.ItemButton(infoBook);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawElementInner(ScreenInfoBook gui, MatrixStack matrixStack, int x, int y, int width, int height, int page, int mx, int my, AdvancedButton button) {
        RecipeAppendix.renderItemForButton(gui, matrixStack, x, y, itemStack, mx, my, true, (RecipeAppendix.ItemButton) button);
    }


}
