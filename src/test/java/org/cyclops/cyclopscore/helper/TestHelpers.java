package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.junit.Test;
import org.objenesis.ObjenesisStd;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestHelpers {

    // Skips the ModBase constructor, as it requires a mod loading context.
    private static ModBase newMod() {
        return new ObjenesisStd().newInstance(DummyMod.class);
    }

    @Test
    public void testGetNewIdSequentialPerMod() {
        ModBase modA = newMod();
        ModBase modB = newMod();
        assertThat(Helpers.getNewId(modA, Helpers.IDType.PACKET), is(0));
        assertThat(Helpers.getNewId(modA, Helpers.IDType.PACKET), is(1));
        assertThat(Helpers.getNewId(modB, Helpers.IDType.PACKET), is(0));
        assertThat(Helpers.getNewId(modA, Helpers.IDType.PACKET), is(2));
        assertThat(Helpers.getNewId(modB, Helpers.IDType.PACKET), is(1));
    }

    @Test
    public void testGetNewIdConcurrent() throws Exception {
        // Mods register packets in parallel during mod loading, so an ID may never be handed out twice.
        int threads = 8;
        int idsPerThread = 1000;
        ModBase mod = newMod();
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        try {
            CountDownLatch start = new CountDownLatch(1);
            List<Future<List<Integer>>> futures = Lists.newArrayList();
            for (int t = 0; t < threads; t++) {
                futures.add(executor.submit(() -> {
                    start.await();
                    List<Integer> ids = Lists.newArrayList();
                    for (int i = 0; i < idsPerThread; i++) {
                        ids.add(Helpers.getNewId(mod, Helpers.IDType.PACKET));
                    }
                    return ids;
                }));
            }
            start.countDown();

            Set<Integer> ids = Sets.newHashSet();
            for (Future<List<Integer>> future : futures) {
                ids.addAll(future.get());
            }
            assertThat("All IDs are unique", ids.size(), is(threads * idsPerThread));
            assertThat("Next ID follows all handed out IDs", Helpers.getNewId(mod, Helpers.IDType.PACKET), is(threads * idsPerThread));
        } finally {
            executor.shutdownNow();
        }
    }

    public static class DummyMod extends ModBase<DummyMod> {

        public DummyMod() {
            super("dummy", mod -> {});
        }

        @Override
        protected IClientProxy constructClientProxy() {
            return null;
        }

        @Override
        protected ICommonProxy constructCommonProxy() {
            return null;
        }

        @Override
        public String toString() {
            return "dummy";
        }
    }

}
