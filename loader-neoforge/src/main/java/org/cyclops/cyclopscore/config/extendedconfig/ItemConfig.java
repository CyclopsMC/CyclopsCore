package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Config for items.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public class ItemConfig extends ItemConfigCommon<ModBase<?>> {

    @Deprecated // TODO: Use BlockClientConfig instead; remove in next major
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation dynamicItemVariantLocation;

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public ItemConfig(ModBase<?> mod, String namedId, Function<ItemConfigCommon<ModBase<?>>, ? extends Item> elementConstructor) {
        super(mod, namedId, elementConstructor);
        if(mod.getModHelpers().getMinecraftHelpers().isClientSide()) {
            dynamicItemVariantLocation  = null;
        }
    }

    /**
     * Register default item models for this item.
     * This should only be used when registering dynamic models.
     * @return The item resource location.
     */
    @Deprecated // TODO: Use BlockClientConfig instead; remove in next major
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation registerDynamicModel() {
        ResourceLocation itemName = ResourceLocation.fromNamespaceAndPath(getMod().getModId(), getNamedId());
        return new ModelResourceLocation(itemName, "inventory");
    }

    /**
     * @return The color handler for the item instance.
     */
    @Deprecated // TODO: Use BlockClientConfig instead; remove in next major
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public ItemColor getItemColorHandler() {
        return null;
    }

}
