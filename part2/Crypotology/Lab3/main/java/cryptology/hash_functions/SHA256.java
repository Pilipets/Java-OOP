package cryptology.hash_functions;

import java.nio.ByteBuffer;

// https://www.researchgate.net/file.PostFileLoader.html?id=534b393ad3df3e04508b45ad&assetKey=AS%3A273514844622849%401442222429260#:~:text=SHA%2D256%20(secure%20hash%20algorithm,each%20block%20requiring%2064%20rounds.
// https://en.wikipedia.org/wiki/SHA-2
/*
SHA-256 (secure hash algorithm, FIPS 182-2) is a cryptographic hash function with digest length of 256
bits. It is a keyless hash function; that is, an MDC (Manipulation Detection Code).
A message is processed by blocks of 512 = 16 × 32 bits, each block requiring 64 rounds
 */
public class SHA256
{
    private static final int NUM_BLOCK_BITS = 512;
    private static final int NUM_BLOCK_WORDS = 16;
    private static final int NUM_DIGEST_WORDS = 8;
    private static final int NUM_HASH_ROUNDS = 64;
    private static int[] toIntArray(byte[] bytes)
    {
        if (bytes.length % Integer.BYTES != 0) {
            throw new IllegalArgumentException("byte array length");
        }

        ByteBuffer buf = ByteBuffer.wrap(bytes);
        int[] result = new int[bytes.length / Integer.BYTES];
        for (int i = 0; i < result.length; ++i) {
            result[i] = buf.getInt();
        }

        return result;
    }

    private static byte[] toByteArray(int[] ints) {
        ByteBuffer buf = ByteBuffer.allocate(ints.length * Integer.BYTES);
        for (int i = 0; i < ints.length; ++i) {
            buf.putInt(ints[i]);
        }
        return buf.array();
    }

    private static int ch(int x, int y, int z) {
        return (x & y) | ((~x) & z);
    }

    private static int maj(int x, int y, int z) {
        return (x & y) | (x & z) | (y & z);
    }

    private static int bigSig0(int x) {
        return Integer.rotateRight(x, 2) ^ Integer.rotateRight(x, 13)
                ^ Integer.rotateRight(x, 22);
    }

    private static int bigSig1(int x) {
        return Integer.rotateRight(x, 6) ^ Integer.rotateRight(x, 11)
                ^ Integer.rotateRight(x, 25);
    }

    private static int smallSig0(int x) {
        return Integer.rotateRight(x, 7) ^ Integer.rotateRight(x, 18)
                ^ (x >>> 3);
    }

    private static int smallSig1(int x) {
        return Integer.rotateRight(x, 17) ^ Integer.rotateRight(x, 19)
                ^ (x >>> 10);
    }

    // working arrays
    private static final int[] W = new int[NUM_HASH_ROUNDS];
    private static final int[] H = new int[NUM_DIGEST_WORDS];
    private static final int[] TEMP = new int[NUM_DIGEST_WORDS];

    // Hashes the given message with SHA-256 and returns the hash.
    public static byte[] hash(byte[] message)
    {
        System.arraycopy(H0, 0, H, 0, H0.length);

        // The message shall always be padded, even if the initial length is already a multiple of 512.
        int[] words = toIntArray(pad(message));

        // hash data block by block(each containing 16 words/512 bytes)
        for (int i = 0, n = words.length / NUM_BLOCK_WORDS; i < n; ++i) {
            // Block decomposition: for each block M, 64 words of 32 bits each are constructed as follows

            System.arraycopy(words, i * NUM_BLOCK_WORDS, W, 0, NUM_BLOCK_WORDS); // the first 16 are obtained by splitting M in 32-bit blocks
            for (int t = 16; t < W.length; ++t) {
                // the remaining 48 are obtained with the formula:
                W[t] = smallSig1(W[t - 2]) + W[t - 7]  + smallSig0(W[t - 15]) + W[t - 16];
            }

            // Hash computation: eight variables are set to their initial values, given by the first 32 bits of the fractional part
            // of the square roots of the first 8 prime numbers
            System.arraycopy(H, 0, TEMP, 0, H.length);

            // operate on TEMP, do 64 rounds
            for (int t = 0; t < W.length; ++t) {
                int t1 = TEMP[7] + bigSig1(TEMP[4])
                        + ch(TEMP[4], TEMP[5], TEMP[6]) + K[t] + W[t];
                int t2 = bigSig0(TEMP[0]) + maj(TEMP[0], TEMP[1], TEMP[2]);
                System.arraycopy(TEMP, 0, TEMP, 1, TEMP.length - 1);
                TEMP[4] += t1;
                TEMP[0] = t1 + t2;
            }

            // add values in TEMP to values in H
            for (int t = 0; t < H.length; ++t) {
                H[t] += TEMP[t];
            }
        }
        return toByteArray(H);
    }

    /**
     * Internal method, no need to call. Pads the given message to have a length
     * that is a multiple of 512 bits (64 bytes), including the addition of a
     * 1-bit, k 0-bits, and the message length as a 64-bit integer.
     */
    public static byte[] pad(byte[] message)
    {
        final int blockBytes = NUM_BLOCK_BITS / 8;

        // new message length: original + 1-bit and padding + 8-byte length
        int newMessageLength = message.length + 1 + 8;
        int padBytes = (blockBytes - newMessageLength % blockBytes) % blockBytes;
        newMessageLength += padBytes;

        // copy message to extended array
        final byte[] paddedMessage = new byte[newMessageLength];
        System.arraycopy(message, 0, paddedMessage, 0, message.length);

        // write 1-bit
        paddedMessage[message.length] = (byte) 0b10000000;

        // skip padBytes many bytes (they are already 0)
        // write 8-byte integer describing the original message length
        int lenPos = message.length + 1 + padBytes;
        ByteBuffer.wrap(paddedMessage, lenPos, 8).putLong(message.length * 8);

        return paddedMessage;
    }

    // the first 32 bits of the fractional parts of the cubic roots of the first 64 primes [2 to 311])
    private static final int[] K = {
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b,
            0x59f111f1, 0x923f82a4, 0xab1c5ed5, 0xd807aa98, 0x12835b01,
            0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7,
            0xc19bf174, 0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc,
            0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da, 0x983e5152,
            0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147,
            0x06ca6351, 0x14292967, 0x27b70a85, 0x2e1b2138, 0x4d2c6dfc,
            0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819,
            0xd6990624, 0xf40e3585, 0x106aa070, 0x19a4c116, 0x1e376c08,
            0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f,
            0x682e6ff3, 0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208,
            0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2 };

    // the first 32 bits of the fractional parts of the square roots of the first eight primes [2 to 19]
    private static final int[] H0 = {
            0x6a09e667, 0xbb67ae85, 0x3c6ef372,
            0xa54ff53a, 0x510e527f, 0x9b05688c,
            0x1f83d9ab, 0x5be0cd19
    };
}