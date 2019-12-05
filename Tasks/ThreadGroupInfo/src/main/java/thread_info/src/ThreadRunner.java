package thread_info.src;

import java.util.logging.Level;
import java.util.logging.Logger;
public class ThreadRunner implements Runnable{
    private int time;
    private final static Logger loger = Logger.getLogger(ThreadRunner.class.getName());

    public ThreadRunner(int time) {
        this.time=time;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            loger.log(Level.SEVERE,e.getMessage());
        }
    }
}