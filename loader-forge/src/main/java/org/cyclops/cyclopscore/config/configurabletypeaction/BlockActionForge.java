package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * @author rubensworks
 */
public class BlockActionForge<M extends ModBaseForge<M>> extends BlockAction<M> {

    public BlockActionForge(M mod) {
        if (mod.getModHelpers().getMinecraftHelpers().isClientSide()) {
            ModelEvent.RegisterModelStateDefinitions.BUS.addListener(BlockActionForge::onModelRegistryLoad);
            ModelEvent.ModifyBakingResult.BUS.addListener(BlockActionForge::onModelBakeEvent);
            RegisterColorHandlersEvent.Block.BUS.addListener(BlockActionForge::onRegisterColorHandlers);
        }
    }

    @Override
    protected void polish(BlockConfigCommon<M> config) {
        super.polish(config);

        if(config.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            IDynamicModelElementCommon dynamicModelElement = config.getBlockClientConfig().getDynamicModelElement();
            if (dynamicModelElement != null) {
                BlockAction.handleDynamicBlockModel(config);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onModelRegistryLoad(ModelEvent.RegisterModelStateDefinitions event) {
        for (BlockConfigCommon<?> config : MODEL_ENTRIES) {
            Pair<BlockState, Identifier> resourceLocations = config.getBlockClientConfig().registerDynamicModel();
            config.getBlockClientConfig().dynamicBlockVariantLocation = resourceLocations.getLeft();
            config.getBlockClientConfig().dynamicItemVariantLocation = resourceLocations.getRight();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event){
        for (BlockConfigCommon<?> config : MODEL_ENTRIES) {
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

    @OnlyIn(Dist.CLIENT)
    public static <M extends ModBaseForge> void onRegisterColorHandlers(RegisterColorHandlersEvent.Block event){
        for (BlockConfigCommon<?> blockConfig : COLOR_ENTRIES) {
            event.register(blockConfig.getBlockClientConfig().getBlockColorHandler(), blockConfig.getInstance());
        }
    }

}
