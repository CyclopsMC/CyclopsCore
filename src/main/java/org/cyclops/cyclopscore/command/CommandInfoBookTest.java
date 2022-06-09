package org.cyclops.cyclopscore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkHooks;
import org.cyclops.cyclopscore.infobook.test.ContainerInfoBookTest;

import javax.annotation.Nullable;

/**
 * Command for opending the test info book.
 * @author rubensworks
 *
 */
public class CommandInfoBookTest implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        NetworkHooks.openGui(context.getSource().getPlayerOrException(), new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable("gui.cyclopscore.infobook");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
                return new ContainerInfoBookTest(id, playerInventory);
            }
        }, (packetBuffer) -> {});

        return 0;
    }

    public static LiteralArgumentBuilder<CommandSourceStack> make() {
        return Commands.literal("infobooktest")
                .requires((commandSource) -> commandSource.hasPermission(2))
                .executes(new CommandInfoBookTest());
    }

}
