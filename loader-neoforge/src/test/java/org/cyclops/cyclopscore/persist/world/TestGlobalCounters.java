package org.cyclops.cyclopscore.persist.world;

import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link GlobalCounters}.
 * @author rubensworks
 */
public class TestGlobalCounters {

    @Test
    public void testGetWithoutServer() {
        // No server is running, like on a client connected to a remote server.
        assertThat(ServerLifecycleHooks.getCurrentServer(), nullValue());
        GlobalCounters.Access access = new GlobalCounters.Access(CyclopsCoreNeoForge._instance);

        GlobalCounters counters = access.get();
        assertThat(counters, notNullValue());
        assertThat(access.get(), sameInstance(counters));

        assertThat(counters.getNext("a"), is(0));
        assertThat(counters.getNext("a"), is(1));
        assertThat(counters.getNext("b"), is(0));
        assertThat(access.get().getNext("a"), is(2));
    }

}
