package thread_info.testing;

import org.junit.Assert;
import org.junit.Test;
import thread_info.src.GroupInfoPrinter;
import thread_info.src.ThreadRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GroupInfoPrinterTest {
    @Test
    public void checkDefaultValues() throws NoSuchFieldException, IllegalAccessException {
        GroupInfoPrinter threadLogger = new GroupInfoPrinter();
        Field field = GroupInfoPrinter.class.
                getDeclaredField("waitPeriod");
        field.setAccessible(true);
        int waitTime = (int) field.get(threadLogger);
        Assert.assertEquals(5000,waitTime);
    }
    @Test
    public void numberOfThreadsInGroupTest() throws InterruptedException {
        int firstGroupPeriod = 1000;
        int secondGroupPeriod = 2000;
        ThreadGroup group1 = new ThreadGroup(Thread.currentThread().getThreadGroup(), "firstGroup");
        ThreadGroup group2 = new ThreadGroup(Thread.currentThread().getThreadGroup(), "secondGroup");
        List<Thread> threadList = new ArrayList<Thread>() {
            {
                add(new Thread(group1, new ThreadRunner(firstGroupPeriod)));
                add(new Thread(group1, new ThreadRunner(firstGroupPeriod)));
                add(new Thread(group2, new ThreadRunner(firstGroupPeriod)));
                add(new Thread(group1, new ThreadRunner(secondGroupPeriod)));
                add(new Thread(group2, new ThreadRunner(secondGroupPeriod)));
            }
        };
        for (Thread t:
                threadList) {
            t.start();
        }

        Assert.assertEquals(3,group1.activeCount());
        Assert.assertEquals(2,group2.activeCount());

        Thread.sleep(secondGroupPeriod);

        Assert.assertEquals(1,group1.activeCount());
        Assert.assertEquals(1,group2.activeCount());

    }
}