package org.cyclops.cyclopscore;

import net.minecraftforge.fml.common.Mod;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.helper.CyclopsCoreInstance;
import org.cyclops.cyclopscore.init.ModBaseForge;
import org.cyclops.cyclopscore.proxy.ClientProxyForge;
import org.cyclops.cyclopscore.proxy.CommonProxyForge;
import org.cyclops.cyclopscore.proxy.IClientProxyCommon;
import org.cyclops.cyclopscore.proxy.ICommonProxyCommon;

/**
 * The main mod class of CyclopsCore.
 * @author rubensworks
 *
 */
@Mod(Reference.MOD_ID)
public class CyclopsCoreForge extends ModBaseForge<CyclopsCoreForge> {

    /**
     * The unique instance of this mod.
     */
    public static CyclopsCoreForge _instance;

    public CyclopsCoreForge() {
        super(Reference.MOD_ID, (instance) -> {
            _instance = instance;
            CyclopsCoreInstance.MOD = instance;
        });
    }

    @Override
    protected IClientProxyCommon constructClientProxy() {
        return new ClientProxyForge();
    }

    @Override
    protected ICommonProxyCommon constructCommonProxy() {
        return new CommonProxyForge();
    }

    @Override
    protected void onConfigsRegister(ConfigHandler configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig(this));
    }
}
