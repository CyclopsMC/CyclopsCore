package org.cyclops.cyclopscore.config.configurabletypeaction;

import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * @author rubensworks
 */
public class BlockActionForge<M extends ModBaseForge<M>> extends BlockAction<M> {

    @Override
    protected void polish(BlockConfigCommon<M> config) {
        super.polish(config);

        if(config.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            IDynamicModelElementCommon dynamicModelElement = config.getBlockClientConfig().getDynamicModelElement();
            if (dynamicModelElement != null) {
                BlockAction.handleDynamicBlockModel(config);
            }
        }
    }


}
