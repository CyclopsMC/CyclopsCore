package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;

import java.util.List;

/**
 * @author rubensworks
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class BlockActionForgeClient {

    @SubscribeEvent
    public static void onModelRegistryLoad(ModelEvent.RegisterModelStateDefinitions event) {
        for (BlockConfigCommon<?> config : BlockActionForge.MODEL_ENTRIES) {
            Pair<BlockState, Identifier> resourceLocations = config.getBlockClientConfig().registerDynamicModel();
            config.getBlockClientConfig().dynamicBlockVariantLocation = resourceLocations.getLeft();
            config.getBlockClientConfig().dynamicItemVariantLocation = resourceLocations.getRight();
        }
    }

    @SubscribeEvent
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event) {
        for (BlockConfigCommon<?> config : BlockActionForge.MODEL_ENTRIES) {
            IDynamicModelElementCommon dynamicModelElement = config.getBlockClientConfig().getDynamicModelElement();
            BlockStateModel dynamicBlockModel = dynamicModelElement.createDynamicBlockModel(pair -> event.getResults().blockStateModels().put(pair.getLeft(), pair.getRight()), key -> event.getResults().blockStateModels().get(key));
            ItemModel dynamicItemModel = dynamicModelElement.createDynamicItemModel(pair -> event.getResults().itemStackModels().put(pair.getLeft(), pair.getRight()), key -> event.getResults().itemStackModels().get(key));

            if (config.getBlockClientConfig().dynamicBlockVariantLocation != null) {
                event.getResults().blockStateModels().put(config.getBlockClientConfig().dynamicBlockVariantLocation, dynamicBlockModel);
            }
            if (config.getBlockClientConfig().dynamicItemVariantLocation != null) {
                event.getResults().itemStackModels().put(config.getBlockClientConfig().dynamicItemVariantLocation, dynamicItemModel);
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Block event) {
        for (BlockConfigCommon<?> blockConfig : BlockActionForge.COLOR_ENTRIES) {
            event.register(List.of(blockConfig.getBlockClientConfig().getBlockColorHandler()), blockConfig.getInstance());
        }
    }

}
