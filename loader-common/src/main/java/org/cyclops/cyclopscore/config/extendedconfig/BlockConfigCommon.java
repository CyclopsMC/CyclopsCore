package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Config for blocks.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class BlockConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<BlockConfigCommon<M>, Block, M> implements IModelProviderConfig {

    @Nullable
    private final BiFunction<BlockConfigCommon<M>, Block, ? extends Item> itemConstructor;

    @Nullable
    private Item itemInstance;

    public BlockConfigCommon(M mod, String namedId, Function<BlockConfigCommon<M>, ? extends Block> blockConstructor,
                             @Nullable BiFunction<BlockConfigCommon<M>, Block, ? extends Item> itemConstructor) {
        super(mod, namedId, blockConstructor);
        this.itemConstructor = itemConstructor;
    }

    protected static <M extends IModBase> BiFunction<BlockConfigCommon<M>, Block, ? extends BlockItem> getDefaultItemConstructor(M mod) {
        return getDefaultItemConstructor(mod, null);
    }

    protected static <M extends IModBase> BiFunction<BlockConfigCommon<M>, Block, ? extends BlockItem> getDefaultItemConstructor(M mod, @Nullable Function<Item.Properties, Item.Properties> itemPropertiesModifier) {
        return (eConfig, block) -> {
            Item.Properties itemProperties = new Item.Properties();
            if (itemPropertiesModifier != null) {
                itemProperties = itemPropertiesModifier.apply(itemProperties);
            }
            return new BlockItem(block, itemProperties);
        };
    }

    @Nullable
    public BiFunction<BlockConfigCommon<M>, Block, ? extends Item> getItemConstructor() {
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
        return ConfigurableType.BLOCK;
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

    @Nullable
    public BlockClientConfig<M> getBlockClientConfig() {
        if (getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            return new BlockClientConfig<>(this);
        }
        return null;
    }

}
