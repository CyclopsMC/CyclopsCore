package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.cyclopscore.client.gui.GuiHandler;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainerGui;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * The action used for {@link BlockConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class BlockAction extends ConfigurableTypeAction<BlockConfig> {

    @Override
    public void preRun(BlockConfig eConfig, Configuration config, boolean startup) {
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
    public void postRun(BlockConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();

        Block block = (Block) eConfig.getSubInstance();

        // Register
        GameRegistry.registerBlock(
                block,
                eConfig.getItemBlockClass(),
                eConfig.getSubUniqueName()
                );

        // Set creative tab
        block.setCreativeTab(eConfig.getTargetTab());

        // Also register tile entity
        if(eConfig.getHolderType().equals(ConfigurableType.BLOCKCONTAINER)) {
            ConfigurableBlockContainer container = (ConfigurableBlockContainer) block;
            GameRegistry.registerTileEntity(container.getTileEntity(), eConfig.getSubUniqueName());
            
            // If the blockState has a GUI, go ahead and register that.
            if(container.hasGui()) {
                ConfigurableBlockContainerGui gui = (ConfigurableBlockContainerGui) container;
                eConfig.getMod().getGuiHandler().registerGUI(gui, GuiHandler.GuiType.BLOCK);
            }
        }
        
        // Register optional ore dictionary ID
        if(eConfig.getOreDictionaryId() != null) {
            OreDictionary.registerOre(eConfig.getOreDictionaryId(), new ItemStack((Block)eConfig.getSubInstance()));
        }
        
        // Register third-party mod blockState parts.
        // TODO: enable when FMP is updated.
        /*if(eConfig.isMultipartEnabled()) {
            ForgeMultipartHelper.registerMicroblock(eConfig);
        }*/
    }

    @Override
    public void polish(BlockConfig config) {
        if (MinecraftHelpers.isClientSide()) {
            ItemAction.handleItemModel(Item.getItemFromBlock(config.getBlockInstance()), config.getNamedId(),
                    config.getTargetTab(), config.getMod().getModId(), config);
        }
    }

}
