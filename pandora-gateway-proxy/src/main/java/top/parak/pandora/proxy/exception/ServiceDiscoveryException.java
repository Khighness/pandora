package top.parak.pandora.proxy.exception;

/**
 * Thrown when the service is cannot be found.
 *
 * @author cantai
 * @since 2023-03-06
 */
public class ServiceDiscoveryException extends RuntimeException {

    public ServiceDiscoveryException(String serviceName) {
        super(serviceName + " is not found");
    }

}
