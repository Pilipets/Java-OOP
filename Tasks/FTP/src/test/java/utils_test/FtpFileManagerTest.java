package utils_test;

import ftp_server.utils.FtpConfiguration;
import ftp_server.utils.FtpFileManager;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static org.mockito.ArgumentMatchers.any;

public class FtpFileManagerTest {

    @Test
    public void saveDataFromTest() throws IOException {
        FtpConfiguration config = new FtpConfiguration();
        FtpFileManager manager = new FtpFileManager(config);

        SocketChannel mocked_channel = Mockito.mock(SocketChannel.class);
        Mockito.doReturn(0).when(mocked_channel).read((ByteBuffer)any());
        manager.saveDataFrom(mocked_channel);
        Mockito.verify(mocked_channel, Mockito.times(1)).read((ByteBuffer)any());
    }
    @Test
    public void writeToTest() throws IOException {
        String sourcePath = "E:\\documents\\University_homework\\OOP\\Tasks\\FTP\\src\\test\\resources\\test_image.gif";
        SocketChannel mocked_channel = Mockito.mock(SocketChannel.class);
        Mockito.doReturn(500).when(mocked_channel).write((ByteBuffer)any());
        FtpFileManager.writeTo(sourcePath, mocked_channel);
        Mockito.verify(mocked_channel, Mockito.atLeastOnce()).write((ByteBuffer)any());
    }
}
