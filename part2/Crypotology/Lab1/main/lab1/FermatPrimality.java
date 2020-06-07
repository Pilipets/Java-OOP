package lab1;

import java.math.BigInteger;
import java.util.Random;

// https://en.wikipedia.org/wiki/Fermat_primality_test
public class FermatPrimality
{
    private final static Random rand = new Random();
    private static BigInteger getRandomFermatBase(BigInteger n)
    {
        while (true)
        {
            final BigInteger a = new BigInteger (n.bitLength(), rand);
            // must be 1 <= a < n
            if (BigInteger.ONE.compareTo(a) <= 0 && a.compareTo(n) < 0)
            {
                return a;
            }
        }
    }

    public static boolean isProbablePrime(BigInteger n, int maxIterations)
    {
        if (n.equals(BigInteger.ONE))
            return false;

        for (int i = 0; i < maxIterations; i++)
        {
            BigInteger a = getRandomFermatBase(n);
            a = a.modPow(n.subtract(BigInteger.ONE), n);

            if (!a.equals(BigInteger.ONE))
                return false;
        }

        return true;
    }
}