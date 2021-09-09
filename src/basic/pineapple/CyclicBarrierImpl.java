package basic.pineapple;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 需要生产一个pen 和 apple组合成 pineapple
 * <p>
 * pen 和 apple 为生产者行为，组合pineapple为消费者行为
 * <p>
 * 一个是线程 pen 和 apple 要做到步调一致，另一个是要能够通知到线程 pineapple
 * <p>
 * 依然可以利用一个计数器来解决这两个难点，计数器初始化为 2，
 * 线程 T1 和 T2 生产完一条数据都将计数器减 1，
 * 如果计数器大于 0 则线程 T1 或者 T2 等待。
 * 如果计数器等于 0，则通知线程 T3，并唤醒等待的线程 T1 或者 T2，
 * 与此同时，将计数器重置为 2，这样线程 T1 和线程 T2 生产下一条数据的时候就可以继续使用这个计数器了。
 * <p>
 * 同样，还是建议你不要在实际项目中这么做，因为 Java 并发包里也已经提供了相关的工具类：CyclicBarrier。
 *
 * @author sunxy
 * @date 2021/9/9 21:20
 */
@SuppressWarnings("unused")
public class CyclicBarrierImpl {

    // 订单队列
    BlockingQueue<String> pens = new LinkedBlockingQueue<>();
    // 派送单队列
    BlockingQueue<String> apples = new LinkedBlockingQueue<>();
    // 执行回调的线程池
    Executor executor = Executors.newFixedThreadPool(1);
    /*
        我们首先创建了一个计数器初始值为 2 的 CyclicBarrier，
        需要注意的是创建 CyclicBarrier 的时候，我们还传入了一个回调函数，
        当计数器减到 0 的时候，会调用这个回调函数。
     */
    final CyclicBarrier barrier = new CyclicBarrier(2, () -> executor.execute(this::generate));

    // 组成10个pineApple
    AtomicInteger pineAppleNumber = new AtomicInteger(10);

    void generate() {
        String p = pens.poll();
        String a = apples.poll();
        // 生成pineapple
        System.out.println("Get PineApple" + p + a);
        pineAppleNumber.decrementAndGet();
    }

    public void runner() {
        // pen线程
        Thread T1 = new Thread(() -> {
            while (pineAppleNumber.get() > 0) {
                // 查询订单库
                System.out.println("I have a pen");
                pens.add("pen" + pineAppleNumber.get());
                try {
                    // 等待
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        T1.start();
        // apple线程
        Thread T2 = new Thread(() -> {
            while (pineAppleNumber.get() > 0) {
                // 生成apple
                System.out.println("I have a apple");
                apples.add("apple" + pineAppleNumber.get());
                try {
                    // 等待
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        T2.start();
    }

    public static void main(String[] args) {
        CyclicBarrierImpl impl = new CyclicBarrierImpl();
        impl.runner();
    }

}
