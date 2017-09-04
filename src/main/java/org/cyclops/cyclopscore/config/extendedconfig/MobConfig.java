package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for mobs.
 * @param <T> The entity type
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class MobConfig<T extends EntityLiving> extends ExtendedConfig<MobConfig<T>> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public MobConfig(ModBase mod, boolean enabled, String namedId, String comment, Class<? extends T> element) {
        super(mod, enabled, namedId, comment, element);
    }
    
    @Override
	public String getUnlocalizedName() {
		return "entity.mob." + getMod().getModId() + "." + getNamedId();
	}
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.MOB;
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        @SuppressWarnings("unchecked")
        Class<? extends T> clazz = (Class<? extends T>) this.getElement();
        RenderingRegistry.registerEntityRenderingHandler(clazz, new IRenderFactory<T>() {
            @Override
            public Render<? super T> createRenderFor(RenderManager manager) {
                return MobConfig.this.getRender(manager);
            }
        });
    }

    /**
     * @return If a spawn egg should be registered for this mob.
     */
    public boolean hasSpawnEgg() {
        return true;
    }
    
    /**
     * Get the background color of the spawn egg.
     * @return The spawn egg background color.
     */
    public abstract int getBackgroundEggColor();
    /**
     * Get the foreground color of the spawn egg.
     * @return The spawn egg foreground color.
     */
    public abstract int getForegroundEggColor();
    
    /**
     * Get the render for this configurable.
     * @param renderManager The render manager.
     * @return Get the render.
     */
    @SideOnly(Side.CLIENT)
    public abstract Render<? super T> getRender(RenderManager renderManager);

    @Override
    public IForgeRegistry<?> getRegistry() {
        return ForgeRegistries.ENTITIES;
    }

}
