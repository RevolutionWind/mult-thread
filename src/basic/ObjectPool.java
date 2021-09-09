package basic;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * 用信号量实现限流器
 *
 * @param <T> 数据
 * @param <R>
 */
@SuppressWarnings("unused")
public class ObjectPool<T, R> {
    private final List<T> pool;
    // 用信号量实现限流器
    final Semaphore sem;

    // 构造函数
    ObjectPool(int size, T t) {
        pool = new Vector<T>() {
        };
        for (int i = 0; i < size; i++) {
            pool.add(t);
        }
        // 初始化信号量
        sem = new Semaphore(size);
    }

    // 利用对象池的对象，调用func
    R exec(Function<T, R> func) throws InterruptedException {
        T t = null;
        sem.acquire();
        try {
            t = pool.remove(0);
            return func.apply(t);
        } finally {
            pool.add(t);
            sem.release();
        }
    }

    public static void main(String[] args) {
        // 线程池
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(16, 32, 3000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(10));
        // 创建对象池
        ObjectPool<Long, String> pool = new ObjectPool<>(10, 2L);
        for (int i = 0; i < 16; i++) {
            poolExecutor.execute(() -> {
                // 通过对象池获取t，之后执行
                try {
                    String exec = pool.exec(t -> {
                        System.out.println(t);
                        return t.toString();
                    });
                    System.out.println(exec);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

    }

}
