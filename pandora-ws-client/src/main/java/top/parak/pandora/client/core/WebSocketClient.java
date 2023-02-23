package top.parak.pandora.client.core;

/**
 * The client for websocket protocol.
 *
 * <p>The recommended practice is such as:</p>
 *
 * <pre>
 * {@code
 * WebSocketClient cli = WebSocketClientFactory.create(uri);
 * try {
 *     cli.open();
 *     cli.send(msg)
 *     // ...
 * } catch(Exception e) {
 *     // You can try to reconnect or print e.
 * } finally {
 *     cli.close();
 * }
 * }
 * </pre>
 *
 * @author Khighness
 * @since 2023-02-22
 */
public interface WebSocketClient {

    /**
     * Try to connect to the websocket server.
     * <p>It can be called more than once.</p>
     */
    void open() throws Exception;

    /**
     * Close the connection to the websocket server.
     * <p>It must be called after {@link #open()}.</p>
     */
    void close() throws Exception;

    /**
     * Send a text message to the websocket server.
     * <p>It can only be called after {@link #open()}.</p>
     *
     * @param msg the text message
     * @throws Exception if I/O exception occurs
     */
    void send(String msg) throws Exception;

}
