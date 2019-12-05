package cryptography;

import java.io.*;

public class CeasarCoder<T> extends Coder {
    final byte shift;
    public CeasarCoder(byte shift){
        super(CipherOption.Ceaser);
        this.shift = shift;
    }
    @Override
    public <T> byte[] encrypt(T data) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(byteStream);
        outStream.writeObject(data);
        outStream.flush();
        byte [] byteData = byteStream.toByteArray();
        for (int i = 0; i < byteData.length; i++){
            byteData[i] = (byte)((byteData[i] + shift));
        }
        return byteData;
    }

    @Override
    public <T> T decrypt(byte[] encryptedData) throws IOException, ClassNotFoundException {
        for (int i = 0; i < encryptedData.length; i++) {
            encryptedData[i] = (byte)((encryptedData[i] - shift));
        }
        ByteArrayInputStream byteStream = new ByteArrayInputStream(encryptedData);
        ObjectInputStream inputStream = new ObjectInputStream(byteStream);
        return (T) inputStream.readObject();
    }
}
