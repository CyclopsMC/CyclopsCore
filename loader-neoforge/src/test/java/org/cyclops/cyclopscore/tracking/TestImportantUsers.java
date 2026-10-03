package org.cyclops.cyclopscore.tracking;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author rubensworks
 */
public class TestImportantUsers {

    @Test
    public void testThreadIsDaemon() {
        // A non-daemon thread would keep the client JVM alive after quitting the game.
        assertThat(ImportantUsers.createThread(() -> {}).isDaemon(), is(true));
    }

}
