package top.parak.pandora.server.frame.strategy.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.parak.pandora.server.frame.strategy.PongFrameStrategy;

/**
 * @author Khighness
 * @since 2023-02-24
 */
public class DefaultPongFrameStrategy implements PongFrameStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultPongFrameStrategy.class);

    @Override
    public void handleMessage(Channel channel, WebSocketFrame frame) {
        PongWebSocketFrame pongFrame = (PongWebSocketFrame) frame;
        ByteBuf content = pongFrame.content();
        final int length = content.readableBytes();
        final byte[] buf = new byte[length];
        content.getBytes(content.readerIndex(), buf, 0, length);
        LOG.info("[handlePongMessage] channel id: {}, content: {}", channel.id(), content);
    }

}
