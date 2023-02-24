package top.parak.pandora.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * Netty handler for {@link top.parak.pandora.server.core.NettyWebSocketServer}.
 *
 * @author Khighness
 * @since 2023-02-23
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(HeartbeatHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            SocketAddress clientAddr = ctx.channel().remoteAddress();
            if (event.state() == IdleState.READER_IDLE) {
                LOG.info("[userEventTriggered] {} -> read idle", clientAddr);
            } else if (event.state() == IdleState.WRITER_IDLE) {
                LOG.info("[userEventTriggered] {} -> write idle", clientAddr);
            } else if (event.state() == IdleState.ALL_IDLE) {
                LOG.info("[userEventTriggered] {} -> all idle", clientAddr);
                ctx.channel().close();
                LOG.info("|==================== [断开连接: {}] ====================|", clientAddr);
            }
        }
    }

}
