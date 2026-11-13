package ZaVezbanje.zavezbanje12;

import java.util.Scanner;

class ZeroDenominatorException extends Exception{
    public ZeroDenominatorException(String message){
        super(message);
    }
}
class GenericFraction<T extends Number,U extends Number>{
    T numerator;
    U denominator;

    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        if (denominator.doubleValue() == 0.0) throw new ZeroDenominatorException("Denominator cannot be zero");
        this.numerator = numerator;

        this.denominator = denominator;
    }
    GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
//        a/b + c/d = (a·d + b·c) / (b·d)

        double n1=this.numerator.doubleValue();
        double d1=this.denominator.doubleValue();
        double n2=gf.numerator.doubleValue();
        double d2=gf.denominator.doubleValue();

        double nonew=(n1*d2 + n2*d1);
        double dennew=d1*d2;


        return new GenericFraction<Double,Double>(nonew,dennew);


    }

    double toDouble(){
        double res=numerator.doubleValue()/denominator.doubleValue();
        return res;
    }

    private static long gcd(long a,long b){
        while(b!=0){
            long temp=b;
            b=a%b;
            a=temp;
        }
        return a;
    }
    public String toString(){
        long nom=(long) this.numerator.doubleValue();
        long den=(long) this.denominator.doubleValue();

        long g=gcd(nom,den);
        nom=nom/g;
        den=den/g;

        return String.format("%.2f / %.2f",(double)nom,(double)den);

    }



}
public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

// вашиот код овде
