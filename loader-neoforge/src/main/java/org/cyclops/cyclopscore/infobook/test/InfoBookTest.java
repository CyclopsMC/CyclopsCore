package org.cyclops.cyclopscore.infobook.test;

import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.infobook.InfoBook;

/**
 * An infobook instance for testing.
 * @author rubensworks
 */
public class InfoBookTest extends InfoBook {

    private static InfoBookTest _instance = null;

    public InfoBookTest() {
        super(CyclopsCore._instance, 2, "http://example.org");
    }

    public static InfoBookTest getInstance() {
        if(_instance == null) {
            _instance = new InfoBookTest();
        }
        return _instance;
    }
}
