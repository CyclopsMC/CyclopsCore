package org.cyclops.cyclopscore.config.configurable;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Item food that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public class ConfigurableItemBucket extends ItemBucket implements IConfigurableItem {
    
    protected ItemConfig eConfig = null;
    
    protected boolean canPickUp = true;

    private final FluidStack fluidStack;
    
    /**
     * Make a new bucket instance.
     * @param eConfig Config for this blockState.
     * @param block The fluid blockState it can pick up.
     * @param fluidStack The filled fluid.
     */
    public ConfigurableItemBucket(ExtendedConfig<ItemConfig> eConfig, Block block, FluidStack fluidStack) {
        super(block);
        this.setConfig((ItemConfig)eConfig); // TODO change eConfig to just be an ItemConfig
        this.setTranslationKey(eConfig.getTranslationKey());
        setContainerItem(Items.BUCKET);
        this.fluidStack = fluidStack;
    }

    private void setConfig(ItemConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ItemConfig getConfig() {
        return eConfig;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        L10NHelpers.addOptionalInfo(list, getTranslationKey());
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public IItemColor getItemColorHandler() {
        return null;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new BucketWrapper(stack);
    }

    public class BucketWrapper extends FluidBucketWrapper {

        public BucketWrapper(ItemStack container) {
            super(container);
        }

        @Nullable
        @Override
        public FluidStack getFluid() {
            return fluidStack;
        }
    }
}
