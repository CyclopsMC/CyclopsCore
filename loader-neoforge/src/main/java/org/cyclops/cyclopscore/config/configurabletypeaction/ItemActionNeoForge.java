package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

/**
 * @author rubensworks
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ItemActionNeoForge<M extends ModBaseNeoForge<M>> extends ItemAction<M> {

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
    @SubscribeEvent
    public static void onModelRegistryLoad(ModelEvent.RegisterAdditional event) {
        for (ItemConfigCommon<?> config : MODEL_ENTRIES) {
            config.getItemClientConfig().dynamicItemVariantLocation = config.getItemClientConfig().registerDynamicModel();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event){
        for (ItemConfigCommon<?> config : MODEL_ENTRIES) {
            IDynamicModelElementCommon dynamicModelElement = config.getItemClientConfig().getDynamicModelElement();
            if (config.getItemClientConfig().dynamicItemVariantLocation != null) {
                event.getBakingResult().blockStateModels().put(config.getItemClientConfig().dynamicItemVariantLocation, dynamicModelElement.createDynamicModel(pair -> event.getBakingResult().blockStateModels().put(pair.getLeft(), pair.getRight())));
            }
        }
    }

}
