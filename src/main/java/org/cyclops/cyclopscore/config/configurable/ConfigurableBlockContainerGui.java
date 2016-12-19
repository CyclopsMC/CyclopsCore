package org.cyclops.cyclopscore.config.configurable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.inventory.IGuiContainerProviderConfigurable;
import org.cyclops.cyclopscore.inventory.container.TileInventoryContainer;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.cyclopscore.tileentity.InventoryTileEntity;

/**
 * Block with a tile entity with a GUI that can hold ExtendedConfigs.
 * The container and GUI must be set inside the constructor of the extension.
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockContainerGui extends ConfigurableBlockContainer implements IGuiContainerProviderConfigurable {
    
    private int guiID;

    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param material Material of this blockState.
     * @param tileEntity The class of the tile entity this blockState holds.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockContainerGui(ExtendedConfig eConfig,
            Material material, Class<? extends CyclopsTileEntity> tileEntity) {
        super(eConfig, material, tileEntity);
        this.hasGui = true;
        if(hasGui()) {
            this.guiID = Helpers.getNewId(eConfig.getMod(), Helpers.IDType.GUI);
        }
    }
    
    @Override
    public int getGuiID() {
        return this.guiID;
    }

    @Override
    public ModBase getModGui() {
        return getConfig().getMod();
    }

    @Override
    public boolean isNormalCube(IBlockState blockState, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer entityplayer, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(world, blockPos, blockState, entityplayer, hand, side, hitX, hitY, hitZ);

        // Drop through if the player is sneaking
        if (entityplayer.isSneaking()) {
            return false;
        }

        if (!world.isRemote && hasGui()) {
            entityplayer.openGui(getConfig().getMod(), getGuiID(), world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }

        return true;
    }
    
    @Override
    protected void onPostBlockDestroyed(World world, BlockPos blockPos) {
    	super.onPostBlockDestroyed(world, blockPos);
    	
    	// Close the GUI if it is open
    	if(world.isRemote) {
    		tryCloseClientGui(world);
    	}
    }

    /**
     * Try to close the gui at client side.
     * @param world The world.
     */
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public void tryCloseClientGui(World world) {
    	if(Minecraft.getMinecraft().player.openContainer instanceof TileInventoryContainer<?>) {
    		TileInventoryContainer<? extends InventoryTileEntity> container =
    				(TileInventoryContainer<? extends InventoryTileEntity>) Minecraft.getMinecraft()
    				.player.openContainer;
    		if(container.getTile() == null || container.getTile().isInvalid()) {
    			Minecraft.getMinecraft().player.closeScreen();
    		}
    	}
	}

}
