package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Config for entities.
 * @param <T> The entity type
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class EntityConfig<T extends Entity, M extends ModBase> extends ExtendedConfigForge<EntityConfig<T, M>, EntityType<T>, M> {

    @Nullable
    private ItemConfigCommon<M> spawnEggItemConfig;

    public EntityConfig(M mod, String namedId, Function<EntityConfig<T, M>, EntityType.Builder<T>> elementConstructor) {
        this(mod, namedId, elementConstructor, null);
    }

    public EntityConfig(M mod, String namedId, Function<EntityConfig<T, M>, EntityType.Builder<T>> elementConstructor,
                        @Nullable BiFunction<EntityConfig<T, M>, Supplier<EntityType<T>>, ItemConfigCommon<M>> spawnEggItemConstructor) {
        super(mod, namedId, elementConstructor
                .andThen(builder -> builder.build(mod.getModId() + ":" + namedId)));

        // Register spawn egg if applicable
        if (spawnEggItemConstructor != null) {
            mod.getConfigHandler().addConfigurable(spawnEggItemConstructor.apply(this, this::getInstance));
        }
    }

    public static <T extends Mob, M extends ModBase> BiFunction<EntityConfig<T, M>, Supplier<EntityType<T>>, ItemConfigCommon<M>> getDefaultSpawnEggItemConfigConstructor(IModBase mod, String itemName, int primaryColorIn, int secondaryColorIn) {
        return getDefaultSpawnEggItemConfigConstructor(mod, itemName, primaryColorIn, secondaryColorIn, null);
    }

    public static <T extends Mob, M extends ModBase> BiFunction<EntityConfig<T, M>, Supplier<EntityType<T>>, ItemConfigCommon<M>> getDefaultSpawnEggItemConfigConstructor(IModBase mod, String itemName, int primaryColorIn, int secondaryColorIn, @Nullable Function<Item.Properties, Item.Properties> itemPropertiesModifier) {
        return (entityConfig, entityType) -> {
            Item.Properties itemProperties = new Item.Properties();
            if (itemPropertiesModifier != null) {
                itemProperties = itemPropertiesModifier.apply(itemProperties);
            }
            Item.Properties finalItemProperties = itemProperties;
            ItemConfigCommon itemConfig = new ItemConfigCommon(mod, itemName, (itemConfigSub) -> new DeferredSpawnEggItem(entityType, primaryColorIn, secondaryColorIn, finalItemProperties));
            entityConfig.setSpawnEggItemConfig(itemConfig);
            return itemConfig;
        };
    }

    @Override
    public String getTranslationKey() {
        return "entity." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableTypesNeoForge.ENTITY;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        EntityRenderers.register(getInstance(),
                manager -> EntityConfig.this.getRender(manager, Minecraft.getInstance().getItemRenderer()));
    }

    /**
     * Get the render for this configurable.
     * @param renderContext The render context.
     * @param renderItem The render item instance.
     * @return Get the render.
     */
    @OnlyIn(Dist.CLIENT)
    public abstract EntityRenderer<? super T> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem);

    @Override
    public Registry<? super EntityType<T>> getRegistry() {
        return BuiltInRegistries.ENTITY_TYPE;
    }

    public void setSpawnEggItemConfig(@Nullable ItemConfigCommon spawnEggItemConfig) {
        this.spawnEggItemConfig = spawnEggItemConfig;
    }
}