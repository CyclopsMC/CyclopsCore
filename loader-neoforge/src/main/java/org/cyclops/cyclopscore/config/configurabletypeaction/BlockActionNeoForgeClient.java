package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;

/**
 * @author rubensworks
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class BlockActionNeoForgeClient {
    @SubscribeEvent
    public static void onModelRegistryLoad(ModelEvent.RegisterStandalone event) {
        for (BlockConfigCommon<?> config : BlockActionNeoForge.MODEL_ENTRIES) {
            Pair<BlockState, Identifier> resourceLocations = config.getBlockClientConfig().registerDynamicModel();
            config.getBlockClientConfig().dynamicBlockVariantLocation = resourceLocations.getLeft();
            config.getBlockClientConfig().dynamicItemVariantLocation = resourceLocations.getRight();
        }
    }

    @SubscribeEvent
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event){
        for (BlockConfigCommon<?> config : BlockActionNeoForge.MODEL_ENTRIES) {
            IDynamicModelElementCommon dynamicModelElement = config.getBlockClientConfig().getDynamicModelElement();
            BlockStateModel dynamicBlockModel = dynamicModelElement.createDynamicBlockModel(pair -> event.getBakingResult().blockStateModels().put(pair.getLeft(), pair.getRight()), key -> event.getBakingResult().blockStateModels().get(key));
            ItemModel dynamicItemModel = dynamicModelElement.createDynamicItemModel(pair -> event.getBakingResult().itemStackModels().put(pair.getLeft(), pair.getRight()), key -> event.getBakingResult().itemStackModels().get(key));

            if (config.getBlockClientConfig().dynamicBlockVariantLocation != null) {
                event.getBakingResult().blockStateModels().put(config.getBlockClientConfig().dynamicBlockVariantLocation, dynamicBlockModel);
            }
            if (config.getBlockClientConfig().dynamicItemVariantLocation != null) {
                event.getBakingResult().itemStackModels().put(config.getBlockClientConfig().dynamicItemVariantLocation, dynamicItemModel);
            }
        }
    }
}
