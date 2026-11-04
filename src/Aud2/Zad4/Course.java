package Aud2.Zad4;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Course{
    private final Student[] students;
    private final String title;
    private int num;

    public Course(String title, int capacity){
        this.title=title;
        this.students=new Student[capacity];
    }

    public int capacity() {
        return students.length;
    }

    public int size() {
        return num;
    }

    public boolean enroll(Supplier<Student> supplier){
        if(this.num<capacity()){
            Student stud=supplier.get();
            students[num++]=stud;
            return true;
        }

        return false;
    }

    public void forEach(Consumer<Student> action){
        for(int i=0;i<num;i++){
            action.accept(students[i]);
        }
    }

    public int count(Predicate<Student> condition){
        int c=0;
        for(int i=0;i<num;i++){
            if(condition.test(students[i])){
                c++;
            }
        }
        return c;
    }

    public Student findFirst(Predicate<Student> condition){
        for(int i=0;i<num;i++){
            if(condition.test(students[i])){
                return students[i];
            }
        }
        return null;
    }

    public Student[] filter(Predicate<Student> condition){
        Student[] result=new Student[count(condition)];
        int j=0;
        for(int i=0;i<num;i++){
            if(condition.test(students[i])){
                result[j++]=students[i];
            }
        }
        return result;
    }

    public String[] mapToLabels(Function<Student, String> mapper){
        String[] labels=new String[num];
        for(int i=0;i<num;i++){
            labels[i]=mapper.apply(students[i]);
        }
        return labels;
    }

    public void mutate(Consumer<Student> mutator){
        forEach(mutator);
    }

    public void conditionalMutate(Predicate<Student> condition, Consumer<Student> mutator){
        for(int i=0;i<num;i++){
            if(condition.test(students[i])){
                mutator.accept(students[i]);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("Course: ").append(title)
                .append(" (").append(num).append("/").append(capacity()).append(" students)\n");
        for(int i=0;i<num;i++){
            sb.append(students[i]).append("\n");
        }
        return sb.toString();
    }
}
