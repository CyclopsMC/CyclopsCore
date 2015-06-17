package org.cyclops.cyclopscore.helper;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.cyclopscore.persist.nbt.INBTSerializable;

import java.util.List;

/**
 * A set of localization helpers.
 *
 * @author rubensworks
 */
public final class L10NHelpers {

    public static final int MAX_TOOLTIP_LINE_LENGTH = 25;
    private static final String KEY_ENABLED = "general.info.enabled";
    private static final String KEY_DISABLED = "general.info.disabled";

    /**
     * Localize a key that has values in language files.
     *
     * @param key The key of the language file entry.
     * @return The localized string.
     */
    public static String localize(String key) {
        return StatCollector.translateToLocal(key);
    }

    /**
     * Localize a key that has values in language files.
     *
     * @param key    The key of the language file entry.
     * @param params The parameters of the formatting
     * @return The localized string.
     */
    public static String localize(String key, Object... params) {
        return StatCollector.translateToLocalFormatted(key, params);
    }

    /**
     * Show status info about the activation about an item to the info box.
     *
     * @param infoLines       The list to add info to.
     * @param isEnabled       If the item is enabled.
     * @param statusPrefixKey The prefix for the l10n key that will show if it is enabled,
     *                        this should be a formatted string with one parameter.
     */
    public static void addStatusInfo(List<String> infoLines, boolean isEnabled, String statusPrefixKey) {
        String autoSupply = EnumChatFormatting.RESET + localize(KEY_DISABLED);
        if (isEnabled) {
            autoSupply = EnumChatFormatting.GREEN + localize(KEY_ENABLED);
        }
        infoLines.add(EnumChatFormatting.BOLD
                + localize(statusPrefixKey, autoSupply));
    }

    /**
     * Localize a given entity id.
     *
     * @param entityId The unique entity name id.
     * @return The localized name.
     */
    public static String getLocalizedEntityName(String entityId) {
        return L10NHelpers.localize("entity." + entityId + ".name");
    }

    /**
     * Add the optional info lines to the item tooltip.
     *
     * @param list   The list to add the lines to.
     * @param prefix The I18N key prefix, being the unlocalized name of blocks or items.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addOptionalInfo(List list, String prefix) {
        String key = prefix + ".info";
        if (StatCollector.canTranslate(key)) {
            if (MinecraftHelpers.isShifted()) {
                String localized = localize(key);
                list.addAll(StringHelpers.splitLines(localized, MAX_TOOLTIP_LINE_LENGTH,
                        IInformationProvider.INFO_PREFIX));
            } else {
                list.add(localize(EnumChatFormatting.GRAY.toString()
                        + EnumChatFormatting.ITALIC.toString()
                        + localize("general." + Reference.MOD_ID + ".tooltip.info")));
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
        public NBTTagCompound toNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("parameterizedString", parameterizedString);
            NBTTagList list = new NBTTagList();
            for (Object parameter : parameters) {
                if(parameter instanceof UnlocalizedString) {
                    NBTTagCompound objectTag = ((UnlocalizedString) parameter).toNBT();
                    objectTag.setString("type", "object");
                    list.appendTag(objectTag);
                } else {
                    NBTTagCompound stringTag = new NBTTagCompound();
                    stringTag.setTag("value", new NBTTagString((String) parameter));
                    stringTag.setString("type", "string");
                    list.appendTag(stringTag);
                }
            }
            tag.setTag("parameters", list);
            return tag;
        }

        @Override
        public void fromNBT(NBTTagCompound tag) {
            this.parameterizedString = tag.getString("parameterizedString");
            NBTTagList list = tag.getTagList("parameters", MinecraftHelpers.NBTTag_Types.NBTTagCompound.ordinal());
            this.parameters = new Object[list.tagCount()];
            for (int i = 0; i < this.parameters.length; i++) {
                NBTTagCompound elementTag = list.getCompoundTagAt(i);
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
