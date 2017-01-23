package org.cyclops.cyclopscore.config.configurable;

import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
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
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import javax.annotation.Nullable;

/**
 * Block that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public class ConfigurableBlock extends Block implements IConfigurableBlock, IDynamicModelElement {

    @Delegate protected IBlockPropertyManager propertyManager;
    @Override protected BlockStateContainer createBlockState() {
        return (propertyManager = new BlockPropertyManagerComponent(this)).createDelegatedBlockState();
    }

    @SuppressWarnings("rawtypes")
    protected BlockConfig eConfig = null;
    protected boolean hasGui = false;
    protected int pass = 0;
    protected boolean isInventoryBlock = false;
    
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param material Material of this blockState.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlock(ExtendedConfig eConfig, Material material) {
        super(material);
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
        if(MinecraftHelpers.isClientSide() && hasDynamicModel()) MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean hasGui() {
        return hasGui;
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
                                     int meta, EntityLivingBase placer) {
        IBlockState blockState = super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        for(IProperty property : blockState.getPropertyKeys()) {
            if(property.getName().equals("facing")) {
                blockState.withProperty(property, placer.getHorizontalFacing());
            }
        }
        return blockState;
    }

    private void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig) {
        this.eConfig = (BlockConfig) eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }

    @Override
    public boolean hasDynamicModel() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
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
            event.getModelRegistry().putObject(eConfig.dynamicItemVariantLocation , cableModel);
        }
    }

}
