package top.parak.pandora.client.factory;

import top.parak.pandora.client.core.NettyWebSocketClient;
import top.parak.pandora.client.core.WebSocketClient;
import top.parak.pandora.client.exception.WebSocketClientException;

import java.net.URI;

/**
 * The factory of {@link WebSocketClient}.
 *
 * @author KHighness
 * @since 2023-02-22
 */
public class WebSocketClientFactory {

    /**
     * The name for websocket protocol.
     */
    public static final String WS_PROTOCOL = "ws";

    /**
     * The name for websocket protocol with ssl/tls.
     * <p>Not support now.</p>
     */
    public static final String WSS_PROTOCOL = "wss";

    /**
     * Create a {@link WebSocketClient} instance.
     *
     * @param uri the uri to connect the websocket server.
     * @return a initial websocket client
     */
    public static WebSocketClient create(String uri) {
        URI ws = URI.create(uri);
        if (!WS_PROTOCOL.equals(ws.getScheme())) {
            throw new WebSocketClientException("Unsupported protocol: " + ws.getScheme());
        }
        return new NettyWebSocketClient(ws);
    }

}
