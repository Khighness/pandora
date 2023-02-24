package top.parak.pandora.server.frame.strategy.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.parak.pandora.server.frame.strategy.BinaryFrameStrategy;

/**
 * @author Khighness
 * @since 2023-02-24
 */
public class DefaultBinaryFrameStrategy implements BinaryFrameStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultBinaryFrameStrategy.class);

    @Override
    public void handleMessage(Channel channel, WebSocketFrame frame) {
        BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) frame;
        ByteBuf content = binaryFrame.content();
        final int length = content.readableBytes();
        final byte[] buf = new byte[length];
        content.getBytes(content.readerIndex(), buf, 0, length);
        LOG.info("[handleBinaryMessage] channel id: {}, byte length: {}", channel.id(), length);

        // TODO(Khighness): process buf
    }

}
