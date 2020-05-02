package Lab1;

import java.math.BigInteger;
import java.util.Random;

// https://en.wikibooks.org/wiki/Algorithm_Implementation/Mathematics/Primality_Testing
// https://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test
public class MillerRabinPrimality {
    private static final BigInteger ZERO = BigInteger.ZERO;
    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = new BigInteger("2");
    private static final BigInteger THREE = new BigInteger("3");

    // Complexity
    // Using repeated squaring, the running time of this algorithm is O(k log3n), where n is the number tested for primality,
    // and k is the number of rounds performed; thus this is an efficient, polynomial-time algorithm. FFT-based multiplication
    // can push the running time down to O(k log2n log log n log log log n) = Õ(k log2n).
    public static boolean isProbablePrime(BigInteger n, int k) {
        if (n.equals(ONE))
            return false;
        if (n.compareTo(THREE) < 0)
            return true;
        int s = 0;
        BigInteger d = n.subtract(ONE);
        while (d.mod(TWO).equals(ZERO)) {
            s++;
            d = d.divide(TWO);
        }
        for (int i = 0; i < k; i++) {
            BigInteger a = uniformRandom(TWO, n.subtract(ONE));
            BigInteger x = a.modPow(d, n);
            if (x.equals(ONE) || x.equals(n.subtract(ONE)))
                continue;
            int r = 0;
            for (; r < s; r++) {
                x = x.modPow(TWO, n);
                if (x.equals(ONE))
                    return false;
                if (x.equals(n.subtract(ONE)))
                    break;
            }
            if (r == s) // None of the steps made x equal n-1.
                return false;
        }
        return true;
    }

    private static BigInteger uniformRandom(BigInteger bottom, BigInteger top) {
        Random rnd = new Random();
        BigInteger res;
        do {
            res = new BigInteger(top.bitLength(), rnd);
        } while (res.compareTo(bottom) < 0 || res.compareTo(top) > 0);
        return res;
    }
}