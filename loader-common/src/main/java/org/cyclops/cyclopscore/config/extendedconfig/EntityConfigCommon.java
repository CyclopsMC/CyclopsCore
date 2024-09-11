package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
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

    public EntityConfigCommon(M mod, String namedId, Function<EntityConfigCommon<M, T>, EntityType.Builder<T>> elementConstructor) {
        this(mod, namedId, elementConstructor, null);
    }

    public EntityConfigCommon(M mod, String namedId, Function<EntityConfigCommon<M, T>, EntityType.Builder<T>> elementConstructor,
                        @Nullable BiFunction<EntityConfigCommon<M, T>, Supplier<EntityType<T>>, ItemConfigCommon<M>> spawnEggItemConstructor) {
        super(mod, namedId, elementConstructor
                .andThen(builder -> builder.build(mod.getModId() + ":" + namedId)));

        // Register spawn egg if applicable
        if (spawnEggItemConstructor != null) {
            mod.getConfigHandler().addConfigurable(spawnEggItemConstructor.apply(this, this::getInstance));
        }
    }

    public static <M extends IModBase, T extends Mob> BiFunction<EntityConfigCommon<M, T>, Supplier<EntityType<T>>, ItemConfigCommon<M>> getDefaultSpawnEggItemConfigConstructor(M mod, String itemName, int primaryColorIn, int secondaryColorIn) {
        return getDefaultSpawnEggItemConfigConstructor(mod, itemName, primaryColorIn, secondaryColorIn, null);
    }

    public static <M extends IModBase, T extends Mob> BiFunction<EntityConfigCommon<M, T>, Supplier<EntityType<T>>, ItemConfigCommon<M>> getDefaultSpawnEggItemConfigConstructor(M mod, String itemName, int primaryColorIn, int secondaryColorIn, @Nullable Function<Item.Properties, Item.Properties> itemPropertiesModifier) {
        return (entityConfig, entityType) -> {
            Item.Properties itemProperties = new Item.Properties();
            if (itemPropertiesModifier != null) {
                itemProperties = itemPropertiesModifier.apply(itemProperties);
            }
            Item.Properties finalItemProperties = itemProperties;
            ItemConfigCommon<M> itemConfig = new ItemConfigCommon<>(mod, itemName, (itemConfigSub) -> mod.getModHelpers().getRegistrationHelpers().createSpawnEgg(entityType, primaryColorIn, secondaryColorIn, finalItemProperties));
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

    public abstract EntityClientConfig<M, T> getEntityClientConfig();

    @Override
    public Registry<? super EntityType<T>> getRegistry() {
        return BuiltInRegistries.ENTITY_TYPE;
    }

    public void setSpawnEggItemConfig(@Nullable ItemConfigCommon spawnEggItemConfig) {
        this.spawnEggItemConfig = spawnEggItemConfig;
    }

}
