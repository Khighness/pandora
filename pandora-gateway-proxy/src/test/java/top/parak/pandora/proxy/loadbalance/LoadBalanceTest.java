package top.parak.pandora.proxy.loadbalance;

import org.junit.Test;
import top.parak.pandora.proxy.loadbalance.impl.ConsistentHashLoadBalance;
import top.parak.pandora.proxy.loadbalance.impl.RandomLoadBalance;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class LoadBalanceTest {

    @Test
    public void randomLoadBalance() {
        LoadBalance<String> loadBalance = new RandomLoadBalance<>();
        List<String> l1 = Arrays.asList("1", "2", "3", "4", "5");
        for (int i = 0; i < 10; i++) {
            System.out.println(loadBalance.select(l1, "s"));
        }
    }

    @Test
    public void consistentHashLoadBalance() {
        LoadBalance<String> loadBalance = new ConsistentHashLoadBalance<>();
        List<String> l1 = Arrays.asList("1", "2", "3", "4", "5");
        for (int i = 0; i < 10; i++) {
            System.out.println(loadBalance.select(l1, "s"));
        }
        List<String> l2 = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        for (int i = 0; i < 10; i++) {
            System.out.println(loadBalance.select(l2, "s"));
        }
    }

}