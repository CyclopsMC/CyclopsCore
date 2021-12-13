package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Config for entities.
 * @param <T> The entity type
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class EntityConfig<T extends Entity> extends ExtendedConfigForge<EntityConfig<T>, EntityType<T>> {

    @Nullable
    private ItemConfig spawnEggItemConfig;

    public EntityConfig(ModBase mod, String namedId, Function<EntityConfig<T>, EntityType.Builder<T>> elementConstructor) {
        this(mod, namedId, elementConstructor, null);
    }

    public EntityConfig(ModBase mod, String namedId, Function<EntityConfig<T>, EntityType.Builder<T>> elementConstructor,
                        @Nullable BiFunction<EntityConfig<T>, EntityType<T>, ItemConfig> spawnEggItemConstructor) {
        super(mod, namedId, elementConstructor
                .andThen(builder -> builder.build(mod.getModId() + ":" + namedId)));

        // Register spawn egg if applicable
        if (spawnEggItemConstructor != null) {
            mod.getConfigHandler().addConfigurable(spawnEggItemConstructor.apply(this, getInstance()));
        }
    }

    public static <T extends Entity> BiFunction<EntityConfig<T>, EntityType<T>, ItemConfig> getDefaultSpawnEggItemConfigConstructor(ModBase mod, String itemName, int primaryColorIn, int secondaryColorIn) {
        return getDefaultSpawnEggItemConfigConstructor(mod, itemName, primaryColorIn, secondaryColorIn, null);
    }

    public static <T extends Entity> BiFunction<EntityConfig<T>, EntityType<T>, ItemConfig> getDefaultSpawnEggItemConfigConstructor(ModBase mod, String itemName, int primaryColorIn, int secondaryColorIn, @Nullable Function<Item.Properties, Item.Properties> itemPropertiesModifier) {
        return (entityConfig, entityType) -> {
            Item.Properties itemProperties = new Item.Properties().tab(mod.getDefaultItemGroup());
            if (itemPropertiesModifier != null) {
                itemProperties = itemPropertiesModifier.apply(itemProperties);
            }
            Item.Properties finalItemProperties = itemProperties;
            ItemConfig itemConfig = new ItemConfig(mod, itemName, (itemConfigSub) -> new SpawnEggItem(entityType, primaryColorIn, secondaryColorIn, finalItemProperties));
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
		return ConfigurableType.ENTITY;
	}

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        RenderingRegistry.registerEntityRenderingHandler(getInstance(),
                manager -> EntityConfig.this.getRender(manager, Minecraft.getInstance().getItemRenderer()));

    }

    /**
     * Get the render for this configurable.
     * @param renderManager The render manager.
     * @param renderItem The render item instance.
     * @return Get the render.
     */
    @OnlyIn(Dist.CLIENT)
    public abstract EntityRenderer<? super T> getRender(EntityRendererManager renderManager, ItemRenderer renderItem);

    @Override
    public IForgeRegistry<EntityType<?>> getRegistry() {
        return ForgeRegistries.ENTITIES;
    }

    @Nullable
    public ItemConfig getSpawnEggItemConfig() {
        return spawnEggItemConfig;
    }

    public void setSpawnEggItemConfig(@Nullable ItemConfig spawnEggItemConfig) {
        this.spawnEggItemConfig = spawnEggItemConfig;
    }
}
