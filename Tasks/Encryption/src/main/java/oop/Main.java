package oop;

import oop.cryptography.CeasarFilter;
import oop.cryptography.Filter;
import oop.cryptography.XorFilter;

import java.io.IOException;

public class Main {
    public static void main(String[]args) throws IOException, ClassNotFoundException {
        Filter f1 = new CeasarFilter<String>((byte)15), f2 = new XorFilter<String>("ThisIsKey");

        String s1 = new String("Caesar original message: 2+2=4");
        String s2 = f1.decrypt(f1.encrypt(s1));
        System.out.println(s1);
        System.out.println(s2);


        String s3 = new String("XOR original message: 2*2=4");
        String s4 = f2.decrypt(f2.encrypt(s3));
        System.out.println(s3);
        System.out.println(s4);
    }

}
