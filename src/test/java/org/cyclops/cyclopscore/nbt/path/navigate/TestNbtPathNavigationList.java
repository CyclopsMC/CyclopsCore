package org.cyclops.cyclopscore.nbt.path.navigate;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.nbt.path.NbtParseException;
import org.cyclops.cyclopscore.nbt.path.NbtPath;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathNavigationList {

    @Test
    public void testEmptyList() {
        NbtPathNavigationList navigation1 = new NbtPathNavigationList(Lists.newArrayList());

        assertThat(navigation1.isLeafKey("a"), is(false));
        assertThat(navigation1.isLeafKey("b"), is(false));
        assertThat(navigation1.isLeafKey("c"), is(false));

        assertThat(navigation1.getNext("a"), nullValue());
        assertThat(navigation1.getNext("b"), nullValue());
        assertThat(navigation1.getNext("c"), nullValue());
    }

    @Test
    public void testListDiverse() throws NbtParseException {
        NbtPathNavigationList navigation1 = new NbtPathNavigationList(Lists.newArrayList(
                NbtPath.parse("$.a.b").asNavigation(),
                NbtPath.parse("$.c.d").asNavigation()
        ));

        assertThat(navigation1.isLeafKey("a"), is(false));
        assertThat(navigation1.isLeafKey("b"), is(false));
        assertThat(navigation1.isLeafKey("c"), is(false));
        assertThat(navigation1.isLeafKey("d"), is(false));

        assertThat(navigation1.getNext("a"), notNullValue());
        assertThat(navigation1.getNext("b"), nullValue());
        assertThat(navigation1.getNext("c"), notNullValue());
        assertThat(navigation1.getNext("d"), nullValue());

        INbtPathNavigation navigationA = navigation1.getNext("a");

        assertThat(navigationA.isLeafKey("a"), is(false));
        assertThat(navigationA.isLeafKey("b"), is(true));
        assertThat(navigationA.isLeafKey("c"), is(false));
        assertThat(navigationA.isLeafKey("d"), is(false));

        assertThat(navigationA.getNext("a"), nullValue());
        assertThat(navigationA.getNext("b"), nullValue());
        assertThat(navigationA.getNext("c"), nullValue());
        assertThat(navigationA.getNext("d"), nullValue());

        INbtPathNavigation navigationC = navigation1.getNext("c");

        assertThat(navigationC.isLeafKey("a"), is(false));
        assertThat(navigationC.isLeafKey("b"), is(false));
        assertThat(navigationC.isLeafKey("c"), is(false));
        assertThat(navigationC.isLeafKey("d"), is(true));

        assertThat(navigationC.getNext("a"), nullValue());
        assertThat(navigationC.getNext("b"), nullValue());
        assertThat(navigationC.getNext("c"), nullValue());
        assertThat(navigationC.getNext("d"), nullValue());
    }

    @Test
    public void testListOverlapSameDepth() throws NbtParseException {
        NbtPathNavigationList navigation1 = new NbtPathNavigationList(Lists.newArrayList(
                NbtPath.parse("$*.b").asNavigation(),
                NbtPath.parse("$.c.d").asNavigation()
        ));

        assertThat(navigation1.isLeafKey("a"), is(false));
        assertThat(navigation1.isLeafKey("b"), is(false));
        assertThat(navigation1.isLeafKey("c"), is(false));
        assertThat(navigation1.isLeafKey("d"), is(false));

        assertThat(navigation1.getNext("a"), notNullValue());
        assertThat(navigation1.getNext("b"), notNullValue());
        assertThat(navigation1.getNext("c"), notNullValue());
        assertThat(navigation1.getNext("d"), notNullValue());

        INbtPathNavigation navigationA = navigation1.getNext("a");

        assertThat(navigationA.isLeafKey("a"), is(false));
        assertThat(navigationA.isLeafKey("b"), is(true));
        assertThat(navigationA.isLeafKey("c"), is(false));
        assertThat(navigationA.isLeafKey("d"), is(false));

        assertThat(navigationA.getNext("a"), nullValue());
        assertThat(navigationA.getNext("b"), nullValue());
        assertThat(navigationA.getNext("c"), nullValue());
        assertThat(navigationA.getNext("d"), nullValue());

        INbtPathNavigation navigationB = navigation1.getNext("b");

        assertThat(navigationB.isLeafKey("a"), is(false));
        assertThat(navigationB.isLeafKey("b"), is(true));
        assertThat(navigationB.isLeafKey("c"), is(false));
        assertThat(navigationB.isLeafKey("d"), is(false));

        assertThat(navigationB.getNext("a"), nullValue());
        assertThat(navigationB.getNext("b"), nullValue());
        assertThat(navigationB.getNext("c"), nullValue());
        assertThat(navigationB.getNext("d"), nullValue());

        INbtPathNavigation navigationC = navigation1.getNext("c");

        assertThat(navigationC.isLeafKey("a"), is(false));
        assertThat(navigationC.isLeafKey("b"), is(true));
        assertThat(navigationC.isLeafKey("c"), is(false));
        assertThat(navigationC.isLeafKey("d"), is(true));

        assertThat(navigationC.getNext("a"), nullValue());
        assertThat(navigationC.getNext("b"), nullValue());
        assertThat(navigationC.getNext("c"), nullValue());
        assertThat(navigationC.getNext("d"), nullValue());

        INbtPathNavigation navigationD = navigation1.getNext("d");

        assertThat(navigationD.isLeafKey("a"), is(false));
        assertThat(navigationD.isLeafKey("b"), is(true));
        assertThat(navigationD.isLeafKey("c"), is(false));
        assertThat(navigationD.isLeafKey("d"), is(false));

        assertThat(navigationD.getNext("a"), nullValue());
        assertThat(navigationD.getNext("b"), nullValue());
        assertThat(navigationD.getNext("c"), nullValue());
        assertThat(navigationD.getNext("d"), nullValue());
    }

    @Test
    public void testListOverlapDifferentDepth() throws NbtParseException {
        NbtPathNavigationList navigation1 = new NbtPathNavigationList(Lists.newArrayList(
                NbtPath.parse("$*").asNavigation(),
                NbtPath.parse("$.c.d").asNavigation()
        ));

        assertThat(navigation1.isLeafKey("a"), is(true));
        assertThat(navigation1.isLeafKey("b"), is(true));
        assertThat(navigation1.isLeafKey("c"), is(true));
        assertThat(navigation1.isLeafKey("d"), is(true));

        assertThat(navigation1.getNext("a"), nullValue());
        assertThat(navigation1.getNext("b"), nullValue());
        assertThat(navigation1.getNext("c"), notNullValue());
        assertThat(navigation1.getNext("d"), nullValue());

        INbtPathNavigation navigationC = navigation1.getNext("c");

        assertThat(navigationC.isLeafKey("a"), is(false));
        assertThat(navigationC.isLeafKey("b"), is(false));
        assertThat(navigationC.isLeafKey("c"), is(false));
        assertThat(navigationC.isLeafKey("d"), is(true));

        assertThat(navigationC.getNext("a"), nullValue());
        assertThat(navigationC.getNext("b"), nullValue());
        assertThat(navigationC.getNext("c"), nullValue());
        assertThat(navigationC.getNext("d"), nullValue());
    }

}
