package thread_info.src;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ThreadGroup group1 = new ThreadGroup(Thread.currentThread().getThreadGroup(), "firstGroup");
        ThreadGroup group2 = new ThreadGroup(Thread.currentThread().getThreadGroup(), "secondGroup");
        (new Thread(group1, new ThreadRunner(7000))).start();
        (new Thread(group1, new ThreadRunner(2000))).start();
        (new Thread(group2, new ThreadRunner(5000))).start();
        Thread.sleep(6000);
        (new Thread(group1, new ThreadRunner(10000))).start();
        (new Thread(group2, new ThreadRunner(15000))).start();
        GroupInfoPrinter.printThreadsInfo(group1);
        GroupInfoPrinter.printThreadsInfo(group2);
    }
}