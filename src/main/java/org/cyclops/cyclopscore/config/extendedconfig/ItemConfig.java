package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for items.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class ItemConfig extends ExtendedConfig<ItemConfig> implements IModelProviderConfig {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.O
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public ItemConfig(ModBase mod, boolean enabled, String namedId, String comment, Class<? extends Item> element) {
        super(mod, enabled, namedId, comment, element);
    }

    @Override
    protected IConfigurable initSubInstance() {
        return this.getElement() == null ? new ConfigurableItem(this) : super.initSubInstance();
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        return getNamedId();
    }
    
    @Override
	public String getUnlocalizedName() {
		return "items." + getMod().getModId() + "." + getNamedId();
	}

    @Override
    public String getFullUnlocalizedName() {
        return "item." + getUnlocalizedName() + ".name";
    }
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.ITEM;
	}
    
    /**
     * If the IConfigurable is registered in the OreDictionary, use this name to identify it.
     * @return the name this IConfigurable is registered with in the OreDictionary.
     */
    public String getOreDictionaryId() {
        return null;
    }
    
    /**
     * Get the casted instance of the item.
     * @return The item.
     */
    public Item getItemInstance() {
    	return (Item) super.getSubInstance();
    }
    
    @Override
    public void onRegistered() {
    	if(isEnabled()) {
	        if(getOreDictionaryId() != null) {
	            OreDictionary.registerOre(getOreDictionaryId(), new ItemStack(this.getItemInstance()));
	        }
    	}
    }

    /**
     * Get the creative tab for this item.
     * @return The creative tab, by default the value in {@link org.cyclops.cyclopscore.init.ModBase#getDefaultCreativeTab()}.
     */
    public CreativeTabs getTargetTab() {
        return getMod().getDefaultCreativeTab();
    }

}
