package org.cyclops.cyclopscore.infobook.test;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.flag.FeatureFlags;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.inventory.container.ContainerTypeData;

/**
 * Config for {@link ContainerInfoBookTest}.
 * @author rubensworks
 */
public class ContainerInfoBookTestConfig extends GuiConfig<ContainerInfoBookTest, ModBase<?>> {

    public ContainerInfoBookTestConfig() {
        super(CyclopsCore._instance,
                "test_infobook",
                eConfig -> new ContainerTypeData<>(ContainerInfoBookTest::new, FeatureFlags.VANILLA_SET));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & MenuAccess<ContainerInfoBookTest>> MenuScreens.ScreenConstructor<ContainerInfoBookTest, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenInfoBookTest::new);
    }

}
