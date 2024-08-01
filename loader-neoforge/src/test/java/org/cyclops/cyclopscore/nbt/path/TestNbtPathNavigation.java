package org.cyclops.cyclopscore.nbt.path;

import org.cyclops.cyclopscore.nbt.path.navigate.INbtPathNavigation;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathNavigation {

    @Test
    public void testParseValid() throws NbtParseException {
        NbtPath.parse("$.a.b.c[\"def\"]*.ghi").asNavigation();
    }

    @Test
    public void testNavigateSimple() throws NbtParseException {
        INbtPathNavigation navigation1 = NbtPath.parse("$.a.b").asNavigation();

        assertThat(navigation1.isLeafKey("a"), is(false));
        assertThat(navigation1.isLeafKey("b"), is(false));
        assertThat(navigation1.isLeafKey("c"), is(false));

        assertThat(navigation1.getNext("a"), notNullValue());
        assertThat(navigation1.getNext("b"), nullValue());
        assertThat(navigation1.getNext("c"), nullValue());

        INbtPathNavigation navigation2 = navigation1.getNext("a");

        assertThat(navigation2.isLeafKey("a"), is(false));
        assertThat(navigation2.isLeafKey("b"), is(true));
        assertThat(navigation2.isLeafKey("c"), is(false));

        assertThat(navigation2.getNext("a"), nullValue());
        assertThat(navigation2.getNext("b"), nullValue());
        assertThat(navigation2.getNext("c"), nullValue());
    }

    @Test
    public void testNavigateWildcardLeaf() throws NbtParseException {
        INbtPathNavigation navigation1 = NbtPath.parse("$.a*").asNavigation();

        assertThat(navigation1.isLeafKey("a"), is(false));
        assertThat(navigation1.isLeafKey("b"), is(false));
        assertThat(navigation1.isLeafKey("c"), is(false));

        assertThat(navigation1.getNext("a"), notNullValue());
        assertThat(navigation1.getNext("b"), nullValue());
        assertThat(navigation1.getNext("c"), nullValue());

        INbtPathNavigation navigation2 = navigation1.getNext("a");

        assertThat(navigation2.isLeafKey("a"), is(true));
        assertThat(navigation2.isLeafKey("b"), is(true));
        assertThat(navigation2.isLeafKey("c"), is(true));

        assertThat(navigation2.getNext("a"), nullValue());
        assertThat(navigation2.getNext("b"), nullValue());
        assertThat(navigation2.getNext("c"), nullValue());
    }

    @Test
    public void testNavigateWildcardInner() throws NbtParseException {
        INbtPathNavigation navigation1 = NbtPath.parse("$*.b").asNavigation();

        assertThat(navigation1.isLeafKey("a"), is(false));
        assertThat(navigation1.isLeafKey("b"), is(false));
        assertThat(navigation1.isLeafKey("c"), is(false));

        assertThat(navigation1.getNext("a"), notNullValue());
        assertThat(navigation1.getNext("b"), notNullValue());
        assertThat(navigation1.getNext("c"), notNullValue());

        INbtPathNavigation navigation2 = navigation1.getNext("a");

        assertThat(navigation2.isLeafKey("a"), is(false));
        assertThat(navigation2.isLeafKey("b"), is(true));
        assertThat(navigation2.isLeafKey("c"), is(false));

        assertThat(navigation2.getNext("a"), nullValue());
        assertThat(navigation2.getNext("b"), nullValue());
        assertThat(navigation2.getNext("c"), nullValue());

        INbtPathNavigation navigation3 = navigation1.getNext("b");

        assertThat(navigation3.isLeafKey("a"), is(false));
        assertThat(navigation3.isLeafKey("b"), is(true));
        assertThat(navigation3.isLeafKey("c"), is(false));

        assertThat(navigation3.getNext("a"), nullValue());
        assertThat(navigation3.getNext("b"), nullValue());
        assertThat(navigation3.getNext("c"), nullValue());

        INbtPathNavigation navigation4 = navigation1.getNext("c");

        assertThat(navigation4.isLeafKey("a"), is(false));
        assertThat(navigation4.isLeafKey("b"), is(true));
        assertThat(navigation4.isLeafKey("c"), is(false));

        assertThat(navigation4.getNext("a"), nullValue());
        assertThat(navigation4.getNext("b"), nullValue());
        assertThat(navigation4.getNext("c"), nullValue());
    }

}
