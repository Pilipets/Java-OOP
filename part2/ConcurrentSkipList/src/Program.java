import skiplist.FineGrainedSkipList;

import java.util.AbstractSet;

class MyProducer  implements Runnable {
    final int start, end;
    AbstractSet list;
    MyProducer(int start, int end, AbstractSet list){
        this.start = start; this.end = end;
        this.list = list;
    }
    @Override
    public void run() {
        for(int i = start; i < end; ++i)
            list.add(i);
    }
}
class MyConsumer  implements Runnable {
    final int start, end;
    AbstractSet list;
    MyConsumer(int start, int end, AbstractSet list){
        this.start = start; this.end = end;
        this.list = list;
    }
    @Override
    public void run() {
        for(int i = start; i < end; ++i)
            list.remove(i);
    }
}
public class Program {
    public static void main(String[] args) throws InterruptedException {
        FineGrainedSkipList list = new FineGrainedSkipList(10);
        System.out.println("Start execution flow");
        Thread[] threads = new Thread[5];
        for(int i = 0; i < threads.length; ++i){
            threads[i] = new Thread(new MyProducer(i*10, i*10 + 10, list));
            threads[i].start();
        }

        for(int i = 0; i < threads.length; ++i){
            threads[i].join();
        }
        System.out.println(list.toString());

        for(int i = threads.length-1; i >= 0; --i){
            threads[i] = new Thread(new MyConsumer(i*10, i*10 + 10, list));
            threads[i].start();
        }
        for(int i = 0; i < threads.length; ++i){
            threads[i].join();
        }
        System.out.println(list.toString());
    }
}
