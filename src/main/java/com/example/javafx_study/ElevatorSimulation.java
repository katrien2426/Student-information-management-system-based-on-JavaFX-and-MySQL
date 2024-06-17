package com.example.javafx_study;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class ElevatorSimulation extends Application {
    private int currentFloor = 1;
    private Label floorLabel;
    private Pane floor1;
    private Pane floor2;
    private Button btnUp;
    private Button btnDown;

    @Override
    public void start(Stage primaryStage) {
        btnUp = new Button("上升");
        btnDown = new Button("下降");

        VBox floors = new VBox();
        floors.setSpacing(0);
        floor1 = new Pane();
        floor1.setMinSize(100, 100);
        floor1.setMaxSize(100, 100);
        floor1.setStyle("-fx-border-color: black; -fx-background-color: transparent;");
        floor2 = new Pane();
        floor2.setMinSize(100, 100);
        floor2.setMaxSize(100, 100);
        floor2.setStyle("-fx-border-color: black; -fx-background-color: transparent;");

        Rectangle elevator = new Rectangle(50, 50);
        elevator.setFill(Color.GRAY);
        elevator.setTranslateY(50); // 确保电梯在楼层的底部
        floor1.getChildren().add(elevator);

        floors.getChildren().addAll(floor2, floor1);

        btnUp.setOnAction(e -> moveElevatorUp(elevator));
        btnDown.setOnAction(e -> moveElevatorDown(elevator));

        BorderPane root = new BorderPane();
        root.setCenter(floors);
        VBox buttons = new VBox(10, btnUp, btnDown);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(0, 30, 0, 30)); // 设置按钮与电梯模拟之间的距离
        root.setLeft(buttons);

        floorLabel = new Label("当前楼层: " + currentFloor);
        floorLabel.setStyle("-fx-font-size: 20px;");
        BorderPane.setAlignment(floorLabel, Pos.CENTER); // 居中显示
        root.setTop(floorLabel);

        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("电梯管理模拟");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void moveElevatorUp(Rectangle elevator) {
        if (currentFloor == 2) {
            showAlert("警告", "已经在二楼，无法再上升！");
        } else {
            btnUp.setDisable(true);
            btnDown.setDisable(true);

            TranslateTransition transition = new TranslateTransition(Duration.seconds(2), elevator);
            transition.setFromY(elevator.getTranslateY());
            transition.setToY(-50); // 二楼的目标位置

            transition.setOnFinished(e -> {
                currentFloor = 2;
                floorLabel.setText("当前楼层: " + currentFloor);
                floor1.setStyle("-fx-border-color: black; -fx-background-color: transparent;");
                floor2.setStyle("-fx-border-color: black; -fx-background-color: lightblue;"); // 设置二楼的颜色为淡蓝色
                btnUp.setDisable(false);
                btnDown.setDisable(false);
            });

            transition.play();
        }
    }

    private void moveElevatorDown(Rectangle elevator) {
        if (currentFloor == 1) {
            showAlert("警告", "已经在一楼，无法再下降！");
        } else {
            btnUp.setDisable(true);
            btnDown.setDisable(true);

            TranslateTransition transition = new TranslateTransition(Duration.seconds(2), elevator);
            transition.setFromY(elevator.getTranslateY());
            transition.setToY(50); // 一楼的目标位置

            transition.setOnFinished(e -> {
                currentFloor = 1;
                floorLabel.setText("当前楼层: " + currentFloor);
                floor2.setStyle("-fx-border-color: black; -fx-background-color: transparent;");
                floor1.setStyle("-fx-border-color: black; -fx-background-color: lightblue;"); // 设置一楼的颜色为淡蓝色
                btnUp.setDisable(false);
                btnDown.setDisable(false);
            });

            transition.play();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
