package org.cyclops.cyclopscore.item;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Configurable item that can show a GUI on right clicking.
 *
 * Implement {@link #getContainer(Level, Player, int, InteractionHand, ItemStack)}
 * and {@link #getContainerClass(Level, Player, ItemStack)} to specify the gui.
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
	public abstract MenuProvider getContainer(Level world, Player player, int itemIndex, InteractionHand hand, ItemStack itemStack);

	public abstract Class<? extends AbstractContainerMenu> getContainerClass(Level world, Player player, ItemStack itemStack);
    
    @Override
	public boolean onDroppedByPlayer(ItemStack itemstack, Player player) {
		if(!itemstack.isEmpty()
				&& player instanceof ServerPlayer
				&& player.containerMenu != null
				&& player.containerMenu.getClass() == getContainerClass(player.level, player, itemstack)) {
			player.closeContainer();
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
    public void openGuiForItemIndex(Level world, ServerPlayer player, int itemIndex, InteractionHand hand) {
    	if (!world.isClientSide()) {
			MenuProvider containerProvider = this.getContainer(world, player, itemIndex, hand, player.getItemInHand(hand));
			if (containerProvider != null) {
				NetworkHooks.openGui(player, containerProvider, packetBuffer -> this.writeExtraGuiData(packetBuffer, world, player, itemIndex, hand));
				Stat<ResourceLocation> openStat = this.getOpenStat();
				if (openStat != null) {
					player.awardStat(openStat);
				}
			}
    	}
    }

	/**
	 * Write additional data to a packet buffer that will be sent to the client when opening the gui.
	 * @param packetBuffer A packet buffer to write to.
	 * @param world The world.
	 * @param player The player.
	 * @param itemIndex The item index in the player inventory.
	 * @param hand The hand the player is using.
	 */
    public void writeExtraGuiData(FriendlyByteBuf packetBuffer, Level world, ServerPlayer player,
								  int itemIndex, InteractionHand hand) {
    	packetBuffer.writeInt(itemIndex);
    	packetBuffer.writeBoolean(hand == InteractionHand.MAIN_HAND);
	}

	/**
	 * @return An optional gui opening statistic.
	 */
	@Nullable
	protected Stat<ResourceLocation> getOpenStat() {
		return null;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		if (player instanceof FakePlayer) {
			return new InteractionResultHolder<>(InteractionResult.FAIL, itemStack);
		}
		if (player instanceof ServerPlayer) {
			openGuiForItemIndex(world, (ServerPlayer) player, player.getInventory().selected, hand);
		}
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
	}

}
