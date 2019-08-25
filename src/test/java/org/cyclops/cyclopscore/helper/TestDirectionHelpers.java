package org.cyclops.cyclopscore.helper;

import net.minecraft.util.Direction;
import org.junit.Test;

import static org.cyclops.cyclopscore.helper.DirectionHelpers.transformFacingForRotation;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestDirectionHelpers {

    @Test
    public void testTransformFacingForRotation() {
        assertThat(transformFacingForRotation(Direction.NORTH, Direction.NORTH), is(Direction.NORTH));
        assertThat(transformFacingForRotation(Direction.EAST,  Direction.NORTH), is(Direction.EAST));
        assertThat(transformFacingForRotation(Direction.SOUTH, Direction.NORTH), is(Direction.SOUTH));
        assertThat(transformFacingForRotation(Direction.WEST,  Direction.NORTH), is(Direction.WEST));
        assertThat(transformFacingForRotation(Direction.UP,    Direction.NORTH), is(Direction.UP));
        assertThat(transformFacingForRotation(Direction.DOWN,  Direction.NORTH), is(Direction.DOWN));

        assertThat(transformFacingForRotation(Direction.NORTH, Direction.EAST), is(Direction.WEST));
        assertThat(transformFacingForRotation(Direction.EAST,  Direction.EAST), is(Direction.NORTH));
        assertThat(transformFacingForRotation(Direction.SOUTH, Direction.EAST), is(Direction.EAST));
        assertThat(transformFacingForRotation(Direction.WEST,  Direction.EAST), is(Direction.SOUTH));
        assertThat(transformFacingForRotation(Direction.UP,    Direction.EAST), is(Direction.UP));
        assertThat(transformFacingForRotation(Direction.DOWN,  Direction.EAST), is(Direction.DOWN));

        assertThat(transformFacingForRotation(Direction.NORTH, Direction.SOUTH), is(Direction.SOUTH));
        assertThat(transformFacingForRotation(Direction.EAST,  Direction.SOUTH), is(Direction.WEST));
        assertThat(transformFacingForRotation(Direction.SOUTH, Direction.SOUTH), is(Direction.NORTH));
        assertThat(transformFacingForRotation(Direction.WEST,  Direction.SOUTH), is(Direction.EAST));
        assertThat(transformFacingForRotation(Direction.UP,    Direction.SOUTH), is(Direction.UP));
        assertThat(transformFacingForRotation(Direction.DOWN,  Direction.SOUTH), is(Direction.DOWN));

        assertThat(transformFacingForRotation(Direction.NORTH, Direction.WEST), is(Direction.EAST));
        assertThat(transformFacingForRotation(Direction.EAST,  Direction.WEST), is(Direction.SOUTH));
        assertThat(transformFacingForRotation(Direction.SOUTH, Direction.WEST), is(Direction.WEST));
        assertThat(transformFacingForRotation(Direction.WEST,  Direction.WEST), is(Direction.NORTH));
        assertThat(transformFacingForRotation(Direction.UP,    Direction.WEST), is(Direction.UP));
        assertThat(transformFacingForRotation(Direction.DOWN,  Direction.WEST), is(Direction.DOWN));

        assertThat(transformFacingForRotation(Direction.NORTH, Direction.UP), is(Direction.DOWN));
        assertThat(transformFacingForRotation(Direction.EAST,  Direction.UP), is(Direction.EAST));
        assertThat(transformFacingForRotation(Direction.SOUTH, Direction.UP), is(Direction.UP));
        assertThat(transformFacingForRotation(Direction.WEST,  Direction.UP), is(Direction.WEST));
        assertThat(transformFacingForRotation(Direction.UP,    Direction.UP), is(Direction.NORTH));
        assertThat(transformFacingForRotation(Direction.DOWN,  Direction.UP), is(Direction.SOUTH));

        assertThat(transformFacingForRotation(Direction.NORTH, Direction.DOWN), is(Direction.UP));
        assertThat(transformFacingForRotation(Direction.EAST,  Direction.DOWN), is(Direction.WEST));
        assertThat(transformFacingForRotation(Direction.SOUTH, Direction.DOWN), is(Direction.DOWN));
        assertThat(transformFacingForRotation(Direction.WEST,  Direction.DOWN), is(Direction.EAST));
        assertThat(transformFacingForRotation(Direction.UP,    Direction.DOWN), is(Direction.SOUTH));
        assertThat(transformFacingForRotation(Direction.DOWN,  Direction.DOWN), is(Direction.NORTH));
    }
}
