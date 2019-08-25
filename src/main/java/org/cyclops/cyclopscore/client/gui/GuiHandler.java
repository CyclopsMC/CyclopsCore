package org.cyclops.cyclopscore.client.gui;

import com.google.common.collect.Maps;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * The handler class that will map Containers to GUI's.
 * TODO: rm
 * @deprecated
 * @author rubensworks
 *
 */
public class GuiHandler implements IGuiHandler {

    private final ModBase mod;
    private Map<Integer, Class<? extends Container>> containers = Maps.newHashMap();
    private Map<Integer, Class<? extends Screen>> guis = Maps.newHashMap();
    private Map<Integer, GuiType> types = Maps.newHashMap();
    
    // Two temporary data maps to avoid collisions in singleplayer worlds
    private Map<GuiType, Object> tempDataHolderClient = Maps.newHashMap();
    private Map<GuiType, Object> tempDataHolderServer = Maps.newHashMap();

    public GuiHandler(ModBase mod) {
        this.mod = mod;
    }

    public ModBase getMod() {
        return this.mod;
    }
    
    /**
     * Register a new GUI.
     * @param guiProvider A provider of GUI data.
     * @param type The GUI type.
     */
//    public void registerGUI(IGuiContainerProvider guiProvider, GuiType type) {
//        containers.put(guiProvider.getGuiID(), guiProvider.getContainer());
//    	if(false) {
//    		guis.put(guiProvider.getGuiID(), guiProvider.getGui());
//    	}
//    	types.put(guiProvider.getGuiID(), type);
//    }
    
    /**
     * Set a temporary object for showing gui's of a certain type.
     * This temporary object will be removed once a gui/container is opened once.
     * @param guiType The type of gui.
     * @param data The data for this gui type.
     * @param <O> The type of temporary data.
     */
    public <O> void setTemporaryData(GuiType<O> guiType, O data) {
    	if(false) {
    		tempDataHolderClient.put(guiType, data);
    	} else {
            tempDataHolderServer.put(guiType, data);
    	}
    }
    
    /**
     * Clear the temporary item index for item containers.
     */
    private <O> void clearTemporaryData(GuiType<O> guiType) {
        setTemporaryData(guiType, null);
    }
    
    @SuppressWarnings("unchecked")
    @Nullable
    private <O> O getTemporaryData(GuiType<O> guiType) throws IllegalArgumentException {
        O data = (O) (false ? tempDataHolderClient.get(guiType) : tempDataHolderServer.get(guiType));
        clearTemporaryData(guiType);
        if(guiType.isCarriesData() && data == null) {
            throw new IllegalArgumentException("Invalid GUI data.");
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getServerGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) {
        Class<? extends Container> containerClass = containers.get(id);
        if(containerClass == null) {
            return null; // Possible with client-only GUI's like books.
        }
        GuiType guiType = types.get(id);
        Object data;
        try {
            data = getTemporaryData(guiType);
        } catch (IllegalArgumentException e) {
            getMod().log(Level.WARN, "Invalid GUI data.");
            return null;
        }
        return guiType.getContainerConstructor().getServerGuiElement(id, player, world, x, y, z, containerClass, data);
    }

    @SuppressWarnings("unchecked")
    @OnlyIn(Dist.CLIENT)
    @Override
    public Object getClientGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) {
        Class<? extends Screen> guiClass = guis.get(id);
        GuiType guiType = types.get(id);
        Object data;
        try {
            data = getTemporaryData(guiType);
        } catch (IllegalArgumentException e) {
            getMod().log(Level.WARN, "Invalid GUI data.");
            return null;
        }
        return guiType.getGuiConstructor().getClientGuiElement(id, player, world, x, y, z, guiClass, data);
    }
    
    /**
     * Types of GUIs.
     * Use the {@link org.cyclops.cyclopscore.client.gui.GuiHandler.GuiType#create(boolean)} method to create new types.
     * @author rubensworks
     */
    public static class GuiType<O> {

        private final boolean carriesData;
        private IContainerConstructor<O> containerConstructor = null;
        private IGuiConstructor<O> guiConstructor = null;

        private GuiType(boolean carriesData) {
            this.carriesData = carriesData;
        }

        public boolean isCarriesData() {
            return this.carriesData;
        }

        public void setContainerConstructor(IContainerConstructor<O> containerConstructor) {
            if(this.containerConstructor != null) {
                throw new IllegalStateException("The container constructor was already set!");
            }
            this.containerConstructor = containerConstructor;
        }

        @OnlyIn(Dist.CLIENT)
        public void setGuiConstructor(IGuiConstructor<O> guiConstructor) {
            if(this.guiConstructor != null) {
                throw new IllegalStateException("The gui constructor was already set!");
            }
            this.guiConstructor = guiConstructor;
        }

        public IContainerConstructor<O> getContainerConstructor() {
            return this.containerConstructor;
        }

        @OnlyIn(Dist.CLIENT)
        public IGuiConstructor<O> getGuiConstructor() {
            return this.guiConstructor;
        }

        /**
         * Create a new gui type.
         * This requires a single call to both
         * {@link org.cyclops.cyclopscore.client.gui.GuiHandler.GuiType#setContainerConstructor(org.cyclops.cyclopscore.client.gui.GuiHandler.IContainerConstructor)}
         * and
         * {@link org.cyclops.cyclopscore.client.gui.GuiHandler.GuiType#setGuiConstructor(org.cyclops.cyclopscore.client.gui.GuiHandler.IGuiConstructor)}
         * for full initialization.
         * @param carriesData If this type requires additional data to be passed when the gui is opened.
         * @param <O> The type of temporary data passed to this gui type, can be Void.
         * @return The new unique gui type instance.
         */
        public static <O> GuiType<O> create(boolean carriesData) {
            return new GuiType<O>(carriesData);
        }

        /**
         * A block.
         */
        public static final GuiType<Void> BLOCK = GuiType.create(false);
        /**
         * A block with a tile entity.
         */
        public static final GuiType<Void> TILE = GuiType.create(false);
        /**
         * An item.
         */
        public static final GuiType<Pair<Integer, Hand>> ITEM = GuiType.create(true);

        static {
            BLOCK.setContainerConstructor(new IContainerConstructor<Void>() {
                @Override
                public Object getServerGuiElement(int id, PlayerEntity player, World world, int x, int y, int z,
                                                  Class<? extends Container> containerClass, Void data) {
                    try {
                        Constructor<? extends Container> containerConstructor = containerClass.getConstructor(PlayerInventory.class);
                        return containerConstructor.newInstance(player.inventory);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        try {
                            Constructor<? extends Container> containerConstructor = containerClass.getConstructor(PlayerInventory.class, World.class, BlockPos.class);
                            return containerConstructor.newInstance(player.inventory, world, new BlockPos(x, y, z));
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e2) {
                            e2.printStackTrace();
                        }
                    }
                    return null;
                }
            });
            TILE.setContainerConstructor(new IContainerConstructor<Void>() {
                @Override
                public Object getServerGuiElement(int id, PlayerEntity player, World world, int x, int y, int z,
                                                  Class<? extends Container> containerClass, Void data) {
                    try {
                        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
                        Constructor<? extends Container> containerConstructor = containerClass.getConstructor(PlayerInventory.class, tileEntity.getClass());
                        return containerConstructor.newInstance(player.inventory, tileEntity);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
            ITEM.setContainerConstructor(new IContainerConstructor<Pair<Integer, Hand>>() {
                @Override
                public Object getServerGuiElement(int id, PlayerEntity player, World world, int x, int y, int z,
                                                  Class<? extends Container> containerClass, Pair<Integer, Hand> data) {
                    try {
                        Constructor<? extends Container> containerConstructor = containerClass.getConstructor(PlayerEntity.class, int.class, Hand.class);
                        return containerConstructor.newInstance(player, data.getLeft(), data.getRight());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e0) {
                        try {
                            Constructor<? extends Container> containerConstructor = containerClass.getConstructor(PlayerEntity.class, int.class);
                            return containerConstructor.newInstance(player, data.getLeft());
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
            });

            if(false) {
                BLOCK.setGuiConstructor(new IGuiConstructor<Void>() {
                    @Override
                    public Object getClientGuiElement(int id, PlayerEntity player, World world, int x, int y, int z,
                                                      Class<? extends Screen> guiClass, Void data) {
                        try {
                            Constructor<? extends Screen> guiConstructor = guiClass.getConstructor(PlayerInventory.class);
                            return guiConstructor.newInstance(player.inventory);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            try {
                                Constructor<? extends Screen> guiConstructor = guiClass.getConstructor(PlayerInventory.class, World.class, BlockPos.class);
                                return guiConstructor.newInstance(player.inventory, world, new BlockPos(x, y, z));
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e2) {
                                e2.printStackTrace();
                            }
                        }
                        return null;
                    }
                });
                TILE.setGuiConstructor(new IGuiConstructor<Void>() {
                    @Override
                    public Object getClientGuiElement(int id, PlayerEntity player, World world, int x, int y, int z,
                                                      Class<? extends Screen> guiClass, Void data) {
                        try {
                            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
                            Constructor<? extends Screen> guiConstructor = guiClass.getConstructor(PlayerInventory.class, tileEntity.getClass());
                            return guiConstructor.newInstance(player.inventory, tileEntity);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
                ITEM.setGuiConstructor(new IGuiConstructor<Pair<Integer, Hand>>() {
                    @Override
                    public Object getClientGuiElement(int id, PlayerEntity player, World world, int x, int y, int z,
                                                      Class<? extends Screen> guiClass, Pair<Integer, Hand> data) {
                        try {
                            Constructor<? extends Screen> guiConstructor = guiClass.getConstructor(PlayerEntity.class, int.class, Hand.class);
                            return guiConstructor.newInstance(player, data.getLeft(), data.getRight());
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e0) {
                            try {
                                Constructor<? extends Screen> guiConstructor = guiClass.getConstructor(PlayerEntity.class, int.class);
                                return guiConstructor.newInstance(player, data.getLeft());
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                });
            }
        }
    	
    }

    public static interface IContainerConstructor<O> {

        public Object getServerGuiElement(int id, PlayerEntity player, World world, int x, int y, int z,
                                          Class<? extends Container> containerClass, O data);

    }

    public static interface IGuiConstructor<O> {

        public Object getClientGuiElement(int id, PlayerEntity player, World world, int x, int y, int z,
                                          Class<? extends Screen> guiClass, O data);

    }

}
