package org.cyclops.cyclopscore.client.icon;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;

import java.lang.reflect.Field;
import java.util.List;

/**
 * An alternative way of registering icons.
 * @author rubensworks
 */
@OnlyIn(Dist.CLIENT)
public class IconProvider {

    private final ClientProxyComponent clientProxy;
    private final List<Pair<Pair<Object, Field>, String>> toRegister = Lists.newLinkedList();

    public IconProvider(ClientProxyComponent clientProxy) {
        this.clientProxy = clientProxy;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onPreTextureStitch);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onPostTextureStitch);
    }

    protected void registerIcon(Object object, Field field, String location) {
        toRegister.add(Pair.of(Pair.of(object, field), location));
    }

    protected TextureAtlasSprite registerIcon(AtlasTexture textureMap, String location) {
        return textureMap.getSprite(new ResourceLocation(clientProxy.getMod().getModId(), location));
    }

    protected ResourceLocation getIconId(String location) {
        return new ResourceLocation(clientProxy.getMod().getModId(), location);
    }

    public void onPreTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) {
            for (Pair<Pair<Object, Field>, String> entry : toRegister) {
                event.addSprite(getIconId(entry.getValue()));
            }
        }
    }

    public void onPostTextureStitch(TextureStitchEvent.Post event) {
        if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) {
            for (Pair<Pair<Object, Field>, String> entry : toRegister) {
                TextureAtlasSprite icon = event.getMap().getSprite(getIconId(entry.getValue()));
                Object object = entry.getLeft().getLeft();
                Field field = entry.getLeft().getRight();
                try {
                    field.set(object, icon);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(String.format("The icon field %s in class %s could not be set.",
                            field.getName(), object.getClass().getCanonicalName()));
                }
            }
        }
    }

    /**
     * Scan all icon fields for the given object.
     * This will automatically populate any {@link org.cyclops.cyclopscore.client.icon.Icon} fields.
     * This should be called before pre-texture stitching.
     * @param object The object to scan.
     */
    public void registerIconHolderObject(Object object) {
        for(Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Icon.class)) {
                    Icon annotation = field.getAnnotation(Icon.class);
                    registerIcon(object, field, annotation.location());
                }
            }
        }
    }

}
