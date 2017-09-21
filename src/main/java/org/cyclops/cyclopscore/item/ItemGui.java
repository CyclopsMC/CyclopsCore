package org.cyclops.cyclopscore.item;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.client.gui.GuiHandler;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.Helpers.IDType;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.inventory.IGuiContainerProviderConfigurable;

/**
 * Configurable item that can show a GUI on right clicking.
 * @author rubensworks
 *
 */
public abstract class ItemGui extends ConfigurableItem implements IGuiContainerProviderConfigurable {

	private int guiID;
	
	/**
     * Make a new item instance.
     * @param eConfig Config for this blockState.
     */
	protected ItemGui(ExtendedConfig<ItemConfig> eConfig) {
		super(eConfig);
		this.guiID = Helpers.getNewId(eConfig.getMod(), IDType.GUI);
	}

    @Override
    public ModBase getModGui() {
        return eConfig.getMod();
    }
    
    @Override
    public int getGuiID() {
        return this.guiID;
    }
    
    @Override
	public abstract Class<? extends Container> getContainer();
    
    @Override
	@SideOnly(Side.CLIENT)
    public abstract Class<? extends GuiScreen> getGui();
    
    @Override
	public boolean onDroppedByPlayer(ItemStack itemstack, EntityPlayer player) {
		if(itemstack != null
				&& player instanceof EntityPlayerMP
				&& player.openContainer != null
				&& player.openContainer.getClass() == getContainer()) {
			player.closeScreen();
		}
		return super.onDroppedByPlayer(itemstack, player);
	}

	/**
	 * Open the gui for a certain item index in the player inventory.
	 * @param world The world.
	 * @param player The player.
	 * @param itemIndex The item index in the player inventory.
	 */
	@Deprecated
	public void openGuiForItemIndex(World world, EntityPlayer player, int itemIndex) {
		openGuiForItemIndex(world, player, itemIndex, EnumHand.MAIN_HAND);
	}
    
    /**
     * Open the gui for a certain item index in the player inventory.
     * @param world The world.
     * @param player The player.
     * @param itemIndex The item index in the player inventory.
	 * @param hand The hand the player is using.
     */
    public void openGuiForItemIndex(World world, EntityPlayer player, int itemIndex, EnumHand hand) {
		getConfig().getMod().getGuiHandler().setTemporaryData(GuiHandler.GuiType.ITEM, Pair.of(itemIndex, hand));
    	if(!world.isRemote || isClientSideOnlyGui()) {
    		player.openGui(getConfig().getMod(), getGuiID(), world, (int) player.posX, (int) player.posY, (int) player.posZ);
    	}
    }

    protected boolean isClientSideOnlyGui() {
        return false;
    }

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack itemStack = player.getHeldItem(hand);
		if (player instanceof FakePlayer || (player.getClass().getName().equals("ffba04.blockhologram.dummy.DummyPlayer"))) {
			return new ActionResult<>(EnumActionResult.FAIL, itemStack);
		}
		openGuiForItemIndex(world, player, player.inventory.currentItem, hand);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
	}

}
