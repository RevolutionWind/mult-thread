package basic;

/**
 * 测试多线程下的数据不准确问题
 *
 * reason：因为不能线程t1对变量count的修改不能立刻被线程t2看见
 *
 * @author sunxy
 * @date 2020/12/2 9:07
 */
public class TestThreadDataError {

    private long count = 0;

    public void add10K() {
        int idx = 0;
        int addNum = 100000;
        while (idx++ < addNum) {
            count += 1;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final TestThreadDataError dataError = new TestThreadDataError();
        Thread t1 = new Thread(() -> dataError.add10K());
        Thread t2 = new Thread(() -> dataError.add10K());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        // 结果为10000 - 20000之间， 且越大数字会越靠近10000
        System.out.println(dataError.count);
    }


}
