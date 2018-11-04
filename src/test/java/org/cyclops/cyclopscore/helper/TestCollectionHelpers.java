package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Collections;

import static org.cyclops.cyclopscore.helper.CollectionHelpers.compareCollection;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestCollectionHelpers {

    @Test
    public void testCompareCollectionEmpty() {
        assertThat(compareCollection(Collections.emptyList(), Collections.emptyList()), is(0));
    }

    @Test
    public void testCompareCollectionEqual() {
        assertThat(compareCollection(
                Lists.newArrayList(1, 2, 3),
                Lists.newArrayList(1, 2, 3)
        ), is(0));

        assertThat(compareCollection(
                Lists.newArrayList(1, 2, 3, 4, 5),
                Lists.newArrayList(1, 2, 3, 4, 5)
        ), is(0));

        assertThat(compareCollection(
                Lists.newArrayList(1, 2, 3, 4, 5),
                Lists.newArrayList(5, 2, 3, 1, 4)
        ), is(0));
    }

    @Test
    public void testCompareCollectionNonEqual() {
        assertThat(compareCollection(
                Lists.newArrayList(1, 2),
                Lists.newArrayList(1, 2, 3)
        ), is(-1));

        assertThat(compareCollection(
                Lists.newArrayList(1, 2, 3),
                Lists.newArrayList(1, 2)
        ), is(1));

        assertThat(compareCollection(
                Lists.newArrayList(2),
                Lists.newArrayList(3)
        ), is(-1));

        assertThat(compareCollection(
                Lists.newArrayList(3),
                Lists.newArrayList(2)
        ), is(1));
    }
}
