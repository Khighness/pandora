package top.parak.pandora.server.exception;

import top.parak.pandora.server.core.WebSocketServer;

/**
 * Thrown when {@link WebSocketServer} occurs exception.
 *
 * @author cantai
 * @since 2023-02-24
 */
public class WebSocketServerException extends RuntimeException {

    public WebSocketServerException(String message) {
        super(message);
    }

    public WebSocketServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
