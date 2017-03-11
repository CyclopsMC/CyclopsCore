package org.cyclops.cyclopscore.infobook.pageelement;

import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.w3c.dom.Element;

/**
 * Factory for {@link IReward}.
 * @author rubensworks
 */
public interface IRewardFactory {
    public IReward create(IInfoBook infoBook, Element node) throws InfoBookParser.InvalidAppendixException;
}
