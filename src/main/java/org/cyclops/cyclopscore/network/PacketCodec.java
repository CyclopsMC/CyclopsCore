package org.cyclops.cyclopscore.network;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.handler.codec.EncoderException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.ClassUtils;
import org.cyclops.cyclopscore.datastructure.SingleCache;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Packet with automatic coding and decoding of basic fields annotated with {@link CodecField}.
 * @author rubensworks
 *
 */
public abstract class PacketCodec extends PacketBase {
	
	private static Map<Class<?>, ICodecAction> codecActions = Maps.newHashMap();
	static {
		codecActions.put(String.class, new ICodecAction() {

			@Override
			public void encode(Object object, ExtendedBuffer output) {
				output.writeString((String) object);
			}

			@Override
			public Object decode(ExtendedBuffer input) {
				return input.readString();
			}
		});
		
		codecActions.put(double.class, new ICodecAction() {

			@Override
			public void encode(Object object, ExtendedBuffer output) {
				output.writeDouble((Double) object);
			}

			@Override
			public Object decode(ExtendedBuffer input) {
				return input.readDouble();
			}
		});
		
		codecActions.put(int.class, new ICodecAction() {

			@Override
			public void encode(Object object, ExtendedBuffer output) {
				output.writeInt((int) object);
			}

			@Override
			public Object decode(ExtendedBuffer input) {
				return input.readInt();
			}
		});

		codecActions.put(short.class, new ICodecAction() {

			@Override
			public void encode(Object object, ExtendedBuffer output) {
				output.writeShort((Short) object);
			}

			@Override
			public Object decode(ExtendedBuffer input) {
				return input.readShort();
			}
		});
		
		codecActions.put(boolean.class, new ICodecAction() {

			@Override
			public void encode(Object object, ExtendedBuffer output) {
				output.writeBoolean((Boolean) object);
			}

			@Override
			public Object decode(ExtendedBuffer input) {
				return input.readBoolean();
			}
		});
		
		codecActions.put(float.class, new ICodecAction() {

			@Override
			public void encode(Object object, ExtendedBuffer output) {
				output.writeFloat((Float) object);
			}

			@Override
			public Object decode(ExtendedBuffer input) {
				return input.readFloat();
			}
		});

		codecActions.put(Vec3d.class, new ICodecAction() {
			@Override
			public void encode(Object object, ExtendedBuffer output) {
				Vec3d v = (Vec3d)object;
				output.writeDouble(v.x);
				output.writeDouble(v.y);
				output.writeDouble(v.z);
			}

			@Override
			public Object decode(ExtendedBuffer input) {
				double x = input.readDouble();
				double y = input.readDouble();
				double z = input.readDouble();
				return new Vec3d(x, y, z);
			}
		});
		
		codecActions.put(Map.class, new ICodecAction() {
			
			// Packet structure:
			// Map length (int)
			// --- end if length == 0
			// Key class name (UTF)
			// Value class name (UTF)
			// for length
			//   key
			//   value

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void encode(Object object, ExtendedBuffer output) {
				Map map = (Map) object;
				output.writeInt(map.size());
				Set<Map.Entry> entries = map.entrySet();
				ICodecAction keyAction = null;
				ICodecAction valueAction = null;
				for(Map.Entry entry : entries) {
					if(keyAction == null) {
						keyAction = getAction(entry.getKey().getClass());
						output.writeString(entry.getKey().getClass().getName());
					}
					if(valueAction == null) {
						valueAction = getAction(entry.getValue().getClass());
						output.writeString(entry.getValue().getClass().getName());
					}
					keyAction.encode(entry.getKey(), output);
					valueAction.encode(entry.getValue(), output);
				}
			}

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public Object decode(ExtendedBuffer input) {
				Map map = Maps.newHashMap();
				int size = input.readInt();
				if(size == 0) {
					return map;
				}
				try {
					ICodecAction keyAction = getAction(Class.forName(input.readString()));
					ICodecAction valueAction = getAction(Class.forName(input.readString()));
					for(int i = 0; i < size; i++) {
						Object key = keyAction.decode(input);
						Object value = valueAction.decode(input);
						map.put(key, value);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				return map;
			}
		});

		codecActions.put(NBTTagCompound.class, new ICodecAction() {

			@Override
			public void encode(Object object, ExtendedBuffer output) {
				output.writeCompoundTag((NBTTagCompound) object);
			}

			@Override
			public Object decode(ExtendedBuffer input) {
				try {
					return input.readCompoundTag();
				} catch (IOException ioexception) {
					throw new EncoderException(ioexception);
				}
			}
		});

		codecActions.put(ItemStack.class, new ICodecAction() {

			@Override
			public void encode(Object object, ExtendedBuffer output) {
				output.writeItemStack((ItemStack) object);
			}

			@Override
			public Object decode(ExtendedBuffer input) {
				try {
					return input.readItemStack();
				} catch (IOException ioexception) {
					throw new EncoderException(ioexception);
				}
			}
		});

		codecActions.put(List.class, new ICodecAction() {

			// Packet structure:
			// list length (int)
			// --- end if length == 0
			// Value class name (UTF)
			// 	id + value
			// -1

			@Override
			public void encode(Object object, ExtendedBuffer output) {
				List<?> list = (List<?>) object;
				output.writeInt(list.size());
				if(list.size() == 0) return;
				ICodecAction valueAction = null;
				for(int i = 0; i < list.size(); i++) {
					Object value = list.get(i);
					if(value != null) {
						if (valueAction == null) {
							valueAction = getAction(value.getClass());
							output.writeString(value.getClass().getName());
						}
						output.writeInt(i);
						valueAction.encode(value, output);
					}
				}
                if(valueAction == null) {
					output.writeString("__noclass");
				} else {
					output.writeInt(-1);
				}
			}

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public Object decode(ExtendedBuffer input) {
				List list;
				int size = input.readInt();
				if(size == 0) {
					return Collections.emptyList();
				} else {
					list = Lists.newArrayListWithExpectedSize(size);
				}
				try {
					String className = input.readString();
					if(!className.equals("__noclass")) {
						ICodecAction valueAction = getAction(Class.forName(className));
                        int i, currentLength = 0;
                        while((i = input.readInt()) >= 0) {
                            while(currentLength < i) {
                                list.add(null);
                                currentLength++;
                            }
                            Object value = valueAction.decode(input);
                            list.add(value);
                            currentLength++;
                        }
                        while(currentLength < size) {
                            list.add(null);
                            currentLength++;
                        }
					} else {
						for (int i = 0; i < size; i++) {
							list.add(null);
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				return list;
			}
		});
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
	
	protected static ICodecAction getAction(Class<?> clazz) {
		if(ClassUtils.isPrimitiveWrapper(clazz)) {
			clazz = ClassUtils.wrapperToPrimitive(clazz);
		}
		ICodecAction action = codecActions.get(clazz);
		if(action == null) {
			System.err.println("No ICodecAction was found for " + clazz
					+ ". You should add one in PacketCodec.");
		}
		return action;
	}
	
	private void loopCodecFields(ICodecRunnable runnable) {
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void encode(final ExtendedBuffer output) {
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
    public void decode(final ExtendedBuffer input) {
		loopCodecFields((field, action) -> {
            Object object = action.decode(input);
            try {
                field.set(PacketCodec.this, object);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
	}
	
	private interface ICodecAction {
		
		/**
		 * Encode the given object.
		 * @param object The object to encode into the output.
		 * @param output The byte array to encode to.
		 */
		public void encode(Object object, ExtendedBuffer output);

		/**
		 * Decode from the input.
		 * @param input The byte array to decode from.
		 * @return The object to return after reading it from the input.
		 */
	    public Object decode(ExtendedBuffer input);
	    
	}
	
	private interface ICodecRunnable {
		
		/**
		 * Run a type of codec.
		 * @param field The field annotated with {@link CodecField}.
		 * @param action The action that must be applied to the field.
		 */
		public void run(Field field, ICodecAction action);
		
	}
	
}
