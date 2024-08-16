package org.cyclops.cyclopscore.config.configurabletypeaction;

import org.cyclops.cyclopscore.config.extendedconfig.DummyConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Just a dummy action.
 * @author rubensworks
 * @param <M> The mod type
 */
public class DummyActionCommon<M extends IModBase> extends ConfigurableTypeActionCommon<DummyConfigCommon<M>, Void, M> {

}
