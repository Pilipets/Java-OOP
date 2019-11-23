
import net.server.Configuration;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;


public class ConfigurationTest {
    @Test
    public void checkSettingAddress() throws IOException {
        Configuration c = new Configuration();
        c.setAddress("test",21);
        Assert.assertEquals("test",c.getActiveAddress().getHostName());
        Assert.assertEquals(21,c.getActiveAddress().getPort());
    }

    @Test
    public void checkSettingConfiguration() throws IOException {
        Configuration c = new Configuration();
        c.setConfiguration("test",21);
        Assert.assertEquals("test",c.getOutputPath());
        Assert.assertEquals(21,c.getBufferSize());
    }
}
