package org.cyclops.cyclopscore.network;

import com.google.common.collect.Lists;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.cyclops.cyclopscore.datastructure.SingleCache;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Packet with automatic coding and decoding of basic fields annotated with {@link CodecField}.
 * @author rubensworks
 *
 */
public abstract class PacketCodec<T extends PacketCodec<T>> extends PacketBase<T> {

    @Deprecated // TODO: rm in next major
    public static final int READ_STRING_MAX_LENGTH = 32767;

    public PacketCodec(Type<T> type) {
        super(type);
    }

    /**
     * Register a new coded action.
     * @param clazz A class type.
     * @param action A codec action for the given type.
     */
    @Deprecated // TODO: rm in next major
    public static void addCodedAction(Class<?> clazz, ICodecAction action) {
        PacketCodecs.addCodedAction(clazz, action);
    }

    @Deprecated // TODO: rm in next major
    @Nullable
    protected static ICodecAction getActionSuper(Class<?> clazz) {
        return PacketCodecs.getActionSuper(clazz);
    }

    @Deprecated // TODO: rm in next major
    public static ICodecAction getAction(Class<?> clazz) {
        return PacketCodecs.getAction(clazz);
    }

    protected SingleCache<Void, List<Field>> fieldCache = new SingleCache<Void, List<Field>>(
            new SingleCache.ICacheUpdater<Void, List<Field>>() {

                @Override
                public List<Field> getNewValue(Void key) {
                    List<Field> fieldList = Lists.newLinkedList();

                    Class<?> clazz = PacketCodec.this.getClass();
                    for (; clazz != PacketCodec.class && clazz != null; clazz = clazz.getSuperclass()) {
                        Field[] fields = clazz.getDeclaredFields();

                        // Sort this because the Java API tells us that getDeclaredFields()
                        // does not deterministically define the order of the fields in the array.
                        // Otherwise we might get nasty class cast exceptions when running in SMP.
                        Arrays.sort(fields, new Comparator<Field>() {

                            @Override
                            public int compare(Field o1, Field o2) {
                                return o1.getName().compareTo(o2.getName());
                            }

                        });

                        for (final Field field : fields) {
                            if (field.isAnnotationPresent(CodecField.class)) {
                                fieldList.add(field);
                            }
                        }
                    }

                    return fieldList;
                }

                @Override
                public boolean isKeyEqual(Void cacheKey, Void newKey) {
                    return true;
                }

            });

    private void loopCodecFields(ICodecRunnable runnable) {
        for (Field field : fieldCache.get(null)) {
            Class<?> clazz = field.getType();
            ICodecAction action = getAction(clazz);

            // Make private fields temporarily accessible.
            boolean accessible = field.isAccessible();
            if (!accessible) {
                field.setAccessible(true);
            }
            runnable.run(field, action);
        }
    }

    @Override
    public void encode(final RegistryFriendlyByteBuf output) {
        loopCodecFields((field, action) -> {
            Object object = null;
            try {
                object = field.get(PacketCodec.this);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            action.encode(object, output);
        });
    }

    @Override
    public void decode(final RegistryFriendlyByteBuf input) {
        loopCodecFields((field, action) -> {
            Object object = action.decode(input);
            try {
                field.set(PacketCodec.this, object);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Write the given object into the packet buffer.
     * @param packetBuffer A packet buffer.
     * @param object An object.
     */
    @Deprecated // TODO: rm in next major
    public static void write(RegistryFriendlyByteBuf packetBuffer, Object object) {
        PacketCodecs.write(packetBuffer, object);
    }

    /**
     * Read the an object of the given type from the packet buffer.
     * @param <T> The type of object.
     * @param packetBuffer A packet buffer.
     * @param clazz The class type to read.
     * @return The read object.
     */
    @Deprecated // TODO: rm in next major
    public static <T> T read(RegistryFriendlyByteBuf packetBuffer, Class<T> clazz) {
        return PacketCodecs.read(packetBuffer, clazz);
    }

    public static interface ICodecAction {

        /**
         * Encode the given object.
         * @param object The object to encode into the output.
         * @param output The byte array to encode to.
         */
        public void encode(Object object, RegistryFriendlyByteBuf output);

        /**
         * Decode from the input.
         * @param input The byte array to decode from.
         * @return The object to return after reading it from the input.
         */
        public Object decode(RegistryFriendlyByteBuf input);

    }

    // TODO: extract to separate file in next major
    public static interface ICodecRunnable {

        /**
         * Run a type of codec.
         * @param field The field annotated with {@link CodecField}.
         * @param action The action that must be applied to the field.
         */
        public void run(Field field, ICodecAction action);

    }

}
