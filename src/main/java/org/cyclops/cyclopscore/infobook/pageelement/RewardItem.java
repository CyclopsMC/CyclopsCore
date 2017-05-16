package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.GuiInfoBook;
import org.cyclops.cyclopscore.infobook.IInfoBook;

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
    @SideOnly(Side.CLIENT)
    public AdvancedButton createButton(IInfoBook infoBook) {
        return new RecipeAppendix.ItemButton(infoBook);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawElementInner(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my, AdvancedButton button) {
        RecipeAppendix.renderItemForButton(gui, x, y, itemStack, mx, my, true, (RecipeAppendix.ItemButton) button);
    }


}
