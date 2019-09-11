package org.cyclops.cyclopscore.helper;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.cyclopscore.persist.nbt.INBTSerializable;

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
    public static void addStatusInfo(List<String> infoLines, boolean isEnabled, String statusPrefixKey) {
        String autoSupply = localize(KEY_DISABLED);
        if (isEnabled) {
            autoSupply = localize(KEY_ENABLED);
        }
        infoLines.add(localize(statusPrefixKey, autoSupply));
    }

    /**
     * Localize a given entity id.
     *
     * @param entityId The unique entity name id.
     * @return The localized name.
     */
    @OnlyIn(Dist.CLIENT)
    public static String getLocalizedEntityName(String entityId) {
        return L10NHelpers.localize("entity." + entityId + ".name");
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

    /**
     * Holder class that acts as a parameterized unlocalized string.
     * This can also take other unlocalized strings in the parameter list, and they will recursively
     * be localized when calling {@link UnlocalizedString#localize()}.
     */
    public static class UnlocalizedString implements INBTSerializable {

        private String parameterizedString;
        private Object[] parameters;

        public UnlocalizedString(String parameterizedString, Object... parameters) {
            this.parameterizedString = parameterizedString;
            this.parameters = parameters;
            for(int i = 0; i < parameters.length; i++) {
                if(!(parameters[i] instanceof UnlocalizedString || parameters[i] instanceof String)) {
                    parameters[i] = String.valueOf(parameters[i]);
                }
            }
        }

        public UnlocalizedString() {
            this.parameterizedString = null;
            this.parameters = null;
        }

        public String localize() {
            Object[] realParameters = new Object[parameters.length];
            for(int i = 0; i < parameters.length; i++) {
                Object param = parameters[i];
                if(param instanceof UnlocalizedString) {
                    realParameters[i] = ((UnlocalizedString) param).localize();
                } else {
                    realParameters[i] = param;
                }
            }
            return L10NHelpers.localize(parameterizedString, realParameters);
        }

        @Override
        public CompoundNBT toNBT() {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("parameterizedString", parameterizedString);
            ListNBT list = new ListNBT();
            for (Object parameter : parameters) {
                if(parameter instanceof UnlocalizedString) {
                    CompoundNBT objectTag = ((UnlocalizedString) parameter).toNBT();
                    objectTag.putString("type", "object");
                    list.add(objectTag);
                } else {
                    CompoundNBT stringTag = new CompoundNBT();
                    stringTag.put("value", new StringNBT((String) parameter));
                    stringTag.putString("type", "string");
                    list.add(stringTag);
                }
            }
            tag.put("parameters", list);
            return tag;
        }

        @Override
        public void fromNBT(CompoundNBT tag) {
            this.parameterizedString = tag.getString("parameterizedString");
            ListNBT list = tag.getList("parameters", Constants.NBT.TAG_COMPOUND);
            this.parameters = new Object[list.size()];
            for (int i = 0; i < this.parameters.length; i++) {
                CompoundNBT elementTag = list.getCompound(i);
                if("object".equals(elementTag.getString("type"))) {
                    UnlocalizedString object = new UnlocalizedString();
                    object.fromNBT(elementTag);
                    this.parameters[i] = object;
                } else {
                    this.parameters[i] = elementTag.getString("value");
                }
            }
        }

    }

}
