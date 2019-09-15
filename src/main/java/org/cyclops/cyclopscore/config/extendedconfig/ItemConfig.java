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
import org.cyclops.cyclopscore.item.ItemInformationProvider;

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
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public ItemConfig(ModBase mod, String namedId, Function<ItemConfig, ? extends Item> elementConstructor) {
        super(mod, namedId, elementConstructor);
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

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();

        // Add item to information provider
        ItemInformationProvider.registerItem(getInstance());
    }

    /**
     * Get the creative tab for this item.
     * @return The creative tab, by default the value in {@link org.cyclops.cyclopscore.init.ModBase#getDefaultItemGroup()}.
     */
    public ItemGroup getTargetTab() {
        return getMod().getDefaultItemGroup();
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
