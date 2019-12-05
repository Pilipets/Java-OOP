package reflection.testing;

import jdk.nashorn.internal.ir.annotations.Ignore;

import java.util.Date;

 class TestClass1 {
    private int x;
    volatile Date d;

    public TestClass1() {

    }

    private TestClass1(int x, double y) {

    }
    @Ignore
    private synchronized void func2(int e){}

}

class TestClass2 {
    int func() {
        return 4;
    }
}