package org.cyclops.cyclopscore.gametest;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import org.cyclops.cyclopscore.RegistryEntriesCommon;
import org.cyclops.cyclopscore.config.DeferredHolderCommon;

/**
 * Tests for {@link DeferredHolderCommon}.
 * @author rubensworks
 */
public class DeferredHolderCommonTest {

    // Registry#safeCastToReference only accepts Holder.Reference, so delegates must resolve to one.
    @GameTest
    public void testDelegateIsReference(GameTestHelper helper) {
        Holder<ParticleType<?>> delegate = RegistryEntriesCommon.PARTICLE_BLUR.getDelegate();
        helper.assertTrue(delegate instanceof Holder.Reference,
                "Expected a Holder.Reference delegate, but got " + delegate);
        helper.succeed();
    }

    @GameTest
    public void testHolderCodecEncoding(GameTestHelper helper) {
        DataResult<JsonElement> result = BuiltInRegistries.PARTICLE_TYPE.holderByNameCodec()
                .encodeStart(JsonOps.INSTANCE, RegistryEntriesCommon.PARTICLE_BLUR);
        helper.assertTrue(result.result().isPresent(), "Expected a successful encoding, but got " + result);
        helper.succeed();
    }

}
