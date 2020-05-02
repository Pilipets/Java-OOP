import cryptology.ciphers.MarsCipher;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class MarsCipherTest {
    final int NUMBER_OF_TESTS = 512;
    final int[] KEY_RANGE = {16, 56}; // 128 bits - 448 bits
    final int INPUT_MAX_LEN = 1000;

    // function to generate a random string of length n
    private static String getAlphaNumericString(int n)
    {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

    @Test
    public void testMarsChars() {
        Random rand = new Random();
        for(int i = 0; i < NUMBER_OF_TESTS; ++i) {
            String key = getAlphaNumericString(KEY_RANGE[0]
                    + rand.nextInt(KEY_RANGE[1] - KEY_RANGE[0] + 1));
            String input = getAlphaNumericString(rand.nextInt(INPUT_MAX_LEN));

            byte[] encrypted = MarsCipher.encrypt(input.getBytes(), key.getBytes());
            Assert.assertNotEquals(input, new String(encrypted));
            byte[] decrypted = MarsCipher.decrypt(encrypted, key.getBytes());
            Assert.assertEquals(input, new String(decrypted));
        }
    }

    @Test
    public void testMarsBytes() {
        Random rand = new Random();
        for(int i = 0; i < NUMBER_OF_TESTS; ++i) {
            byte[] keyBytes = new byte[KEY_RANGE[0]
                    + rand.nextInt(KEY_RANGE[1] - KEY_RANGE[0] + 1)];
            rand.nextBytes(keyBytes);
            byte[] inputBytes = new byte[rand.nextInt(INPUT_MAX_LEN)];
            rand.nextBytes(inputBytes);

            byte[] encrypted = MarsCipher.encrypt(inputBytes, keyBytes);
            Assert.assertFalse(Arrays.equals(inputBytes, encrypted));
            byte[] decrypted = MarsCipher.decrypt(encrypted, keyBytes);
            Assert.assertTrue(Arrays.equals(inputBytes, decrypted));
        }
    }
}
