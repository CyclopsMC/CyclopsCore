package org.cyclops.cyclopscore.metadata;

import org.cyclops.cyclopscore.init.IRegistry;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Registry for {@link IRegistryExportable}.
 */
public interface IRegistryExportableRegistry extends IRegistry {

    public void register(IRegistryExportable exportable);

    public List<IRegistryExportable> getExportables();

    public void export(Path basePath) throws IOException;

}
