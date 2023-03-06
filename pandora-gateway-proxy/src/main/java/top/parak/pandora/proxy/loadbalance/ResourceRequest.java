package top.parak.pandora.proxy.loadbalance;

/**
 * The request to get resource by load balance.
 *
 * @author cantai
 * @since 2023-03-06
 */
public class ResourceRequest {

    private String resourceName;

    private String requestSource;

    public ResourceRequest() {
    }

    public ResourceRequest(String resourceName, String requestSource) {
        this.resourceName = resourceName;
        this.requestSource = requestSource;
    }

    public String getSelectKey() {
        return resourceName + requestSource + System.currentTimeMillis();
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }

}
