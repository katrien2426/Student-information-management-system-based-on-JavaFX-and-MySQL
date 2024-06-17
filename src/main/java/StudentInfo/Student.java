package StudentInfo;

import java.time.LocalDate;

public class Student {
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

    public Student(String id, String name, LocalDate birthDate, String gender, String academy, String major, String _class, String address, String homePhone, String phone) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.academy = academy;
        this.major = major;
        this._class = _class;
        this.address = address;
        this.homePhone = homePhone;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getAcademy() {
        return academy;
    }

    public String getMajor() {
        return major;
    }

    public String get_class() {
        return _class;
    }

    public String getAddress() {
        return address;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public String getPhone() {
        return phone;
    }
}
