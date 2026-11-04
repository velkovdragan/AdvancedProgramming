package Aud2.Zad4;

public class Student{
    private final String index;
    private String name;
    private int grade;
    private int attendance;

    public Student(String index, String name, int grade, int attendance) {
        this.index = index;
        this.name = name;
        setGrade(grade);
        this.attendance = attendance;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getGrade() {
        return grade;
    }

    public int getAttendance() {
        return attendance;
    }

    @Override
    public String toString() {
        return "Student{" +
                "index='" + index + '\'' +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                ", attendance=" + attendance +
                '}';
    }

    public void setGrade(int grade) {
        if(grade<5){
               grade=5;
        }

        if(grade>10){
            grade=10;
        }
        this.grade = grade;
    }
}
