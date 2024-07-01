package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.client.model.IDynamicModelElement;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

/**
 * The action used for {@link BlockConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class BlockAction extends ConfigurableTypeActionForge<BlockConfig, Block> {

    private static final List<BlockConfig> MODEL_ENTRIES = Lists.newArrayList();
    private static final List<BlockConfig> COLOR_ENTRIES = Lists.newArrayList();

    static {
        if (MinecraftHelpers.isClientSide()) {
            CyclopsCore._instance.getModEventBus().addListener(BlockAction::onModelRegistryLoad);
            CyclopsCore._instance.getModEventBus().addListener(BlockAction::onModelBakeEvent);
            CyclopsCore._instance.getModEventBus().addListener(BlockAction::onRegisterColorHandlers);
        }
    }

    /**
     * Registers a block and its optional item block.
     * @param itemBlockConstructor The optional item block constructor.
     * @param config The config.
     * @param callback A callback that will be called when the entry is registered.
     */
    public static void register(@Nullable BiFunction<BlockConfig, Block, ? extends Item> itemBlockConstructor, BlockConfig config, @Nullable Callable<?> callback) {
        register(config, itemBlockConstructor == null ? callback : null); // Delay onForgeRegistered callback until item has been registered if one is applicable
        if(itemBlockConstructor != null) {
            config.getMod().getModEventBus().addListener((RegisterEvent event) -> {
                if (event.getRegistryKey().equals(BuiltInRegistries.ITEM.key())) {
                    Item itemBlock = itemBlockConstructor.apply(config, config.getInstance());
                    Objects.requireNonNull(itemBlock, "Received a null item for the item block constructor of " + config.getNamedId());
                    event.register(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(config.getMod().getModId(), config.getNamedId()), () -> itemBlock);
                    config.setItemInstance(itemBlock);
                    try {
                        if (callback != null) {
                            callback.call();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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

    @OnlyIn(Dist.CLIENT)
    public static void onModelRegistryLoad(ModelEvent.RegisterAdditional event) {
        for (BlockConfig config : MODEL_ENTRIES) {
            Pair<ModelResourceLocation, ModelResourceLocation> resourceLocations = config.registerDynamicModel();
            config.dynamicBlockVariantLocation = resourceLocations.getLeft();
            config.dynamicItemVariantLocation  = resourceLocations.getRight();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event){
        for (BlockConfig config : MODEL_ENTRIES) {
            IDynamicModelElement dynamicModelElement = (IDynamicModelElement) config.getInstance();
            BakedModel dynamicModel = dynamicModelElement.createDynamicModel(event);
            if (config.dynamicBlockVariantLocation != null) {
                event.getModels().put(config.dynamicBlockVariantLocation, dynamicModel);
            }
            if (config.dynamicItemVariantLocation != null) {
                event.getModels().put(config.dynamicItemVariantLocation, dynamicModel);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Block event){
        for (BlockConfig blockConfig : COLOR_ENTRIES) {
            event.register(blockConfig.getBlockColorHandler(), blockConfig.getInstance());
        }
    }

    public static void handleDynamicBlockModel(BlockConfig extendedConfig) {
        MODEL_ENTRIES.add(extendedConfig);
    }

    protected void polish(BlockConfig config) {
        if(MinecraftHelpers.isClientSide()) {
            BlockColor blockColorHandler = config.getBlockColorHandler();
            if (blockColorHandler != null) {
                COLOR_ENTRIES.add(config);
            }
        }
    }
}
