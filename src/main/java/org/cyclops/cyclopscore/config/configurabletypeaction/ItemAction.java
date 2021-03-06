package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.client.model.IDynamicModelElement;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.List;

/**
 * The action used for {@link ItemConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class ItemAction extends ConfigurableTypeActionForge<ItemConfig, Item>{

    private static final List<ItemConfig> MODEL_ENTRIES = Lists.newArrayList();

    static {
        if (MinecraftHelpers.isClientSide()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemAction::onModelRegistryLoad);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemAction::onModelBakeEvent);
        }
    }

    @Override
    public void onRegisterForge(ItemConfig eConfig) {
        // Register item and set creative tab.
        register(eConfig, () -> {
            this.polish(eConfig);
            eConfig.onForgeRegistered();
            return null;
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void onModelRegistryLoad(ModelRegistryEvent event) {
        for (ItemConfig config : MODEL_ENTRIES) {
            config.dynamicItemVariantLocation  = config.registerDynamicModel();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onModelBakeEvent(ModelBakeEvent event){
        for (ItemConfig config : MODEL_ENTRIES) {
            IDynamicModelElement dynamicModelElement = (IDynamicModelElement) config.getInstance();
            if (config.dynamicItemVariantLocation != null) {
                event.getModelRegistry().put(config.dynamicItemVariantLocation, dynamicModelElement.createDynamicModel(event));
            }
        }
    }

    public static void handleItemModel(ItemConfig extendedConfig) {
        MODEL_ENTRIES.add(extendedConfig);
    }

    protected void polish(ItemConfig config) {
        if (MinecraftHelpers.isClientSide()) {
            IItemColor itemColorHandler = config.getItemColorHandler();
            if (itemColorHandler != null) {
                Minecraft.getInstance().getItemColors().register(itemColorHandler, config.getInstance());
            }
        }
    }
}
