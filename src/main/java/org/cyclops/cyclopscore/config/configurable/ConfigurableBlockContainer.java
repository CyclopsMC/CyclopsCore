package org.cyclops.cyclopscore.config.configurable;

import lombok.experimental.Delegate;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockPropertyManagerComponent;
import org.cyclops.cyclopscore.block.property.IBlockPropertyManager;
import org.cyclops.cyclopscore.client.model.IDynamicModelElement;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.cyclopscore.tileentity.TileEntityNBTStorage;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Block with a tile entity that can hold ExtendedConfigs.
 * @author rubensworks
 *
 */
public class ConfigurableBlockContainer extends BlockContainer implements IConfigurableBlock, IDynamicModelElement {

    @Delegate protected IBlockPropertyManager propertyManager;
    @Override protected BlockStateContainer createBlockState() {
        return (propertyManager = new BlockPropertyManagerComponent(this)).createDelegatedBlockState();
    }

    protected BlockConfig eConfig = null;
    
    protected Random random;
    private Class<? extends CyclopsTileEntity> tileEntity;

    protected boolean hasGui = false;
    
    private boolean rotatable;
    
    protected int pass = 0;
    protected boolean isInventoryBlock = false;
    
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param material Material of this blockState.
     * @param tileEntity The class of the tile entity this blockState holds.
     */
    public ConfigurableBlockContainer(ExtendedConfig<BlockConfig> eConfig, Material material, Class<? extends CyclopsTileEntity> tileEntity) {
        super(material);
        this.setConfig((BlockConfig)eConfig); // TODO change eConfig to just be a BlockConfig
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
        this.random = new Random();
        this.tileEntity = tileEntity;
        setHardness(5F);
        setSoundType(SoundType.STONE);
        if(hasDynamicModel()) MinecraftForge.EVENT_BUS.register(this);
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColorHandler() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
                                     int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState blockState = super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        for(IProperty property : blockState.getPropertyKeys()) {
            if(property.getName().equals("facing")) {
                blockState = blockState.withProperty(property, placer.getHorizontalFacing());
            }
        }
        return blockState;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    /**
     * Get the class of the tile entity this blockState holds.
     * @return The tile entity class.
     */
    public Class<? extends TileEntity> getTileEntity() {
        return this.tileEntity;
    }

    private void setConfig(BlockConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public boolean hasGui() {
        return hasGui;
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        try {
            CyclopsTileEntity tile = tileEntity.newInstance();
            tile.onLoad();
            tile.setRotatable(isRotatable());
            return tile;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * If the NBT data of this tile entity should be added to the dropped blockState.
     * @return If the NBT data should be added.
     */
    public boolean saveNBTToDroppedItem() {
        return true;
    }

    /**
     * Sets a block to air, but also plays the sound and particles and can spawn drops.
     * This includes calls to {@link ConfigurableBlockContainer#onPreBlockDestroyed(World, BlockPos)}
     * and {@link ConfigurableBlockContainer#onPostBlockDestroyed(World, BlockPos)}.
     * @param world The world.
     * @param pos The position.
     * @param dropBlock If this should produce item drops.
     * @return If the block was destroyed and not air.
     */
    public boolean destroyBlock(World world, BlockPos pos, boolean dropBlock) {
        onPreBlockDestroyedPersistence(world, pos);
        boolean result = world.destroyBlock(pos, dropBlock);
        onPostBlockDestroyed(world, pos);
        return result;
    }

    /**
     * Called before the block is broken or destroyed.
     * @param world The world.
     * @param blockPos The position of the to-be-destroyed block.
     * @param player The player destroying the block.
     */
    protected void onPreBlockDestroyed(World world, BlockPos blockPos, EntityPlayer player) {
        onPreBlockDestroyedPersistence(world, blockPos);
    }

    /**
     * Called before the block is broken or destroyed.
     * @param world The world.
     * @param blockPos The position of the to-be-destroyed block.
     */
    protected void onPreBlockDestroyed(World world, BlockPos blockPos) {

    }

    /**
     * Called before the block is broken or destroyed when the tile data needs to be persisted.
     * @param world The world.
     * @param blockPos The position of the to-be-destroyed block.
     */
    protected void onPreBlockDestroyedPersistence(World world, BlockPos blockPos) {
        onPreBlockDestroyed(world, blockPos);
        if (!world.isRemote) {
            MinecraftHelpers.preDestroyBlock(this, world, blockPos, saveNBTToDroppedItem());
        }
    }

    /**
     * Called after the block is broken or destroyed.
     * @param world The world.
     * @param blockPos The position of the destroyed block.
     */
    protected void onPostBlockDestroyed(World world, BlockPos blockPos) {
    	
    }
    
    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
        onPreBlockDestroyed(world, blockPos);
        super.breakBlock(world, blockPos, blockState);
        onPostBlockDestroyed(world, blockPos);
    }

    @Override
    public boolean removedByPlayer(IBlockState blockState, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        onPreBlockDestroyed(world, pos, player);
        return super.removedByPlayer(blockState, world, pos, player, willHarvest);
    }

    @Override
    public void onBlockExploded(World world, BlockPos blockPos, Explosion explosion) {
    	onPreBlockDestroyed(world, blockPos, null);
    	super.onBlockExploded(world, blockPos, explosion);
        onPostBlockDestroyed(world, blockPos);
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase entity, ItemStack stack) {
        if(entity != null) {
            CyclopsTileEntity tile = (CyclopsTileEntity) world.getTileEntity(blockPos);

            if(tile != null && stack.getTagCompound() != null) {
                stack.getTagCompound().setInteger("x", blockPos.getX());
                stack.getTagCompound().setInteger("y", blockPos.getY());
                stack.getTagCompound().setInteger("z", blockPos.getZ());
                tile.readFromNBT(stack.getTagCompound());
            }

            if(tile instanceof CyclopsTileEntity.ITickingTile) {
                ((CyclopsTileEntity.ITickingTile) tile).update();
            }
        }
        super.onBlockPlacedBy(world, blockPos, blockState, entity, stack);
    }
    
    /**
     * Write additional info about the tile into the item.
     * @param tile The tile that is being broken.
     * @param tag The tag that will be added to the dropped item.
     */
    public void writeAdditionalInfo(TileEntity tile, NBTTagCompound tag) {
    	
    }

    /**
     * If this block should drop its block item.
     * @param world The world.
     * @param pos The position.
     * @param blockState The block state.
     * @param fortune Fortune level.
     * @return If the item should drop.
     */
    public boolean isDropBlockItem(IBlockAccess world, BlockPos pos, IBlockState blockState, int fortune) {
        return true;
    }
    
    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState blockState, int fortune) {
        Item item = getItemDropped(blockState, new Random(), fortune);
        if(item != null && isDropBlockItem(world, pos, blockState, fortune)) {
            ItemStack itemStack = new ItemStack(item, 1, damageDropped(blockState));
            if (TileEntityNBTStorage.TILE != null) {
                itemStack = tileDataToItemStack(TileEntityNBTStorage.TILE, itemStack);
            }
            drops.add(itemStack);
        }
        
        MinecraftHelpers.postDestroyBlock(world, pos);
    }

    protected ItemStack tileDataToItemStack(CyclopsTileEntity tile, ItemStack itemStack) {
        if(isKeepNBTOnDrop()) {
            if (TileEntityNBTStorage.TAG != null) {
                itemStack.setTagCompound(TileEntityNBTStorage.TAG);
            }
            if (TileEntityNBTStorage.NAME != null) {
                itemStack.setStackDisplayName(TileEntityNBTStorage.NAME);
            }
        }
        return itemStack;
    }

    /**
     * If the NBT data of this blockState should be preserved in the item when it
     * is broken into an item.
     * @return If it should keep NBT data.
     */
    public boolean isKeepNBTOnDrop() {
		return true;
	}

	/**
     * If this blockState can be rotated.
     * @return Can be rotated.
     */
    public boolean isRotatable() {
        return rotatable;
    }

    /**
     * Set whether of not this container must be able to be rotated.
     * @param rotatable Can be rotated.
     */
    public void setRotatable(boolean rotatable) {
        this.rotatable = rotatable;
    }
    
    /**
     * Get the texture path of the GUI.
     * @return The path of the GUI for this blockState.
     */
    public String getGuiTexture() {
        return getGuiTexture("");
    }
    
    /**
     * Get the texture path of the GUI.
     * @param suffix Suffix to add to the path.
     * @return The path of the GUI for this blockState.
     */
    public String getGuiTexture(String suffix) {
        return getConfig().getMod().getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_GUI) + eConfig.getNamedId() + "_gui" + suffix + ".png";
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        ItemStack itemStack = super.getPickBlock(state, target, world, pos, player);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof CyclopsTileEntity && isKeepNBTOnDrop()) {
            CyclopsTileEntity ecTile = ((CyclopsTileEntity) tile);
            itemStack.setTagCompound(ecTile.getNBTTagCompound());
        }
        return itemStack;
    }

    @Override
    public final BlockContainerConfig getConfig() {
        return (BlockContainerConfig) this.eConfig;
    }

    @Override
    public boolean hasDynamicModel() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel createDynamicModel() {
        return null;
    }

    /**
     * Called for baking the model of this cable depending on its state.
     * @param event The bake event.
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event){
        if(hasDynamicModel()) {
            IBakedModel cableModel = createDynamicModel();
            event.getModelRegistry().putObject(eConfig.dynamicBlockVariantLocation, cableModel);
            event.getModelRegistry().putObject(eConfig.dynamicItemVariantLocation, cableModel);
        }
    }

}
