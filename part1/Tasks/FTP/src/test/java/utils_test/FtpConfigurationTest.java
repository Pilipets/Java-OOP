package utils_test;

import ftp_server.utils.FtpConfiguration;
import org.junit.Assert;
import org.junit.Test;

import java.net.InetSocketAddress;

public class FtpConfigurationTest {
    @Test
    public void initTest(){
        FtpConfiguration config = new FtpConfiguration();
        Assert.assertEquals(config.getBufferSize(), 2048);
        Assert.assertEquals(config.getOutputPath(), "File.pdf");
        InetSocketAddress expected_address = new InetSocketAddress("127.0.0.1",1111);
        Assert.assertTrue(config.getInetAddress().equals(expected_address));
    }

}
