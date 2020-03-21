import org.junit.Assert;
import org.junit.Test;
import view.Canvas;
import view.NetPaintGUI;

import java.awt.*;
import java.lang.reflect.Field;

public class GuiTest {
    @Test
    public void canvasDefaultValuesTest() throws NoSuchFieldException, IllegalAccessException {
        Canvas canvas = new Canvas();
        Field field = Canvas.class.
                getDeclaredField("Width");
        field.setAccessible(true);
        int width = (int) field.get(canvas);
        Assert.assertEquals(5000,width);

        field = Canvas.class.
                getDeclaredField("Height");
        field.setAccessible(true);
        int height = (int) field.get(canvas);
        Assert.assertEquals(4000,height);

        field = Canvas.class.
                getDeclaredField("backgroundColor");
        field.setAccessible(true);
        Color color = (Color) field.get(canvas);
        Assert.assertEquals(Color.WHITE,color);
    }

    @Test
    public void netGuiCheckDefaultValuesTest() throws NoSuchFieldException, IllegalAccessException {
        NetPaintGUI gui = new NetPaintGUI();
        Field field = NetPaintGUI.class.
                getDeclaredField("DX");
        field.setAccessible(true);
        int dx = (int) field.get(gui);
        Assert.assertEquals(250,dx);

        field = NetPaintGUI.class.
                getDeclaredField("DY");
        field.setAccessible(true);
        int dy = (int) field.get(gui);
        Assert.assertEquals(150,dy);

        field = NetPaintGUI.class.
                getDeclaredField("YLocation");
        field.setAccessible(true);
        int y = (int) field.get(gui);
        Assert.assertEquals(0,y);

        field = NetPaintGUI.class.
                getDeclaredField("XLocation");
        field.setAccessible(true);
        int x = (int) field.get(gui);
        Assert.assertEquals(100,x);
    }
}
