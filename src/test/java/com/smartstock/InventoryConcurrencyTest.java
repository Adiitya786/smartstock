package com.smartstock;

import com.smartstock.Service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

@SpringBootTest
public class InventoryConcurrencyTest {

    @Autowired
    private InventoryService inventoryService;

    @Test
    void testConcurrentReservation() throws Exception {

        Long productId = 5L; // change to your actual product ID

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        CountDownLatch startLatch = new CountDownLatch(1);

        Future<?> customerA = executor.submit(() -> {
            try {
                startLatch.await();

                inventoryService.reserveStock(productId, 70);

                System.out.println("Customer A SUCCESS");

            } catch (Exception e) {
                System.out.println("Customer A FAILED: "
                        + e.getMessage());
            }
        });

        Future<?> customerB = executor.submit(() -> {
            try {
                startLatch.await();

                inventoryService.reserveStock(productId, 50);

                System.out.println("Customer B SUCCESS");

            } catch (Exception e) {
                System.out.println("Customer B FAILED: "
                        + e.getMessage());
            }
        });

        // Release both threads at almost the same time
        startLatch.countDown();

        customerA.get();
        customerB.get();

        executor.shutdown();
    }
}