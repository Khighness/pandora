package top.parak.pandora.client.exception;

import top.parak.pandora.client.core.WebSocketClient;

/**
 * Thrown when {@link WebSocketClient} occurs exception.
 *
 * @author cantai
 * @since 2023-02-22
 */
public class WebSocketClientException extends RuntimeException {

    public WebSocketClientException(String message) {
        super(message);
    }

    public WebSocketClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
