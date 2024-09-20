package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.config.extendedconfig.CreativeModeTabConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.init.RegistryManager;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.proxy.IClientProxyCommon;
import org.cyclops.cyclopscore.proxy.ICommonProxyCommon;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author rubensworks
 */
public abstract class ModBaseCommon<T extends ModBaseCommon<T>> implements IModBase {

    private static final Map<String, ModBaseCommon<?>> MOD_BASES = Maps.newConcurrentMap();

    private final String modId;
    private final LoggerHelper loggerHelper;
    private final RegistryManager registryManager;
    private final ModCompatLoader modCompatLoader;

    @Nullable
    private CreativeModeTab defaultCreativeTab = null;
    private final List<Pair<ItemStack, CreativeModeTab.TabVisibility>> defaultCreativeTabEntries = Lists.newArrayList();

    public ModBaseCommon(String modId, Consumer<T> instanceSetter) {
        instanceSetter.accept((T) this);
        MOD_BASES.put(modId, this);
        this.modId = modId;
        this.loggerHelper = constructLoggerHelper();
        this.registryManager = constructRegistryManager();
        this.modCompatLoader = constructModCompatLoader();
    }

    @Override
    public String getModId() {
        return modId;
    }

    @Override
    public LoggerHelper getLoggerHelper() {
        return loggerHelper;
    }

    protected LoggerHelper constructLoggerHelper() {
        return new LoggerHelper(getModId());
    }

    protected RegistryManager constructRegistryManager() {
        return new RegistryManager();
    }

    public RegistryManager getRegistryManager() {
        return registryManager;
    }

    protected abstract IClientProxyCommon constructClientProxy();

    protected abstract ICommonProxyCommon constructCommonProxy();

    @Override
    public ModCompatLoader getModCompatLoader() {
        return modCompatLoader;
    }

    protected ModCompatLoader constructModCompatLoader() {
        return new ModCompatLoader(this);
    }

    /**
     * This is called only once to let the mod compatibilities register themselves.
     * @param modCompatLoader The loader.
     */
    protected void loadModCompats(ModCompatLoader modCompatLoader) {

    }

    protected LiteralArgumentBuilder<CommandSourceStack> constructBaseCommand(Commands.CommandSelection selection, CommandBuildContext context) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(this.getModId());

        return root;
    }

    @Nullable
    @Override
    public CreativeModeTab getDefaultCreativeTab() {
        return defaultCreativeTab;
    }

    @Override
    public List<Pair<ItemStack, CreativeModeTab.TabVisibility>> getDefaultCreativeTabEntries() {
        return defaultCreativeTabEntries;
    }

    @Override
    public void registerDefaultCreativeTabEntry(ItemStack itemStack, CreativeModeTab.TabVisibility visibility) {
        if (defaultCreativeTabEntries == null) {
            throw new IllegalStateException("Tried to register default tab entries after the CreativeModeTabEvent.BuildContents event");
        }
        if (itemStack.getCount() != 1) {
            throw new IllegalStateException("Tried to register default tab entries with a non-1-count ItemStack");
        }
        defaultCreativeTabEntries.add(Pair.of(itemStack, visibility));
    }

    protected CreativeModeTabConfigCommon<ModBaseCommon<T>> constructDefaultCreativeModeTabConfig() {
        return new CreativeModeTabConfigCommon<>(this, "default", (config) -> this.defaultCreativeTab = this.constructDefaultCreativeModeTab(CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)).build());
    }

    protected CreativeModeTab.Builder constructDefaultCreativeModeTab(CreativeModeTab.Builder builder) {
        return builder
                .title(Component.translatable("itemGroup." + getModId()))
                .icon(() -> new ItemStack(Items.BARRIER))
                .displayItems((parameters, output) -> {
                    for (Pair<ItemStack, CreativeModeTab.TabVisibility> entry : defaultCreativeTabEntries) {
                        output.accept(entry.getLeft(), entry.getRight());
                    }
                });
    }

    /**
     * @return If a default creative tab should be constructed.
     *         If so, make sure to override {@link #constructDefaultCreativeModeTab(CreativeModeTab.Builder)}.
     */
    protected boolean hasDefaultCreativeModeTab() {
        return true;
    };

    /**
     * Called when the configs should be registered.
     * @param configHandler The config handler to register to.
     */
    protected void onConfigsRegister(ConfigHandlerCommon configHandler) {
        // Register default creative tab
        if (this.hasDefaultCreativeModeTab()) {
            this.getConfigHandler().addConfigurable(this.constructDefaultCreativeModeTabConfig());
        }
    }

    @Override
    public String toString() {
        return getModId();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return object == this;
    }

    /**
     * Get the mod by id.
     * @param modId The mod id.
     * @return The mod instance or null.
     */
    @Nullable
    public static ModBaseCommon getCommon(String modId) {
        return MOD_BASES.get(modId);
    }

    public static Map<String, ModBaseCommon<?>> getCommonMods() {
        return MOD_BASES;
    }

}
