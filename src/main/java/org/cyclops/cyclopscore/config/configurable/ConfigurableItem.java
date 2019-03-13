package org.cyclops.cyclopscore.config.configurable;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.client.model.IDynamicModelElement;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Item that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public class ConfigurableItem extends Item implements IConfigurableItem, IDynamicModelElement {
    
    protected ItemConfig eConfig = null;
    
    /**
     * Make a new item instance.
     * @param eConfig Config for this blockState.
     */
    public ConfigurableItem(ExtendedConfig<ItemConfig> eConfig) {
        this.setConfig((ItemConfig)eConfig); // TODO change eConfig to just be an ItemConfig
        this.setTranslationKey(eConfig.getTranslationKey());
        if(MinecraftHelpers.isClientSide() && hasDynamicModel()) MinecraftForge.EVENT_BUS.register(this);
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
        L10NHelpers.addOptionalInfo(list, getTranslationKey(itemStack));
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
            IBakedModel model = createDynamicModel();
            event.getModelRegistry().putObject(eConfig.dynamicItemVariantLocation, model);
        }
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public IItemColor getItemColorHandler() {
        return null;
    }
}
