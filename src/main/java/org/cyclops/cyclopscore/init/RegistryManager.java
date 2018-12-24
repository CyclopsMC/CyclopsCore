package org.cyclops.cyclopscore.init;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Manager for all the registries in this mod.
 * @author rubensworks
 *
 */
public class RegistryManager {

	private RegistryManager _instance = null;
	
	private Map<Class<? extends IRegistry>, IRegistry> registries;

    /**
     * Create a new registry manager.
     * Only one should exist per mod.
     */
	public RegistryManager() {
		registries = Maps.newIdentityHashMap();
	}
	
	public <R extends IRegistry> void addRegistry(Class<R> clazz, R registry) {
		registries.put(clazz, registry);
	}
	
	/**
	 * Get the unique registry of the given class.
	 * @param clazz The class of the registry.
     * @param <T> The type of registry.
	 * @return The unique registry.
	 */
	@SuppressWarnings("unchecked")
	public <T extends IRegistry> T getRegistry(Class<T> clazz) {
		return (T) registries.get(clazz);
	}
	
}
