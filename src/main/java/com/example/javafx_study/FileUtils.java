package com.example.javafx_study;

import java.io.File;

public class FileUtils {

    // 获取指定目录下的文件名及文件大小
    public static void listFiles(String directoryPath) {
        File directory = new File(directoryPath);

        // 检查目录是否存在且是一个目录
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        System.out.println("文件名: " + file.getName() + ", 文件大小: " + file.length() + " bytes");
                    }
                }
            } else {
                System.out.println("指定的路径下没有文件。");
            }
        } else {
            System.out.println("指定的路径不是一个目录或目录不存在。");
        }
    }

    public static void main(String[] args) {
        String directoryPath = "F:\\ObjectOriented_school\\ex3_22030426";
        listFiles(directoryPath);
    }
}

