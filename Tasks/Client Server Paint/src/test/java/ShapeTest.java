import model.Line;
import model.Oval;
import model.Rectangle;
import org.junit.Test;
import org.junit.Assert;

public class ShapeTest {
    @Test
    public void nameTest(){
        Assert.assertEquals("line", Line.Name);
        Assert.assertEquals("oval", Oval.Name);
        Assert.assertEquals("rect", Rectangle.Name);
    }
}
