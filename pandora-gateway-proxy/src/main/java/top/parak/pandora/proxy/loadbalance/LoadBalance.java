package top.parak.pandora.proxy.loadbalance;

import top.parak.pandora.toolkit.request.BaseRequest;
import top.parak.pandora.toolkit.spi.SPI;

import java.util.List;

/**
 * Load balance.
 *
 * @author Khighness
 * @since 2023-03-04
 */
@SPI
public interface LoadBalance<T> {

    /**
     * Select one of the resources.
     *
     * @param resources the resource list
     * @param request   the resource request
     * @return the resource selected by load balance algorithm
     */
    T select(List<T> resources, BaseRequest request);

}
