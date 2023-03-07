package top.parak.pandora.spi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI extension context.
 *
 * @author Khighness
 * @since 2023-03-06
 */
public class ExtensionContext {

    static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
    static final Map<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();


    /**
     * Get the extension loader for the given interface.
     *
     * @param type the given interface that must annotated by @{@link SPI}.
     * @return the extension loader
     * @param <X>  custom type
     */
    public static <X> ExtensionLoader<X> getExtensionLoader(Class<X> type) {
        if (type == null) {
            throw new IllegalArgumentException("type should not be null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("type must be an interface");
        }
        if (type.getAnnotation(SPI.class) == null) {
            throw new IllegalArgumentException("type must be annotated by SPI");
        }
        ExtensionLoader<X> extensionLoader = (ExtensionLoader<X>) EXTENSION_LOADERS.get(type);
        if (extensionLoader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<X>(type));
            extensionLoader = (ExtensionLoader<X>) EXTENSION_LOADERS.get(type);
        }
        return extensionLoader;
    }

}
