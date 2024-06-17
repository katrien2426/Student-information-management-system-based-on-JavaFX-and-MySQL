package com.example.javafx_study;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileCopy {

    // 复制文件方法
    public static void copyFile(String sourceFilePath, String destinationFilePath) {
        try {
            Path sourcePath = Paths.get(sourceFilePath);
            Path destinationPath = Paths.get(destinationFilePath);
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("文件复制成功: " + sourceFilePath + " 到 " + destinationFilePath);
        } catch (IOException e) {
            System.out.println("文件复制失败: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String sourceFilePath = "F:\\ObjectOriented_school\\ex3_22030426\\ElevatorSimulation.java";
        String destinationFilePath = "F:\\ObjectOriented_school\\ElevatorSimulation.java";
        copyFile(sourceFilePath, destinationFilePath);
    }
}
