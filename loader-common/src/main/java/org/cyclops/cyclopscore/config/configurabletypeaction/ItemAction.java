package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.extendedconfig.ItemClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.item.ItemInformationProviderCommon;

import java.util.List;

/**
 * The action used for {@link ItemConfigCommon}.
 * @author rubensworks
 * @param <M> The mod type
 * @see ConfigurableTypeActionCommon
 */
public class ItemAction<M extends IModBase> extends ConfigurableTypeActionRegistry<ItemConfigCommon<M>, Item, M> {

    protected static final List<ItemConfigCommon<?>> MODEL_ENTRIES = Lists.newArrayList();

    @Override
    public void onRegistriesFilled(ItemConfigCommon<M> eConfig) {
        // Register item and set creative tab.
        register(eConfig, () -> {
            this.polish(eConfig);
            eConfig.onRegistryRegistered();
            return null;
        });
    }

    public static <M extends IModBase> void handleItemModel(ItemConfigCommon<M> extendedConfig) {
        MODEL_ENTRIES.add(extendedConfig);
    }

    protected void polish(ItemConfigCommon<M> config) {
        // Register creative tab entry
        for (ItemStack itemStack : config.getDefaultCreativeTabEntries()) {
            config.getMod().registerDefaultCreativeTabEntry(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        // Add item to information provider
        ItemInformationProviderCommon.registerItem(config.getInstance());

        if (config.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            ItemClientConfig<M> clientConfig = config.getItemClientConfig();
            if (clientConfig != null) {
                // Nothing to do yet
            }
        }
    }
}
