package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.client.color.item.ItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.client.model.IDynamicModelElement;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * @author rubensworks
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ItemActionNeoForge<M extends ModBase> extends ItemAction<M> {

    @Override
    protected void polish(ItemConfigCommon<M> config) {
        super.polish(config);

        if(config.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            // Handle dynamic models
            if(config.getInstance() instanceof IDynamicModelElement &&
                    ((IDynamicModelElement) config.getInstance()).hasDynamicModel()) {
                ItemAction.handleItemModel(config);
            }

            // TODO: backwards-compat, remove the following in next major
            ItemColor itemColorHandler = ((ItemConfig) config).getItemColorHandler();
            if (itemColorHandler != null) {
                COLOR_ENTRIES.add(config);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModelRegistryLoad(ModelEvent.RegisterAdditional event) {
        for (ItemConfigCommon<?> config : MODEL_ENTRIES) {
            config.getItemClientConfig().dynamicItemVariantLocation = config.getItemClientConfig().registerDynamicModel();

            // TODO: backwards-compat, remove the following in next major
            ((ItemConfig) config).dynamicItemVariantLocation  = ((ItemConfig) config).registerDynamicModel();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event){
        for (ItemConfigCommon<?> config : MODEL_ENTRIES) {
            IDynamicModelElement dynamicModelElement = (IDynamicModelElement) config.getInstance();
            if (config.getItemClientConfig().dynamicItemVariantLocation != null) {
                event.getModels().put(config.getItemClientConfig().dynamicItemVariantLocation, dynamicModelElement.createDynamicModel(event));
            }

            // TODO: backwards-compat, remove the following in next major
            if (((ItemConfig) config).dynamicItemVariantLocation != null) {
                event.getModels().put(((ItemConfig) config).dynamicItemVariantLocation, dynamicModelElement.createDynamicModel(event));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Item event){
        for (ItemConfigCommon<?> itemConfig : COLOR_ENTRIES) {
            ItemColor colorHandler = itemConfig.getItemClientConfig().getItemColorHandler();
            if (colorHandler == null) {
                // TODO: backwards-compat, remove the following in next major
                colorHandler = ((ItemConfig) itemConfig).getItemColorHandler();
            }
            event.register(colorHandler, itemConfig.getInstance());
        }
    }

}
