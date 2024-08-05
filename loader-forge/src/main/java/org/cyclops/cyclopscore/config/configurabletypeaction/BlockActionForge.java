package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.client.model.IDynamicModelElement;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.helper.ModHelpersForge;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * @author rubensworks
 */
public class BlockActionForge<M extends ModBaseForge<M>> extends BlockAction<M> {

    static {
        if (ModHelpersForge.INSTANCE.getMinecraftHelpers().isClientSide()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener((ModelEvent.RegisterAdditional event) -> BlockActionForge.onModelRegistryLoad(event));
            FMLJavaModLoadingContext.get().getModEventBus().addListener((ModelEvent.ModifyBakingResult event) -> BlockActionForge.onModelBakeEvent(event));
            FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterColorHandlersEvent.Block event) -> BlockActionForge.onRegisterColorHandlers(event));
        }
    }

    @Override
    protected void polish(BlockConfigCommon<M> config) {
        super.polish(config);

        if(config.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            // Handle dynamic models
            if(config.getInstance() instanceof IDynamicModelElement &&
                    ((IDynamicModelElement) config.getInstance()).hasDynamicModel()) {
                BlockAction.handleDynamicBlockModel(config);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onModelRegistryLoad(ModelEvent.RegisterAdditional event) {
        for (BlockConfigCommon<?> config : MODEL_ENTRIES) {
            Pair<ModelResourceLocation, ModelResourceLocation> resourceLocations = config.getBlockClientConfig().registerDynamicModel();
            config.getBlockClientConfig().dynamicBlockVariantLocation = resourceLocations.getLeft();
            config.getBlockClientConfig().dynamicItemVariantLocation = resourceLocations.getRight();
        }
    }

    @OnlyIn(Dist.CLIENT)
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
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static <M extends ModBaseForge> void onRegisterColorHandlers(RegisterColorHandlersEvent.Block event){
        for (BlockConfigCommon<?> blockConfig : COLOR_ENTRIES) {
            event.register(blockConfig.getBlockClientConfig().getBlockColorHandler(), blockConfig.getInstance());
        }
    }

}
