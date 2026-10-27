package Aud2.Zad4;

public class Student {
    final String index;
    String name;
    int grade;
    int attendance;

    public Student(String index, String name, int grade, int attendance) {
        this.index = index;
        this.name = name;
        this.grade = grade;
        this.attendance = attendance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " (" + index + "), grade=" + grade + ", attendance=" + attendance + "%";
    }

    public int getGrade() {
        return grade;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setGrade(int grade) {
        if (grade > 10) {
            grade = 10;
        }
        if (grade < 5) {
            grade = 5;
        }
        this.grade = grade;
    }
}
