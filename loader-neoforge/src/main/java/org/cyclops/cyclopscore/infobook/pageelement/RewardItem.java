package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.helper.IModHelpers;

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
    public boolean canObtain(Player player) {
        return true;
    }

    @Override
    public void obtain(Player player) {
        if (!player.getInventory().add(itemStack.copy())) {
            IModHelpers.get().getItemStackHelpers().spawnItemStack(player.level(), player.blockPosition(), itemStack.copy());
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
    public IRewardClient constructRewardClient() {
        return new RewardItemClient(this);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
