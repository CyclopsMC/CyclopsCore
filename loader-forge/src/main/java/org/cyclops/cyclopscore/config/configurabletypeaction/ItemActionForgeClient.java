package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;

/**
 * @author rubensworks
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ItemActionForgeClient {

    @SubscribeEvent
    public static void onModelRegistryLoad(ModelEvent.RegisterModelStateDefinitions event) {
        for (ItemConfigCommon<?> config : ItemActionForge.MODEL_ENTRIES) {
            config.getItemClientConfig().dynamicItemVariantLocation = config.getItemClientConfig().registerDynamicModel();
        }
    }

    @SubscribeEvent
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event) {
        for (ItemConfigCommon<?> config : ItemActionForge.MODEL_ENTRIES) {
            IDynamicModelElementCommon dynamicModelElement = config.getItemClientConfig().getDynamicModelElement();
            if (config.getItemClientConfig().dynamicItemVariantLocation != null) {
                event.getResults().itemStackModels().put(config.getItemClientConfig().dynamicItemVariantLocation, dynamicModelElement.createDynamicItemModel(pair -> event.getResults().itemStackModels().put(pair.getLeft(), pair.getRight()), key -> event.getResults().itemStackModels().get(key)));
            }
        }
    }

}
