import Lab1.Karatsuba;
import Lab1.MontgomeryReducer;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Random;

public class MultiplyTest {
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
    public void karatsubaMultiplyTest() {
        Random random = new Random();
        for(int i = 0; i < NUMBER_OF_TESTS; i++){
            BigInteger a = new BigInteger(NUMBER_OF_BITS,random);
            BigInteger b = new BigInteger(NUMBER_OF_BITS,random);
            Assert.assertEquals(a.multiply(b), Karatsuba.karatsubaMultiply(a,b));
        }
    }

    @Test
    public void montgomeryMultiplyTest() {
        Random random = new Random();
        for(int i = 0; i < NUMBER_OF_TESTS; i++){
            BigInteger a = new BigInteger(NUMBER_OF_BITS,random);
            BigInteger b = new BigInteger(NUMBER_OF_BITS,random);
            BigInteger base = getRandomOdd(NUMBER_OF_BITS, random);
            MontgomeryReducer reducer = new MontgomeryReducer(base);
            Assert.assertEquals(a.multiply(b).mod(base), reducer.convertOut(reducer.multiply(reducer.convertIn(a),reducer.convertIn(b))));
        }
    }
}
