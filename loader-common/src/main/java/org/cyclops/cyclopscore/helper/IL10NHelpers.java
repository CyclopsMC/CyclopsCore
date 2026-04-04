package org.cyclops.cyclopscore.helper;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

/**
 * @author rubensworks
 */
public interface IL10NHelpers {

    public default int getMaxTooltipLineLength() {
        return 25;
    }

    public default String getInfoPrefix() {
        return ChatFormatting.DARK_PURPLE.toString() + ChatFormatting.ITALIC.toString();
    }

    public String localize(String key, Object... params);

    public void addStatusInfo(Consumer<Component> tooltipAdder, boolean isEnabled, String statusPrefixKey);

    public void addOptionalInfo(Consumer<Component> tooltipAdder, String prefix, TooltipFlag tooltipFlag);

}
