package org.cyclops.cyclopscore.helper;

import net.minecraft.util.EnumFacing;
import org.junit.Test;

import static org.cyclops.cyclopscore.helper.DirectionHelpers.transformFacingForRotation;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestDirectionHelpers {

    @Test
    public void testTransformFacingForRotation() {
        assertThat(transformFacingForRotation(EnumFacing.NORTH, EnumFacing.NORTH), is(EnumFacing.NORTH));
        assertThat(transformFacingForRotation(EnumFacing.EAST,  EnumFacing.NORTH), is(EnumFacing.EAST));
        assertThat(transformFacingForRotation(EnumFacing.SOUTH, EnumFacing.NORTH), is(EnumFacing.SOUTH));
        assertThat(transformFacingForRotation(EnumFacing.WEST,  EnumFacing.NORTH), is(EnumFacing.WEST));
        assertThat(transformFacingForRotation(EnumFacing.UP,    EnumFacing.NORTH), is(EnumFacing.UP));
        assertThat(transformFacingForRotation(EnumFacing.DOWN,  EnumFacing.NORTH), is(EnumFacing.DOWN));

        assertThat(transformFacingForRotation(EnumFacing.NORTH, EnumFacing.EAST), is(EnumFacing.WEST));
        assertThat(transformFacingForRotation(EnumFacing.EAST,  EnumFacing.EAST), is(EnumFacing.NORTH));
        assertThat(transformFacingForRotation(EnumFacing.SOUTH, EnumFacing.EAST), is(EnumFacing.EAST));
        assertThat(transformFacingForRotation(EnumFacing.WEST,  EnumFacing.EAST), is(EnumFacing.SOUTH));
        assertThat(transformFacingForRotation(EnumFacing.UP,    EnumFacing.EAST), is(EnumFacing.UP));
        assertThat(transformFacingForRotation(EnumFacing.DOWN,  EnumFacing.EAST), is(EnumFacing.DOWN));

        assertThat(transformFacingForRotation(EnumFacing.NORTH, EnumFacing.SOUTH), is(EnumFacing.SOUTH));
        assertThat(transformFacingForRotation(EnumFacing.EAST,  EnumFacing.SOUTH), is(EnumFacing.WEST));
        assertThat(transformFacingForRotation(EnumFacing.SOUTH, EnumFacing.SOUTH), is(EnumFacing.NORTH));
        assertThat(transformFacingForRotation(EnumFacing.WEST,  EnumFacing.SOUTH), is(EnumFacing.EAST));
        assertThat(transformFacingForRotation(EnumFacing.UP,    EnumFacing.SOUTH), is(EnumFacing.UP));
        assertThat(transformFacingForRotation(EnumFacing.DOWN,  EnumFacing.SOUTH), is(EnumFacing.DOWN));

        assertThat(transformFacingForRotation(EnumFacing.NORTH, EnumFacing.WEST), is(EnumFacing.EAST));
        assertThat(transformFacingForRotation(EnumFacing.EAST,  EnumFacing.WEST), is(EnumFacing.SOUTH));
        assertThat(transformFacingForRotation(EnumFacing.SOUTH, EnumFacing.WEST), is(EnumFacing.WEST));
        assertThat(transformFacingForRotation(EnumFacing.WEST,  EnumFacing.WEST), is(EnumFacing.NORTH));
        assertThat(transformFacingForRotation(EnumFacing.UP,    EnumFacing.WEST), is(EnumFacing.UP));
        assertThat(transformFacingForRotation(EnumFacing.DOWN,  EnumFacing.WEST), is(EnumFacing.DOWN));

        assertThat(transformFacingForRotation(EnumFacing.NORTH, EnumFacing.UP), is(EnumFacing.DOWN));
        assertThat(transformFacingForRotation(EnumFacing.EAST,  EnumFacing.UP), is(EnumFacing.EAST));
        assertThat(transformFacingForRotation(EnumFacing.SOUTH, EnumFacing.UP), is(EnumFacing.UP));
        assertThat(transformFacingForRotation(EnumFacing.WEST,  EnumFacing.UP), is(EnumFacing.WEST));
        assertThat(transformFacingForRotation(EnumFacing.UP,    EnumFacing.UP), is(EnumFacing.NORTH));
        assertThat(transformFacingForRotation(EnumFacing.DOWN,  EnumFacing.UP), is(EnumFacing.SOUTH));

        assertThat(transformFacingForRotation(EnumFacing.NORTH, EnumFacing.DOWN), is(EnumFacing.UP));
        assertThat(transformFacingForRotation(EnumFacing.EAST,  EnumFacing.DOWN), is(EnumFacing.WEST));
        assertThat(transformFacingForRotation(EnumFacing.SOUTH, EnumFacing.DOWN), is(EnumFacing.DOWN));
        assertThat(transformFacingForRotation(EnumFacing.WEST,  EnumFacing.DOWN), is(EnumFacing.EAST));
        assertThat(transformFacingForRotation(EnumFacing.UP,    EnumFacing.DOWN), is(EnumFacing.SOUTH));
        assertThat(transformFacingForRotation(EnumFacing.DOWN,  EnumFacing.DOWN), is(EnumFacing.NORTH));
    }
}
