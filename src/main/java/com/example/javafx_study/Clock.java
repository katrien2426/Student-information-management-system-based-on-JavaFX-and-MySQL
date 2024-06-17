package com.example.javafx_study;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Clock extends Application {

    private static final int CLOCK_RADIUS = 100;
    private static final int TICK_LENGTH = 10;
    private static final int HOUR_HAND_LENGTH = 50;
    private static final int MINUTE_HAND_LENGTH = 70;
    private static final int SECOND_HAND_LENGTH = 80;
    private static final double WINDOW_WIDTH = 400;
    private static final double WINDOW_HEIGHT = 300;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        double centerX = WINDOW_WIDTH / 2;
        double centerY = WINDOW_HEIGHT / 2 - 10;

        Circle clock = new Circle(centerX, centerY, CLOCK_RADIUS);
        clock.setFill(Color.WHITE);
        clock.setStroke(Color.BLACK);
        root.getChildren().add(clock);

        for (int i = 1; i <= 12; i++) {
            double angle = Math.PI / 6 * (i - 3);
            double innerX = centerX + Math.cos(angle) * (CLOCK_RADIUS - TICK_LENGTH);
            double innerY = centerY + Math.sin(angle) * (CLOCK_RADIUS - TICK_LENGTH);
            double outerX = centerX + Math.cos(angle) * CLOCK_RADIUS;
            double outerY = centerY + Math.sin(angle) * CLOCK_RADIUS;
            Line tick = new Line(innerX, innerY, outerX, outerY);
            tick.setStrokeWidth(1);
            root.getChildren().add(tick);

            double numberX = centerX + Math.cos(angle) * (CLOCK_RADIUS - 20);
            double numberY = centerY + Math.sin(angle) * (CLOCK_RADIUS - 20);
            Label number = new Label(Integer.toString(i));
            number.setFont(new Font("Arial", 12));
            number.setLayoutX(numberX - number.getPrefWidth() / 2);
            number.setLayoutY(numberY - number.getPrefHeight() / 2 - 10);
            root.getChildren().add(number);
        }

        Line hourHand = new Line(centerX, centerY, centerX, centerY - HOUR_HAND_LENGTH);
        Line minuteHand = new Line(centerX, centerY, centerX, centerY - MINUTE_HAND_LENGTH);
        Line secondHand = new Line(centerX, centerY, centerX, centerY - SECOND_HAND_LENGTH);

        hourHand.setStrokeWidth(5);
        minuteHand.setStrokeWidth(3);
        secondHand.setStrokeWidth(1);

        root.getChildren().addAll(hourHand, minuteHand, secondHand);

        Label timeLabel = new Label();
        timeLabel.setFont(new Font("Arial", 16));
        timeLabel.setLayoutX((WINDOW_WIDTH / 2) - 70);
        timeLabel.setLayoutY(WINDOW_HEIGHT - 30);
        root.getChildren().add(timeLabel);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    LocalDateTime currentTime = LocalDateTime.now();
                    String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy年M月d日 EEEE HH时mm分ss秒"));
                    timeLabel.setText(formattedTime);
                    timeLabel.setLayoutX(WINDOW_WIDTH / 2 - timeLabel.getWidth() / 2);

                    double hourAngle = (currentTime.getHour() % 12 + currentTime.getMinute() / 60.0) * 30;
                    double minuteAngle = currentTime.getMinute() * 6;
                    double secondAngle = currentTime.getSecond() * 6;

                    hourHand.setEndX(centerX + Math.sin(Math.toRadians(hourAngle)) * HOUR_HAND_LENGTH);
                    hourHand.setEndY(centerY - Math.cos(Math.toRadians(hourAngle)) * HOUR_HAND_LENGTH);

                    minuteHand.setEndX(centerX + Math.sin(Math.toRadians(minuteAngle)) * MINUTE_HAND_LENGTH);
                    minuteHand.setEndY(centerY - Math.cos(Math.toRadians(minuteAngle)) * MINUTE_HAND_LENGTH);

                    secondHand.setEndX(centerX + Math.sin(Math.toRadians(secondAngle)) * SECOND_HAND_LENGTH);
                    secondHand.setEndY(centerY - Math.cos(Math.toRadians(secondAngle)) * SECOND_HAND_LENGTH);
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Analog Clock");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
