package org.cyclops.cyclopscore.helper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.TooltipFlag;
import org.cyclops.cyclopscore.Reference;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author rubensworks
 */
public class L10NHelpersCommon implements IL10NHelpers {

    private static final String KEY_ENABLED = "general." + Reference.MOD_ID + ".info.enabled";
    private static final String KEY_DISABLED = "general." + Reference.MOD_ID + ".info.disabled";

    private final IModHelpers modHelpers;

    public L10NHelpersCommon(IModHelpers modHelpers) {
        this.modHelpers = modHelpers;
    }

    @Override
    public String localize(String key, Object... params) {
        if(modHelpers.getMinecraftHelpers().isModdedEnvironment()) {
            return I18n.get(key, params);
        } else {
            return String.format("%s: %s", key, Arrays.toString(params));
        }
    }

    @Override
    public void addStatusInfo(Consumer<Component> tooltipAdder, boolean isEnabled, String statusPrefixKey) {
        Component autoSupply = Component.translatable(KEY_DISABLED);
        if (isEnabled) {
            autoSupply = Component.translatable(KEY_ENABLED);
        }
        tooltipAdder.accept(Component.translatable(statusPrefixKey, autoSupply));
    }

    @Override
    public void addOptionalInfo(Consumer<Component> tooltipAdder, String prefix, TooltipFlag tooltipFlag) {
        String key = prefix + ".info";
        if (I18n.exists(key)) {
            if (tooltipFlag.isAdvanced() || modHelpers.getMinecraftClientHelpers().isShifted()) {
                String localized = localize(key);
                StringHelpers.splitLines(localized, getMaxTooltipLineLength(), getInfoPrefix())
                        .stream()
                        .map(Component::literal)
                        .forEach(tooltipAdder);
            } else {
                tooltipAdder.accept(Component.translatable("general." + Reference.MOD_ID + ".tooltip.info")
                        .setStyle(Style.EMPTY
                                .withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY))
                                .withItalic(true)));
            }
        }
    }
}
