package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.cyclops.cyclopscore.client.gui.GuiHandler;
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

    /**
     * Registers an item.
     * @param item The item instance.
     * @param config The config.
     * @param creativeTabs The creative tab this block will reside in.
     */
    public static void register(Item item, ExtendedConfig config, @Nullable CreativeTabs creativeTabs) {
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
    }

    public static void handleItemModel(Item item, String namedId, CreativeTabs tab, String modId, IModelProviderConfig modelProvider) {
        if(MinecraftHelpers.isClientSide()) {
            if(item.getHasSubtypes()) {
                List<ItemStack> itemStacks = Lists.newLinkedList();
                item.getSubItems(item, tab, itemStacks);
                for(ItemStack itemStack : itemStacks) {
                    String itemName = modelProvider.getModelName(itemStack);
                    ModelResourceLocation model = new ModelResourceLocation(modId + ":" + itemName, "inventory");
                    ModelBakery.registerItemVariants(item, model);
                    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, itemStack.getMetadata(), model);
                }
            } else {
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
                        new ModelResourceLocation(modId + ":" + namedId, "inventory"));
            }
        }
    }

    @Override
    public void polish(ItemConfig config) {
        Item item = config.getItemInstance();
        handleItemModel(item, config.getNamedId(), config.getTargetTab(),
                config.getMod().getModId(), config);
        if(item instanceof IItemColor) {
            Minecraft.getMinecraft().getItemColors().registerItemColorHandler((IItemColor) item, item);
        }
    }

}
