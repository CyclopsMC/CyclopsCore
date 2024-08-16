package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

/**
 * Config for items.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public class ItemConfig extends ExtendedConfigForge<ItemConfig, Item> implements IModelProviderConfig {

    @Deprecated // TODO: Use BlockClientConfig instead; remove in next major
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation dynamicItemVariantLocation;

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public ItemConfig(ModBase<?> mod, String namedId, Function<ItemConfig, ? extends Item> elementConstructor) {
        super(mod, namedId, elementConstructor);
        if(mod.getModHelpers().getMinecraftHelpers().isClientSide()) {
            dynamicItemVariantLocation  = null;
        }
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        return getNamedId();
    }

    @Override
    public String getTranslationKey() {
        return "item." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.ITEM;
    }

    public Collection<ItemStack> getDefaultCreativeTabEntriesPublic() { // TODO: rm in next major, and make other method public
        return this.getDefaultCreativeTabEntries();
    }

    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
        return Collections.singleton(new ItemStack(getInstance()));
    }

    @Override
    public Registry<? super Item> getRegistry() {
        return BuiltInRegistries.ITEM;
    }

    /**
     * Register default item models for this item.
     * This should only be used when registering dynamic models.
     * @return The item resource location.
     */
    @Deprecated // TODO: Use BlockClientConfig instead; remove in next major
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation registerDynamicModel() {
        ResourceLocation itemName = ResourceLocation.fromNamespaceAndPath(getMod().getModId(), getNamedId());
        return new ModelResourceLocation(itemName, "inventory");
    }

    /**
     * @return The color handler for the item instance.
     */
    @Deprecated // TODO: Use BlockClientConfig instead; remove in next major
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public ItemColor getItemColorHandler() {
        return null;
    }

    public String getConfigPropertyPrefixPublic(ConfigurableProperty annotation) {
        return getConfigPropertyPrefix(annotation);
    }

    /**
     * @param annotation The annotation to define the prefix for.
     * @return The prefix that will be used inside the config file for {@link ConfigurableProperty}'s.
     */
    protected String getConfigPropertyPrefix(ConfigurableProperty annotation) {
        return annotation.namedId().isEmpty() ? this.getNamedId() : annotation.namedId();
    }

}
