package org.cyclops.cyclopscore.infobook;

import lombok.Data;
import org.cyclops.cyclopscore.datastructure.EvictingStack;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Default implementation for an {@link IInfoBook}.
 * @author rubensworks
 */
@Data
public class InfoBook implements IInfoBook {

    private final ModBase mod;
    private final int pagesPerView;
    private final EvictingStack<InfoSection.Location> history = new EvictingStack<InfoSection.Location>(128);
    private int currentPage = 0;
    private InfoSection currentSection = null;

}
