package top.parak.pandora.toolkit.exception;

import top.parak.pandora.toolkit.spi.ExtensionLoader;

/**
 * Thrown when {@link ExtensionLoader} occurs exception.
 *
 * @author Khighness
 * @since 2023-03-06
 */
public class ExtensionLoadException extends RuntimeException {

    public ExtensionLoadException(String message) {
        super(message);
    }

    public ExtensionLoadException(String message, Throwable cause) {
        super(message, cause);
    }

}
