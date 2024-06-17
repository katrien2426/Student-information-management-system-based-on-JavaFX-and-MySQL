package StudentInfo;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public record StudentDetailView(Stage primaryStage, Student student) {

    public Scene getDetailScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        TextField nameInput = new TextField(student.getName());
        DatePicker birthDatePicker = new DatePicker(student.getBirthDate());
        ComboBox<String> genderInput = new ComboBox<>(FXCollections.observableArrayList("男", "女"));
        genderInput.setValue(student.getGender());
        ComboBox<String> institutionInput = new ComboBox<>(FXCollections.observableArrayList(DatabaseUtil.getAcademies()));
        institutionInput.setValue(student.getAcademy());
        ComboBox<String> majorInput = new ComboBox<>(FXCollections.observableArrayList(DatabaseUtil.getMajors(student.getAcademy())));
        majorInput.setValue(student.getMajor());
        ComboBox<String> classInput = new ComboBox<>(FXCollections.observableArrayList(DatabaseUtil.getClasses(student.getAcademy(), student.getMajor())));
        classInput.setValue(student.get_class());
        TextField addressInput = new TextField(student.getAddress());
        TextField homePhoneInput = new TextField(student.getHomePhone());
        TextField personalPhoneInput = new TextField(student.getPhone());

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

        Button updateButton = new Button("更新");
        updateButton.setOnAction(e -> {
            try {
                DatabaseUtil.updateStudent(student.getId(), nameInput.getText(), birthDatePicker.getValue(), genderInput.getValue(),
                        institutionInput.getValue(), majorInput.getValue(), classInput.getValue(), addressInput.getText(),
                        homePhoneInput.getText(), personalPhoneInput.getText());
                showAlert("成功", "学生信息更新成功", Alert.AlertType.INFORMATION);
                primaryStage.setScene(new StudentInfoView(primaryStage).getStudentInfoScene());
            } catch (ValidationException ex) {
                showAlert("错误", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button deleteButton = new Button("删除");
        deleteButton.setOnAction(e -> {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("确认删除");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("确定要删除该学生信息吗？");

            ButtonType confirmButton = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == confirmButton) {
                    DatabaseUtil.deleteStudent(student.getId());
                    showAlert("成功", "学生信息删除成功", Alert.AlertType.INFORMATION);
                    primaryStage.setScene(new StudentInfoView(primaryStage).getStudentInfoScene());
                }
            });
        });

        Button backButton = new Button("返回");
        backButton.setOnAction(e -> primaryStage.setScene(new StudentInfoView(primaryStage).getStudentInfoScene()));

        grid.addRow(0, new Label("姓名:"), nameInput);
        grid.addRow(1, new Label("出生日期:"), birthDatePicker);
        grid.addRow(2, new Label("性别:"), genderInput);
        grid.addRow(3, new Label("所在院校:"), institutionInput);
        grid.addRow(4, new Label("专业:"), majorInput);
        grid.addRow(5, new Label("班级:"), classInput);
        grid.addRow(6, new Label("家庭住址:"), addressInput);
        grid.addRow(7, new Label("家庭电话:"), homePhoneInput);
        grid.addRow(8, new Label("本人电话:"), personalPhoneInput);
        grid.addRow(9, updateButton, deleteButton, backButton);

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
