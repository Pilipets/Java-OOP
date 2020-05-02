import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

import cryptology.hash_functions.SHA256;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SHA256Test
{
    @Test
    public void testHashGeneral() throws NoSuchAlgorithmException {
        final int NUMBER_OF_TEST = 1000;
        final int MAX_INPUT_LEN = 1000;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        Random rand = new Random();

        for(int i = 0; i < NUMBER_OF_TEST; ++i) {
            byte[] input = new byte[rand.nextInt(MAX_INPUT_LEN)];
            rand.nextBytes(input);

            Assert.assertArrayEquals(SHA256.hash(input), digest.digest(input));
        }
    }


    @Test
    public void testHashEmpty() {
        byte[] b = {};
        byte[] expected = DatatypeConverter.parseHexBinary(
                "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");

        assertArrayEquals(expected, SHA256.hash(b));
    }

    @Test
    public void testHashRegular()
    {
        byte[] b = "Hello world!".getBytes(StandardCharsets.US_ASCII);
        byte[] expected = DatatypeConverter.parseHexBinary(
                "c0535e4be2b79ffd93291305436bf889314e4a3faec05ecffcbb7df31ad9e51a");

        assertArrayEquals(expected, SHA256.hash(b));
    }

    @Test
    public void testHashLong()
    {
        byte[] b = ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                + "Proin pulvinar turpis purus, sit amet dapibus magna commodo "
                + "quis metus.").getBytes(StandardCharsets.US_ASCII);
        byte[] expected = DatatypeConverter.parseHexBinary(
                "60497604d2f6b4df42cea5efb8956f587f81a4ad66fa1b65d9e085224d255036");

        assertArrayEquals(expected, SHA256.hash(b));
    }

    @Test
    public void testHashRawBytes()
    {
        byte[] b = new byte[256];
        for (int i = 0; i < b.length; ++i) {
            b[i] = (byte) i;
        }

        byte[] expected = DatatypeConverter.parseHexBinary(
                "40aff2e9d2d8922e47afd4648e6967497158785fbd1da870e7110266bf944880");

        assertArrayEquals(expected, SHA256.hash(b));
    }

    @Test
    public void testHash55()
    {
        byte[] b = new byte[55];
        for (int i = 0; i < b.length; ++i) {
            b[i] = (byte) 'a';
        }

        byte[] expected = DatatypeConverter.parseHexBinary(
                "9f4390f8d30c2dd92ec9f095b65e2b9ae9b0a925a5258e241c9f1e910f734318");

        assertArrayEquals(expected, SHA256.hash(b));
    }

    // PADDING
    @Test
    public void testPaddedLengthDivisibleBy512()
    {
        byte[] b = { 0, 1, 2, 3, 0 };
        byte[] padded = SHA256.pad(b);
        int paddedLengthBits = padded.length * 8;

        assertTrue(paddedLengthBits % 512 == 0);
    }

    @Test
    public void testPaddedMessageHas1Bit()
    {
        byte[] b = new byte[64];
        byte[] padded = SHA256.pad(b);

        assertEquals((byte) 0b1000_0000, padded[b.length]);
    }

    @Test
    public void testPaddingAllZero()
    {
        byte[] b = { 1, 1, 1, 1, 1, 1, 1, };
        byte[] padded = SHA256.pad(b);

        for (int i = b.length + 1; i < padded.length - 8; ++i) {
            assertEquals("byte " + i + " not 0", 0, padded[i]);
        }
    }

}