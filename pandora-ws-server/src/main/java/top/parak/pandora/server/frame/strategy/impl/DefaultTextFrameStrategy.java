package top.parak.pandora.server.frame.strategy.impl;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.parak.pandora.server.frame.strategy.TextFrameStrategy;

/**
 * @author Khighness
 * @since 2023-02-24
 */
public class DefaultTextFrameStrategy implements TextFrameStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTextFrameStrategy.class);

    @Override
    public void handleMessage(Channel channel, WebSocketFrame frame) {
        TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
        LOG.info("[handleTextMessage] channel id: {}, text: {}", channel.id(), textFrame.text());

        // TODO(Khighness): process text
    }

}
