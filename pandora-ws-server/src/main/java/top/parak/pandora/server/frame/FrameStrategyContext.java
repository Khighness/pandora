package top.parak.pandora.server.frame;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * Frame context to handle {@link WebSocketFrame}
 *
 * @author Khighness
 * @since 2023-02-24
 */
public class FrameStrategyContext {

    /**
     * Handle websocket frame.
     *
     * @param channel I/O channel
     * @param frame   websocket frame
     */
    public static void handleWebsocketFrame(Channel channel, WebSocketFrame frame) {
        FrameStrategyFactory.builder(frame.getClass())
                .handleMessage(channel, frame);
    }

}
