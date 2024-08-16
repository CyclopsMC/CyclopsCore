package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.config.ConfigurableTypesForge;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfigForge;
import org.cyclops.cyclopscore.init.ModBaseForge;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * The action used for {@link FluidConfigForge}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FluidActionForge<M extends ModBaseForge> extends ConfigurableTypeActionCommon<FluidConfigForge<M>, ForgeFlowingFluid.Properties, M> {

    private final Multimap<String, Pair<FluidConfigForge, Callable<?>>> registryEntriesHolder = Multimaps.newListMultimap(Maps.<String, Collection<Pair<FluidConfigForge, Callable<?>>>>newHashMap(), new com.google.common.base.Supplier<List<Pair<FluidConfigForge, Callable<?>>>>() {
        // Compiler complains when this is replaced with a lambda :-(
        @Override
        public List<Pair<FluidConfigForge, Callable<?>>> get() {
            return Lists.newArrayList();
        }
    });
    private boolean registryEventPassed = false;

    @Override
    public void onRegisterForge(FluidConfigForge config) {
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
    public static void onRegistryEvent(RegisterEvent event) {
        ((FluidActionForge<ModBaseForge>) ConfigurableTypesForge.FLUID.getConfigurableTypeAction()).onRegistryEventInner(event);
    }

    public void onRegistryEventInner(RegisterEvent event) {
        if (event.getRegistryKey() == BuiltInRegistries.FLUID.key()) {
            this.registryEventPassed = true;
            IForgeRegistry<Fluid> registry = event.getForgeRegistry();
            registryEntriesHolder.get(registry.getRegistryKey().toString()).forEach((pair) -> {
                FluidConfigForge<M> config = pair.getLeft();
                ForgeFlowingFluid.Properties instance = config.getInstance();
                Supplier<Fluid> still = ObfuscationReflectionHelper.getPrivateValue(ForgeFlowingFluid.Properties.class, instance, "still");
                Supplier<Fluid> flowing = ObfuscationReflectionHelper.getPrivateValue(ForgeFlowingFluid.Properties.class, instance, "flowing");
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
        } else if (event.getRegistryKey() == ForgeRegistries.FLUID_TYPES.getKey()) {
            registryEntriesHolder.get(BuiltInRegistries.FLUID.key().toString()).forEach((pair) -> {
                FluidConfigForge<M> config = pair.getLeft();
                ForgeFlowingFluid.Properties instance = config.getInstance();
                Supplier<FluidType> fluidType = ObfuscationReflectionHelper.getPrivateValue(ForgeFlowingFluid.Properties.class, instance, "fluidType");
                event.register((ResourceKey) event.getForgeRegistry().getRegistryKey(), ResourceLocation.fromNamespaceAndPath(config.getMod().getModId(), config.getNamedId()), fluidType);
            });
        }
    }

    protected void registerFluid(IForgeRegistry<Fluid> registry, RegisterEvent event, Supplier<Fluid> fluidSupplier, ResourceLocation name) {
        registry.register(name, fluidSupplier.get());
    }

}
