package top.parak.pandora.server.frame.strategy;

import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import top.parak.pandora.server.frame.FrameStrategy;

/**
 * Strategy to handle {@link PongWebSocketFrame}.
 *
 * @author cantai
 * @since 2023-02-24
 */
public interface PongFrameStrategy extends FrameStrategy {

}
