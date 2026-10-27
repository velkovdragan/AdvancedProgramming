package Aud2.Zad4;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.Consumer;
public class CourseDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Course se = new Course("Software Engineering", 10);

        int n = sc.nextInt();

        // Supplier that reads one student per line
        Supplier<Student> studentFromInput = () -> {
            System.out.print("Enter student (index name grade attendance): ");
            String index = sc.next();
            String name = sc.next();
            int grade = sc.nextInt();
            int attendance = sc.nextInt();
            sc.nextLine(); // consume leftover newline
            return new Student(index, name, grade, attendance);
        };

        // Enroll n students using the supplier
        for (int i = 0; i < n; i++) {
            se.enroll(studentFromInput);
        }

        sc.close(); // close scanner after done
        System.out.println("\nEnrolled students:");
        se.forEach(System.out::println);

        // --- Print all enrolled students using Consumer + forEach ---
        System.out.println("\n=== All Students ===");
        Consumer<Student> printer = System.out::println;
        se.forEach(printer);

        // --- Use Predicate to filter passing students ---
        Predicate<Student> isPassing = s -> s.getGrade() >= 6;
        Predicate<Student> goodAttendance = s -> s.getAttendance() >= 70;
        Predicate<Student> passingAndPresent = isPassing.and(goodAttendance);

        System.out.println("\n=== Students with passing grade and good attendance ===");
        Student[] passing = se.filter(passingAndPresent);
        for (Student s : passing) System.out.println(s);

        // --- Find first student with grade >= 9 ---
        System.out.println("\n=== First honor student (grade >= 9) ===");
        Student honor = se.findFirst(s -> s.getGrade() >= 9);
        System.out.println(honor != null ? honor : "None found");

        // --- Mutate: curve all grades by +1 (max 10) ---
        System.out.println("\n=== Curving all grades by +1 (max 10) ===");
        Consumer<Student> curve = s -> s.setGrade(s.getGrade() + 1);
        se.mutate(curve);
        se.forEach(printer);

        // --- Conditional mutation: if attendance is above 90%, award +1 grade to student;
        System.out.println("\n=== Curving high attendance students' grades by +1 ===");

        se.conditionalMutate(
                s -> s.getAttendance()>=90,
                s -> s.setGrade(s.getGrade()+1)
        );
    }
}
