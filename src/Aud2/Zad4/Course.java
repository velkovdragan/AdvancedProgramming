package Aud2.Zad4;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Course {
    Student[] students;
    String title;
    int size=0;

    public Course(String title, int capacity) {
        this.students = new Student[capacity];
        this.title = title;

    }

    boolean enroll(Supplier<Student> supplier){
        if(size>=students.length){
            return false;
        }

        students[size++]=supplier.get();
        return true;
    }
    void forEach(Consumer<Student> action){
        for (int i = 0; i < size; i++) {
            action.accept(students[i]);
        }
    }
    public int count(Predicate<Student> predicate){
        int c=0;
        for(int i=0;i<size;i++){
            if(predicate.test(students[i])){
                c++;
            }
        }
        return c;
    }

    public Student findFirst(Predicate<Student> predicate) {
        for (int i = 0; i < size; i++) {
            if (predicate.test(students[i])) {
                return students[i];
            }
        }
        return null;
    }
    public Student[] filter(Predicate<Student> predicate) {
        // 1st pass: count matches to size array exactly
        int matches = count(predicate);
        Student[] out = new Student[matches];
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(students[i])) {
                out[j++] = students[i];
            }
        }
        return out;
    }

    /**
     * Map students to Strings (labels) with a Function.
     * (We return String[] to avoid generics + array creation complexity.)
     */
    public String[] mapToLabels(Function<Student, String> mapper) {
        String[] out = new String[size];
        for (int i = 0; i < size; i++) {
            out[i] = mapper.apply(students[i]);
        }
        return out;
    }

    /**
     * In-place update using a Consumer (mutation allowed).
     * Example: curve grades +1, cap at 10.
     */
    public void mutate(Consumer<Student> mutator) {
        for (int i = 0; i < size; i++) {
            mutator.accept(students[i]);
        }
    }

    public void conditionalMutate(Predicate<Student> condition, Consumer<Student> mutator) {
        for (int i = 0; i < size; i++) {
            if (condition.test(students[i])) {
                mutator.accept(students[i]);
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Course: " + title + " (" + size + "/" + students.length + " students)");
        for (Student student : students) {
            sb.append(student.toString()).append("\n");
        }
        return sb.toString();

    }
}
