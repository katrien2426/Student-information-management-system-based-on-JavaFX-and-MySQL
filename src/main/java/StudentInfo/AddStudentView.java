package StudentInfo;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public record AddStudentView(Stage primaryStage) {

    public Scene getAddStudentScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        TextField idInput = new TextField();
        TextField nameInput = new TextField();
        DatePicker birthDatePicker = new DatePicker();
        ComboBox<String> genderInput = new ComboBox<>(FXCollections.observableArrayList("男", "女"));
        ComboBox<String> institutionInput = new ComboBox<>(FXCollections.observableArrayList(DatabaseUtil.getAcademies()));
        ComboBox<String> majorInput = new ComboBox<>();
        ComboBox<String> classInput = new ComboBox<>();
        TextField addressInput = new TextField();
        TextField homePhoneInput = new TextField();
        TextField personalPhoneInput = new TextField();

        institutionInput.setOnAction(e -> {
            String selectedAcademy = institutionInput.getValue();
            majorInput.setItems(FXCollections.observableArrayList(DatabaseUtil.getMajors(selectedAcademy)));
            classInput.setItems(FXCollections.observableArrayList());
        });

        majorInput.setOnAction(e -> {
            String selectedAcademy = institutionInput.getValue();
            String selectedMajor = majorInput.getValue();
            classInput.setItems(FXCollections.observableArrayList(DatabaseUtil.getClasses(selectedAcademy, selectedMajor)));
        });

        Button addButton = new Button("添加");
        addButton.setOnAction(e -> {
            try {
                DatabaseUtil.addStudent(idInput.getText(), nameInput.getText(), birthDatePicker.getValue(), genderInput.getValue(),
                        institutionInput.getValue(), majorInput.getValue(), classInput.getValue(), addressInput.getText(),
                        homePhoneInput.getText(), personalPhoneInput.getText());
                showAlert("成功", "学生信息添加成功", Alert.AlertType.INFORMATION);
                primaryStage.setScene(new StudentInfoView(primaryStage).getStudentInfoScene());
            } catch (ValidationException ex) {
                showAlert("错误", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button backButton = new Button("返回");
        backButton.setOnAction(e -> primaryStage.setScene(new StudentInfoView(primaryStage).getStudentInfoScene()));

        grid.addRow(0, new Label("学号:"), idInput);
        grid.addRow(1, new Label("姓名:"), nameInput);
        grid.addRow(2, new Label("出生日期:"), birthDatePicker);
        grid.addRow(3, new Label("性别:"), genderInput);
        grid.addRow(4, new Label("所在院校:"), institutionInput);
        grid.addRow(5, new Label("专业:"), majorInput);
        grid.addRow(6, new Label("班级:"), classInput);
        grid.addRow(7, new Label("家庭住址:"), addressInput);
        grid.addRow(8, new Label("家庭电话:"), homePhoneInput);
        grid.addRow(9, new Label("本人电话:"), personalPhoneInput);
        grid.addRow(10, addButton, backButton);

        return new Scene(grid, 400, 400);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
