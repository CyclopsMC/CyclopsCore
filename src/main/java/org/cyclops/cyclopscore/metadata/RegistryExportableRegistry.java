package org.cyclops.cyclopscore.metadata;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link IRegistryExportableRegistry}.
 */
public class RegistryExportableRegistry implements IRegistryExportableRegistry {

    public static final RegistryExportableRegistry INSTANCE = new RegistryExportableRegistry();

    private final List<IRegistryExportable> exportables = Collections.synchronizedList(Lists.newArrayList());

    private RegistryExportableRegistry() {

    }

    public static RegistryExportableRegistry getInstance() {
        return INSTANCE;
    }

    @Override
    public void register(IRegistryExportable exportable) {
        this.exportables.add(exportable);
    }

    @Override
    public List<IRegistryExportable> getExportables() {
        return exportables;
    }

    @Override
    public void export(Path basePath) throws IOException {
        basePath.toFile().mkdirs();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        for (IRegistryExportable exportable : exportables) {
            String fileName = exportable.getName() + ".json";
            JsonObject data = exportable.export();
            FileUtils.writeStringToFile(basePath.resolve(fileName).toFile(), gson.toJson(data), Charsets.UTF_8);
        }
    }

}
