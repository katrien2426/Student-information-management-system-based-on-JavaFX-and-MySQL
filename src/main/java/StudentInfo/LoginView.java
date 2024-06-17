package StudentInfo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public record LoginView(Stage primaryStage) {

    public Scene getLoginScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        TextField usernameInput = new TextField();
        usernameInput.setPromptText("用户名");
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("密码");
        TextField passwordVisibleInput = new TextField();
        passwordVisibleInput.setPromptText("密码");

        passwordVisibleInput.textProperty().bindBidirectional(passwordInput.textProperty());

        CheckBox showPassword = new CheckBox("显示密码");
        showPassword.setOnAction(e -> {
            if (showPassword.isSelected()) {
                passwordVisibleInput.setManaged(true);
                passwordVisibleInput.setVisible(true);
                passwordInput.setManaged(false);
                passwordInput.setVisible(false);
            } else {
                passwordVisibleInput.setManaged(false);
                passwordVisibleInput.setVisible(false);
                passwordInput.setManaged(true);
                passwordInput.setVisible(true);
            }
        });

        HBox passwordBox = new HBox(10, passwordInput, passwordVisibleInput, showPassword);
        passwordBox.setAlignment(Pos.CENTER_LEFT);

        passwordVisibleInput.setManaged(false);
        passwordVisibleInput.setVisible(false);

        VBox vbox = new VBox(10, grid, passwordBox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        Button loginButton = new Button("登录");
        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.isVisible() ? passwordInput.getText() : passwordVisibleInput.getText();
            if (authenticate(username, password)) {
                primaryStage.setScene(new StudentInfoView(primaryStage).getStudentInfoScene());
            } else {
                showAlert();
            }
        });

        grid.add(new Label("用户名:"), 0, 0);
        grid.add(usernameInput, 1, 0);
        grid.add(new Label("密码:"), 0, 1);
        grid.add(passwordBox, 1, 1);
        grid.add(loginButton, 1, 2);

        return new Scene(vbox, 400, 250);
    }

    private boolean authenticate(String username, String password) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("登录失败");
        alert.setHeaderText(null);
        alert.setContentText("用户名或密码错误");
        alert.showAndWait();
    }
}
