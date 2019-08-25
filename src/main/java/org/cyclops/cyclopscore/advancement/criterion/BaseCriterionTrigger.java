package org.cyclops.cyclopscore.advancement.criterion;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base implementation of an {@link ICriterionTrigger}.
 * @author rubensworks
 */
public abstract class BaseCriterionTrigger<D, T extends ICriterionInstanceTestable<D>> implements ICriterionTrigger<T> {

    private final Map<PlayerAdvancements, Listeners<D, T>> listeners = Maps.<PlayerAdvancements, BaseCriterionTrigger.Listeners<D, T>>newHashMap();
    private final ResourceLocation id;

    protected BaseCriterionTrigger(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<T> listener) {
        BaseCriterionTrigger.Listeners<D, T> listeners = this.listeners.get(playerAdvancementsIn);

        if (listeners == null) {
            listeners = new BaseCriterionTrigger.Listeners<>(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, listeners);
        }

        listeners.add(listener);
    }

    @Override
    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<T> listener) {
        BaseCriterionTrigger.Listeners<D, T> listeners = this.listeners.get(playerAdvancementsIn);

        if (listeners != null) {
            listeners.remove(listener);

            if (listeners.isEmpty()) {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    @Override
    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    public void trigger(ServerPlayerEntity player, D criterionData) {
        BaseCriterionTrigger.Listeners<D, T> listeners = this.listeners.get(player.getAdvancements());

        if (listeners != null) {
            listeners.trigger(player, criterionData);
        }
    }

    static class Listeners<D, T extends ICriterionInstanceTestable<D>> {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<T>> listeners = Sets.<Listener<T>>newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(Listener<T> listener) {
            this.listeners.add(listener);
        }

        public void remove(Listener<T> listener) {
            this.listeners.remove(listener);
        }

        public void trigger(ServerPlayerEntity player, D criterionData) {
            List<Listener<T>> list = null;

            for (Listener<T> listener : this.listeners) {
                if (listener.getCriterionInstance().test(player, criterionData)) {
                    if (list == null) {
                        list = Lists.<Listener<T>>newArrayList();
                    }
                    list.add(listener);
                }
            }

            if (list != null) {
                for (Listener<T> listener1 : list) {
                    listener1.grantCriterion(this.playerAdvancements);
                }
            }
        }
    }
}
