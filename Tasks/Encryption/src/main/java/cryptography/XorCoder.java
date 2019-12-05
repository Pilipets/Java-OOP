package cryptography;

import java.io.*;

public class XorCoder<T> extends Coder {
    private final String key;
    public XorCoder(String key)
    {
        super(CipherOption.XOR);
        if(key.isEmpty())
            this.key = "Default123";
        else
            this.key = key;
    }

    @Override
    public <T> byte[] encrypt(T data) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(byteStream);
        outStream.writeObject(data);
        outStream.flush();
        byte [] byteData = byteStream.toByteArray();
        int k = 0;
        for (int i = 0; i < byteData.length; i++){
            byteData[i] = (byte) (byteData[i] ^ key.charAt(k));
            k = (k+1)%key.length();
        }
        return byteData;
    }

    @Override
    public <T> T decrypt(byte[] encryptedData) throws IOException, ClassNotFoundException {
        byte[] inData = encryptedData;
        int k = 0;
        for (int i = 0; i < inData.length; i++) {
            inData[i] = (byte) (inData[i] ^ key.charAt(k));
            k=(k+1)%key.length();
        }
        ByteArrayInputStream bInputStream = new ByteArrayInputStream(inData);
        ObjectInputStream inputStream = new ObjectInputStream(bInputStream);
        return (T) inputStream.readObject();
    }
}
