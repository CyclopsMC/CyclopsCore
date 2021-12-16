package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.config.extendedconfig.CapabilityConfig;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * The action used for {@link CapabilityConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class CapabilityAction<T> extends ConfigurableTypeAction<CapabilityConfig<T>, T> {

    private final List<Pair<Class<?>, Callable<?>>> registryEntriesHolder = Lists.newLinkedList();
    private boolean registryEventPassed = false;

    public CapabilityAction() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @Override
    public void onRegisterForge(CapabilityConfig<T> config) {
        super.onRegisterForge(config);

        if (this.registryEventPassed) {
            throw new IllegalStateException(String.format("Tried registering %s after its registration event.",
                    config.getNamedId()));
        }
        registryEntriesHolder.add(Pair.of(config.getType(), () -> {
            config.onForgeRegistered();
            return null;
        }));
    }

    @SubscribeEvent
    public void onRegistryEvent(RegisterCapabilitiesEvent event) {
        this.registryEventPassed = true;
        for (Pair<Class<?>, Callable<?>> pair : registryEntriesHolder) {
            event.register(pair.getLeft());
            try {
                if (pair.getRight() != null) {
                    pair.getRight().call();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
