package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
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
    private BlockClientConfig<M> clientConfig;

    public BlockConfigCommon(M mod, String namedId, BiFunction<BlockConfigCommon<M>, Block.Properties, ? extends Block> blockConstructor,
                             @Nullable BiFunction<BlockConfigCommon<M>, Block, ? extends Item> itemConstructor) {
        super(mod, namedId, eConfig -> blockConstructor.apply(eConfig, Block.Properties.of().setId((ResourceKey<Block>) eConfig.getResourceKey())));
        this.itemConstructor = itemConstructor;
    }

    protected static <M extends IModBase> BiFunction<BlockConfigCommon<M>, Block, ? extends BlockItem> getDefaultItemConstructor(M mod) {
        return getDefaultItemConstructor(mod, null);
    }

    protected static <M extends IModBase> BiFunction<BlockConfigCommon<M>, Block, ? extends BlockItem> getDefaultItemConstructor(M mod, @Nullable Function<Item.Properties, Item.Properties> itemPropertiesModifier) {
        return (eConfig, block) -> {
            Item.Properties itemProperties = new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(eConfig.getMod().getModId(), eConfig.getNamedId())))
                    .useBlockDescriptionPrefix();
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
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.BLOCK;
    }

    public Collection<ItemStack> getDefaultCreativeTabEntries() {
        return Collections.singleton(new ItemStack(getInstance()));
    }

    @Override
    public Registry<? super Block> getRegistry() {
        return BuiltInRegistries.BLOCK;
    }

    @Nullable
    public BlockClientConfig<M> constructBlockClientConfig() {
        if (getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            return new BlockClientConfig<>(this);
        }
        return null;
    }

    @Nullable
    public final BlockClientConfig<M> getBlockClientConfig() {
        if (getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            if (this.clientConfig == null) {
                this.clientConfig = constructBlockClientConfig();
            }
            return this.clientConfig;
        }
        return null;
    }

}
