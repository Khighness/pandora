package top.parak.pandora.toolkit.spi.example;

/**
 * SPI extension for {@link Animal}.
 *
 * @author Khighness
 * @since 2023-03-06
 */
public class Bird implements Animal {

    @Override
    public String type() {
        return "鸟";
    }

    @Override
    public void say() {
        System.out.println("唧");
    }

}
