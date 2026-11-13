package Midterm2025.midterm2025_4;

//package midterms.m1;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


class SkillsMatcher{

    private List<String>requiredSkills;
    public SkillsMatcher(List<String> requiredSkills){
        this.requiredSkills=requiredSkills;

    }
    public void evaluateCandidates(InputStream is, OutputStream os) throws IOException {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
        String line;

        while((line=bufferedReader.readLine())!=null){
            String[] parts=line.split("\\s+");
            String id=parts[0];

        }

    }
}
public class SkillsMatcherTest {

    public static void wtf(Scanner in) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream("input.txt"));
        while (in.hasNextLine()) {
            pw.println(in.nextLine());
        }
        pw.flush();
        pw.close();
        in.close();
    }

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);


        List<String> required = Arrays.stream(sc.nextLine().split(";"))
                .collect(Collectors.toList());


        wtf(sc);

        SkillsMatcher matcher = new SkillsMatcher(required);


        matcher.evaluateCandidates(new FileInputStream("input.txt"), System.out);
    }
}
