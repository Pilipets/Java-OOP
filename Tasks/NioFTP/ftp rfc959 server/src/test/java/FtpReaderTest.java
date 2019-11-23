import net.server.FtpReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class FtpReaderTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkReading() throws IOException {
        FtpReader r = new FtpReader();
        SocketChannel s = Mockito.mock(SocketChannel.class);
        FileChannel f = Mockito.mock(FileChannel.class);
        when(s.read((ByteBuffer)any())).thenReturn(-5);
        when(f.write((ByteBuffer)any())).thenReturn(0);
        r.readFile(s,f,1024);
        verify(s,atLeast(1)).read((ByteBuffer)any());
    }
}
