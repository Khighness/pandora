package top.parak.pandora.toolkit.request;

import java.io.Serializable;

/**
 * A basic request.
 *
 * @author Khighness
 * @since 2023-03-06
 */
public class BaseRequest implements Serializable {

    private String resourceName;

    private String requestSource;

    public BaseRequest() {
    }

    public BaseRequest(String resourceName, String requestSource) {
        this.resourceName = resourceName;
        this.requestSource = requestSource;
    }

    public String getSelectKey() {
        return resourceName + requestSource + System.nanoTime();
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
