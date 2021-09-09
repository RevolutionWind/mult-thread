package basic;

/**
 * 线程安全的单例模式
 * <p>
 * 我们以为的new操作:
 * 1. 分配一块内存 M；
 * 2. 在内存 M 上初始化 Singleton 对象；
 * 3. 然后 M 的地址赋值给 instance 变量。
 * 编译器优化后的new操作:
 * 1. 分配一块内存 M；
 * 2. 然后 M 的地址赋值给 instance 变量；
 * 3. 在内存 M 上初始化 Singleton 对象。
 * <p>
 * 如果线程A到了第二步，线程B访问getInstance()方法，则线程B会直接返回空地址M。
 * 线程B后续的访问instance操作会直接报空指针异常
 *
 * 解决: private volatile static ThreadSafeSingleton instance;
 *
 * @author sunxy
 * @date 2020/12/2 9:21
 */
@SuppressWarnings("unused")
public class ThreadSafeSingleton {

    private static ThreadSafeSingleton instance;

    private static ThreadSafeSingleton getInstance() {
        if (instance == null) {
            synchronized (ThreadSafeSingleton.class) {
                if (instance == null) {
                    instance = new ThreadSafeSingleton();
                }
            }
        }
        return instance;
    }

}
