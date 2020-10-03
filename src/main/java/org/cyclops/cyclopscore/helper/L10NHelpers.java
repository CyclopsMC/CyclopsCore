package org.cyclops.cyclopscore.helper;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.item.IInformationProvider;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A set of localization helpers.
 *
 * @author rubensworks
 */
public final class L10NHelpers {

    public static final int MAX_TOOLTIP_LINE_LENGTH = 25;
    private static final String KEY_ENABLED = "general." + Reference.MOD_ID + ".info.enabled";
    private static final String KEY_DISABLED = "general." + Reference.MOD_ID + ".info.disabled";

    /**
     * Localize a key that has values in language files.
     *
     * @param key    The key of the language file entry.
     * @param params The parameters of the formatting
     * @return The localized string.
     */
    @OnlyIn(Dist.CLIENT)
    public static String localize(String key, Object... params) {
        if(MinecraftHelpers.isModdedEnvironment()) {
            return I18n.format(key, params);
        } else {
            return String.format("%s: %s", key, Arrays.toString(params));
        }
    }

    /**
     * Show status info about the activation about an item to the info box.
     *
     * @param infoLines       The list to add info to.
     * @param isEnabled       If the item is enabled.
     * @param statusPrefixKey The prefix for the l10n key that will show if it is enabled,
     *                        this should be a formatted string with one parameter.
     */
    @OnlyIn(Dist.CLIENT)
    public static void addStatusInfo(List<ITextComponent> infoLines, boolean isEnabled, String statusPrefixKey) {
        ITextComponent autoSupply = new TranslationTextComponent(KEY_DISABLED);
        if (isEnabled) {
            autoSupply = new TranslationTextComponent(KEY_ENABLED);
        }
        infoLines.add(new TranslationTextComponent(statusPrefixKey, autoSupply));
    }

    /**
     * Add the optional info lines to the item tooltip.
     *
     * @param list   The list to add the lines to.
     * @param prefix The I18N key prefix, being the unlocalized name of blocks or items.
     */
    @OnlyIn(Dist.CLIENT)
    public static void addOptionalInfo(List<ITextComponent> list, String prefix) {
        String key = prefix + ".info";
        if (I18n.hasKey(key)) {
            if (MinecraftHelpers.isShifted()) {
                String localized = localize(key);
                list.addAll(StringHelpers.splitLines(localized, MAX_TOOLTIP_LINE_LENGTH, IInformationProvider.INFO_PREFIX)
                        .stream()
                        .map(StringTextComponent::new)
                        .collect(Collectors.toList()));
            } else {
                list.add(new TranslationTextComponent("general." + Reference.MOD_ID + ".tooltip.info")
                        .setStyle(new Style()
                                .setColor(TextFormatting.GRAY)
                                .setItalic(true)));
            }
        }
    }

}
