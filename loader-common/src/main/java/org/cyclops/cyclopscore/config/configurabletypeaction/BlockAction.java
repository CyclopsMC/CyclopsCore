package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.item.ItemInformationProviderCommon;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

/**
 * The action used for {@link BlockConfigCommon}.
 * @author rubensworks
 * @param <M> The mod type
 * @see ConfigurableTypeActionCommon
 */
public class BlockAction<M extends IModBase> extends ConfigurableTypeActionRegistry<BlockConfigCommon<M>, Block, M> {

    protected static final List<BlockConfigCommon<?>> MODEL_ENTRIES = Lists.newArrayList();
    protected static final List<BlockConfigCommon<?>> COLOR_ENTRIES = Lists.newArrayList();

    /**
     * Registers a block and its optional item block.
     * @param itemBlockConstructor The optional item block constructor.
     * @param config The config.
     * @param callback A callback that will be called when the entry is registered.
     */
    public static <M extends IModBase> void register(@Nullable BiFunction<BlockConfigCommon<M>, Block, ? extends Item> itemBlockConstructor, BlockConfigCommon<M> config, @Nullable Callable<?> callback) {
        register(config, itemBlockConstructor == null ? callback : null); // Delay onForgeRegistered callback until item has been registered if one is applicable
        if(itemBlockConstructor != null) {
            ItemConfigCommon<M> itemConfig = new ItemConfigCommon<>(config.getMod(), config.getNamedId(), (iConfig) -> {
                Item itemBlock = itemBlockConstructor.apply(config, config.getInstance());
                Objects.requireNonNull(itemBlock, "Received a null item for the item block constructor of " + config.getNamedId());
                return itemBlock;
            });
            config.getMod().getConfigHandler().registerToRegistry(BuiltInRegistries.ITEM, itemConfig, () -> {
                config.setItemInstance(itemConfig.getInstance());
                try {
                    if (callback != null) {
                        callback.call();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });
        }
    }

    @Override
    public void onRegisterForgeFilled(BlockConfigCommon<M> eConfig) {
        // Register block and set creative tab.
        register(eConfig.getItemConstructor(), eConfig, () -> {
            eConfig.onForgeRegistered(); // Manually call after item has been registered
            this.polish(eConfig);
            return null;
        });
    }

    public static void handleDynamicBlockModel(BlockConfigCommon extendedConfig) {
        MODEL_ENTRIES.add(extendedConfig);
    }

    protected void polish(BlockConfigCommon<M> config) {
        // Register creative tab entry
        for (ItemStack itemStack : config.getDefaultCreativeTabEntriesPublic()) {
            config.getMod().registerDefaultCreativeTabEntry(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        // Add item to information provider
        Item item = config.getItemInstance();
        if (item != null) {
            ItemInformationProviderCommon.registerItem(item);
        }

        if(config.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            BlockClientConfig<M> clientConfig = config.getBlockClientConfig();
            if (clientConfig != null) {
                // Handle colors
                BlockColor blockColorHandler = clientConfig.getBlockColorHandler();
                if (blockColorHandler != null) {
                    COLOR_ENTRIES.add(config);
                }
            }
        }
    }
}
