package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.config.StreamCodecConsumerHack;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.inventory.container.IContainerFactoryCommon;

/**
 * @author rubensworks
 */
public class GuiActionFabric<T extends AbstractContainerMenu, M extends IModBase> extends GuiActionCommonBase<T, M> {
    @Override
    protected MenuType<T> transformMenuType(MenuType<T> menuType) {
        if (menuType.constructor instanceof IContainerFactoryCommon<T> containerFactoryCommon) {
            // This is a hack to make Fabric's aproach to passing custom data to guis compatible using StreamCodec's
            // with Forge's and NeoForge's approach that relies on raw FriendlyByteBufs.
            StreamCodecConsumerHack<? super RegistryFriendlyByteBuf> packetCodec = new StreamCodecConsumerHack<>();
            return new ExtendedScreenHandlerType<>((syncId, inventory, voidData) -> {
                FriendlyByteBuf buffer = packetCodec.getAndResetBuffer();
                T screen = containerFactoryCommon.create(syncId, inventory, buffer);
                buffer.release();
                return screen;
            }, packetCodec);
        }
        return menuType;
    }
}
