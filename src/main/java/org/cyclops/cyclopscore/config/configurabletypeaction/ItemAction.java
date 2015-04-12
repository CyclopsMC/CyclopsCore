package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.cyclops.cyclopscore.client.gui.GuiHandler;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.inventory.IGuiContainerProvider;

import java.util.List;

/**
 * The action used for {@link ItemConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class ItemAction extends ConfigurableTypeAction<ItemConfig>{

    @Override
    public void preRun(ItemConfig eConfig, Configuration config, boolean startup) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.getNamedId(),
        		eConfig.isEnabled());
        property.setRequiresMcRestart(true);
        property.comment = eConfig.getComment();
        
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
        
        // Register
        GameRegistry.registerItem(
                item,
                eConfig.getSubUniqueName()
        );
        
        // Set creative tab
        item.setCreativeTab(eConfig.getTargetTab());
        
        // Optionally register gui
        if(item instanceof IGuiContainerProvider) {
        	IGuiContainerProvider gui = (IGuiContainerProvider) item;
        	GuiHandler.registerGUI(gui, GuiHandler.GuiType.ITEM);
        }
    }

    @Override
    public void polish(ItemConfig config) {
        if(MinecraftHelpers.isClientSide()) {
            Item item = config.getItemInstance();
            if(item.getHasSubtypes()) {
                List<ItemStack> itemStacks = Lists.newLinkedList();
                item.getSubItems(item, config.getTargetTab(), itemStacks);
                for(ItemStack itemStack : itemStacks) {
                    String itemName = item.getUnlocalizedName(itemStack);
                    itemName =  itemName.substring(itemName.indexOf(".") + 1);

                    ModelBakery.addVariantName(item, config.getMod().getModId() + ":" + itemName);
                    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, itemStack.getMetadata(),
                            new ModelResourceLocation(config.getMod().getModId() + ":" + itemName, "inventory"));
                }
            } else {
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
                        new ModelResourceLocation(config.getMod().getModId() + ":" + config.getNamedId(), "inventory"));
            }
        }
    }

}
