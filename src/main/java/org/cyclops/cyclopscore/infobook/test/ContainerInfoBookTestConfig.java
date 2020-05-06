package org.cyclops.cyclopscore.infobook.test;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;
import org.cyclops.cyclopscore.inventory.container.ContainerTypeData;

/**
 * Config for {@link ContainerInfoBookTest}.
 * @author rubensworks
 */
public class ContainerInfoBookTestConfig extends GuiConfig<ContainerInfoBookTest> {

    public ContainerInfoBookTestConfig() {
        super(CyclopsCore._instance,
                "test_infobook",
                eConfig -> new ContainerTypeData<>(ContainerInfoBookTest::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & IHasContainer<ContainerInfoBookTest>> ScreenManager.IScreenFactory<ContainerInfoBookTest, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenInfoBookTest::new);
    }

}
