package top.parak.pandora.server.frame;

import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import top.parak.pandora.server.frame.strategy.impl.DefaultBinaryFrameStrategy;
import top.parak.pandora.server.frame.strategy.impl.DefaultPongFrameStrategy;
import top.parak.pandora.server.frame.strategy.impl.DefaultTextFrameStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for {@link FrameStrategy}.
 *
 * @author Khighness
 * @since 2023-02-24
 */
public class FrameStrategyFactory {

    private static final Map<Class<? extends WebSocketFrame>, FrameStrategy> FRAME_STRATEGY_MAP = new HashMap<>();

    static {
        FrameStrategyFactory.register(PongWebSocketFrame.class, new DefaultPongFrameStrategy());
        FrameStrategyFactory.register(TextWebSocketFrame.class, new DefaultTextFrameStrategy());
        FrameStrategyFactory.register(BinaryWebSocketFrame.class, new DefaultBinaryFrameStrategy());
    }

    /**
     * Register strategy into factory.
     *
     * @param frameClass    the class of websocket frame
     * @param frameStrategy strategy to handle websocket frame
     */
    public static void register(Class<? extends WebSocketFrame> frameClass, FrameStrategy frameStrategy) {
        FRAME_STRATEGY_MAP.put(frameClass, frameStrategy);
    }

    /**
     * Get strategy from factory corresponding to the frame class.
     *
     * @param frameClass the class of websocket frame
     * @return strategy to handle websocket frame
     */
    public static FrameStrategy builder(Class<? extends WebSocketFrame> frameClass) {
        return FRAME_STRATEGY_MAP.get(frameClass);
    }

}
