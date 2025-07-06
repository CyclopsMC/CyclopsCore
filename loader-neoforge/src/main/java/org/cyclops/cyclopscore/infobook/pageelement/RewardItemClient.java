package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.client.gui.GuiGraphics;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

/**
 * @author rubensworks
 */
public class RewardItemClient implements IRewardClient {

    private final RewardItem rewardItem;

    public RewardItemClient(RewardItem rewardItem) {
        this.rewardItem = rewardItem;
    }

    @Override
    public AdvancedButton createButton(IInfoBook infoBook) {
        return new RecipeAppendixClient.ItemButton(infoBook);
    }

    @Override
    public void drawElementInner(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my, AdvancedButton button) {
        RecipeAppendixClient.renderItemForButton(gui, guiGraphics, x, y, this.rewardItem.getItemStack(), mx, my, true, (RecipeAppendixClient.ItemButton) button);
    }

}
