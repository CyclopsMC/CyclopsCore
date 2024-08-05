package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraft.client.color.item.ItemColor;
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
 * @see ConfigurableTypeAction
 */
public class ItemAction<M extends IModBase> extends ConfigurableTypeActionForge<ItemConfigCommon<M>, Item, M>{

    protected static final List<ItemConfigCommon<?>> MODEL_ENTRIES = Lists.newArrayList();
    protected static final List<ItemConfigCommon<?>> COLOR_ENTRIES = Lists.newArrayList();

    @Override
    public void onRegisterSetup(ItemConfigCommon<M> eConfig) {
        super.onRegisterSetup(eConfig);
        for (ItemStack itemStack : eConfig.getDefaultCreativeTabEntriesPublic()) {
            eConfig.getMod().registerDefaultCreativeTabEntry(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    @Override
    public void onRegisterForgeFilled(ItemConfigCommon<M> eConfig) {
        // Register item and set creative tab.
        register(eConfig, () -> {
            this.polish(eConfig);
            eConfig.onForgeRegistered();
            return null;
        });
    }

    public static <M extends IModBase> void handleItemModel(ItemConfigCommon<M> extendedConfig) {
        MODEL_ENTRIES.add(extendedConfig);
    }

    protected void polish(ItemConfigCommon<M> config) {
        // Add item to information provider
        ItemInformationProviderCommon.registerItem(config.getInstance());

        if (config.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            ItemClientConfig<M> clientConfig = config.getItemClientConfig();
            if (clientConfig != null) {
                // Handle colors
                ItemColor itemColorHandler = clientConfig.getItemColorHandler();
                if (itemColorHandler != null) {
                    COLOR_ENTRIES.add(config);
                }
            }
        }
    }
}
