package org.cyclops.cyclopscore.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stat;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;

/**
 * Configurable item that can show a GUI on right clicking.
 *
 * Implement {@link #getContainer(World, PlayerEntity, ItemStack)}
 * and {@link #getContainerClass(World, PlayerEntity, ItemStack)} to specify the gui.
 *
 * Optionally implement {@link #getOpenStat()} to specify a stat on gui opening.
 *
 * @author rubensworks
 */
public abstract class ItemGui extends Item {

	protected ItemGui(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	public abstract INamedContainerProvider getContainer(World world, PlayerEntity player, ItemStack itemStack);

	public abstract Class<? extends Container> getContainerClass(World world, PlayerEntity player, ItemStack itemStack);
    
    @Override
	public boolean onDroppedByPlayer(ItemStack itemstack, PlayerEntity player) {
		if(!itemstack.isEmpty()
				&& player instanceof ServerPlayerEntity
				&& player.openContainer != null
				&& player.openContainer.getClass() == getContainerClass(player.world, player, itemstack)) {
			player.closeScreen();
		}
		return super.onDroppedByPlayer(itemstack, player);
	}
    
    /**
     * Open the gui for a certain item index in the player inventory.
     * @param world The world.
     * @param player The player.
     * @param itemIndex The item index in the player inventory.
	 * @param hand The hand the player is using.
     */
    public void openGuiForItemIndex(World world, PlayerEntity player, int itemIndex, Hand hand) {
		// TODO: temp data?: getConfig().getMod().getGuiHandler().setTemporaryData(GuiHandler.GuiType.ITEM, Pair.of(itemIndex, hand));
    	if(!world.isRemote() || isClientSideOnlyGui()) {
			INamedContainerProvider containerProvider = this.getContainer(world, player, player.getHeldItem(hand));
			if (containerProvider != null) {
				player.openContainer(containerProvider);
				Stat<ResourceLocation> openStat = this.getOpenStat();
				if (openStat != null) {
					player.addStat(openStat);
				}
			}
    	}
    }

    protected boolean isClientSideOnlyGui() {
        return false;
    }

	/**
	 * @return An optional gui opening statistic.
	 */
	@Nullable
	protected Stat<ResourceLocation> getOpenStat() {
		return null;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getHeldItem(hand);
		if (player instanceof FakePlayer) {
			return new ActionResult<>(ActionResultType.FAIL, itemStack);
		}
		openGuiForItemIndex(world, player, player.inventory.currentItem, hand);
		return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
	}

}
