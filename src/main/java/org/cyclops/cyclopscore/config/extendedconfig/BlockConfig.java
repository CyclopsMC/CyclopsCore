package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.item.ItemBlockExtended;

/**
 * Config for blocks.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class BlockConfig extends ExtendedConfig<BlockConfig> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public BlockConfig(ModBase mod, boolean enabled, String namedId, String comment, Class<? extends Block> element) {
        super(mod, enabled, namedId, comment, element);
    }

    @Override
	public String getUnlocalizedName() {
		return "blocks." + getMod().getModId() + "." + getNamedId();
	}

    @Override
    public String getFullUnlocalizedName() {
        return "tile." + getUnlocalizedName() + ".name";
    }
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.BLOCK;
	}
    
    /**
     * If hasSubTypes() returns true this method can be overwritten to define another ItemBlock class
     * @return the ItemBlock class to use for the target blockState.
     */
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockExtended.class;
    }
    
    /**
     * If the IConfigurable is registered in the OreDictionary, use this name to identify it.
     * @return the name this IConfigurable is registered with in the OreDictionary.
     */
    public String getOreDictionaryId() {
        return null;
    }
    
    /**
     * If this blockState should enable Forge Multiparts and BC facades.
     * @return If that should be enabled for this blockState.
     */
    public boolean isMultipartEnabled() {
        return false;
    }

    /**
     * Get the casted instance of the blockState.
     * @return The blockState.
     */
    public Block getBlockInstance() {
        return (Block) super.getSubInstance();
    }

    /**
     * Get the creative tab for this item.
     * @return The creative tab, by default the value in {@link org.cyclops.cyclopscore.init.ModBase#getDefaultCreativeTab()}.
     */
    public CreativeTabs getTargetTab() {
        return getMod().getDefaultCreativeTab();
    }

}
