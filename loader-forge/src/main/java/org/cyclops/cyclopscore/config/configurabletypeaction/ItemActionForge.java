package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.helper.ModHelpersForge;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * @author rubensworks
 */
public class ItemActionForge<M extends ModBaseForge> extends ItemAction<M> {

    static {
        if (ModHelpersForge.INSTANCE.getMinecraftHelpers().isClientSide()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener((ModelEvent.RegisterModelStateDefinitions event) -> ItemActionForge.onModelRegistryLoad(event));
            FMLJavaModLoadingContext.get().getModEventBus().addListener((ModelEvent.ModifyBakingResult event) -> ItemActionForge.onModelBakeEvent(event));
        }
    }

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
    public static void onModelRegistryLoad(ModelEvent.RegisterModelStateDefinitions event) {
        for (ItemConfigCommon<?> config : MODEL_ENTRIES) {
            config.getItemClientConfig().dynamicItemVariantLocation = config.getItemClientConfig().registerDynamicModel();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event){
        for (ItemConfigCommon<?> config : MODEL_ENTRIES) {
            IDynamicModelElementCommon dynamicModelElement = config.getItemClientConfig().getDynamicModelElement();
            if (config.getItemClientConfig().dynamicItemVariantLocation != null) {
                event.getResults().blockStateModels().put(config.getItemClientConfig().dynamicItemVariantLocation, dynamicModelElement.createDynamicModel(pair -> event.getResults().blockStateModels().put(pair.getLeft(), pair.getRight()), key -> event.getResults().blockStateModels().get(key)));
            }
        }
    }

}
