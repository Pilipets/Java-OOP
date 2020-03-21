package cryptography;

import java.io.IOException;

public abstract class Coder {
    protected enum CipherOption{
        Ceaser, XOR
    };
    CipherOption cipherType;
    protected Coder(CipherOption type){
        cipherType = type;
    }
    public abstract <T> byte[] encrypt(T data) throws IOException;
    public abstract <T> T decrypt(byte[] encryptedData) throws IOException, ClassNotFoundException;
}
