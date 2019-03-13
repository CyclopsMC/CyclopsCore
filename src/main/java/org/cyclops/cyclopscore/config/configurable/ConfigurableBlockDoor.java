package org.cyclops.cyclopscore.config.configurable;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.BlockDoorConfig;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Door blockState that can hold ExtendedConfigs
 * @author josephcsible
 *
 */
public class ConfigurableBlockDoor extends BlockDoor implements IConfigurableBlock {
    public Item item;

    // Note the intentional lack of any block state or property related code.
    // There's only room for 1 more bit of information, and using it would be
    // exceedingly complex, so code to use it won't be written unless/until a
    // door actually needs it for something.

    protected BlockDoorConfig eConfig = null;
    protected boolean hasGui = false;

    /**
     * Make a new blockState instance.
     * @param config Config for this blockState.
     * @param material The door material.
     */
    public ConfigurableBlockDoor(BlockDoorConfig config, Material material) {
        super(material);
        setConfig(config);
        setTranslationKey(config.getTranslationKey());
        disableStats();
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(item);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? Items.AIR : item;
    }

    // overriding to make it public instead of protected
    @Override
    public ConfigurableBlockDoor setSoundType(SoundType sound) {
        super.setSoundType(sound);
        return this;
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

    private void setConfig(BlockDoorConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public BlockDoorConfig getConfig() {
        return eConfig;
    }
}
