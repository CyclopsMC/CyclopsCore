package org.cyclops.cyclopscore;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.cyclops.cyclopscore.advancement.criterion.GuiContainerOpenTriggerConfig;
import org.cyclops.cyclopscore.advancement.criterion.GuiContainerOpenTriggerEventHooksForge;
import org.cyclops.cyclopscore.advancement.criterion.ItemCraftedTriggerConfig;
import org.cyclops.cyclopscore.advancement.criterion.ItemCraftedTriggerTriggerEventHooksForge;
import org.cyclops.cyclopscore.advancement.criterion.ModItemObtainedTriggerConfig;
import org.cyclops.cyclopscore.advancement.criterion.ModItemObtainedTriggerEventHooksForge;
import org.cyclops.cyclopscore.component.DataComponentCapacityConfig;
import org.cyclops.cyclopscore.component.DataComponentEnergyStorageConfig;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
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
    protected void setup(FMLCommonSetupEvent event) {
        super.setup(event);

        new GuiContainerOpenTriggerEventHooksForge();
        new ItemCraftedTriggerTriggerEventHooksForge();
        new ModItemObtainedTriggerEventHooksForge();
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
