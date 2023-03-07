package top.parak.pandora.toolkit.spi.example;

import top.parak.pandora.spi.SPI;

/**
 * SPI example interface.
 *
 * @author Khighness
 * @since 2023-03-06
 */
@SPI
public interface Animal {

    String type();

    void say();

}
