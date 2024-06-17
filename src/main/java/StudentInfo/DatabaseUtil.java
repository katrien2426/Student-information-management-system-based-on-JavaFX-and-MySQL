package StudentInfo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentInfo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Wang-2242426";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC驱动加载失败", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM students");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDate("birth_date").toLocalDate(),
                        rs.getString("gender"),
                        rs.getString("institution"),
                        rs.getString("major"),
                        rs.getString("class"),
                        rs.getString("address"),
                        rs.getString("home_phone"),
                        rs.getString("personal_phone")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询学生信息失败: " + e.getMessage(), e);
        }
        return students;
    }

    public static void addStudent(String id, String name, LocalDate birthDate, String gender, String institution,
                                  String major, String classRoom, String address, String homePhone, String personalPhone) throws ValidationException {
        if (id.isEmpty() || name.isEmpty() || birthDate == null || gender.isEmpty() ||
                institution.isEmpty() || major.isEmpty() || classRoom.isEmpty() ||
                address.isEmpty() || homePhone.isEmpty() || personalPhone.isEmpty()) {
            throw new ValidationException("有信息未填写");
        }

        if (!id.matches("\\d{8}")) {
            throw new ValidationException("学号必须是8位数字");
        }

        int currentYear = java.time.Year.now().getValue();
        int age = currentYear - birthDate.getYear();
        if (age < 16 || age > 32) {
            throw new ValidationException("学生的年龄必须在16岁到32岁之间");
        }

        if (homePhone.length() != 11 || personalPhone.length() != 11) {
            throw new ValidationException("电话号码必须是11位数字");
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO students (id, name, birth_date, gender, institution, major, class, address, home_phone, personal_phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setDate(3, Date.valueOf(birthDate));
            stmt.setString(4, gender);
            stmt.setString(5, institution);
            stmt.setString(6, major);
            stmt.setString(7, classRoom);
            stmt.setString(8, address);
            stmt.setString(9, homePhone);
            stmt.setString(10, personalPhone);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("添加学生信息失败: " + e.getMessage(), e);
        }
    }

    public static void updateStudent(String id, String name, LocalDate birthDate, String gender, String institution,
                                     String major, String classRoom, String address, String homePhone, String personalPhone) throws ValidationException {
        if (name.isEmpty() || birthDate == null || gender.isEmpty() ||
                institution.isEmpty() || major.isEmpty() || classRoom.isEmpty() ||
                address.isEmpty() || homePhone.isEmpty() || personalPhone.isEmpty()) {
            throw new ValidationException("有信息未填写");
        }

        if (homePhone.length() != 11 || personalPhone.length() != 11) {
            throw new ValidationException("电话号码必须是11位数字");
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE students SET name = ?, birth_date = ?, gender = ?, institution = ?, major = ?, class = ?, address = ?, home_phone = ?, personal_phone = ? WHERE id = ?")) {
            stmt.setString(1, name);
            stmt.setDate(2, Date.valueOf(birthDate));
            stmt.setString(3, gender);
            stmt.setString(4, institution);
            stmt.setString(5, major);
            stmt.setString(6, classRoom);
            stmt.setString(7, address);
            stmt.setString(8, homePhone);
            stmt.setString(9, personalPhone);
            stmt.setString(10, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("更新学生信息失败: " + e.getMessage(), e);
        }
    }

    public static void deleteStudent(String id) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM students WHERE id = ?")) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("删除学生信息失败: " + e.getMessage(), e);
        }
    }

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
    public static List<String> getAcademies() {
        List<String> academies = new ArrayList<>();
        String query = "SELECT DISTINCT academy FROM class";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                academies.add(rs.getString("academy"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询院校信息失败: " + e.getMessage(), e);
        }
        return academies;
    }

    public static List<String> getMajors(String academy) {
        List<String> majors = new ArrayList<>();
        String query = "SELECT DISTINCT major FROM class WHERE academy = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, academy);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                majors.add(rs.getString("major"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询专业信息失败: " + e.getMessage(), e);
        }
        return majors;
    }

    public static List<String> getClasses(String academy, String major) {
        List<String> classes = new ArrayList<>();
        String query = "SELECT class FROM class WHERE academy = ? AND major = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, academy);
            stmt.setString(2, major);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                classes.add(rs.getString("class"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询班级信息失败: " + e.getMessage(), e);
        }
        return classes;
    }

}
