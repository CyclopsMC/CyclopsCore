package org.cyclops.cyclopscore.client.icon;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;

import java.lang.reflect.Field;
import java.util.List;

/**
 * An alternative way of registering icons.
 * @author rubensworks
 */
@SideOnly(Side.CLIENT)
public class IconProvider {

    private final ClientProxyComponent clientProxy;
    private final List<Pair<Pair<Object, Field>, String>> toRegister = Lists.newLinkedList();

    public IconProvider(ClientProxyComponent clientProxy) {
        this.clientProxy = clientProxy;
        MinecraftForge.EVENT_BUS.register(this);
    }

    protected void registerIcon(Object object, Field field, String location) {
        toRegister.add(Pair.of(Pair.of(object, field), location));
    }

    protected TextureAtlasSprite registerIcon(TextureMap textureMap, String location) {
        return textureMap.registerSprite(new ResourceLocation(clientProxy.getMod().getModId(), location));
    }

    @SubscribeEvent
    public void onPreTextureStitch(TextureStitchEvent.Pre event) {
        for(Pair<Pair<Object, Field>, String> entry : toRegister) {
            TextureAtlasSprite icon = registerIcon(event.map, entry.getValue());
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
