package top.parak.pandora.server.logic;

import top.parak.pandora.server.model.Session;

import java.net.SocketAddress;

/**
 * First handshake service.
 *
 * @author cantai
 * @since 2023-02-24
 */
public interface FirstHandshakeService {

    /**
     * Extract session from uri.
     *
     * @param uri websocket uri
     * @param addr client address
     * @return websocket session
     */
    Session extractSession(String uri, SocketAddress addr);

}
