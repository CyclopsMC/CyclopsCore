package org.cyclops.cyclopscore.helper;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

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

    public void addStatusInfo(List<Component> infoLines, boolean isEnabled, String statusPrefixKey);

    public void addOptionalInfo(List<Component> list, String prefix);

}
