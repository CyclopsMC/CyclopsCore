package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.client.model.IDynamicModelElement;
import org.cyclops.cyclopscore.config.ConfigHandlerNeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.item.ItemInformationProviderCommon;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

/**
 * @author rubensworks
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class BlockActionNeoForge extends ConfigurableTypeActionForge<BlockConfig, Block> {

    protected static final List<BlockConfig> MODEL_ENTRIES = Lists.newArrayList();
    protected static final List<BlockConfig> COLOR_ENTRIES = Lists.newArrayList();

    /**
     * Registers a block and its optional item block.
     * @param itemBlockConstructor The optional item block constructor.
     * @param config The config.
     * @param callback A callback that will be called when the entry is registered.
     */
    public static void register(@Nullable BiFunction<BlockConfig, Block, ? extends Item> itemBlockConstructor, BlockConfig config, @Nullable Callable<?> callback) {
        register(config, itemBlockConstructor == null ? callback : null); // Delay onForgeRegistered callback until item has been registered if one is applicable
        if(itemBlockConstructor != null) {
            ItemConfig itemConfig = new ItemConfig(config.getMod(), config.getNamedId(), (iConfig) -> {
                Item itemBlock = itemBlockConstructor.apply(config, config.getInstance());
                Objects.requireNonNull(itemBlock, "Received a null item for the item block constructor of " + config.getNamedId());
                return itemBlock;
            });
            ((ConfigHandlerNeoForge) config.getMod().getConfigHandler()).registerToRegistry(BuiltInRegistries.ITEM, itemConfig, () -> {
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
    public void onRegisterForgeFilled(BlockConfig eConfig) {
        // Register block and set creative tab.
        register(eConfig.getItemConstructor(), eConfig, () -> {
            eConfig.onForgeRegistered(); // Manually call after item has been registered
            this.polish(eConfig);
            return null;
        });
    }

    public static void handleDynamicBlockModel(BlockConfig extendedConfig) {
        MODEL_ENTRIES.add(extendedConfig);
    }

    protected void polish(BlockConfig config) {
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
            // Handle dynamic models
            if(config.getInstance() instanceof IDynamicModelElement &&
                    ((IDynamicModelElement) config.getInstance()).hasDynamicModel()) {
                BlockActionNeoForge.handleDynamicBlockModel(config);
            }

            // TODO: backwards-compat, remove the following in next major
            if (config instanceof BlockConfig blockConfig) {
                BlockColor blockColorHandler = blockConfig.getBlockColorHandler();
                if (blockColorHandler != null) {
                    COLOR_ENTRIES.add(config);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModelRegistryLoad(ModelEvent.RegisterAdditional event) {
        for (BlockConfig config : MODEL_ENTRIES) {
            Pair<ModelResourceLocation, ModelResourceLocation> resourceLocations = config.registerDynamicModel();
            config.dynamicBlockVariantLocation = resourceLocations.getLeft();
            config.dynamicItemVariantLocation  = resourceLocations.getRight();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event){
        for (BlockConfig config : MODEL_ENTRIES) {
            IDynamicModelElement dynamicModelElement = (IDynamicModelElement) config.getInstance();
            BakedModel dynamicModel = dynamicModelElement.createDynamicModel(event);

            // TODO: backwards-compat, remove the following in next major
            if (config.dynamicBlockVariantLocation != null) {
                event.getModels().put(config.dynamicBlockVariantLocation, dynamicModel);
            }
            if (config.dynamicItemVariantLocation != null) {
                event.getModels().put(config.dynamicItemVariantLocation, dynamicModel);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static <M extends ModBase> void onRegisterColorHandlers(RegisterColorHandlersEvent.Block event){
        for (BlockConfig blockConfig : COLOR_ENTRIES) {
            BlockColor colorHander = blockConfig.getBlockColorHandler();
            event.register(colorHander, blockConfig.getInstance());
        }
    }

}
