package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

/**
 * Config for items.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public class ItemConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<ItemConfigCommon<M>, Item, M> implements IModelProviderConfig {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public ItemConfigCommon(M mod, String namedId, Function<ItemConfigCommon<M>, ? extends Item> elementConstructor) {
        super(mod, namedId, elementConstructor);
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
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.ITEM;
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

    @Nullable
    public ItemClientConfig<M> getItemClientConfig() {
        if (getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            return new ItemClientConfig<>(this);
        }
        return null;
    }

}
