package org.cyclops.cyclopscore.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.stats.Stat;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Configurable item that can show a GUI on right clicking.
 *
 * Implement {@link #getContainer(World, PlayerEntity, int, Hand, ItemStack)}
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
	public abstract INamedContainerProvider getContainer(World world, PlayerEntity player, int itemIndex, Hand hand, ItemStack itemStack);

	public abstract Class<? extends Container> getContainerClass(World world, PlayerEntity player, ItemStack itemStack);
    
    @Override
	public boolean onDroppedByPlayer(ItemStack itemstack, PlayerEntity player) {
		if(!itemstack.isEmpty()
				&& player instanceof ServerPlayerEntity
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
    public void openGuiForItemIndex(World world, ServerPlayerEntity player, int itemIndex, Hand hand) {
    	if (!world.isClientSide()) {
			INamedContainerProvider containerProvider = this.getContainer(world, player, itemIndex, hand, player.getItemInHand(hand));
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
    public void writeExtraGuiData(PacketBuffer packetBuffer, World world, ServerPlayerEntity player,
								  int itemIndex, Hand hand) {
    	packetBuffer.writeInt(itemIndex);
    	packetBuffer.writeBoolean(hand == Hand.MAIN_HAND);
	}

	/**
	 * @return An optional gui opening statistic.
	 */
	@Nullable
	protected Stat<ResourceLocation> getOpenStat() {
		return null;
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		if (player instanceof FakePlayer) {
			return new ActionResult<>(ActionResultType.FAIL, itemStack);
		}
		if (player instanceof ServerPlayerEntity) {
			openGuiForItemIndex(world, (ServerPlayerEntity) player, player.inventory.selected, hand);
		}
		return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
	}

}
