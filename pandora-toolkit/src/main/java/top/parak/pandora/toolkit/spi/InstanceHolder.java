package top.parak.pandora.toolkit.spi;

/**
 * The holder which guarantees the multithreaded visibility of the instance hold.
 *
 * @author Khighness
 * @since 2023-03-06
 */
public class InstanceHolder<T> {

    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

}
