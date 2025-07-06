package org.cyclops.cyclopscore.config.configurabletypeaction;

import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

public class BlockActionNeoForge extends BlockAction<ModBaseNeoForge<?>> {
    @Override
    protected void polish(BlockConfigCommon<ModBaseNeoForge<?>> config) {
        super.polish(config);

        if(config.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            // Handle dynamic models
            IDynamicModelElementCommon dynamicModelElement = config.getBlockClientConfig().getDynamicModelElement();
            if (dynamicModelElement != null) {
                BlockAction.handleDynamicBlockModel(config);
            }
        }
    }
}
