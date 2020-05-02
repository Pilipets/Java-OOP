package Lab1;

import java.math.BigInteger;

// https://brilliant.org/wiki/extended-euclidean-algorithm/
// https://en.wikibooks.org/wiki/Algorithm_Implementation/Mathematics/Extended_Euclidean_algorithm
// https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm

// ax + by = gcd(a, b)
public class ExtendedEuclidean {
    // Assumes a and b are positive
    public static BigInteger[] xgcd(BigInteger a, BigInteger b) {
        BigInteger[] qRem;
        BigInteger[] result = new BigInteger[3];
        BigInteger x = a; // x or y equals to gcd(a,b)
        BigInteger y = b;

        BigInteger x0 = BigInteger.ONE;  // These variables describe current and next values in the
        BigInteger x1 = BigInteger.ZERO; // respective sequences. Please have a look at the algortim
        BigInteger y0 = BigInteger.ZERO; // https://brilliant.org/wiki/extended-euclidean-algorithm/.
        BigInteger y1 = BigInteger.ONE;  // The Bézout's coefficients are deduced from them.
        while (true){
            qRem = x.divideAndRemainder(y);
            x = qRem[1];
            x0 = x0.subtract(y0.multiply(qRem[0]));
            x1 = x1.subtract(y1.multiply(qRem[0]));
            if (x.equals(BigInteger.ZERO)) {
                result[0]=y;
                result[1]=y0;
                result[2]=y1;
                return result;
            };

            qRem = y.divideAndRemainder(x);
            y = qRem[1];
            y0 = y0.subtract(x0.multiply(qRem[0]));
            y1 = y1.subtract(x1.multiply(qRem[0]));
            if (y.equals(BigInteger.ZERO)) {
                result[0]=x;
                result[1]=x0;
                result[2]=x1;
                return result;
            };
        }
    }
}