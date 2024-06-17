package com.example.javafx_study;

import java.util.Scanner;
import java.util.ArrayList;

public class MinIntegerFinder {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Integer> numbers = new ArrayList<>();

        System.out.println("请输入若干个整数，输入非整数字符结束：");

        // 通过循环读取输入
        while (scanner.hasNext()) {
            String input = scanner.next();
            try {
                // 尝试将输入转换为整数
                int number = Integer.parseInt(input);
                numbers.add(number);
            } catch (NumberFormatException e) {
                // 如果输入不是整数，结束输入
                System.out.println("非整数输入，结束读取。");
                break;
            }
        }
        scanner.close();

        // 如果有有效的整数输入，找出最小值
        if (!numbers.isEmpty()) {
            int min = numbers.get(0);
            for (int num : numbers) {
                if (num < min) {
                    min = num;
                }
            }
            System.out.println("输入的最小整数是：" + min);
        } else {
            System.out.println("没有有效的整数输入。");
        }
    }
}
