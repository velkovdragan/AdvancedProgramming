package ZaVezbanje.zavezbanje7;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class UnsupportedFormatException extends Exception{
    public UnsupportedFormatException(String message){
        super(message);
    }
}
class InvalidTimeException extends Exception{
    public InvalidTimeException(String message){
        super(message);
    }
}
class Time implements Comparable<Time>{
    int hour;
    int minutes;

    public Time(int hour,int minutes){
        this.hour=hour;
        this.minutes=minutes;
    }

    public int getHour(){
        return hour;
    }
    public int getMinutes(){
        return minutes;
    }
    public void setHour(int br){
        hour+=br;
    }


    @Override
    public int compareTo(Time o) {
        int cmp = Integer.compare(this.hour, o.hour);

        if (cmp != 0) {
            return cmp;
        }

        return Integer.compare(this.minutes, o.minutes);
    }
}
class TimeTable{
    List<Time>times=new ArrayList<>();

    public TimeTable() {
    }

    public void readTimes(InputStream in) throws UnsupportedFormatException ,
            InvalidTimeException {
        Scanner sc=new Scanner(in);

        while(sc.hasNext()){
            String line=sc.next();
            if(!line.contains(":") && !line.contains(".")){
                throw new UnsupportedFormatException(line);
            }


            String[] separated=line.split("\\.|:");

            int hours=Integer.parseInt(separated[0]);
            int minutes=Integer.parseInt(separated[1]);

            if(hours<0 || hours>23 || minutes<0 || minutes>59){
                throw new InvalidTimeException(line);
            }

            Time time=new Time(hours,minutes);

            times.add(time);


        }
    }

    public void writeTimes(PrintStream out, TimeFormat timeFormat) {
        PrintWriter printWriter = new PrintWriter(out);

        times.sort(null);

//        FORMAT_24, FORMAT_AMPM

        if(timeFormat==TimeFormat.FORMAT_AMPM){
            for(Time time:times){
               int displayhour=time.getHour();
               String period;

               if(displayhour==0){
                   displayhour+=12;
                   period="AM";
               }

               else if(displayhour>=1&&displayhour<=11){
                   period="AM";
               }
               else if(displayhour==12){
                   period="PM";
               }
               else{
                   displayhour-=12;
                   period="PM";
               }

                printWriter.printf("%2d:%02d %s%n", displayhour, time.getMinutes(), period);

            }


        }
        else{
            for(Time time:times){
                printWriter.printf("%2d:%02d%n", time.getHour(), time.getMinutes());
            }
        }

        printWriter.flush();




    }
}
public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}
