package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.client.model.IDynamicModelElement;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for items.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class ItemConfig extends ExtendedConfig<ItemConfig> implements IModelProviderConfig {

    @SideOnly(Side.CLIENT) public ModelResourceLocation dynamicItemVariantLocation;

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
        if(MinecraftHelpers.isClientSide()) {
            dynamicItemVariantLocation  = null;
        }
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

    /**
     * Register default item models for this item.
     * This should only be used when registering dynamic models.
     * @return The item resource location.
     */
    @SideOnly(Side.CLIENT)
    protected ModelResourceLocation registerDynamicModel() {
        String blockName = getMod().getModId() + ":" + getNamedId();
        ModelResourceLocation itemLocation = new ModelResourceLocation(blockName, "inventory");
        ModelLoader.setCustomModelResourceLocation(getItemInstance(), 0, itemLocation);
        return itemLocation;
    }
    
    @Override
    public void onRegistered() {
        super.onRegistered();
    	if(isEnabled()) {
	        if(getOreDictionaryId() != null) {
	            OreDictionary.registerOre(getOreDictionaryId(), new ItemStack(this.getItemInstance()));
	        }
    	}
        if(MinecraftHelpers.isClientSide() && getItemInstance() instanceof IDynamicModelElement &&
                ((IDynamicModelElement) getItemInstance()).hasDynamicModel()) {
            this.dynamicItemVariantLocation  = registerDynamicModel();
        }
    }

    /**
     * Get the creative tab for this item.
     * @return The creative tab, by default the value in {@link org.cyclops.cyclopscore.init.ModBase#getDefaultCreativeTab()}.
     */
    public CreativeTabs getTargetTab() {
        return getMod().getDefaultCreativeTab();
    }

    @Override
    public IForgeRegistry<?> getRegistry() {
        return ForgeRegistries.ITEMS;
    }

}
