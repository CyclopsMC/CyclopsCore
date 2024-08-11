package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.client.model.IDynamicModelElement;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * @author rubensworks
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class BlockActionNeoForge<M extends ModBase> extends BlockAction<M> {

    @Override
    protected void polish(BlockConfigCommon<M> config) {
        super.polish(config);

        if(config.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            // Handle dynamic models
            if(config.getInstance() instanceof IDynamicModelElement &&
                    ((IDynamicModelElement) config.getInstance()).hasDynamicModel()) {
                BlockAction.handleDynamicBlockModel(config);
            }

            // TODO: backwards-compat, remove the following in next major
            if (config instanceof BlockConfig blockConfig) {
                BlockColor blockColorHandler = blockConfig.getBlockColorHandler();
                if (blockColorHandler != null) {
                    COLOR_ENTRIES.add(config);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModelRegistryLoad(ModelEvent.RegisterAdditional event) {
        for (BlockConfigCommon<?> config : MODEL_ENTRIES) {
            Pair<ModelResourceLocation, ModelResourceLocation> resourceLocations = config.getBlockClientConfig().registerDynamicModel();
            config.getBlockClientConfig().dynamicBlockVariantLocation = resourceLocations.getLeft();
            config.getBlockClientConfig().dynamicItemVariantLocation = resourceLocations.getRight();

            // TODO: backwards-compat, remove the following in next major
            resourceLocations = ((BlockConfig) config).registerDynamicModel();
            ((BlockConfig) config).dynamicBlockVariantLocation = resourceLocations.getLeft();
            ((BlockConfig) config).dynamicItemVariantLocation  = resourceLocations.getRight();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event){
        for (BlockConfigCommon<?> config : MODEL_ENTRIES) {
            IDynamicModelElement dynamicModelElement = (IDynamicModelElement) config.getInstance();
            BakedModel dynamicModel = dynamicModelElement.createDynamicModel(event);
            if (config.getBlockClientConfig().dynamicBlockVariantLocation != null) {
                event.getModels().put(config.getBlockClientConfig().dynamicBlockVariantLocation, dynamicModel);
            }
            if (config.getBlockClientConfig().dynamicItemVariantLocation != null) {
                event.getModels().put(config.getBlockClientConfig().dynamicItemVariantLocation, dynamicModel);
            }

            // TODO: backwards-compat, remove the following in next major
            if (((BlockConfig) config).dynamicBlockVariantLocation != null) {
                event.getModels().put(((BlockConfig) config).dynamicBlockVariantLocation, dynamicModel);
            }
            if (((BlockConfig) config).dynamicItemVariantLocation != null) {
                event.getModels().put(((BlockConfig) config).dynamicItemVariantLocation, dynamicModel);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static <M extends ModBase> void onRegisterColorHandlers(RegisterColorHandlersEvent.Block event){
        for (BlockConfigCommon<?> blockConfig : COLOR_ENTRIES) {
            BlockColor colorHander = blockConfig.getBlockClientConfig().getBlockColorHandler();
            if (colorHander == null) {
                // TODO: backwards-compat, remove the following in next major
                ((BlockConfig) blockConfig).getBlockColorHandler();
            }
            event.register(colorHander, blockConfig.getInstance());
        }
    }

}
