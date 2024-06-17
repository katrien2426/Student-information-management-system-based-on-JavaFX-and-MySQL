package Examination;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.sql.Statement;

public class GraphicInterface extends Application {

    private Statement statement; // 模拟数据库连接和操作

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Computer Information");

        GridPane grid = new GridPane();

        Label label1 = new Label("型号：");
        TextField textField1 = new TextField();
        Label label2 = new Label("品牌：");
        TextField textField2 = new TextField();
        Label label3 = new Label("处理器：");
        TextField textField3 = new TextField();
        Label label4 = new Label("内存：");
        TextField textField4 = new TextField();
        Label label5 = new Label("硬盘：");
        TextField textField5 = new TextField();

        grid.add(label1, 0, 0);
        grid.add(textField1, 1, 0);
        grid.add(label2, 0, 1);
        grid.add(textField2, 1, 1);
        grid.add(label3, 0, 2);
        grid.add(textField3, 1, 2);
        grid.add(label4, 0, 3);
        grid.add(textField4, 1, 3);
        grid.add(label5, 0, 4);
        grid.add(textField5, 1, 4);

        Button btnSave = new Button("保存");
        Button btnLoad = new Button("读取");
        Button btnClear = new Button("清空");

        grid.add(btnSave, 0, 5);
        grid.add(btnLoad, 1, 5);
        grid.add(btnClear, 2, 5);

        btnSave.setOnAction(event -> {
            try{
                String model = textField1.getText();
                String brand = textField2.getText();
                String processor = textField3.getText();
                String memory = textField4.getText();
                String hardDrive = textField5.getText();

                if (model.isEmpty() || brand.isEmpty() || processor.isEmpty() || memory.isEmpty() || hardDrive.isEmpty()) {
                    System.out.println("请输入所有字段");
                    return;
                }

                String sql = "INSERT INTO computer_info (model, brand, processor, memory, hard_drive) VALUES ('" + model + "', '" + brand + "', '" + processor + "', '" + memory + "', '" + hardDrive + "')";
                statement.executeUpdate(sql); // 模拟执行SQL语句
                System.out.println("数据已保存");
            }catch(Exception e){
                e.printStackTrace();
            }
        });

        btnLoad.setOnAction(event -> {
            try {
                String sql = "SELECT * FROM computer_info WHERE id = 1";
                ResultSet rs = statement.executeQuery(sql); // 模拟执行SQL查询
                rs = null; // 模拟数据
                if (rs != null && rs.next()) {
                    String model = rs.getString("model");
                    String brand = rs.getString("brand");
                    String processor = rs.getString("processor");
                    String memory = rs.getString("memory");
                    String hardDrive = rs.getString("hard_drive");

                    textField1.setText(model);
                    textField2.setText(brand);
                    textField3.setText(processor);
                    textField4.setText(memory);
                    textField5.setText(hardDrive);

                    System.out.println("数据已读取");
                } else {
                    System.out.println("未找到数据");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btnClear.setOnAction(event -> {
            textField1.clear();
            textField2.clear();
            textField3.clear();
            textField4.clear();
            textField5.clear();
            System.out.println("数据已清空");
        });

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
