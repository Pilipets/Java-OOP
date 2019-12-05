package cryptography.testing;

import cryptography.CeasarCoder;
import cryptography.Coder;
import cryptography.XorCoder;
import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.stream.Stream;

public class CoderTest {

    private static <T> byte[] convertToBytes(T data) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(byteStream);
        outStream.writeObject(data);
        outStream.flush();
        return  byteStream.toByteArray();
    }
    private static Stream<String> messageProvider() {
        return Stream.of(
                new String(),
                new String("Scalable tracking of network endpoints in a Kubernetes cluster."),
                new String("Restarts containers that fail, replaces and reschedules containers when nodes die, kills containers that don’t respond to your user-defined health check, and doesn’t advertise them to clients until they are ready to serve."),
                new String("Allocation of IPv4 and IPv6 addresses to Pods and Services"));
    }
    private static String[] xorKeys = {
            "",
            "Simple Key",
            "324234453454453324343543534543",
            "6%32?!4cbdgg"
    };
    private static byte[] ceasarKeys = {
            (byte)-5,
            (byte)126,
            (byte)-56,
            (byte)0,
    };

    @ParameterizedTest
    @MethodSource("messageProvider")
    public void cypherXORTest(String message) throws IOException, ClassNotFoundException {
        for (int i = 0; i < xorKeys.length; ++i) {
            Coder f = new XorCoder<String>(xorKeys[i]);
            byte[] inputBytes = convertToBytes(message);
            byte[] encryptedMessage = f.encrypt(message);
            Assert.assertEquals(inputBytes.length, encryptedMessage.length);
            Assert.assertNotEquals(inputBytes, encryptedMessage);
            Assert.assertEquals(f.decrypt(encryptedMessage), message);
        }
    }

    @ParameterizedTest
    @MethodSource("messageProvider")
    public void cypherCeasarTest(String message) throws IOException, ClassNotFoundException {
        for(int i = 0; i < ceasarKeys.length; ++i){
            Coder f = new CeasarCoder<String>(ceasarKeys[i]);
            byte[] inputBytes = convertToBytes(message);
            byte[] encryptedMessage = f.encrypt(message);
            Assert.assertEquals(inputBytes.length, encryptedMessage.length);
            Assert.assertNotEquals(inputBytes, encryptedMessage);
            Assert.assertEquals(f.decrypt(encryptedMessage), message);
        }
    }
}
