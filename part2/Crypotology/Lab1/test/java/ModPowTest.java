import Lab1.FastModPow;
import Lab1.MontgomeryReducer;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Random;

public class ModPowTest {
    static final int NUMBER_OF_TESTS = 1000;
    static final int NUMBER_OF_BITS = 512;

    public static BigInteger getRandomOdd(int bitLength, Random random) {
        while(true) {
            BigInteger a = new BigInteger(bitLength, random);
            if(a.testBit(0))
                return a;
        }
    }
    @Test
    public void fastModPowTest() {
        Random random = new Random();
        for(int i = 0; i < NUMBER_OF_TESTS; i++){
            BigInteger a = new BigInteger(NUMBER_OF_BITS,random);
            BigInteger e = new BigInteger(NUMBER_OF_BITS,random);
            BigInteger n = new BigInteger(NUMBER_OF_BITS,random);
            Assert.assertEquals(a.modPow(e,n),FastModPow.modPow(a,e,n));
        }
    }

    @Test
    public void montgomeryModPowTest() {
        Random random = new Random();
        for(int i = 0; i < NUMBER_OF_BITS; i++){
            BigInteger a = new BigInteger(NUMBER_OF_BITS,random);
            BigInteger e = new BigInteger(NUMBER_OF_BITS,random);
            BigInteger n = getRandomOdd(NUMBER_OF_BITS, random);
            MontgomeryReducer reducer = new MontgomeryReducer(n);
            Assert.assertEquals(a.modPow(e,n), reducer.convertOut(reducer.pow(reducer.convertIn(a), e)));
        }
    }
}
