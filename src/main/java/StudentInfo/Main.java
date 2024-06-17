package StudentInfo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseUtil.createUsersTable();
        DatabaseUtil.addUser("admin", "password");

        LoginView loginView = new LoginView(primaryStage);
        Scene loginScene = loginView.getLoginScene();

        primaryStage.setTitle("学生信息管理系统");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
