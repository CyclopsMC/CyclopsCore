package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
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

    static {
        if (MinecraftHelpers.isClientSide()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(BlockAction::onModelRegistryLoad);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(BlockAction::onModelBakeEvent);
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
            FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> {
                Item itemBlock = itemBlockConstructor.apply(config, config.getInstance());
                Objects.requireNonNull(itemBlock, "Received a null item for the item block constructor of " + config.getNamedId());
                if (itemBlock.getRegistryName() == null) {
                    itemBlock.setRegistryName(new ResourceLocation(config.getMod().getModId(), config.getNamedId()));
                }
                event.getRegistry().register(itemBlock);
                config.setItemInstance(itemBlock);
                try {
                    if (callback != null) {
                        callback.call();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onRegisterForge(BlockConfig eConfig) {
        // Register block and set creative tab.
        register(eConfig.getItemConstructor(), eConfig, () -> {
            eConfig.onForgeRegistered(); // Manually call after item has been registered
            this.polish(eConfig);
            return null;
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void onModelRegistryLoad(ModelRegistryEvent event) {
        for (BlockConfig config : MODEL_ENTRIES) {
            Pair<ModelResourceLocation, ModelResourceLocation> resourceLocations = config.registerDynamicModel();
            config.dynamicBlockVariantLocation = resourceLocations.getLeft();
            config.dynamicItemVariantLocation  = resourceLocations.getRight();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onModelBakeEvent(ModelBakeEvent event){
        for (BlockConfig config : MODEL_ENTRIES) {
            IDynamicModelElement dynamicModelElement = (IDynamicModelElement) config.getInstance();
            IBakedModel dynamicModel = dynamicModelElement.createDynamicModel(event);
            event.getModelRegistry().put(config.dynamicBlockVariantLocation, dynamicModel);
            event.getModelRegistry().put(config.dynamicItemVariantLocation, dynamicModel);
        }
    }

    public static void handleDynamicBlockModel(BlockConfig extendedConfig) {
        MODEL_ENTRIES.add(extendedConfig);
    }

    protected void polish(BlockConfig config) {
        Block block = config.getInstance();
        if(MinecraftHelpers.isClientSide()) {
            IBlockColor blockColorHandler = config.getBlockColorHandler();
            if (blockColorHandler != null) {
                Minecraft.getInstance().getBlockColors().register(blockColorHandler, block);
            }
        }
    }
}
