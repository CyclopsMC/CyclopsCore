package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.client.gui.GuiHandler;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * The action used for {@link BlockConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class BlockAction extends ConfigurableTypeAction<BlockConfig, Block> {

    private static final List<BlockConfig> MODEL_ENTRIES = Lists.newArrayList();

    static {
        MinecraftForge.EVENT_BUS.register(BlockAction.class);
    }

    /**
     * Registers a block.
     * @param block The block instance.
     * @param config The config.
     */
    public static void register(Block block, BlockConfig config) {
        register(block, config, null);
    }

    /**
     * Registers a block.
     * @param block The block instance.
     * @param config The config.
     * @param callback A callback that will be called when the entry is registered.
     */
    public static void register(Block block, BlockConfig config, @Nullable Callable<?> callback) {
        register(block, null, config, callback);
    }

    /**
     * Registers a block.
     * @param block The block instance.
     * @param itemBlockClass The optional item block class.
     * @param config The config.
     */
    public static void register(Block block, @Nullable Class<? extends Item> itemBlockClass, BlockConfig config) {
        register(block, itemBlockClass, config, null);
    }

    /**
     * Registers a block.
     * @param block The block instance.
     * @param itemBlockClass The optional item block class.
     * @param config The config.
     * @param callback A callback that will be called when the entry is registered.
     */
    public static void register(Block block, @Nullable Class<? extends Item> itemBlockClass, BlockConfig config, @Nullable Callable<?> callback) {
        register(block, config, null); // Delay onForgeRegistered callback until item has been registered
        if(itemBlockClass != null) {
            // TODO: handle item registration for ItemBlock
//            try {
//                Item item = itemBlockClass.getConstructor(Block.class).newInstance(block);
//                register(ForgeRegistries.ITEMS, item, config, callback);
//            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    public void onRegisterForge(BlockConfig eConfig) {
        Block block = eConfig.getInstance();

        // Register block and set creative tab.
        register(block, eConfig.getItemBlockClass(), eConfig, () -> {
            eConfig.onForgeRegistered(); // Manually call after item has been registered
            this.polish(eConfig);
            return null;
        });

        // Also register tile entity
        GuiHandler.GuiType<Void> guiType = GuiHandler.GuiType.BLOCK;
//        if(eConfig.getConfigurableType().equals(ConfigurableType.BLOCKCONTAINER)) {
//            ConfigurableBlockContainer container = (ConfigurableBlockContainer) block;
//            // This alternative registration is required to remain compatible with old worlds.
//            try {
//                // TODO: register guis via registry
////                GameRegistry.registerTileEntity(container.getTileEntity(),
////                        eConfig.getMod().getModId() + ":" + eConfig.getSubUniqueName());
//            } catch (IllegalArgumentException e) {
//                // Ignore duplicate tile entity registration errors
//            }
//            guiType = GuiHandler.GuiType.TILE;
//        }

        // If the block has a GUI, go ahead and register that.
        // TODO: handle guis
//        if(block instanceof IConfigurableBlock && ((IConfigurableBlock) block).hasGui()) {
//            IGuiContainerProvider gui = (IGuiContainerProvider) block;
//            eConfig.getMod().getGuiHandler().registerGUI(gui, guiType);
//        }

        if (MinecraftHelpers.isClientSide()) {
            ItemAction.handleItemModel(eConfig);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModelRegistryLoad(ModelRegistryEvent event) {
        for (BlockConfig config : MODEL_ENTRIES) {
            Pair<ModelResourceLocation, ModelResourceLocation> resourceLocations = config.registerDynamicModel();
            config.dynamicBlockVariantLocation = resourceLocations.getLeft();
            config.dynamicItemVariantLocation  = resourceLocations.getRight();
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
