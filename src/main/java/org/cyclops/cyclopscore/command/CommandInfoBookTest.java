package org.cyclops.cyclopscore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;
import org.cyclops.cyclopscore.infobook.test.ContainerInfoBookTest;

import javax.annotation.Nullable;

/**
 * Command for opending the test info book.
 * @author rubensworks
 *
 */
public class CommandInfoBookTest implements Command<CommandSource> {

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        NetworkHooks.openGui(context.getSource().asPlayer(), new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("gui.cyclopscore.infobook");
            }

            @Nullable
            @Override
            public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                return new ContainerInfoBookTest(id, playerInventory);
            }
        }, (packetBuffer) -> {});

        return 0;
    }

    public static LiteralArgumentBuilder<CommandSource> make() {
        return Commands.literal("infobooktest")
                .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                .executes(new CommandInfoBookTest());
    }

}
