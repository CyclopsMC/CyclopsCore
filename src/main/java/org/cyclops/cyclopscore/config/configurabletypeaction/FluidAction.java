package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.CyclopsCore;
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
public class FluidAction extends ConfigurableTypeAction<FluidConfig, BaseFlowingFluid.Properties> {

    private final Multimap<String, Pair<FluidConfig, Callable<?>>> registryEntriesHolder = Multimaps.newListMultimap(Maps.<String, Collection<Pair<FluidConfig, Callable<?>>>>newHashMap(), new com.google.common.base.Supplier<List<Pair<FluidConfig, Callable<?>>>>() {
        // Compiler complains when this is replaced with a lambda :-(
        @Override
        public List<Pair<FluidConfig, Callable<?>>> get() {
            return Lists.newArrayList();
        }
    });
    private boolean registryEventPassed = false;

    public FluidAction() {
        CyclopsCore._instance.getModEventBus().register(this);
    }

    @Override
    public void onRegisterForge(FluidConfig config) {
        if (this.registryEventPassed) {
            throw new IllegalStateException(String.format("Tried registering %s after its registration event.",
                    config.getNamedId()));
        }
        registryEntriesHolder.put(BuiltInRegistries.FLUID.key().toString(), Pair.of(config, () -> {
            config.onForgeRegistered();
            return null;
        }));
    }

    @SubscribeEvent
    public void onRegistryEvent(RegisterEvent event) {
        if (event.getRegistryKey() == BuiltInRegistries.FLUID.key()) {
            this.registryEventPassed = true;
            Registry<Fluid> registry = (Registry<Fluid>) event.getRegistry();
            registryEntriesHolder.get(registry.key().toString()).forEach((pair) -> {
                FluidConfig config = pair.getLeft();
                BaseFlowingFluid.Properties instance = config.getInstance();
                Supplier<Fluid> still = ObfuscationReflectionHelper.getPrivateValue(BaseFlowingFluid.Properties.class, instance, "still");
                Supplier<Fluid> flowing = ObfuscationReflectionHelper.getPrivateValue(BaseFlowingFluid.Properties.class, instance, "flowing");
                registerFluid(registry, event, still, ResourceLocation.fromNamespaceAndPath(config.getMod().getModId(), config.getNamedId()));
                registerFluid(registry, event, flowing, ResourceLocation.fromNamespaceAndPath(config.getMod().getModId(), config.getNamedId() + "_flowing"));
                try {
                    if (pair.getRight() != null) {
                        pair.getRight().call();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else if (event.getRegistryKey() == NeoForgeRegistries.FLUID_TYPES.key()) {
            registryEntriesHolder.get(BuiltInRegistries.FLUID.key().toString()).forEach((pair) -> {
                FluidConfig config = pair.getLeft();
                BaseFlowingFluid.Properties instance = config.getInstance();
                Supplier<FluidType> fluidType = ObfuscationReflectionHelper.getPrivateValue(BaseFlowingFluid.Properties.class, instance, "fluidType");
                event.register((ResourceKey) event.getRegistry().key(), ResourceLocation.fromNamespaceAndPath(config.getMod().getModId(), config.getNamedId()), fluidType);
            });
        }
    }

    protected void registerFluid(Registry<Fluid> registry, RegisterEvent event, Supplier<Fluid> fluidSupplier, ResourceLocation name) {
        event.register(registry.key(), name, fluidSupplier);
    }

}
