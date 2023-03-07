package top.parak.pandora.toolkit.spi;

import org.junit.Test;
import top.parak.pandora.toolkit.spi.example.Animal;

public class ExtensionContextTest {

    @Test
    public void getExtensionLoader() {
        ExtensionLoader<Animal> extensionLoader = ExtensionContext.getExtensionLoader(Animal.class);
        Animal dog = extensionLoader.getExtension("小狗");
        dog.say();
    }

}