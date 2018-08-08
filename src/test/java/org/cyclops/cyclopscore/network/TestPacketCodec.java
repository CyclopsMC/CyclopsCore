package org.cyclops.cyclopscore.network;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
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
        packet1.value = new Vec3d(1, 2, 3);
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
    public void testNBTEmpty() {
        NBTPacketCodec packet1 = new NBTPacketCodec();
        packet1.value = new NBTTagCompound();
        NBTPacketCodec packet2 = new NBTPacketCodec();
        encodeDecode(packet1, packet2);
        assertThat("Input equals output", packet1.value, equalTo(packet2.value));
    }

    @Test
    public void testNBTNonEmpty() {
        NBTPacketCodec packet1 = new NBTPacketCodec();
        packet1.value = new NBTTagCompound();
        packet1.value.setBoolean("b", true);
        packet1.value.setInteger("c", 45678);
        NBTTagCompound subTag = new NBTTagCompound();
        subTag.setFloat("f", 790.5F);
        packet1.value.setTag("(", subTag);
        NBTPacketCodec packet2 = new NBTPacketCodec();
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
        ExtendedBuffer buffer = new ExtendedBuffer(Unpooled.buffer());
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
        public Vec3d value;
    }

    public static class MapPacketCodec extends SimplePacketCodec {
        @CodecField
        public Map value;
    }

    public static class NBTPacketCodec extends SimplePacketCodec {
        @CodecField
        public NBTTagCompound value;
    }

    public static class ItemStackPacketCodec extends SimplePacketCodec {
        @CodecField
        public ItemStack value;
    }

    public static class ListPacketCodec extends SimplePacketCodec {
        @CodecField
        public List<String> value;
    }

}
