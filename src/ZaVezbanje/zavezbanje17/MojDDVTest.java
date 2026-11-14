package ZaVezbanje.zavezbanje17;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(String message){
        super(message);
    }
}
class Smetka{
    private String id;
    int sum;
    double taxReturn;

    public Smetka(String id,int sum,double taxReturn){
        this.id=id;
        this.sum=sum;
        this.taxReturn=taxReturn;

    }

    @Override
    public String toString(){
        return String.format("%10s\t%10d\t%10.5f", id, sum, taxReturn);
    }
}
class MojDDV{
    private List<Smetka>smetki=new ArrayList<>();
    public MojDDV(){

    }
    void readRecords (InputStream inputStream) {
        Scanner scanner=new Scanner(inputStream);

        while(scanner.hasNextLine()){
            String[] vlez=(scanner.nextLine().split("\\s+"));
            String id=vlez[0];
            int sum=0;
            List<Double> returns=new ArrayList<>();
            for(int i=1;i<vlez.length;i+=2){
                int price=Integer.parseInt(vlez[i]);
                sum+=price;
                if(vlez[i+1].equals("A")){
                    returns.add(price*0.18*0.15);
                }
                else if(vlez[i+1].equals("B")){
                    returns.add(price*0.05*0.15);
                }
            }

            try {
                if(sum>30000){
                    throw new AmountNotAllowedException("Receipt with amount "+ sum+" is not allowed to be scanned");
                }
                double taxReturn=returns.stream().mapToDouble(Double::doubleValue).sum();
                smetki.add(new Smetka(id,sum,taxReturn));
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    void printTaxReturns (OutputStream outputStream){
        PrintWriter printWriter=new PrintWriter(outputStream);

        printWriter.println(smetki.stream().map(Object::toString).collect(Collectors.joining("\n")));
        printWriter.flush();
    }
    void printStatistics (OutputStream outputStream){
        PrintWriter printWriter=new PrintWriter(outputStream);
        DoubleSummaryStatistics stats=smetki.stream()
                .mapToDouble(s->s.taxReturn)
                .summaryStatistics();

        printWriter.println(String.format("min:\t%5.3f",stats.getMin()));
        printWriter.println(String.format("max:\t%5.3f",stats.getMax()));
        printWriter.println(String.format("sum:\t%5.3f",stats.getSum()));
        printWriter.println(String.format("count:\t%-5d",stats.getCount()));
        printWriter.println(String.format("avg:\t%5.3f",stats.getAverage()));
        printWriter.flush();
    }
}
public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}