package reflection.testing;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runners.model.TestClass;
import reflection.utils.MyClassInfo;

public class MyClassInfoTest {
    @Test
    public void printConstructorsTest() {
        Class cl = TestClass1.class;
        String real = MyClassInfo.printConstructors(cl);
        String expected = "   public reflection.testing.TestClass1();\n" +
                "   private reflection.testing.TestClass1(int, double);\n";
        Assert.assertEquals(real, expected);
        real = MyClassInfo.printConstructors(TestClass2.class);
        expected = "   reflection.testing.TestClass2();\n";
        Assert.assertEquals(real, expected);
    }

    @Test
    public void printMethodsTest() {
        String real = MyClassInfo.printMethods(TestClass1.class);
        String expected = "   @jdk.nashorn.internal.ir.annotations.Ignore()\n" +
                "   private synchronized void func2(int);\n";
        Assert.assertEquals(real, expected);
        real = MyClassInfo.printMethods(TestClass2.class);
        expected = "   int func();\n";
        Assert.assertEquals(real, expected);
    }

    @Test
    public void printFieldsTest() {
        String real = MyClassInfo.printFields(TestClass1.class);
        String expected = String.join(
                "",
                "   private int x;\n",
                "   volatile java.util.Date d;\n");
        Assert.assertEquals(real, expected);
        real = MyClassInfo.printFields(TestClass2.class);
        expected = "";
        Assert.assertEquals(real, expected);
    }

    @Test
    public void printInfoTest() {
        String real = null;
        try {
            real = MyClassInfo.printInfo(TestClass2.class.getName(),new String[]{""});
        } catch (ClassNotFoundException e) {}
        String expected = "class reflection.testing.TestClass2\n" +
                "{\n" +
                "   reflection.testing.TestClass2();\n" +
                "\n" +
                "\n" +
                "   int func();\n" +
                "\n" +
                "\n" +
                "\n" +
                "}";
        Assert.assertEquals(real, expected);
    }
}
