package com.example.javafx_study;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.ArcType;
import javafx.geometry.Pos;

public class FaceSwitcher extends Application {
    private boolean isSmiling = true;

    @Override
    public void start(Stage primaryStage) {
        // 创建画布
        Canvas canvas = new Canvas(200, 200);
        drawFace(canvas.getGraphicsContext2D(), isSmiling);

        // 创建按钮，用于切换表情
        Button btn = new Button("切换表情");

        // 使用VBox作为根容器，确保按钮位于画布正下方且居中
        VBox root = new VBox(10); // 增加间距
        root.setAlignment(Pos.CENTER); // 设置VBox内容居中
        root.getChildren().addAll(canvas, btn);
        updateBackground(root, isSmiling);  // 初始化背景色

        btn.setOnAction(e -> {
            isSmiling = !isSmiling;
            drawFace(canvas.getGraphicsContext2D(), isSmiling);
            updateBackground(root, isSmiling);  // 动态更新背景色
        });

        // 设置场景，增加高度以容纳按钮
        Scene scene = new Scene(root, 200, 250);

        primaryStage.setTitle("表情切换器");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 更新背景色的方法
    private void updateBackground(VBox root, boolean isSmiling) {
        root.setStyle("-fx-background-color: " + (isSmiling ? "lightpink" : "lightblue") + ";");
    }

    // 绘制表情的方法
    private void drawFace(GraphicsContext gc, boolean isSmiling) {
        // 设置背景色
        gc.setFill(isSmiling ? Color.LIGHTPINK : Color.LIGHTBLUE);
        gc.fillRect(0, 0, 200, 200);

        // 绘制脸部和眼睛
        gc.setStroke(Color.BLACK);
        gc.strokeOval(50, 50, 100, 100);
        gc.fillOval(70, 80, 20, 20);
        gc.fillOval(110, 80, 20, 20);
        gc.setFill(Color.BLACK);
        gc.fillOval(75, 85, 10, 10);
        gc.fillOval(115, 85, 10, 10);

        // 绘制嘴巴
        if (isSmiling) {
            gc.strokeArc(75, 110, 50, 20, 0, -180, ArcType.CHORD);
        } else {
            gc.strokeArc(75, 115, 50, 20, 0, 180, ArcType.CHORD);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

