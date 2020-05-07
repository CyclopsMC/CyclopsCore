package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.List;

/**
 * The action used for {@link ItemConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class ItemAction extends ConfigurableTypeActionForge<ItemConfig, Item>{

    private static final List<ExtendedConfig<?, ?>> MODEL_ENTRIES = Lists.newArrayList();

    @Override
    public void onRegisterForge(ItemConfig eConfig) {
        // Register item and set creative tab.
        register(eConfig, () -> {
            this.polish(eConfig);
            eConfig.onForgeRegistered();
            return null;
        });

        if(MinecraftHelpers.isClientSide()) {
            handleItemModel(eConfig);
        }
    }

    public static void handleItemModel(ExtendedConfig<?, ?> extendedConfig) {
        if(MinecraftHelpers.isClientSide()) {
            MODEL_ENTRIES.add(extendedConfig);
        }
    }

    protected void polish(ItemConfig config) {
        if (MinecraftHelpers.isClientSide()) {
            IItemColor itemColorHandler = config.getItemColorHandler();
            if (itemColorHandler != null) {
                Minecraft.getInstance().getItemColors().register(itemColorHandler, config.getInstance());
            }
        }
    }
}
