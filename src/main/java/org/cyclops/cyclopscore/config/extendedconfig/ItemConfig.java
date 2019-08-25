package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Config for items.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class ItemConfig extends ExtendedConfigForge<ItemConfig, Item> implements IModelProviderConfig {

    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation dynamicItemVariantLocation;

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.O
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param elementConstructor The element constructor.
     */
    public ItemConfig(ModBase mod, boolean enabled, String namedId, String comment,
                      Function<ItemConfig, ? extends Item> elementConstructor) {
        super(mod, enabled, namedId, comment, elementConstructor);
        if(MinecraftHelpers.isClientSide()) {
            dynamicItemVariantLocation  = null;
        }
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        return getNamedId();
    }
    
    @Override
	public String getTranslationKey() {
		return "items." + getMod().getModId() + "." + getNamedId();
	}

    @Override
    public String getFullTranslationKey() {
        return "item." + getTranslationKey() + ".name";
    }
    
    @Override
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.ITEM;
	}

    /**
     * Register default item models for this item.
     * This should only be used when registering dynamic models.
     * @return The item resource location.
     */
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation registerDynamicModel() {
        String blockName = getMod().getModId() + ":" + getNamedId();
        ModelResourceLocation itemLocation = new ModelResourceLocation(blockName, "inventory");
        // TODO: model mapping
        // ModelLoader.setCustomModelResourceLocation(getItemInstance(), 0, itemLocation);
        return itemLocation;
    }

    /**
     * Get the creative tab for this item.
     * @return The creative tab, by default the value in {@link org.cyclops.cyclopscore.init.ModBase#getDefaultCreativeTab()}.
     */
    public ItemGroup getTargetTab() {
        return getMod().getDefaultCreativeTab();
    }

    @Override
    public IForgeRegistry<Item> getRegistry() {
        return ForgeRegistries.ITEMS;
    }

    /**
     * @return The color handler for the item instance.
     */
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public IItemColor getItemColorHandler() {
        return null;
    }

}
