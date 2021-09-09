package basic.pineapple;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * 需要生产一个pen 和 apple组合成 pineapple
 *
 * 异步生产 pen 和 apple，同步组合 pineapple
 *
 * @author sunxy
 * @date 2021/9/9 21:15
 */
@SuppressWarnings("unused")
public class CountDownLatchImpl {

    public static void main(String[] args) throws InterruptedException {
        // 创建2个线程的线程池
        Executor executor = Executors.newFixedThreadPool(2);
        // 组成10个pineApple
        int pineAppleNumber = 10;
        while (pineAppleNumber > 0) {
            // 计数器初始化为2
            CountDownLatch latch = new CountDownLatch(2);
            executor.execute(() -> {
                System.out.println("I have a pen");
                latch.countDown();
            });
            executor.execute(() -> {
                System.out.println("I have a apple");
                latch.countDown();
            });
            // 等待两个生产者完成
            latch.await();
            System.out.println("Get PineApple");
            pineAppleNumber--;
        }
    }


}
