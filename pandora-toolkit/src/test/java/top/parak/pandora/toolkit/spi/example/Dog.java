package top.parak.pandora.toolkit.spi.example;

/**
 * SPI extension for {@link Animal}.
 *
 * @author Khighness
 * @since 2023-03-06
 */
public class Dog implements Animal {

    @Override
    public String type() {
        return "狗";
    }

    @Override
    public void say() {
        System.out.println("汪");
    }

}
