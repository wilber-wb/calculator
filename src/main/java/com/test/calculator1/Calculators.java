package com.test.calculator1;

import java.util.Scanner;

/**
 * 第一种解法, 使用Scanner进行动态undo和redo
 */
public class Calculators {

    /**
     * 直接运行下面代码，可在控制台进行undo和redo操作
     *
     * @param args
     */
    public static void main(String args[]) {
        while (true) {
            System.out.println("请输入第一个数字：");
            Scanner sc = new Scanner(System.in);
            int num1 = sc.nextInt();

            System.out.println("请输入运算符号：");
            String symbol = sc.next();

            System.out.println("请输入第二个数字：");
            int num2 = sc.nextInt();

            String result = "";
            if (symbol.equals("+")) {
                result = (num1 + num2 + "");
            } else if (symbol.equals("-")) {
                result = (num1 - num2 + "");
            } else if (symbol.equals("*")) {
                result = (num1 * num2 + "");
            } else if (symbol.equals("/")) {
                result = (num1 / num2 + "");
            } else {
                System.out.println("运算符号输入有误！");
            }
            System.out.println("计算结果为：" + num1 + symbol + num2 + "=" + result + ";");
            System.out.println("是否继续运算？y/n");
            String s = sc.next();
            if (s.equals("y")) {
            } else if (s.equals("n")) {
                break;
            } else {
                System.out.println("是否继续运算？y/n");
                s = sc.next();
                if (s.equals("n")) {
                    break;
                }
            }
        }
    }
}
