import reflection.utils.MyClassInfo;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        String dirPath = "E:\\documents\\University_homework\\OOP\\Tasks\\ReflectionApp\\build\\classes\\java\\main\\reflection\\utils";
        try {
            System.out.println(MyClassInfo.printInfo("reflection.data.SortingAlgorithm", new String[]{dirPath}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
