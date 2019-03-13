package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.client.gui.GuiHandler;
import org.cyclops.cyclopscore.client.model.IDynamicModelElement;
import org.cyclops.cyclopscore.config.configurable.IConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.IModelProviderConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.inventory.IGuiContainerProvider;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The action used for {@link ItemConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class ItemAction extends ConfigurableTypeAction<ItemConfig>{

    private static final List<ExtendedConfig<?>> MODEL_ENTRIES = Lists.newArrayList();

    static {
        MinecraftForge.EVENT_BUS.register(ItemAction.class);
    }

    /**
     * Registers an item.
     * @param item The item instance.
     * @param config The config.
     * @param creativeTabs The creative tab this block will reside in.
     */
    public static void register(Item item, ExtendedConfig<?> config, @Nullable CreativeTabs creativeTabs) {
        register(item, config);

        if(creativeTabs != null) {
            item.setCreativeTab(creativeTabs);
        }
    }

    @Override
    public void preRun(ItemConfig eConfig, Configuration config, boolean startup) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.getNamedId(),
        		eConfig.isEnabled());
        property.setRequiresMcRestart(true);
        property.setComment(eConfig.getComment());
        property.setLanguageKey(eConfig.getFullTranslationKey());
        
        if(startup) {
	        // Update the ID, it could've changed
	        eConfig.setEnabled(property.getBoolean(true));
        }
    }

    @Override
    public void postRun(ItemConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        Item item = (Item) eConfig.getSubInstance();

        // Register item and set creative tab.
        register(item, eConfig, eConfig.getTargetTab());
        
        // Optionally register gui
        if(item instanceof IGuiContainerProvider) {
        	IGuiContainerProvider gui = (IGuiContainerProvider) item;
        	eConfig.getMod().getGuiHandler().registerGUI(gui, GuiHandler.GuiType.ITEM);
        }

        if(MinecraftHelpers.isClientSide()) {
            handleItemModel(eConfig);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegistryLoad(ModelRegistryEvent event) {
        for (ExtendedConfig<?> entry : MODEL_ENTRIES) {
            Item item = null;
            Block block = null;
            IModelProviderConfig modelProvider = null;
            if (entry instanceof ItemConfig) {
                item = ((ItemConfig) entry).getItemInstance();
                modelProvider = (IModelProviderConfig) entry;
            } else if (entry instanceof BlockConfig) {
                block = ((BlockConfig) entry).getBlockInstance();
                item = ((BlockConfig) entry).getItemInstance();
                modelProvider = (IModelProviderConfig) entry;
            } else {
                throw new IllegalStateException("An unsupported config was registered to the model loader: "
                        + entry.getNamedId());
            }

            String modId = item.getRegistryName().getNamespace();
            if(item.getHasSubtypes()) {
                NonNullList<ItemStack> itemStacks = NonNullList.create();
                item.getSubItems(item.getCreativeTab(), itemStacks);
                for(ItemStack itemStack : itemStacks) {
                    String itemName = modelProvider.getModelName(itemStack);
                    ModelResourceLocation model = new ModelResourceLocation(modId + ":" + itemName, "inventory");
                    ModelBakery.registerItemVariants(item, model);
                    ModelLoader.setCustomModelResourceLocation(item, itemStack.getMetadata(), model);
                }
            } else {
                ModelLoader.setCustomModelResourceLocation(item, 0,
                        new ModelResourceLocation(modId + ":" + item.getRegistryName().getPath(), "inventory"));
            }
            if (item instanceof IDynamicModelElement && ((IDynamicModelElement) item).hasDynamicModel()) {
                ItemConfig itemConfig = (ItemConfig) entry;
                itemConfig.dynamicItemVariantLocation = itemConfig.registerDynamicModel();
            }
        }
    }

    public static void handleItemModel(ExtendedConfig<?> extendedConfig) {
        if(MinecraftHelpers.isClientSide()) {
            MODEL_ENTRIES.add(extendedConfig);
        }
    }

    @Override
    public void polish(ItemConfig config) {
        super.polish(config);
        Item item = config.getItemInstance();
        if (item instanceof IConfigurableItem && MinecraftHelpers.isClientSide()) {
            IConfigurableItem configurableItem = (IConfigurableItem) item;
            IItemColor itemColorHandler = configurableItem.getItemColorHandler();
            if (itemColorHandler != null) {
                Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColorHandler, item);
            }
        }
    }
}
