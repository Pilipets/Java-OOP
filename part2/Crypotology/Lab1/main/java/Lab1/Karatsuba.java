package Lab1;

import java.math.BigInteger;

// https://en.wikipedia.org/wiki/Karatsuba_algorithm
public class Karatsuba {
    private final static int MULT_BIT_LENGTH = 200; // if bitlength is small, do common multiply
    public static BigInteger karatsubaMultiply(BigInteger x, BigInteger y) {
        // cutoff to brute force
        int N = Math.max(x.bitLength(), y.bitLength());
        if (N <= MULT_BIT_LENGTH) return x.multiply(y);

        // number of bits divided by 2, rounded up
        N = (N / 2) + (N % 2);

        // x = a + 2^N b,   y = c + 2^N d
        BigInteger b = x.shiftRight(N);
        BigInteger a = x.subtract(b.shiftLeft(N));
        BigInteger d = y.shiftRight(N);
        BigInteger c = y.subtract(d.shiftLeft(N));

        BigInteger ac    = karatsubaMultiply(a, c);
        BigInteger bd    = karatsubaMultiply(b, d);
        BigInteger abcd  = karatsubaMultiply(a.add(b), c.add(d));

        return ac.add(abcd.subtract(ac).subtract(bd).shiftLeft(N)).add(bd.shiftLeft(2*N));
    }
}