package baubles.api.cap;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class BaublesCapabilities {
	
	/**
     * Access to the baubles capability. 
     */
    @CapabilityInject(IBaublesItemHandler.class)
    public static final Capability<IBaublesItemHandler> CAPABILITY_BAUBLES = null;
    
    public static class CapabilityBaubles<T extends IBaublesItemHandler> implements IStorage<IBaublesItemHandler> {
        
        @Override
        public INBT writeNBT (Capability<IBaublesItemHandler> capability, IBaublesItemHandler instance, Direction side) {
            
            return null;
        }
        
        @Override
        public void readNBT (Capability<IBaublesItemHandler> capability, IBaublesItemHandler instance, Direction side, INBT nbt) {
        
        }
    }
    
}
