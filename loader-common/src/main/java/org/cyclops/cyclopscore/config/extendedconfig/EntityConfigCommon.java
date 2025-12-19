package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Config for entities.
 * @author rubensworks
 * @param <M> The mod type
 * @param <T> The entity type
 * @see ExtendedConfigCommon
 */
public abstract class EntityConfigCommon<M extends IModBase, T extends Entity> extends ExtendedConfigRegistry<EntityConfigCommon<M, T>, EntityType<T>, M> {

    @Nullable
    private ItemConfigCommon<M> spawnEggItemConfig;
    private EntityClientConfig<M, T> clientConfig;

    public EntityConfigCommon(M mod, String namedId, Function<EntityConfigCommon<M, T>, EntityType.Builder<T>> elementConstructor) {
        this(mod, namedId, elementConstructor, null);
    }

    public EntityConfigCommon(M mod, String namedId, Function<EntityConfigCommon<M, T>, EntityType.Builder<T>> elementConstructor,
                        @Nullable BiFunction<EntityConfigCommon<M, T>, Supplier<EntityType<T>>, ItemConfigCommon<M>> spawnEggItemConstructor) {
        super(mod, namedId, elementConstructor
                .andThen(builder -> builder.build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(mod.getModId(), namedId)))));

        // Register spawn egg if applicable
        if (spawnEggItemConstructor != null) {
            mod.getConfigHandler().addConfigurable(spawnEggItemConstructor.apply(this, this::getInstance));
        }
    }

    public static <M extends IModBase, T extends Mob> BiFunction<EntityConfigCommon<M, T>, Supplier<EntityType<T>>, ItemConfigCommon<M>> getDefaultSpawnEggItemConfigConstructor(M mod, String itemName) {
        return getDefaultSpawnEggItemConfigConstructor(mod, itemName, null);
    }

    public static <M extends IModBase, T extends Mob> BiFunction<EntityConfigCommon<M, T>, Supplier<EntityType<T>>, ItemConfigCommon<M>> getDefaultSpawnEggItemConfigConstructor(M mod, String itemName, @Nullable Function<Item.Properties, Item.Properties> itemPropertiesModifier) {
        return (entityConfig, entityType) -> {
            ItemConfigCommon<M> itemConfig = new ItemConfigCommon<>(mod, itemName, (itemConfigSub, properties) -> {
                if (itemPropertiesModifier != null) {
                    properties = itemPropertiesModifier.apply(properties);
                }
                return new SpawnEggItem(properties.spawnEgg(entityType.get()));
            });
            entityConfig.setSpawnEggItemConfig(itemConfig);
            return itemConfig;
        };
    }

    @Override
    public String getTranslationKey() {
        return "entity." + getNamedId();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.ENTITY;
    }

    public abstract EntityClientConfig<M, T> constructEntityClientConfig();

    public final EntityClientConfig<M, T> getEntityClientConfig() {
        if (getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            if (this.clientConfig == null) {
                this.clientConfig = constructEntityClientConfig();
            }
            return this.clientConfig;
        }
        return null;
    }

    @Override
    public Registry<? super EntityType<T>> getRegistry() {
        return BuiltInRegistries.ENTITY_TYPE;
    }

    public void setSpawnEggItemConfig(@Nullable ItemConfigCommon spawnEggItemConfig) {
        this.spawnEggItemConfig = spawnEggItemConfig;
    }

}
