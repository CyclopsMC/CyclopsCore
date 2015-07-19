package org.cyclops.cyclopscore.config.configurable;

import com.google.common.collect.Maps;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.client.model.ConnectedBlockModel;
import org.cyclops.cyclopscore.client.model.DirectionCorner;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.Map;

/**
 * Blocks that have a connected texture.
 * The block model file can contain anything, it won't be used anyways, but it must exist.
 * Make sure that following textures exist within your textures/blocks folder:
 *  * [blockname].png
 *  * [blockname]_border.png
 *  * [blockname]_corner.png
 *  * [blockname]_innerCorner.png
 *  If you don't want these filenames, you'll have to overwrite {@link ConfigurableBlockConnectedTexture#loadedTextureMap}
 *  and register the same keys.
 * @author rubensworks
 */
public class ConfigurableBlockConnectedTexture extends ConfigurableBlock {

    @SuppressWarnings("unchecked")
    @BlockProperty
    public static final IUnlistedProperty<Boolean>[] CONNECTED = new IUnlistedProperty[6];
    @SuppressWarnings("unchecked")
    @BlockProperty
    public static final IUnlistedProperty<Boolean>[] CONNECTED_CORNER = new IUnlistedProperty[DirectionCorner.values().length];
    static {
        for(EnumFacing side : EnumFacing.values()) {
            CONNECTED[side.ordinal()] = Properties.toUnlisted(PropertyBool.create("connect-" + side.getName()));
        }
        for(DirectionCorner corner : DirectionCorner.values()) {
            CONNECTED_CORNER[corner.ordinal()] = Properties.toUnlisted(PropertyBool.create("connect_corner-" + corner.name()));
        }
    }

    @SideOnly(Side.CLIENT)
    private Map<String, ResourceLocation> textureMap;
    @SideOnly(Side.CLIENT)
    private Map<String, TextureAtlasSprite> loadedTextureMap;

    /**
     * Make a new blockState instance.
     *
     * @param eConfig  Config for this blockState.
     * @param material Material of this blockState.
     */
    public ConfigurableBlockConnectedTexture(ExtendedConfig eConfig, Material material) {
        super(eConfig, material);

        if(MinecraftHelpers.isClientSide()) {
            textureMap = Maps.newHashMap();
            loadedTextureMap = Maps.newHashMap();
            FMLCommonHandler.instance().bus().register(this);
            loadTextureMap();
        }
    }

    @SideOnly(Side.CLIENT)
    protected void loadTextureMap() {
        String mod = getConfig().getMod().getModId();
        String base = "blocks/" + getConfig().getNamedId();
        textureMap.put("background", new ResourceLocation(mod, base));
        textureMap.put("border", new ResourceLocation(mod, base + "_border"));
        textureMap.put("corner", new ResourceLocation(mod, base + "_corner"));
        textureMap.put("innerCorner", new ResourceLocation(mod, base + "_innerCorner"));
    }

    /**
     * Get a texture from the model file.
     * @param name Texture name.
     * @return The texture or null if it did not exist.
     */
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(String name) {
        return loadedTextureMap.get(name);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPreTextureStitch(TextureStitchEvent.Pre event) {
        for(Map.Entry<String, ResourceLocation> textureEntry : textureMap.entrySet()) {
            loadedTextureMap.put(textureEntry.getKey(), event.map.registerSprite(textureEntry.getValue()));
        }
    }

    protected boolean isConnected(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return world.getBlockState(pos.offset(side)).getBlock() == this;
    }

    protected boolean isConnected(IBlockAccess world, BlockPos pos, DirectionCorner corner) {
        return world.getBlockState(corner.fromOffset(pos)).getBlock() == this;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IExtendedBlockState extendedState = (IExtendedBlockState) getDefaultState();
        for(EnumFacing side : EnumFacing.VALUES) {
            extendedState = extendedState.withProperty(CONNECTED[side.ordinal()], isConnected(world, pos, side));
        }
        for(DirectionCorner corner : DirectionCorner.values()) {
            extendedState = extendedState.withProperty(CONNECTED_CORNER[corner.ordinal()], isConnected(world, pos, corner));
        }
        return extendedState;
    }

    @Override
    public boolean hasDynamicModel() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IBakedModel createDynamicModel() {
        return new ConnectedBlockModel(this);
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

}
