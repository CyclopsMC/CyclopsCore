package org.cyclops.cyclopscore.init;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.Set;

/**
 * Helps with code debugging.
 * @author rubensworks
 *
 */
public class Debug {
    
    private static final String CONFIGCHECKER_PREFIX = "[CONFIGCHECKER] ";
    private final Set<ExtendedConfig<?, ?>> savedConfigs = Sets.newHashSet();
    private final ModBase mod;
    
    private boolean ok = true;

    public Debug(ModBase mod) {
        this.mod = mod;
    }

    /**
     * Loops over the list of configs and checks their correctness.
     * @param configs List of configs
     */
    public void checkPreConfigurables(Set<ExtendedConfig<?, ?>> configs) {
        for(ExtendedConfig<?, ?> config : configs) {
            // _instance field on ExtendedConfig
            try {
                config.getClass().getField("_instance");
            } catch (NoSuchFieldException e) {
                log(config+" has no static '_instance' field.");
            } catch (SecurityException e) {
                log(config+" has a non-public static '_instance' field, make it public.");
            }
        }
        
        // Save for Post call
        savedConfigs.addAll(configs);
    }
    
    /**
     * Loops over the list of configs (was saved from the Pre call) and checks their correctness.
     */
    public void checkPostConfigurables() {
        for(ExtendedConfig<?, ?> config : savedConfigs) {
            if(config.getHolderType().hasUniqueInstance() && config.isEnabled()) {
                // The sub-instance of ExtendedConfig (can be in a higher class hierarchy, bit of a hack...
                if(config.getSubInstance() == null) {
                    log(config.getElement()+" has no sub-instance, even though it is enabled.");
                }
                
                // getInstance() in the sub-instance of ExtendedConfig
                try {
                    config.getElement().getMethod("getInstance");
                } catch (NoSuchMethodException e) {
                    log(config.getElement()+" has no static 'getInstance()' method.");
                } catch (SecurityException e) {
                    log(config.getElement()+" has a non-public static 'getInstance()' method, make it public.");
                }
            }
        }
        
        if(ok) {
            log("Everything is just fine!");
        }
    }
    
    private void log(String message) {
        ok = false;
        mod.log(Level.INFO, CONFIGCHECKER_PREFIX + message);
    }

}
