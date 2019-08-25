package org.cyclops.cyclopscore.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

/**
 * A component that can be used for {@link IInformationProvider} implementations.
 * @author rubensworks
 *
 */
public class InformationProviderComponent {
    
    private boolean hasInfo = false;
    private Block block = null;
    
    /**
     * Make a new instance.
     * @param block The blockState for which the component is used.
     */
    public InformationProviderComponent(Block block) {
        setBlock(block);
    }
    
    /**
     * Add information to the given list for the given item.
     * @param itemStack The {@link ItemStack} to add info for.
     * @param world The player that will see the info.
     * @param list The info list where the info will be added.
     * @param flag No idea...
     */
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        if(hasInfo) {
            if(((IInformationProvider) block).getInfo(itemStack) != null) {
                list.add(new StringTextComponent(IInformationProvider.BLOCK_PREFIX
                        + ((IInformationProvider) block).getInfo(itemStack)));
            }
            ((IInformationProvider) block).provideInformation(itemStack, world, list, flag);
        }
    }

    /**
     * If the blockState that uses this component implements {@link IInformationProvider}.
     * @return If the blockState provides info.
     */
    public boolean isHasInfo() {
        return hasInfo;
    }

    /**
     * Get the blockState that uses this component.
     * @return The blockState.
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Set the blockState that uses this component.
     * @param block The blockState.
     */
    public void setBlock(Block block) {
        this.block = block;
        if(block instanceof IInformationProvider)
            this.hasInfo = true;
    }
    
}
