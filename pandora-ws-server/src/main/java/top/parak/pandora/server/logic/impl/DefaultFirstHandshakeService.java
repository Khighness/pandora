package top.parak.pandora.server.logic.impl;

import top.parak.pandora.server.logic.FirstHandshakeService;
import top.parak.pandora.server.model.Session;

import java.net.SocketAddress;

/**
 * @author cantai
 * @since 2023-02-24
 */
public class DefaultFirstHandshakeService implements FirstHandshakeService {

    @Override
    public Session extractSession(String uri, SocketAddress addr) {
        int idx = uri.lastIndexOf("/");
        if (idx == -1) {
            throw new IllegalStateException("Invalid uri: " + uri);
        }
        String id = uri.substring(idx + 1);
        String cliAddr = addr.toString();
        return new Session(id, cliAddr);
    }

}
