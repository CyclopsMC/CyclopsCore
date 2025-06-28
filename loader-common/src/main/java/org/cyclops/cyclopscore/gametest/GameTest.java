package org.cyclops.cyclopscore.gametest;

import net.minecraft.gametest.framework.GameTestEnvironments;
import net.minecraft.world.level.block.Rotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GameTest {
    int timeoutTicks() default 100;

    String environment() default GameTestEnvironments.DEFAULT;

    boolean skyAccess() default false;

    Rotation rotation() default Rotation.NONE;

    boolean required() default true;

    boolean manualOnly() default false;

    String template() default "cyclopscore:empty";

    int setupTicks() default 0;

    int attempts() default 1;

    int requiredSuccesses() default 1;
}
