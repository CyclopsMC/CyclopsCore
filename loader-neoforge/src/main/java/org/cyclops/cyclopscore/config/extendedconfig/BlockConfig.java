package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Config for blocks.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class BlockConfig extends ExtendedConfigForge<BlockConfig, Block> implements IModelProviderConfig {

    @Nullable
    private final BiFunction<BlockConfig, Block, ? extends Item> itemConstructor;

    @Nullable
    private Item itemInstance;

    @Deprecated // TODO: Use BlockClientConfig instead; remove in next major
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation dynamicBlockVariantLocation;
    @Deprecated // TODO: Use BlockClientConfig instead; remove in next major
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation dynamicItemVariantLocation;

    public BlockConfig(ModBase<?> mod, String namedId, Function<BlockConfig, ? extends Block> blockConstructor,
                       @Nullable BiFunction<BlockConfig, Block, ? extends Item> itemConstructor) {
        super(mod, namedId, blockConstructor);
        this.itemConstructor = itemConstructor;
        if(mod.getModHelpers().getMinecraftHelpers().isClientSide()) {
            dynamicBlockVariantLocation = null;
            dynamicItemVariantLocation = null;
        }
    }

    protected static BiFunction<BlockConfig, Block, ? extends BlockItem> getDefaultItemConstructor(ModBase<?> mod) {
        return getDefaultItemConstructor(mod, null);
    }

    protected static BiFunction<BlockConfig, Block, ? extends BlockItem> getDefaultItemConstructor(ModBase<?> mod, @Nullable Function<Item.Properties, Item.Properties> itemPropertiesModifier) {
        return (eConfig, block) -> {
            Item.Properties itemProperties = new Item.Properties();
            if (itemPropertiesModifier != null) {
                itemProperties = itemPropertiesModifier.apply(itemProperties);
            }
            return new BlockItem(block, itemProperties);
        };
    }

    @Nullable
    public BiFunction<BlockConfig, Block, ? extends Item> getItemConstructor() {
        return itemConstructor;
    }

    @Nullable
    public Item getItemInstance() {
        return itemInstance;
    }

    public void setItemInstance(@Nullable Item itemInstance) {
        this.itemInstance = itemInstance;
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        return getNamedId();
    }

    @Override
    public String getTranslationKey() {
        return "block." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableTypesNeoForge.D_BLOCK;
    }

    public Collection<ItemStack> getDefaultCreativeTabEntriesPublic() { // TODO: rm in next major, and make other method public
        return this.defaultCreativeTabEntries();
    }

    protected Collection<ItemStack> defaultCreativeTabEntries() {
        return Collections.singleton(new ItemStack(getInstance()));
    }

    @Override
    public Registry<? super Block> getRegistry() {
        return BuiltInRegistries.BLOCK;
    }

    /**
     * Register default block and item models for this block.
     * This should only be used when registering dynamic models.
     * @return The pair of block resource location and item resource location.
     */
    @OnlyIn(Dist.CLIENT)
    @Deprecated // TODO: Use BlockClientConfig instead; remove in next major
    public Pair<ModelResourceLocation, ModelResourceLocation> registerDynamicModel() {
        ResourceLocation blockName = ResourceLocation.fromNamespaceAndPath(getMod().getModId(), getNamedId());
        ModelResourceLocation blockLocation = new ModelResourceLocation(blockName, "");
        ModelResourceLocation itemLocation = new ModelResourceLocation(blockName, "inventory");
        return Pair.of(blockLocation, itemLocation);
    }

    /**
     * @return An optional color handler for the block instance.
     */
    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Deprecated // TODO: Use BlockClientConfig instead; remove in next major
    public BlockColor getBlockColorHandler() {
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
