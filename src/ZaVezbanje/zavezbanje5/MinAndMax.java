package ZaVezbanje.zavezbanje5;

import java.util.Scanner;

class MinMax<T extends Comparable<T>>{
    private T min;
    private T max;
    private int mincounter=0;
    private int maxcounter=0;
    private int counter=0;
    public MinMax(){

    }
    public void update(T element){
        counter++;

        if(min==null){
            min=element;
            max=element;
            mincounter=1;
            maxcounter=1;
            return;
        }

        if(element.compareTo(min)<0){
            min=element;
            mincounter=1;
        } else if(element.compareTo(min)==0){
            mincounter++;
        }

        if(element.compareTo(max)>0){
            max=element;
            maxcounter=1;
        } else if(element.compareTo(max)==0){
            maxcounter++;
        }
    }
    public T max(){
        return max;
    }
    public T min(){
        return min;
    }
    @Override
    public String toString(){
        int others;
        if(min.compareTo(max)==0){
            others=counter-mincounter;
        } else {
            others=counter-mincounter-maxcounter;
        }

        return min+" "+max+" "+others+"\n";
    }
}
public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}