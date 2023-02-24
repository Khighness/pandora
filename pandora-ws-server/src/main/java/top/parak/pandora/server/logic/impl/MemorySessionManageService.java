package top.parak.pandora.server.logic.impl;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import top.parak.pandora.server.logic.SessionManageService;
import top.parak.pandora.server.model.Session;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Khighness
 * @since 2023-02-24
 */
public class MemorySessionManageService implements SessionManageService {

    private final Map<String, Channel> idSession = new ConcurrentHashMap<>();
    private final Map<String, Session> addrSession = new ConcurrentHashMap<>();

    @Override
    public boolean saveSession(Session session, Channel channel) {
        idSession.put(session.getId(), channel);
        addrSession.put(session.getCliAddr(), session);
        return true;
    }

    @Override
    public Session getByAddr(SocketAddress cliAddr) {
        return addrSession.get(cliAddr.toString());
    }

    @Override
    public Session getByAddr(String cliAddr) {
        return addrSession.get(cliAddr);
    }

    @Override
    public Session removeSession(SocketAddress cliAddr) {
        Session session = addrSession.remove(cliAddr.toString());
        if (session != null) {
            idSession.remove(session.getId());
        }
        return session;
    }

    @Override
    public Session removeSession(String cliAddr) {
        Session session = addrSession.remove(cliAddr);
        if (session != null) {
            idSession.remove(session.getId());
        }
        return session;
    }

    @Override
    public boolean publishMessage(String id, String message) {
        Channel channel = idSession.get(id);
        if (channel != null && channel.isOpen()) {
            channel.writeAndFlush(new TextWebSocketFrame(message));
            return true;
        }
        return false;
    }

}
