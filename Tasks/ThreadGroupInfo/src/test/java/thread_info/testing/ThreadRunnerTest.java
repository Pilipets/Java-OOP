package thread_info.testing;

import org.junit.Assert;
import org.junit.Test;
import thread_info.src.ThreadRunner;

import static org.junit.Assert.*;

public class ThreadRunnerTest {

    @Test
    public void runTest() throws InterruptedException {
        int sleepTime = 700;
        Thread thread = new Thread(new ThreadRunner(sleepTime));
        thread.start();
        Assert.assertTrue(thread.isAlive());
        Thread.sleep(sleepTime*3/2);
        Assert.assertFalse(thread.isAlive());
    }
}