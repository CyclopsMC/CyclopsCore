package org.cyclops.cyclopscore.infobook.test;

import net.minecraft.world.flag.FeatureFlags;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.inventory.container.ContainerTypeDataCommon;

/**
 * Config for {@link ContainerInfoBookTest}.
 * @author rubensworks
 */
public class ContainerInfoBookTestConfig<M extends IModBase> extends GuiConfigCommon<ContainerInfoBookTest, M> {

    public ContainerInfoBookTestConfig(M mod) {
        super(mod,
                "test_infobook",
                eConfig -> new ContainerTypeDataCommon<>(ContainerInfoBookTest::new, FeatureFlags.VANILLA_SET));
    }

    @Override
    public GuiConfigScreenFactoryProvider<ContainerInfoBookTest> getScreenFactoryProvider() {
        return new ContainerInfoBookTestConfigScreenFactoryProvider();
    }
}
