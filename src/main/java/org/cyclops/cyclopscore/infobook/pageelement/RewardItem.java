package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.infobook.GuiInfoBook;
import org.cyclops.cyclopscore.infobook.IInfoBook;

/**
 * An item reward.
 * @author rubensworks
 */
public class RewardItem implements IReward<RecipeAppendix.ItemButton> {

    protected static final int SLOT_SIZE = 16;

    private final ItemStack itemStack;

    public RewardItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public boolean canObtain(EntityPlayer player) {
        return true;
    }

    @Override
    public void obtain(EntityPlayer player) {
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
    public RecipeAppendix.ItemButton createButton(IInfoBook infoBook) {
        return new RecipeAppendix.ItemButton(infoBook);
    }

    @Override
    public void drawElementInner(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my, RecipeAppendix.ItemButton button) {
        RecipeAppendix.renderItemForButton(gui, x, y, itemStack, mx, my, true, button);
    }


}
