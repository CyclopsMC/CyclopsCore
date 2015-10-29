package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for mobs.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class MobConfig extends ExtendedConfig<MobConfig> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public MobConfig(ModBase mod, boolean enabled, String namedId, String comment, Class<?> element) {
        super(mod, enabled, namedId, comment, element);
    }
    
    @Override
	public String getUnlocalizedName() {
		return "entity.mob." + getNamedId();
	}
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.MOB;
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void onInit(Step step) {
        super.onInit(step);
        if(step == Step.INIT) {
            Render render = getRender(Minecraft.getMinecraft().getRenderManager());
            if(render != null) {
                @SuppressWarnings("unchecked")
                Class<? extends EntityLiving> clazz = (Class<? extends EntityLiving>) this.getElement();
                getMod().getProxy().registerRenderer(clazz, render);
            }
        }
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
    public abstract Render getRender(RenderManager renderManager);

}
