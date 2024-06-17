package com.example.javafx_study;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class GuessNumberGame extends Application {
    private int targetNumber;
    private int lowerBound;
    private int upperBound;
    private Label resultLabel;
    private TextField inputField;
    private Button guessButton;
    private Button resetButton;
    private Label rangeLabel;
    private BorderPane root;
    private ScaleTransition scaleTransition; // 用于管理动画状态的成员变量

    @Override
    public void start(Stage primaryStage) {
        initializeGame();

        resultLabel = new Label("请输入一个数字进行猜测：");
        resultLabel.setStyle("-fx-font-size: 18px;");
        rangeLabel = new Label("当前猜测范围: 1 - 100");
        rangeLabel.setStyle("-fx-font-size: 18px;");

        inputField = new TextField();
        inputField.setMaxWidth(200);

        guessButton = new Button("猜测");
        guessButton.setOnAction(e -> makeGuess());

        resetButton = new Button("重置");
        resetButton.setOnAction(e -> resetGame());

        VBox vbox = new VBox(10, resultLabel, inputField, guessButton, resetButton, rangeLabel);
        vbox.setAlignment(Pos.CENTER);

        root = new BorderPane();
        root.setCenter(vbox);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("猜数游戏");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeGame() {
        targetNumber = new Random().nextInt(100) + 1; // 生成1到100之间的随机数
        lowerBound = 1;
        upperBound = 100;
        System.out.println("随机生成的数字是: " + targetNumber); // 在控制台打印随机生成的数字
    }

    private void makeGuess() {
        stopAnimation(); // 每次猜测前停止当前动画

        try {
            int guess = Integer.parseInt(inputField.getText());
            if (guess > targetNumber) {
                resultLabel.setText("Too Large!");
                root.setStyle("-fx-background-color: red;");
                upperBound = guess - 1; // 更新上限
            } else if (guess < targetNumber) {
                resultLabel.setText("Too Small!");
                root.setStyle("-fx-background-color: blue;");
                lowerBound = guess + 1; // 更新下限
            } else {
                resultLabel.setText("Right, Good!");
                root.setStyle("-fx-background-color: green;");
                inputField.setDisable(true);
                guessButton.setDisable(true);
            }
            rangeLabel.setText("当前猜测范围: " + lowerBound + " - " + upperBound);
            playAnimation(resultLabel);
        } catch (NumberFormatException e) {
            resultLabel.setText("请输入一个有效的数字！");
            root.setStyle("-fx-background-color: yellow;");
            playAnimation(resultLabel);
        }
        inputField.requestFocus(); // 设置焦点回到输入框
    }

    private void resetGame() {
        stopAnimation(); // 重置游戏时停止当前动画

        initializeGame();
        resultLabel.setText("请输入一个数字进行猜测：");
        rangeLabel.setText("当前猜测范围: 1 - 100");
        inputField.clear();
        inputField.setDisable(false);
        guessButton.setDisable(false);
        root.setStyle("-fx-background-color: white;");
        inputField.requestFocus(); // 设置焦点回到输入框
    }

    private void playAnimation(Label label) {
        String text = label.getText();
        if ("Too Large!".equals(text) || "Too Small!".equals(text) || "Right, Good!".equals(text) || "请输入一个有效的数字！".equals(text)) {
            scaleTransition = new ScaleTransition(Duration.millis(500), label);
            scaleTransition.setFromX(1.0);
            scaleTransition.setFromY(1.0);
            scaleTransition.setToX(1.5);
            scaleTransition.setToY(1.5);
            scaleTransition.setAutoReverse(true);
            scaleTransition.setCycleCount(ScaleTransition.INDEFINITE); // 无限循环
            scaleTransition.play();
        }
    }

    private void stopAnimation() {
        if (scaleTransition != null) {
            scaleTransition.stop();
            scaleTransition = null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
