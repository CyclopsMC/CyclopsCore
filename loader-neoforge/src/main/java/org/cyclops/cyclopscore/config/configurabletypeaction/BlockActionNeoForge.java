package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class BlockActionNeoForge extends BlockAction<ModBaseNeoForge<?>> {
    @Override
    protected void polish(BlockConfigCommon<ModBaseNeoForge<?>> config) {
        super.polish(config);

        if(config.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            // Handle dynamic models
            IDynamicModelElementCommon dynamicModelElement = config.getBlockClientConfig().getDynamicModelElement();
            if (dynamicModelElement != null) {
                BlockAction.handleDynamicBlockModel(config);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModelRegistryLoad(ModelEvent.RegisterStandalone event) {
        for (BlockConfigCommon<?> config : MODEL_ENTRIES) {
            Pair<BlockState, ResourceLocation> resourceLocations = config.getBlockClientConfig().registerDynamicModel();
            config.getBlockClientConfig().dynamicBlockVariantLocation = resourceLocations.getLeft();
            config.getBlockClientConfig().dynamicItemVariantLocation = resourceLocations.getRight();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event){
        for (BlockConfigCommon<?> config : MODEL_ENTRIES) {
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
