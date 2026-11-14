package Midterm2025.midterm2025_3;

//package midterms.m1;

import java.io.*;
import java.lang.reflect.AnnotatedWildcardType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class Student{
    private String index;

    private int counter;


    public Student(String index,int counter){
        this.index=index;

        this.counter=counter;
    }

    public String getIndex() {
        return index;
    }

    public int getCounter(){
        return counter;
    }

    @Override
    public String toString(){
        return index+" "+counter;
    }
}
class QuizEvaluator{

    List<String>correctAnswers;
    List<Student>students=new ArrayList<>();
    public QuizEvaluator(List<String> correctAnswers){
        this.correctAnswers=correctAnswers;
    }

    public void evaluateQuizAttempts(InputStream is, OutputStream os){
        Scanner scanner = new Scanner(is);
        PrintWriter printWriter=new PrintWriter(os);

        while(scanner.hasNextLine()){
            String vlez= scanner.nextLine();


            String[] podeleno=vlez.split("\\s+");

            List<String>answers=new ArrayList<>(podeleno.length-1);
            String index=podeleno[0];

            for(int i=1;i<podeleno.length;i++){
                answers.add(podeleno[i]);
            }
            int correctCounter=0;
            for(int i=0;i<answers.size();i++){
                if(answers.get(i).equals(correctAnswers.get(i))){
                    correctCounter++;
                }
            }
            Student student = new Student(index,correctCounter);
            students.add(student);

        }

        students.sort(Comparator.comparingInt(Student::getCounter).reversed());



        for(Student student:students){
            printWriter.println(student);
        }
        printWriter.flush();
    }

}

public class QuizEvaluatorTest {

    public static void wtf (Scanner in) throws FileNotFoundException {
        /*
        Read all lines from scanner and write them to a file input.txt
        * */
        PrintWriter pw = new PrintWriter(new FileOutputStream("input.txt"));
        while (in.hasNextLine()) {
            String line = in.nextLine();
            pw.println(line);
        }

        in.close();
        pw.flush();
    }

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        List<String> correct = Arrays.stream(sc.nextLine().split(";")).collect(Collectors.toList());

        wtf(sc);

        QuizEvaluator qe = new QuizEvaluator(correct);

        qe.evaluateQuizAttempts(new FileInputStream("input.txt"), System.out);
    }
}
