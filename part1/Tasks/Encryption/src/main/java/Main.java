import cryptography.Coder;

import java.io.IOException;

import cryptography.XorCoder;
import cryptography.CeasarCoder;

public class Main {
    public static void main(String[]args) throws IOException, ClassNotFoundException {
        Coder f1 = new CeasarCoder<String>((byte)15);
        Coder f2 = new XorCoder<String>("ThisIsKey");

        String s1 = new String("Caesar original message: 2+2=4");
        String s2 = f1.decrypt(f1.encrypt(s1));
        System.out.println(s1);
        System.out.print("After encoding + decoding: ");
        System.out.println(s2);
        System.out.println();


        String s3 = new String("XOR original message: 2*2=4");
        String s4 = f2.decrypt(f2.encrypt(s3));
        System.out.println(s3);
        System.out.print("After encoding + decoding: ");
        System.out.println(s4);
    }
}
