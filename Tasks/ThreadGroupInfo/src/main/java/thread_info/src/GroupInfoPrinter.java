package thread_info.src;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GroupInfoPrinter {
    private final static int waitPeriod = 5000;
    private final static Logger loger = Logger.getLogger(GroupInfoPrinter.class.getName());

    public static Thread printThreadsInfo(ThreadGroup group) {
        Runnable log = () -> {
            while (group.activeCount() > 0) {
                Thread[] threads = new Thread[group.activeCount()];
                int count = group.enumerate(threads);
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < count; i++) {
                    str.append(String.format("%s\n",threads[i]));
                }
                System.out.println(String.format(
                        "Group: %s;\nThreads: %s;\n",
                        group.getName(),
                        str.toString()));

                try {
                    Thread.sleep(waitPeriod);
                } catch (InterruptedException e) {
                    loger.log(Level.SEVERE,e.getMessage());
                }
            }
        };
        Thread printThread = new Thread(log);
        printThread.start();
        return printThread;
    }
}