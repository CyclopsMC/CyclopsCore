package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * The action used for {@link ItemConfigCommon}.
 * @author rubensworks
 * @param <M> The mod type
 * @see ConfigurableTypeActionCommon
 */
public class EntityActionCommon<M extends IModBase, T extends Entity> extends ConfigurableTypeActionRegistry<EntityConfigCommon<M, T>, EntityType<T>, M> {

    @Override
    public void onRegisterForgeFilled(EntityConfigCommon<M, T> eConfig) {
        // Register item and set creative tab.
        register(eConfig, () -> {
            this.polish(eConfig);
            eConfig.onForgeRegistered();
            return null;
        });
    }

    protected void polish(EntityConfigCommon<M, T> config) {
        if (config.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            EntityRenderers.register(config.getInstance(),
                    manager -> config.getEntityClientConfig().getRender(manager, Minecraft.getInstance().getItemRenderer()));
        }
    }
}
