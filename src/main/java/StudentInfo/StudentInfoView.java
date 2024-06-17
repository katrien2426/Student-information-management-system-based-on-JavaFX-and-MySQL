package StudentInfo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class StudentInfoView {
    private final Stage primaryStage;

    public StudentInfoView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene getStudentInfoScene() {
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        TableView<Student> table = new TableView<>();
        table.setItems(getAllStudents());

        TableColumn<Student, String> idColumn = new TableColumn<>("学号");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));

        TableColumn<Student, String> nameColumn = new TableColumn<>("姓名");
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Student, LocalDate> birthDateColumn = new TableColumn<>("出生日期");
        birthDateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getBirthDate()));

        TableColumn<Student, String> genderColumn = new TableColumn<>("性别");
        genderColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGender()));

        TableColumn<Student, String> institutionColumn = new TableColumn<>("所在院校");
        institutionColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAcademy()));

        TableColumn<Student, String> majorColumn = new TableColumn<>("专业");
        majorColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMajor()));

        TableColumn<Student, String> classColumn = new TableColumn<>("班级");
        classColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().get_class()));

        TableColumn<Student, String> addressColumn = new TableColumn<>("家庭住址");
        addressColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAddress()));

        TableColumn<Student, String> homePhoneColumn = new TableColumn<>("家庭电话");
        homePhoneColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHomePhone()));

        TableColumn<Student, String> personalPhoneColumn = new TableColumn<>("本人电话");
        personalPhoneColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhone()));

        table.getColumns().addAll(idColumn, nameColumn, birthDateColumn, genderColumn, institutionColumn, majorColumn, classColumn, addressColumn, homePhoneColumn, personalPhoneColumn);

        table.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Student rowData = row.getItem();
                    showStudentDetail(rowData);
                }
            });
            return row;
        });

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefViewportWidth(600);
        scrollPane.setPrefViewportHeight(400);

        Button addButton = new Button("添加学生信息");
        addButton.setOnAction(e -> showAddStudentDialog());

        vbox.getChildren().addAll(scrollPane, addButton);

        return new Scene(vbox, 650, 500);
    }

    private ObservableList<Student> getAllStudents() {
        return FXCollections.observableArrayList(DatabaseUtil.getAllStudents());
    }

    private void showStudentDetail(Student student) {
        StudentDetailView detailView = new StudentDetailView(primaryStage, student);
        primaryStage.setScene(detailView.getDetailScene());
    }

    private void showAddStudentDialog() {
        AddStudentView addStudentView = new AddStudentView(primaryStage);
        primaryStage.setScene(addStudentView.getAddStudentScene());
    }
}
