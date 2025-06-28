package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * @author rubensworks
 */
public class ItemActionForge<M extends ModBaseForge<M>> extends ItemAction<M> {

    public ItemActionForge(M mod) {
        if (mod.getModHelpers().getMinecraftHelpers().isClientSide()) {
            ModelEvent.RegisterModelStateDefinitions.getBus(mod.getModBusGroup()).addListener(ItemActionForge::onModelRegistryLoad);
            ModelEvent.ModifyBakingResult.getBus(mod.getModBusGroup()).addListener(ItemActionForge::onModelBakeEvent);
        }
    }

    @Override
    protected void polish(ItemConfigCommon<M> config) {
        super.polish(config);

        if(config.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            IDynamicModelElementCommon dynamicModelElement = config.getItemClientConfig().getDynamicModelElement();
            if (dynamicModelElement != null) {
                ItemAction.handleItemModel(config);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onModelRegistryLoad(ModelEvent.RegisterModelStateDefinitions event) {
        for (ItemConfigCommon<?> config : MODEL_ENTRIES) {
            config.getItemClientConfig().dynamicItemVariantLocation = config.getItemClientConfig().registerDynamicModel();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event){
        for (ItemConfigCommon<?> config : MODEL_ENTRIES) {
            IDynamicModelElementCommon dynamicModelElement = config.getItemClientConfig().getDynamicModelElement();
            if (config.getItemClientConfig().dynamicItemVariantLocation != null) {
                event.getResults().itemStackModels().put(config.getItemClientConfig().dynamicItemVariantLocation, dynamicModelElement.createDynamicItemModel(pair -> event.getResults().itemStackModels().put(pair.getLeft(), pair.getRight()), key -> event.getResults().itemStackModels().get(key)));
            }
        }
    }

}
