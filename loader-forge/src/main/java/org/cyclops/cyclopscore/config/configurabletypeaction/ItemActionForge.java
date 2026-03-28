package org.cyclops.cyclopscore.config.configurabletypeaction;

import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * @author rubensworks
 */
public class ItemActionForge<M extends ModBaseForge<M>> extends ItemAction<M> {

    @Override
    protected void polish(ItemConfigCommon<M> config) {
        super.polish(config);

        if(config.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            IDynamicModelElementCommon dynamicModelElement = config.getItemClientConfig().getDynamicModelElement();
            if (dynamicModelElement != null) {
                ItemAction.handleItemModel(config);
            }
        }
    }


}
