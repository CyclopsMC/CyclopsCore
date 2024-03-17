package org.cyclops.cyclopscore.modcompat;

import com.google.common.collect.Maps;
import net.neoforged.fml.InterModComms;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.Map;

/**
 * Handler for {@link InterModComms}s.
 * @author rubensworks
 */
public class IMCHandler {

    protected final ModBase mod;
    private Map<String, IIMCAction> actions = Maps.newHashMap();

    public IMCHandler(ModBase mod) {
        this.mod = mod;
    }

    /**
     * Register a new action for messages.
     * @param key The action key which will be used to distinguish messages.
     * @param action The action to execute when messages for that type are received.
     */
    public void registerAction(String key, IIMCAction action) {
        actions.put(key, action);
    }

    public void handle(InterModComms.IMCMessage message) {
        mod.log(Level.INFO, String.format("Handling IMC message from %s.", message.senderModId()));
        IIMCAction action = actions.get(message.method());
        if(action != null) {
            if(!action.handle(message)) {
                mod.log(Level.ERROR, String.format("The IMC message for key %s was rejected. " +
                        "It may have been incorrectly formatted or has resulted in an error.", message.method()));
            }
        } else {
            mod.log(Level.ERROR, String.format("An IMC message with invalid key %s was received.", message.method()));
        }
    }

    public static interface IIMCAction {

        /**
         * Handle the given message, corresponds to the given key used with registration.
         * @param message The message.
         * @return If the handling occured without any problems.
         */
        public boolean handle(InterModComms.IMCMessage message);

    }

}
