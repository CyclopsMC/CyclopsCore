package baubles.api.cap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class BaublesContainerProvider implements INBTSerializable<CompoundNBT>, ICapabilityProvider {

	private final BaublesContainer container;
	
	public BaublesContainerProvider(BaublesContainer container) {        
        this.container = container;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> LazyOptional<T> getCapability (Capability<T> capability, Direction facing) {
        if (capability == BaublesCapabilities.CAPABILITY_BAUBLES) return LazyOptional.of(() -> this.container).cast();
        return null;
    }
    
    @Override
    public CompoundNBT serializeNBT () {
        return this.container.serializeNBT();
    }
    
    @Override
    public void deserializeNBT (CompoundNBT nbt) {
        this.container.deserializeNBT(nbt);
    }

}
