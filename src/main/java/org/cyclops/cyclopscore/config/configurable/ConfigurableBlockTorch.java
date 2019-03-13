package org.cyclops.cyclopscore.config.configurable;

import lombok.experimental.Delegate;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.block.property.BlockPropertyManagerComponent;
import org.cyclops.cyclopscore.block.property.IBlockPropertyManager;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import javax.annotation.Nullable;

/**
 * Torch blockState that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public class ConfigurableBlockTorch extends BlockTorch implements IConfigurableBlock {

    @Delegate private IBlockPropertyManager propertyManager;
    @Override protected BlockStateContainer createBlockState() {
        return (propertyManager = new BlockPropertyManagerComponent(this)).createDelegatedBlockState();
    }

    // This is to make sure that the MC properties are also loaded.
    @BlockProperty
    public static final IProperty[] _COMPAT = {FACING};

    protected BlockConfig eConfig = null;
    protected boolean hasGui = false;

    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     */
    public ConfigurableBlockTorch(ExtendedConfig<BlockConfig> eConfig) {
        this.setConfig((BlockConfig)eConfig); // TODO change eConfig to just be a BlockConfig
        this.setTranslationKey(eConfig.getTranslationKey());
        this.setHardness(0.0F);
        this.setLightLevel(0.9375F);
        this.setSoundType(SoundType.WOOD);
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

    private void setConfig(BlockConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public BlockConfig getConfig() {
        return eConfig;
    }

}
