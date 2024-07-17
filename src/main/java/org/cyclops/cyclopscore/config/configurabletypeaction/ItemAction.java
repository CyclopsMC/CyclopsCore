package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.client.model.IDynamicModelElement;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.List;

/**
 * The action used for {@link ItemConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ItemAction extends ConfigurableTypeActionForge<ItemConfig, Item>{

    private static final List<ItemConfig> MODEL_ENTRIES = Lists.newArrayList();
    private static final List<ItemConfig> COLOR_ENTRIES = Lists.newArrayList();

    @Override
    public void onRegisterForgeFilled(ItemConfig eConfig) {
        // Register item and set creative tab.
        register(eConfig, () -> {
            this.polish(eConfig);
            eConfig.onForgeRegistered();
            return null;
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModelRegistryLoad(ModelEvent.RegisterAdditional event) {
        for (ItemConfig config : MODEL_ENTRIES) {
            config.dynamicItemVariantLocation  = config.registerDynamicModel();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event){
        for (ItemConfig config : MODEL_ENTRIES) {
            IDynamicModelElement dynamicModelElement = (IDynamicModelElement) config.getInstance();
            if (config.dynamicItemVariantLocation != null) {
                event.getModels().put(config.dynamicItemVariantLocation, dynamicModelElement.createDynamicModel(event));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Item event){
        for (ItemConfig itemConfig : COLOR_ENTRIES) {
            event.register(itemConfig.getItemColorHandler(), itemConfig.getInstance());
        }
    }

    public static void handleItemModel(ItemConfig extendedConfig) {
        MODEL_ENTRIES.add(extendedConfig);
    }

    protected void polish(ItemConfig config) {
        if (MinecraftHelpers.isClientSide()) {
            ItemColor itemColorHandler = config.getItemColorHandler();
            if (itemColorHandler != null) {
                COLOR_ENTRIES.add(config);
            }
        }
    }
}
