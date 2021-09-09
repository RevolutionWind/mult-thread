package basic;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 消费队列实现管程
 *
 * @author sunxy
 * @date 2020/12/1 16:06
 */
@SuppressWarnings("unused")
public class ConsumeQueue<T> {
    final Lock lock = new ReentrantLock();
    /**
     * 条件变量：队列不满
     */
    final Condition notFull = lock.newCondition();
    /**
     * 条件变量：队列不为空
     */
    final Condition notEmpty = lock.newCondition();
    /**
     * 数据存放队列
     */
    final Queue<T> queue = new LinkedList<>();

    private final static int MAX_QUEUE_SIZE = 10;

    /**
     * 入队
     *
     * @param data T
     */
    public void inQueue(T data) {
        lock.lock();
        try {
            while (queue.size() == MAX_QUEUE_SIZE) {
                // 等待队列不满
                notFull.await();
            }
            // 入队
            queue.add(data);
            // 入队后, 队列不为空, 则通知可以出队
            notEmpty.signal();
        } catch (Exception ignored) {
        } finally {
            lock.unlock();
        }
    }

    /**
     * 出队
     *
     * @return T
     */
    public T outQueue() {
        T data = null;
        lock.lock();
        try {
            while (queue.size() < 1) {
                // 等待队列不为空
                notEmpty.await();
            }
            data = queue.poll();
            // 通知其他线程队列不满
            notFull.signal();
            return data;
        } catch (Exception ignored) {
        } finally {
            lock.unlock();
        }
        return data;
    }

}
