package org.cyclops.cyclopscore;

import net.fabricmc.api.ModInitializer;
import org.cyclops.cyclopscore.advancement.criterion.GuiContainerOpenTriggerConfig;
import org.cyclops.cyclopscore.advancement.criterion.GuiContainerOpenTriggerEventHooksFabric;
import org.cyclops.cyclopscore.advancement.criterion.ItemCraftedTriggerConfig;
import org.cyclops.cyclopscore.advancement.criterion.ItemCraftedTriggerTriggerEventHooksFabric;
import org.cyclops.cyclopscore.advancement.criterion.ModItemObtainedTriggerConfig;
import org.cyclops.cyclopscore.advancement.criterion.ModItemObtainedTriggerEventHooksFabric;
import org.cyclops.cyclopscore.component.DataComponentCapacityConfig;
import org.cyclops.cyclopscore.component.DataComponentEnergyStorageConfig;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
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
public class CyclopsCoreFabric extends ModBaseFabric<CyclopsCoreFabric> implements ModInitializer {

    /**
     * The unique instance of this mod.
     */
    public static CyclopsCoreFabric _instance;

    public CyclopsCoreFabric() {
        super(Reference.MOD_ID, (instance) -> {
            _instance = instance;
            CyclopsCoreInstance.MOD = instance;
        });
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        new GuiContainerOpenTriggerEventHooksFabric();
        new ItemCraftedTriggerTriggerEventHooksFabric();
        new ModItemObtainedTriggerEventHooksFabric();
    }

    @Override
    protected IClientProxyCommon constructClientProxy() {
        return new ClientProxyFabric();
    }

    @Override
    protected ICommonProxyCommon constructCommonProxy() {
        return new CommonProxyFabric();
    }

    @Override
    protected void onConfigsRegister(ConfigHandlerCommon configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig(this));

        // Triggers
        configHandler.addConfigurable(new GuiContainerOpenTriggerConfig<>(this));
        configHandler.addConfigurable(new ItemCraftedTriggerConfig<>(this));
        configHandler.addConfigurable(new ModItemObtainedTriggerConfig<>(this));

        // Data components
        configHandler.addConfigurable(new DataComponentCapacityConfig(this));
        configHandler.addConfigurable(new DataComponentEnergyStorageConfig(this));
    }
}
