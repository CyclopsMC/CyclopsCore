package org.cyclops.cyclopscore.gametest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * @author rubensworks
 */
public class MethodGameTestInstance extends GameTestInstance {

    public static final MapCodec<? extends GameTestInstance> CODEC = RecordCodecBuilder.<MethodGameTestInstance>mapCodec(instance -> instance.group(
            TestData.CODEC.forGetter(MethodGameTestInstance::info),
            Codec.STRING.fieldOf("modId").forGetter(MethodGameTestInstance::getModId),
            Codec.STRING.fieldOf("class").forGetter(MethodGameTestInstance::getClassName),
            Codec.STRING.fieldOf("method").forGetter(MethodGameTestInstance::getMethodName)
    ).apply(instance, MethodGameTestInstance::new));

    private final String modId;
    private final String className;
    private final String methodName;

    public MethodGameTestInstance(TestData<Holder<TestEnvironmentDefinition>> info, String modId, String className, String methodName) {
        super(info);
        this.modId = modId;
        this.className = className;
        this.methodName = methodName;
    }

    public String getModId() {
        return modId;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public void run(GameTestHelper gameTestHelper) {
        try {
            Class<?> clazz = Class.forName(this.className);
            Object instance = clazz.newInstance();
            Method method = clazz.getMethod(this.methodName, GameTestHelper.class);
            method.invoke(instance, gameTestHelper);
        } catch (InvocationTargetException | IllegalAccessException | ClassNotFoundException | InstantiationException |
                 NoSuchMethodException e) {
            e.printStackTrace();
            throw new GameTestAssertException(Component.literal(e.getMessage()), (int) gameTestHelper.getTick());
        }
    }

    @Override
    public MapCodec<? extends GameTestInstance> codec() {
        return CODEC;
    }

    @Override
    protected MutableComponent typeDescription() {
        return Component.literal("Method-based test instance for " + getClassName() + "." + getMethodName());
    }

    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(this.modId, (this.className + "." + this.methodName).toLowerCase(Locale.ROOT).replace('.', '_'));
    }
}
