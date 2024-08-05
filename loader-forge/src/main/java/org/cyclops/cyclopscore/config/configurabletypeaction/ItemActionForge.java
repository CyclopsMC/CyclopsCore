package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.client.model.IDynamicModelElement;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.helper.ModHelpersForge;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * @author rubensworks
 */
public class ItemActionForge<M extends ModBaseForge> extends ItemAction<M> {

    static {
        if (ModHelpersForge.INSTANCE.getMinecraftHelpers().isClientSide()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener((ModelEvent.RegisterAdditional event) -> ItemActionForge.onModelRegistryLoad(event));
            FMLJavaModLoadingContext.get().getModEventBus().addListener((ModelEvent.ModifyBakingResult event) -> ItemActionForge.onModelBakeEvent(event));
            FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterColorHandlersEvent.Item event) -> ItemActionForge.onRegisterColorHandlers(event));
        }
    }

    @Override
    protected void polish(ItemConfigCommon<M> config) {
        super.polish(config);

        if(config.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            // Handle dynamic models
            if(config.getInstance() instanceof IDynamicModelElement &&
                    ((IDynamicModelElement) config.getInstance()).hasDynamicModel()) {
                ItemAction.handleItemModel(config);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onModelRegistryLoad(ModelEvent.RegisterAdditional event) {
        for (ItemConfigCommon<?> config : MODEL_ENTRIES) {
            config.getItemClientConfig().dynamicItemVariantLocation = config.getItemClientConfig().registerDynamicModel();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event){
        for (ItemConfigCommon<?> config : MODEL_ENTRIES) {
            IDynamicModelElement dynamicModelElement = (IDynamicModelElement) config.getInstance();
            if (config.getItemClientConfig().dynamicItemVariantLocation != null) {
                event.getModels().put(config.getItemClientConfig().dynamicItemVariantLocation, dynamicModelElement.createDynamicModel(event));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Item event){
        for (ItemConfigCommon<?> itemConfig : COLOR_ENTRIES) {
            event.register(itemConfig.getItemClientConfig().getItemColorHandler(), itemConfig.getInstance());
        }
    }

}
