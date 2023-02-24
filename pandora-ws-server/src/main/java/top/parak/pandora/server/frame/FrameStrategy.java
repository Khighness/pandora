package top.parak.pandora.server.frame;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * Strategy to handle websocket frame.
 *
 * @author Khighness
 * @since 2023-02-24
 */
public interface FrameStrategy {

    /**
     * Handle websocket message.
     *
     * @param channel I/O channel
     * @param frame   websocket message
     */
    void handleMessage(Channel channel, WebSocketFrame frame);

}
