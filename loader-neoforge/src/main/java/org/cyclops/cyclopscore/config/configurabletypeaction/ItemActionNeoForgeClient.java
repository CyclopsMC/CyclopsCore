package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;

/**
 * @author rubensworks
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ItemActionNeoForgeClient {
    @SubscribeEvent
    public static void onModelRegistryLoad(ModelEvent.RegisterStandalone event) {
        for (ItemConfigCommon<?> config : ItemActionNeoForge.MODEL_ENTRIES) {
            config.getItemClientConfig().dynamicItemVariantLocation = config.getItemClientConfig().registerDynamicModel();
        }
    }

    @SubscribeEvent
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event){
        for (ItemConfigCommon<?> config : ItemActionNeoForge.MODEL_ENTRIES) {
            IDynamicModelElementCommon dynamicModelElement = config.getItemClientConfig().getDynamicModelElement();
            if (config.getItemClientConfig().dynamicItemVariantLocation != null) {
                event.getBakingResult().itemStackModels().put(config.getItemClientConfig().dynamicItemVariantLocation, dynamicModelElement.createDynamicItemModel(pair -> event.getBakingResult().itemStackModels().put(pair.getLeft(), pair.getRight()), key -> event.getBakingResult().itemStackModels().get(key)));
            }
        }
    }
}
