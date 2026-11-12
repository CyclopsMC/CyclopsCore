package org.cyclops.cyclopscore.network;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3d;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * @author rubensworks
 */
public class TestPacketCodec {

    @Test
    public void testStringSimple() {
        StringPacketCodec packet1 = new StringPacketCodec();
        packet1.value = "abc";
        StringPacketCodec packet2 = new StringPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, is(packet2.value));
    }

    @Test
    public void testStringComplex() {
        StringPacketCodec packet1 = new StringPacketCodec();
        packet1.value = "lkjdertyuiopùm=:;,nbfr(§è!çàp^=:;,nbvcdert!ol;,nbvfrtui";
        StringPacketCodec packet2 = new StringPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, is(packet2.value));
    }

    @Test
    public void testDouble() {
        DoublePacketCodec packet1 = new DoublePacketCodec();
        packet1.value = 10.5D;
        DoublePacketCodec packet2 = new DoublePacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, is(packet2.value));
    }

    @Test
    public void testInt() {
        IntPacketCodec packet1 = new IntPacketCodec();
        packet1.value = 10;
        IntPacketCodec packet2 = new IntPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, is(packet2.value));
    }

    @Test
    public void testLong() {
        LongPacketCodec packet1 = new LongPacketCodec();
        packet1.value = 1000000000000L;
        LongPacketCodec packet2 = new LongPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, is(packet2.value));
    }

    @Test
    public void testShort() {
        ShortPacketCodec packet1 = new ShortPacketCodec();
        packet1.value = 10;
        ShortPacketCodec packet2 = new ShortPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, is(packet2.value));
    }

    @Test
    public void testBoolean() {
        BooleanPacketCodec packet1 = new BooleanPacketCodec();
        packet1.value = true;
        BooleanPacketCodec packet2 = new BooleanPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, is(packet2.value));
    }

    @Test
    public void testFloat() {
        FloatPacketCodec packet1 = new FloatPacketCodec();
        packet1.value = 10.5F;
        FloatPacketCodec packet2 = new FloatPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, is(packet2.value));
    }

    @Test
    public void testVec3() {
        Vec3PacketCodec packet1 = new Vec3PacketCodec();
        packet1.value = new Vector3d(1, 2, 3);
        Vec3PacketCodec packet2 = new Vec3PacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value.x, equalTo(packet2.value.x));
    }

    @Test
    public void testMapEmpty() {
        MapPacketCodec packet1 = new MapPacketCodec();
        packet1.value = Maps.newHashMap();
        MapPacketCodec packet2 = new MapPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, equalTo(packet2.value));
    }

    @Test
    public void testMapNonEmpty() {
        MapPacketCodec packet1 = new MapPacketCodec();
        Map<String, Integer> map = Maps.newHashMap();
        map.put("a", 2);
        map.put("i", 2);
        map.put("qsddq", 976);
        packet1.value = map;
        MapPacketCodec packet2 = new MapPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, equalTo(packet2.value));
    }

    @Test
    public void testMapNested() {
        MapPacketCodec packet1 = new MapPacketCodec();
        Map<String, List<Integer>> map = Maps.newHashMap();
        map.put("a", Lists.newArrayList(1, 2));
        map.put("i", Lists.newArrayList(2, 3));
        map.put("qsddq", Lists.newArrayList(4, 6));
        packet1.value = map;
        MapPacketCodec packet2 = new MapPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, equalTo(packet2.value));
    }

    @Test
    public void testMapModifiedDuringEncoding() {
        // Simulates another thread adding an entry after the map size was read.
        Map<String, Integer> mapA = new ConcurrentHashMap<>() {
            @Override
            public Set<Entry<String, Integer>> entrySet() {
                putIfAbsent("late", 3);
                return super.entrySet();
            }
        };
        mapA.put("a", 1);
        Map<String, Integer> mapB = Maps.newHashMap();
        mapB.put("b", 2);
        DoubleMapPacketCodec packet1 = new DoubleMapPacketCodec();
        packet1.a = mapA;
        packet1.b = mapB;
        DoubleMapPacketCodec packet2 = new DoubleMapPacketCodec();

        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        packet1.encode(buffer);
        packet2.decode(buffer);

        assertThat("Input equals output", packet2.a, equalTo(Maps.newHashMap(mapA)));
        assertThat("Input equals output", packet2.b, equalTo(mapB));
        assertThat("Buffer is fully read", buffer.readableBytes(), is(0));
    }

    @Test
    public void testNBTEmpty() {
        NBTPacketCodec packet1 = new NBTPacketCodec();
        packet1.value = new CompoundTag();
        NBTPacketCodec packet2 = new NBTPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, equalTo(packet2.value));
    }

    @Test
    public void testNBTNonEmpty() {
        NBTPacketCodec packet1 = new NBTPacketCodec();
        packet1.value = new CompoundTag();
        packet1.value.putBoolean("b", true);
        packet1.value.putInt("c", 45678);
        CompoundTag subTag = new CompoundTag();
        subTag.putFloat("f", 790.5F);
        packet1.value.put("(", subTag);
        NBTPacketCodec packet2 = new NBTPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, equalTo(packet2.value));
    }

    @Test
    public void testEnumFacing() {
        EnumFacingPacketCodec packet1 = new EnumFacingPacketCodec();
        packet1.value = Direction.NORTH;
        EnumFacingPacketCodec packet2 = new EnumFacingPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, equalTo(packet2.value));
    }

    @Test
    public void testBlockPos() {
        BlockPosPacketCodec packet1 = new BlockPosPacketCodec();
        packet1.value = new BlockPos(234, 123, 389);
        BlockPosPacketCodec packet2 = new BlockPosPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, equalTo(packet2.value));
    }

    @Test
    public void testListEmpty() {
        ListPacketCodec packet1 = new ListPacketCodec();
        packet1.value = Lists.newArrayList();
        ListPacketCodec packet2 = new ListPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, equalTo(packet2.value));
    }

    @Test
    public void testListNonEmpty() {
        ListPacketCodec packet1 = new ListPacketCodec();
        List<String> list = Lists.newArrayList();
        list.add(null);
        list.add(null);
        list.add(null);
        list.add("a");
        list.add("b");
        list.add("ghjkbvf");
        list.add(null);
        packet1.value = list;
        ListPacketCodec packet2 = new ListPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, equalTo(packet2.value));
    }

    @Test
    public void testListOnlyNulls() {
        ListPacketCodec packet1 = new ListPacketCodec();
        List<String> list = Lists.newArrayList();
        list.add(null);
        list.add(null);
        packet1.value = list;
        ListPacketCodec packet2 = new ListPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, equalTo(packet2.value));
    }

    @Test
    public void testListMaxNullElements() {
        ListPacketCodec packet1 = new ListPacketCodec();
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < PacketCodec.MAX_LIST_NULL_ELEMENTS; i++) {
            list.add(null);
        }
        list.add("a");
        packet1.value = list;
        ListPacketCodec packet2 = new ListPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, equalTo(packet2.value));
    }

    @Test(expected = DecoderException.class)
    public void testListSizeTooLarge() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeInt(300000000);
        buffer.writeUtf("__noclass");
        new ListPacketCodec().decode(buffer);
    }

    @Test(expected = DecoderException.class)
    public void testListSizeNegative() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeInt(-1);
        buffer.writeUtf("__noclass");
        new ListPacketCodec().decode(buffer);
    }

    @Test(expected = DecoderException.class)
    public void testListTooManyNullsNoClass() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeInt(PacketCodec.MAX_LIST_NULL_ELEMENTS + 1);
        buffer.writeUtf("__noclass");
        new ListPacketCodec().decode(buffer);
    }

    @Test(expected = DecoderException.class)
    public void testListTooManyNullsBeforeIndex() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeInt(PacketCodec.MAX_LIST_NULL_ELEMENTS + 10);
        buffer.writeUtf(String.class.getName());
        buffer.writeInt(PacketCodec.MAX_LIST_NULL_ELEMENTS + 1);
        buffer.writeUtf("a");
        buffer.writeInt(-1);
        new ListPacketCodec().decode(buffer);
    }

    @Test(expected = DecoderException.class)
    public void testListIndexOutOfBounds() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeInt(2);
        buffer.writeUtf(String.class.getName());
        buffer.writeInt(5);
        buffer.writeUtf("a");
        buffer.writeInt(-1);
        new ListPacketCodec().decode(buffer);
    }

    @Test(expected = DecoderException.class)
    public void testListIndexDecreasing() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeInt(3);
        buffer.writeUtf(String.class.getName());
        buffer.writeInt(1);
        buffer.writeUtf("a");
        buffer.writeInt(0);
        buffer.writeUtf("b");
        buffer.writeInt(-1);
        new ListPacketCodec().decode(buffer);
    }

    @Test(expected = DecoderException.class)
    public void testListUnknownClass() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeInt(1);
        buffer.writeUtf("org.cyclops.cyclopscore.DoesNotExist");
        buffer.writeInt(0);
        buffer.writeInt(-1);
        new ListPacketCodec().decode(buffer);
    }

    @Test
    public void testListClassWithoutCodecIsNotInitialized() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeInt(1);
        buffer.writeUtf("org.cyclops.cyclopscore.network.TestPacketCodec$ClassWithInitializer");
        buffer.writeInt(0);
        buffer.writeInt(-1);
        try {
            new ListPacketCodec().decode(buffer);
            throw new AssertionError("Expected a DecoderException");
        } catch (DecoderException e) {
            // Expected
        }
        assertFalse("Class must not be initialized", ClassInitializationTracker.initialized);
    }

    @Test(expected = DecoderException.class)
    public void testMapSizeTooLarge() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeInt(Integer.MAX_VALUE);
        buffer.writeUtf(String.class.getName());
        buffer.writeUtf(String.class.getName());
        new MapPacketCodec().decode(buffer);
    }

    @Test(expected = DecoderException.class)
    public void testMapUnknownClass() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeInt(1);
        buffer.writeUtf("org.cyclops.cyclopscore.DoesNotExist");
        buffer.writeUtf(String.class.getName());
        buffer.writeUtf("a");
        buffer.writeUtf("b");
        new MapPacketCodec().decode(buffer);
    }

    //@Test
    //@Ignore // Not yet supported
    public void testMapList() {
        MapPacketCodec packet1 = new MapPacketCodec();
        Map<List<String>, List<Integer>> map = Maps.newHashMap();
        List<String> a = Lists.newArrayList();
        a.add("qsd");
        a.add("!uhba");
        a.add("poqjsd");
        List<Integer> b = Lists.newLinkedList();
        b.add(1);
        List<String> c = Lists.newLinkedList();
        a.add("qqsdsd");
        a.add("!uhqsdba");
        a.add("pqsdoqjsd");
        List<Integer> d = Lists.newLinkedList();
        b.add(1087);
        b.add(730);
        b.add(294);
        b.add(97654);
        map.put(a, b);
        map.put(c, d);
        packet1.value = map;
        MapPacketCodec packet2 = new MapPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, equalTo(packet2.value));
    }

    protected static <T extends PacketCodec> void encodeDecode(T packetIn, T packetOut) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        packetIn.encode(buffer);
        packetOut.decode(buffer);
    }

    public static class StringPacketCodec extends SimplePacketCodec {
        @CodecField
        public String value;
    }

    public static class DoublePacketCodec extends SimplePacketCodec {
        @CodecField
        public double value;
    }

    public static class IntPacketCodec extends SimplePacketCodec {
        @CodecField
        public int value;
    }

    public static class LongPacketCodec extends SimplePacketCodec {
        @CodecField
        public long value;
    }

    public static class ShortPacketCodec extends SimplePacketCodec {
        @CodecField
        public short value;
    }

    public static class BooleanPacketCodec extends SimplePacketCodec {
        @CodecField
        public boolean value;
    }

    public static class FloatPacketCodec extends SimplePacketCodec {
        @CodecField
        public float value;
    }

    public static class Vec3PacketCodec extends SimplePacketCodec {
        @CodecField
        public Vector3d value;
    }

    public static class MapPacketCodec extends SimplePacketCodec {
        @CodecField
        public Map value;
    }

    public static class DoubleMapPacketCodec extends SimplePacketCodec {
        @CodecField
        public Map a;
        @CodecField
        public Map b;
    }

    public static class NBTPacketCodec extends SimplePacketCodec {
        @CodecField
        public CompoundTag value;
    }

    public static class ItemStackPacketCodec extends SimplePacketCodec {
        @CodecField
        public ItemStack value;
    }

    public static class EnumFacingPacketCodec extends SimplePacketCodec {
        @CodecField
        public Direction value;
    }

    public static class BlockPosPacketCodec extends SimplePacketCodec {
        @CodecField
        public BlockPos value;
    }

    public static class ListPacketCodec extends SimplePacketCodec {
        @CodecField
        public List<String> value;
    }

    public static class ClassInitializationTracker {
        public static boolean initialized = false;
    }

    public static class ClassWithInitializer {
        static {
            ClassInitializationTracker.initialized = true;
        }
    }

}
