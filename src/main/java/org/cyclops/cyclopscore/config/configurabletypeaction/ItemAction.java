package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.IModelProviderConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.List;

/**
 * The action used for {@link ItemConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class ItemAction extends ConfigurableTypeActionForge<ItemConfig, Item>{

    private static final List<ExtendedConfig<?, ?>> MODEL_ENTRIES = Lists.newArrayList();

    static {
        MinecraftForge.EVENT_BUS.register(ItemAction.class);
    }

    @Override
    public void onRegisterForge(ItemConfig eConfig) {
        // Register item and set creative tab.
        register(eConfig, () -> {
            this.polish(eConfig);
            eConfig.onForgeRegistered();
            return null;
        });

        if(MinecraftHelpers.isClientSide()) {
            handleItemModel(eConfig);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModelRegistryLoad(ModelRegistryEvent event) {
        for (ExtendedConfig<?, ?> entry : MODEL_ENTRIES) {
            Item item = null;
            Block block = null;
            IModelProviderConfig modelProvider = null;
            if (entry instanceof ItemConfig) {
                item = ((ItemConfig) entry).getInstance();
                modelProvider = (IModelProviderConfig) entry;
            } else if (entry instanceof BlockConfig) {
                block = ((BlockConfig) entry).getInstance();
                item = ((BlockConfig) entry).getItemInstance();
                modelProvider = (IModelProviderConfig) entry;
            } else {
                throw new IllegalStateException("An unsupported config was registered to the model loader: "
                        + entry.getNamedId());
            }

            String modId = item.getRegistryName().getNamespace();
            // TODO: handle mode mapping
//            if(item.getHasSubtypes()) {
//                NonNullList<ItemStack> itemStacks = NonNullList.create();
//                item.getSubItems(item.getCreativeTab(), itemStacks);
//                for(ItemStack itemStack : itemStacks) {
//                    String itemName = modelProvider.getModelName(itemStack);
//                    ModelResourceLocation model = new ModelResourceLocation(modId + ":" + itemName, "inventory");
//                    ModelBakery.registerItemVariants(item, model);
//                    ModelLoader.setCustomModelResourceLocation(item, itemStack.getMetadata(), model);
//                }
//            } else {
//                ModelLoader.setCustomModelResourceLocation(item, 0,
//                        new ModelResourceLocation(modId + ":" + item.getRegistryName().getPath(), "inventory"));
//            }
//            if (item instanceof IDynamicModelElement && ((IDynamicModelElement) item).hasDynamicModel()) {
//                ItemConfig itemConfig = (ItemConfig) entry;
//                itemConfig.dynamicItemVariantLocation = itemConfig.registerDynamicModel();
//            }
        }
    }

    public static void handleItemModel(ExtendedConfig<?, ?> extendedConfig) {
        if(MinecraftHelpers.isClientSide()) {
            MODEL_ENTRIES.add(extendedConfig);
        }
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
