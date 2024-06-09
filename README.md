# 学生信息管理系统

## 目录结构

| 文件名称                 | 文件功能         |
| ------------------------ | ---------------- |
| AddStudentView.java      | 添加学生信息界面 |
| DatabaseUtil.java        | 数据库工具类     |
| LoginView.java           | 登录界面         |
| Main.java                | 应用程序入口     |
| Student.java             | 学生实体类       |
| StudentDetailView.java   | 学生详细信息界面 |
| StudentInfoView.java     | 学生信息展示界面 |
| ValidationException.java | 自定义验证异常类 |

## Student.java

定义一个名为`Student`的Java类，用于表示学生的信息。

### 类的属性（成员变量）

```Java
private final String id;
private final String name;
private final LocalDate birthDate;
private final String gender;
private final String academy;
private final String major;
private final String _class;
private final String address;
private final String homePhone;
private final String phone;
```

- `private`关键字表示这些变量只能在类的内部访问。
- `final`关键字表示这些变量一旦初始化就不能改变。

```Java
import java.time.LocalDate;
```

### 构造方法

### 成员方法（getter方法）

## DatabaseUtil.java

定义一个名为`DatabaseUtil`的Java类，该类包含了与学生信息管理系统相关的数据库操作方法。

### 静态变量

定义了连接到 MySQL 数据库的 URL、用户名和密码。

```Java
private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentInfo";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "******";
```

硬编码密码在代码中是不安全的做法，可以使用环境变量或配置文件来存储敏感信息。

### 静态块

用于加载 MySQL JDBC 驱动。`Class.forName("com.mysql.cj.jdbc.Driver")` 负责加载驱动类，如果加载失败，则抛出 `RuntimeException`。

```Java
static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC驱动加载失败", e);
        }
    }
```

### 获取数据库连接

静态方法`getConnection`用于获取数据库连接。`DriverManager.getConnection` 方法使用定义的数据库 URL、用户名和密码来建立连接。

```Java
public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
}
```

### 创建用户表

静态方法`createUsersTable`创建用户表 `users`，包含自增主键 `id`，用户名 `username` 和密码 `password`。

```Java
public static void createUsersTable() {
    String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
            + "id INT(11) NOT NULL AUTO_INCREMENT, "
            + "username VARCHAR(50) NOT NULL, "
            + "password VARCHAR(50) NOT NULL, "
            + "PRIMARY KEY (id))";

    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
        stmt.execute(createTableSQL);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

### 添加用户

静态方法`addUser`向用户表 `users` 添加新用户。使用 `PreparedStatement` 执行插入操作。

```Java
public static void addUser(String username, String password) {
    String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
        stmt.setString(1, username);
        stmt.setString(2, password);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

### 查询所有学生信息

静态方法`getAllStudents`查询并返回所有学生信息。使用 `PreparedStatement` 和 `ResultSet` 执行 SQL 查询，并将结果封装到 `Student` 对象中，存储在 `List<Student>` 中返回。

```Java
public static List<Student> getAllStudents() {
    List<Student> students = new ArrayList<>();
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM students");
         ResultSet rs = stmt.executeQuery()) {…} catch (SQLException e) {…}
    return students;
}
```

### 添加学生信息

静态方法`addStudent`用于添加新学生信息。包含各种数据验证（如学号、年龄、电话号码的格式）。验证通过后，使用 `PreparedStatement` 执行插入操作。

```Java
public static void addStudent(String id, String name, LocalDate birthDate, String gender, String institution,
                              String major, String classRoom, String address, String homePhone, String personalPhone) throws ValidationException {
	//数据验证具体实现代码
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement("INSERT INTO students (id, name, birth_date, gender, institution, major, class, address, home_phone, personal_phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {…} catch (SQLException e) {…}
}
```

### 更新学生信息

静态方法`updateStudent`用于更新已有学生的信息。包含必要的数据验证，然后使用 `PreparedStatement` 执行更新操作。

```Java
public static void updateStudent(String id, String name, LocalDate birthDate, String gender, String institution,
                                 String major, String classRoom, String address, String homePhone, String personalPhone) throws ValidationException {
    //数据验证具体实现代码
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement("UPDATE students SET name = ?, birth_date = ?, gender = ?, institution = ?, major = ?, class = ?, address = ?, home_phone = ?, personal_phone = ? WHERE id = ?")) {…} catch (SQLException e) {…}
}
```

### 删除学生信息

静态方法`deleteStudent`按学生学号删除学生信息。使用 `PreparedStatement` 执行删除操作。

```java 
public static void deleteStudent(String id) {
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement("DELETE FROM students WHERE id = ?")) {
        stmt.setString(1, id);
        stmt.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException("删除学生信息失败: " + e.getMessage(), e);
    }
}
```

### 获取院校、班级、专业信息

## ValidationException.java

定义了一个名为 `ValidationException` 的公共类，它继承了 Java 标准库中的 `Exception` 类。

```Java
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
```

当输入数据不符合要求时，会抛出 `ValidationException`，并带有具体的错误信息，比如说：

```Java
try {
    addStudent("12345678", "小王", LocalDate.of(2004, 1, 1), "男", "计算机信息工程学院", "软件工程", "22软件一", "江苏省", "12345678901", "09876543210");
} catch (ValidationException e) {
    System.out.println("添加学生信息失败: " + e.getMessage());
}
```

## Main.java

### 数据库初始化

```Java
DatabaseUtil.createUsersTable();
DatabaseUtil.addUser("admin", "password");
```

- 调用 `DatabaseUtil.createUsersTable()`：创建用户表 `users`，如果表不存在的话。
- 调用 `DatabaseUtil.addUser("admin", "password")`：向用户表中添加一个默认用户 `admin`，密码为 `password`。这是为了确保系统初始时有一个可用的管理员账户。

## LoginView.java

定义了一个名为 `LoginView` 的 Java 类，用于创建一个用户登录界面。该界面由 JavaFX 库构建，并与 MySQL 数据库交互以进行用户身份验证。

在 Java 14 中引入的 `record` 是一种特殊的类，用于简化不可变数据载体（data carrier）的定义。它通过自动生成常见的样板代码，如构造方法、`equals`、`hashCode` 和 `toString` 方法，使代码更简洁和易于维护。

### 获取登录场景的方法

```Java
public Scene getLoginScene() {
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setVgap(8);
    grid.setHgap(10);
    grid.setAlignment(Pos.CENTER);
```

- `GridPane grid = new GridPane();`：创建一个 `GridPane` 布局，用于组织控件。
- `grid.setPadding(new Insets(10, 10, 10, 10));`：设置网格的内边距。
- `grid.setVgap(8);` 和 `grid.setHgap(10);`：设置行间距和列间距。
- `grid.setAlignment(Pos.CENTER);`：将网格居中对齐。

### 用户名和密码输入框

```Java
    TextField usernameInput = new TextField();//创建一个用于输入用户名的文本字段。
    usernameInput.setPromptText("用户名");//设置输入提示文本，当文本字段为空时显示。
    PasswordField passwordInput = new PasswordField();//创建一个用于输入密码的密码字段。PasswordField 会自动隐藏输入的文本，用于密码输入。
    passwordInput.setPromptText("密码");//设置密码输入框的提示文本。
    TextField passwordVisibleInput = new TextField();//创建一个普通的文本字段，用于显示明文密码。
    passwordVisibleInput.setPromptText("密码");//设置明文密码输入框的提示文本。
    passwordVisibleInput.textProperty().bindBidirectional(passwordInput.textProperty());
	//这行代码将 passwordVisibleInput 和 passwordInput 的文本属性绑定在一起。具体来说，bindBidirectional 方法使得这两个输入框的文本内容相互同步。无论在哪个输入框中输入或修改文本，另一个输入框中的文本也会相应更新
```

- 创建 `TextField` 和 `PasswordField` 控件，用于用户输入用户名和密码。
- `passwordVisibleInput.textProperty().bindBidirectional(passwordInput.textProperty());`：绑定两个文本字段，以便显示/隐藏密码功能。

### 显示密码复选框

```Java
    CheckBox showPassword = new CheckBox("显示密码");
    showPassword.setOnAction(e -> {…});
```

- `CheckBox showPassword = new CheckBox("显示密码");`：创建一个复选框，用于切换密码显示/隐藏。
- `showPassword.setOnAction(e -> { ... });`：为复选框添加事件处理器，根据复选框的状态切换密码输入框的显示和隐藏。

### 布局设置

```Java
        HBox passwordBox = new HBox(10, passwordInput, passwordVisibleInput, showPassword);
        passwordBox.setAlignment(Pos.CENTER_LEFT);

        passwordVisibleInput.setManaged(false);
        passwordVisibleInput.setVisible(false);

        VBox vbox = new VBox(10, grid, passwordBox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 10, 10, 10));
```

- 创建 `HBox` 和 `VBox` 布局，用于组织密码输入框和复选框。
- 设置控件的对齐方式和内边距。

### 登录按钮和事件处理

```Java
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
```

- 创建一个登录按钮，并添加事件处理器。
- 当用户点击登录按钮时，获取用户名和密码，并调用 `authenticate` 方法进行身份验证。
- 如果验证成功，切换到学生信息界面；否则，显示错误提示。

### 将控件添加到网络

```Java
        grid.add(new Label("用户名:"), 0, 0);
        grid.add(usernameInput, 1, 0);
        grid.add(new Label("密码:"), 0, 1);
        grid.add(passwordBox, 1, 1);
        grid.add(loginButton, 1, 2);

        return new Scene(vbox, 400, 250);
    }
```

### 用户身份验证方法

```Java
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
```

- 连接数据库，执行查询语句，根据用户名和密码验证用户身份。
- 如果查询返回结果，则表示身份验证成功，返回 `true`；否则返回 `false`。

### 显示错误提示方法

```Java
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("登录失败");
        alert.setHeaderText(null);
        alert.setContentText("用户名或密码错误");
        alert.showAndWait();
    }
}
```

- 创建并显示一个错误提示框，当用户名或密码错误时调用该方法。

## StudentInfoView.java

定义了一个名为 `StudentInfoView` 的 Java 类，用于创建和显示学生信息的主界面。该界面通过 JavaFX 构建，包含一个表格视图（`TableView`）用于显示学生信息，以及一个按钮用于添加新的学生信息。

### 获取学生信息场景并定义添加表格列

### 设置双击事件

```Java
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
```

设置行工厂，用于处理行的双击事件：当用户双击表格行时，如果该行不为空，调用 `showStudentDetail` 方法显示学生详细信息。

### 设置滚动面板和添加按钮

```Java
ScrollPane scrollPane = new ScrollPane(table);
scrollPane.setFitToWidth(true);
scrollPane.setFitToHeight(true);
scrollPane.setPrefViewportWidth(600);
scrollPane.setPrefViewportHeight(400);

Button addButton = new Button("添加学生信息");
addButton.setOnAction(e -> showAddStudentDialog());
vbox.getChildren().addAll(scrollPane, addButton);
return new Scene(vbox, 650, 500);
```

- `ScrollPane scrollPane = new ScrollPane(table);`：创建一个滚动面板，包含表格，以便当表格内容超出视图时可以滚动查看。
- 设置滚动面板的宽度和高度，并将其添加到 `VBox` 中。
- 创建一个按钮 `addButton`，并设置其点击事件处理器，点击按钮时调用 `showAddStudentDialog` 方法显示添加学生信息的对话框。
- 将滚动面板和按钮添加到 `VBox` 容器中，并返回一个包含该容器的 `Scene` 对象。

### 获取所有学生信息的方法

```Java
private ObservableList<Student> getAllStudents() {
    return FXCollections.observableArrayList(DatabaseUtil.getAllStudents());
}
```

调用 `DatabaseUtil.getAllStudents` 获取所有学生信息，并将其转换为 `ObservableList` 以便于表格视图使用。

### 显示学生详细信息的方法

```Java
private void showStudentDetail(Student student) {
    StudentDetailView detailView = new StudentDetailView(primaryStage, student);
    primaryStage.setScene(detailView.getDetailScene());
}
```

接收一个 `Student` 对象，创建一个 `StudentDetailView` 实例，并将主窗口的场景切换到学生详细信息界面。

### 显示添加学生信息对话框的方法

```Java
private void showAddStudentDialog() {
    AddStudentView addStudentView = new AddStudentView(primaryStage);
    primaryStage.setScene(addStudentView.getAddStudentScene());
}
```

创建一个 `AddStudentView` 实例，并将主窗口的场景切换到添加学生信息界面。

## AddStudentView.java

定义了一个 `AddStudentView` 类，用于创建一个添加学生信息的界面。它提供多种输入控件，包括文本输入框、日期选择器和下拉菜单，用于输入学生的各种信息。界面支持院校和专业的级联选择，用户选择院校后，专业和班级的选项会自动更新。界面包含“添加”和“返回”按钮，分别用于提交学生信息和返回主界面。点击“添加”按钮后，系统会尝试将学生信息添加到数据库，并显示成功或错误的提示框。所有控件都通过网格布局（`GridPane`）组织在一起，使界面整齐有序。

## StudentDetailView.java

和AddStudentView类非常相似，将“添加”按钮变为“更新”和“删除”按钮，其中“删除”按钮设置了防误触功能。
