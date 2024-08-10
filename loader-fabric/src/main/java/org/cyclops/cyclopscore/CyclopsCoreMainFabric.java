package org.cyclops.cyclopscore;

import net.fabricmc.api.ModInitializer;
import org.cyclops.cyclopscore.helper.CyclopsCoreInstance;
import org.cyclops.cyclopscore.init.ModBaseFabric;
import org.cyclops.cyclopscore.proxy.ClientProxyFabric;
import org.cyclops.cyclopscore.proxy.CommonProxyFabric;
import org.cyclops.cyclopscore.proxy.IClientProxyCommon;
import org.cyclops.cyclopscore.proxy.ICommonProxyCommon;

/**
 * The main mod class of CyclopsCore.
 * @author rubensworks
 */
public class CyclopsCoreMainFabric extends ModBaseFabric<CyclopsCoreMainFabric> implements ModInitializer {

    /**
     * The unique instance of this mod.
     */
    public static CyclopsCoreMainFabric _instance;

    public CyclopsCoreMainFabric() {
        super(Reference.MOD_ID, (instance) -> {
            _instance = instance;
            CyclopsCoreInstance.MOD = instance;
        });
    }

    @Override
    protected IClientProxyCommon constructClientProxy() {
        return new ClientProxyFabric();
    }

    @Override
    protected ICommonProxyCommon constructCommonProxy() {
        return new CommonProxyFabric();
    }
}
