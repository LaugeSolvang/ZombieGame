package managers;

import common.services.IEntityProcessingService;
import common.services.IGamePluginService;
import common.services.IPostEntityProcessingService;
import common.services.KeyPressListener;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class ServiceLoaderUtils {
    public static Collection<? extends IGamePluginService> getPluginServices() {
        return java.util.ServiceLoader.load(IGamePluginService.class).stream().map(java.util.ServiceLoader.Provider::get).collect(toList());
    }

    public static Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return java.util.ServiceLoader.load(IEntityProcessingService.class).stream().map(java.util.ServiceLoader.Provider::get).collect(toList());
    }

    public static Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return java.util.ServiceLoader.load(IPostEntityProcessingService.class).stream().map(java.util.ServiceLoader.Provider::get).collect(toList());
    }

    public static Collection<? extends KeyPressListener> getKeyPressListeners() {
        return java.util.ServiceLoader.load(KeyPressListener.class).stream().map(java.util.ServiceLoader.Provider::get).collect(toList());
    }
}
