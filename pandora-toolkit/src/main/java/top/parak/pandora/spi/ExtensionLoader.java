package top.parak.pandora.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.parak.pandora.exception.ExtensionLoadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI extension loader.
 *
 * @author Khighness
 * @since 2023-03-06
 */
public final class ExtensionLoader<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ExtensionLoader.class);
    private static final String EXTENSION_DIRECTORY = "META-INF/pandora/";

    private final Class<?> type;
    private final Map<String, InstanceHolder<Object>> cachedInstances = new ConcurrentHashMap<>();
    private final InstanceHolder<Map<String, Class<?>>> cachedClasses = new InstanceHolder<>();

    ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    /**
     * Get the extension instance by the implementation name.
     *
     * @param name the implementation name
     * @return the extension instance
     */
    public T getExtension(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name should not be null or empty");
        }

        InstanceHolder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new InstanceHolder<>());
            holder = cachedInstances.get(name);
        }

        // Create singleton instance by double-check lock.
        Object instance = holder.get();
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = createExtension(name);
                    holder.set(instance);
                }
            }
        }

        return (T) instance;
    }

    /**
     * It is called in {@code synchronized} code block.
     * So, do not need to worry thread-safe problem.
     *
     * @param name the extension instance
     * @return the extension instance
     */
    private T createExtension(String name) {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new ExtensionLoadException("No such extension of name: " + name);
        }

        T instance = (T) ExtensionContext.EXTENSION_INSTANCES.get(clazz);
        if (instance == null) {
            try {
                instance = (T) clazz.newInstance();
                ExtensionContext.EXTENSION_INSTANCES.put(clazz, instance);
            } catch (Exception e) {
                LOG.error("[createExtension] failed to create instance", e);
            }
        }

        return instance;
    }

    /**
     * Get the extension classes with double-check lock in order to
     * resolve the race problem for {@link #cachedClasses}.
     *
     * @return the map of the extension name and the corresponding class.
     */
    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClasses.get();

        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    classes = new HashMap<>();
                    loadDirectory(classes);
                    cachedClasses.set(classes);
                }
            }
        }

        return classes;
    }

    /**
     * Load the extension classes from {@link #EXTENSION_DIRECTORY}.
     *
     * @param extensionClasses the container for extension classes
     */
    private void loadDirectory(Map<String, Class<?>> extensionClasses) {
        String fileName = ExtensionLoader.EXTENSION_DIRECTORY + type.getTypeName();

        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL resourceUrl = urls.nextElement();
                    loadResource(extensionClasses, classLoader, resourceUrl);
                }
            }
        } catch (IOException e) {
            throw new ExtensionLoadException("Failed to load file: " + fileName, e);
        }
    }

    /**
     * Load the extension classes from the specific url.
     *
     * @param extensionClasses the container for extension classes
     * @param classLoader      the class loader
     * @param resourceUrl      the resource url
     */
    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceUrl) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceUrl.openStream()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                final int noteIdx = line.indexOf('#');
                if (noteIdx > 0) {
                    line = line.substring(0, noteIdx);
                }
                line = line.trim();

                if (line.length() > 0) {
                    String clazzName = null;
                    try {
                        final int equalIdx = line.indexOf('=');
                        String extensionName = line.substring(0, equalIdx).trim();
                        clazzName = line.substring(equalIdx + 1).trim();

                        if (!extensionName.isEmpty() && !clazzName.isEmpty()) {
                            Class<?> clazz = classLoader.loadClass(clazzName);
                            extensionClasses.put(extensionName, clazz);
                            LOG.info("[loadResource] SPI load, name: {}, class: {}", extensionName, clazzName);
                        }

                    } catch (ClassNotFoundException e) {
                        throw new ExtensionLoadException("Class [" +  clazzName + "] is not found", e);
                    }
                }
            }
        } catch (IOException e) {
            throw new ExtensionLoadException("Failed to open resource: " + resourceUrl, e);
        }
    }

}
