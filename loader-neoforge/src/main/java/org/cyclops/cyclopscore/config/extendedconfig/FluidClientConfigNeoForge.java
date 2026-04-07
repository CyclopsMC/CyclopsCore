package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.fluid.FluidTintSource;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class FluidClientConfigNeoForge {

    private final FluidConfigNeoForge fluidConfig;

    public FluidClientConfigNeoForge(FluidConfigNeoForge fluidConfig) {
        this.fluidConfig = fluidConfig;
    }

    /**
     * Get the still icon location.
     * @return The icon location.
     */
    public Identifier getIconLocationStill() {
        return Identifier.fromNamespaceAndPath(fluidConfig.getMod().getModId(), "block/" + fluidConfig.getNamedId() + "_still");
    }

    /**
     * Get the flow icon location.
     * @return The icon location.
     */
    public Identifier getIconLocationFlow() {
        return Identifier.fromNamespaceAndPath(fluidConfig.getMod().getModId(), "block/" + fluidConfig.getNamedId() + "_flow");
    }

    @Nullable
    public Material getOverlay() {
        return null;
    }

    @Nullable
    public FluidTintSource getTintSource() {
        return null;
    }

    public void onRegisterFluidModels(RegisterFluidModelsEvent event) {
        event.register(
                new FluidModel.Unbaked(
                        new Material(getIconLocationStill()),
                        new Material(getIconLocationFlow()),
                        getOverlay(),
                        getTintSource()
                ),
                fluidConfig.getSourceFluid(),
                fluidConfig.getFlowingFluid());
    }

}
