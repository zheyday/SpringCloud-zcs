package zcs;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("腿：");
        int leg = Integer.parseInt(scanner.nextLine());
        int head = Integer.parseInt(scanner.nextLine());
        int rabbit = leg / 2 - head;
        int chi = head - rabbit;
        System.out.println("兔子:" + rabbit);
        System.out.println("鸡:" + chi);
    }
}
