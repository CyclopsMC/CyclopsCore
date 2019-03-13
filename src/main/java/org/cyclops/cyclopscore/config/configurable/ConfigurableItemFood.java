package org.cyclops.cyclopscore.config.configurable;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
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
public class ConfigurableItemFood extends ItemFood implements IConfigurableItem {
    
    protected ItemConfig eConfig = null;
    
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param healAmount Amount of health to regen.
     * @param saturationModifier The modifier for the saturation.
     * @param isWolfsFavoriteMeat If this is wolf food.
     */
    public ConfigurableItemFood(ExtendedConfig<ItemConfig> eConfig, int healAmount, float saturationModifier, boolean isWolfsFavoriteMeat) {
        super(healAmount, saturationModifier, isWolfsFavoriteMeat);
        this.setConfig((ItemConfig)eConfig); // TODO change eConfig to just be an ItemConfig
        this.setTranslationKey(eConfig.getTranslationKey());
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

}
