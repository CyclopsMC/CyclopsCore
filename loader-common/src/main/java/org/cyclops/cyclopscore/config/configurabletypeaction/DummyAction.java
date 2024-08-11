package org.cyclops.cyclopscore.config.configurabletypeaction;

import org.cyclops.cyclopscore.config.extendedconfig.DummyConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Just a dummy action.
 * @author rubensworks
 * @param <M> The mod type
 */
public class DummyAction<M extends IModBase> extends ConfigurableTypeAction<DummyConfigCommon<M>, Void, M> {

}