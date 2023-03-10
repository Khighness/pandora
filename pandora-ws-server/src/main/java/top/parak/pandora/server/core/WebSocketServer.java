package top.parak.pandora.server.core;

import top.parak.pandora.server.exception.WebSocketServerException;
import top.parak.pandora.server.model.Session;

/**
 * The server for websocket protocol.
 *
 * @author Khighness
 * @since 2023-02-23
 */
public interface WebSocketServer {

    /**
     * Start the websocket server.
     */
    void start() throws WebSocketServerException;

    /**
     * Send message to the client.
     *
     * @param session the client session
     * @param message the text message
     */
    void send(Session session, String message) throws WebSocketServerException;

}
