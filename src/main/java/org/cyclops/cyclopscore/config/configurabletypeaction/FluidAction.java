package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfig;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * The action used for {@link FluidConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class FluidAction extends ConfigurableTypeAction<FluidConfig, ForgeFlowingFluid.Properties> {

    private final Multimap<String, Pair<FluidConfig, Callable<?>>> registryEntriesHolder = Multimaps.newListMultimap(Maps.<String, Collection<Pair<FluidConfig, Callable<?>>>>newHashMap(), new com.google.common.base.Supplier<List<Pair<FluidConfig, Callable<?>>>>() {
        // Compiler complains when this is replaced with a lambda :-(
        @Override
        public List<Pair<FluidConfig, Callable<?>>> get() {
            return Lists.newArrayList();
        }
    });
    private boolean registryEventPassed = false;

    public FluidAction() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @Override
    public void onRegisterForge(FluidConfig config) {
        if (this.registryEventPassed) {
            throw new IllegalStateException(String.format("Tried registering %s after its registration event.",
                    config.getNamedId()));
        }
        registryEntriesHolder.put(ForgeRegistries.FLUIDS.getRegistryKey().toString(), Pair.of(config, () -> {
            config.onForgeRegistered();
            return null;
        }));
    }

    @SubscribeEvent
    public void onRegistryEvent(RegisterEvent event) {
        if (event.getRegistryKey() == ForgeRegistries.FLUIDS.getRegistryKey()) {
            this.registryEventPassed = true;
            IForgeRegistry<Fluid> registry = event.getForgeRegistry();
            registryEntriesHolder.get(registry.getRegistryKey().toString()).forEach((pair) -> {
                FluidConfig config = pair.getLeft();
                ForgeFlowingFluid.Properties instance = config.getInstance();
                Supplier<? extends Fluid> still = ObfuscationReflectionHelper.getPrivateValue(ForgeFlowingFluid.Properties.class, instance, "still");
                Supplier<? extends Fluid> flowing = ObfuscationReflectionHelper.getPrivateValue(ForgeFlowingFluid.Properties.class, instance, "flowing");
                registerFluid(registry, still, new ResourceLocation(config.getMod().getModId(), config.getNamedId()));
                registerFluid(registry, flowing, new ResourceLocation(config.getMod().getModId(), config.getNamedId() + "_flowing"));
                try {
                    if (pair.getRight() != null) {
                        pair.getRight().call();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    protected void registerFluid(IForgeRegistry<Fluid> registry, Supplier<? extends Fluid> fluidSupplier, ResourceLocation name) {
        registry.register(name, fluidSupplier.get());
    }

}
