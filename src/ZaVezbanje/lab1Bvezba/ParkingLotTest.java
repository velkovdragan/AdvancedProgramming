package ZaVezbanje.lab1Bvezba;

import com.sun.source.tree.IfTree;
import jdk.swing.interop.LightweightFrameWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.module.InvalidModuleDescriptorException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

// ===================== Part A: your classes =====================
//
// Copy your Ticket, HourlyTicket, DailyTicket, LotFullException and ParkingLot classes from Part A here.
// Do not change Ticket, HourlyTicket, DailyTicket or LotFullException.

/*
TODO: your Part A code here
*/
class LotFullException extends Exception{
    public LotFullException(){
        super();
    }
}
abstract class Ticket{
    private final String plate;
    private final int minutes;

    protected Ticket(String plate, int minutes){

        if(plate==null || plate.isEmpty()){
            throw new IllegalArgumentException();
        }

        if(minutes<=0){
            throw new IllegalArgumentException();
        }
        this.plate=plate;
        this.minutes=minutes;
    }

    public String getPlate(){
        return plate;
    }

    public int getMinutes(){
        return minutes;
    }

    public abstract double price();

    public String toString() {
        return plate;
    }

}

class HourlyTicket extends Ticket {

    public HourlyTicket(String plate, int minutes){
        super(plate,minutes);
    }

    @Override
    public double price() {
        return Math.ceil(getMinutes() / 60.0) * 50;
    }

}

class DailyTicket extends Ticket {
    public DailyTicket(String plate, int minutes){
        super(plate,minutes);
    }

    public double price(){
        return Math.ceil(getMinutes() / 1440.0) * 300;
    }
}

// ===================== Part B: new code =====================

/*
TODO: PricingRule, PricingRules, and the new ParkingLot methods
*/
@FunctionalInterface
interface PricingRule{
    double adjust(Ticket ticket, double price);
}
class PricingRules{
    private PricingRules(){

    }

    public static final PricingRule GRACE_PERIOD=(ticket,price)->
    {
        if(ticket.getMinutes()<=15){
            return 0;
        }
        return price;
    };




    public static final PricingRule LONG_STAY_DISCOUNT=(ticket,price)->{
        if(ticket.getMinutes()>480){
            return price*0.8;
        }
        return price;
    };

    public static PricingRule percentOff(int percent){
        if(percent<0 || percent>100){
            throw new IllegalArgumentException();
        }
        return (ticket,price)->{
           return price*((100-percent)/100.0);
        };


    }
}

class ParkingLot{
    private Ticket[] tickets;
    private int ticketCount;
    private int capacity;

    public ParkingLot(int capacity){
        if(capacity<=0){
            throw new IllegalArgumentException();
        }
        this.capacity=capacity;
        this.tickets=new Ticket[capacity];
    }

    public void addTicket(Ticket ticket) throws LotFullException {
        if(ticket==null){
            throw new IllegalArgumentException();

        }

        if(ticketCount==capacity){
            throw new LotFullException();
        }

        tickets[ticketCount++]=ticket;
    }

    public int getTicketCount(){
        return ticketCount;
    }
    public double totalRevenue(){
        if(ticketCount==0){
            return 0.0;
        }

        double sum=0;
        for(int i=0;i<ticketCount;i++){
            sum+=tickets[i].price();
        }
        return sum;
    }

    public double revenueWithRule(PricingRule rule){
//        double adjust(Ticket ticket, double price);
        if(rule==null){
            throw new IllegalArgumentException();
        }
        double sum=0;
        for(int i=0;i<ticketCount;i++){
            sum+=rule.adjust(tickets[i],tickets[i].price());
        }
        return sum;
    }

    public double revenueWhere(Predicate<Ticket> condition){
        double sum=0;
        if(condition==null){
            throw new IllegalArgumentException();
        }
        for(int i=0;i<ticketCount;i++){
            if(condition.test(tickets[i])){
                sum+=tickets[i].price();
            }
        }

        return sum;
    }

    public static Predicate<Ticket> longerThan(int minutes){

        return ticket-> ticket.getMinutes()>minutes;
    }

    public String[] describeAll(Function<Ticket, String> describer){

        if(ticketCount==0){
            String[] niza=new String[0];
        }
        if(describer==null){
            throw new IllegalArgumentException();
        }
        String[] niza=new String[ticketCount];
        for(int i=0;i<ticketCount;i++){
            niza[i]=describer.apply(tickets[i]);

        }

        return niza;
    }

    public Ticket findFirst(Predicate<Ticket> condition){
        if(condition==null){
            throw new IllegalArgumentException();
        }

        for(int i=0;i<ticketCount;i++){
            if(condition.test(tickets[i])){
                return tickets[i];
            }
        }

        return null;
    }
}

public class ParkingLotTest {
    private static ParkingLot lot = null;

    private static String plateArg(String token) {
        if (token.equals("NULL")) return null;
        if (token.equals("EMPTY")) return "";
        return token;
    }

    private static boolean noLot() {
        if (lot == null) {
            System.out.println("No lot created");
            return true;
        }
        return false;
    }

    private static PricingRule ruleByName(String name) {
        if (name.startsWith("OFF:")) return PricingRules.percentOff(Integer.parseInt(name.substring(4)));
        switch (name) {
            case "GRACE": return PricingRules.GRACE_PERIOD;
            case "LONG": return PricingRules.LONG_STAY_DISCOUNT;
            case "PLUS10": return (t, p) -> p + 10;
            case "TIMES2": return (t, p) -> p * 2;
            case "IDENTITY": return (t, p) -> p;
            default: throw new IllegalStateException("unknown rule " + name);
        }
    }

    // "A,B,C" applies A first, then B to A's result, then C.
    private static PricingRule parseRules(String expr) {
        PricingRule result = (t, p) -> p;
        for (String name : expr.split(",")) {
            PricingRule before = result;
            PricingRule next = ruleByName(name);
            result = (t, p) -> next.adjust(t, before.adjust(t, p));
        }
        return result;
    }

    private static Predicate<Ticket> atom(String s) {
        if (s.startsWith("!")) {
            return atom(s.substring(1)).negate();
        }
        if (s.equals("ALL")) return t -> true;
        if (s.equals("NONE")) return t -> false;
        if (s.startsWith("LONGER:")) return ParkingLot.longerThan(Integer.parseInt(s.substring(7)));
        if (s.startsWith("SHORT:")) {
            int n = Integer.parseInt(s.substring(6));
            return t -> t.getMinutes() <= n;
        }
        if (s.startsWith("PLATE:")) {
            String prefix = s.substring(6);
            return t -> t.getPlate().startsWith(prefix);
        }
        throw new IllegalStateException("unknown predicate " + s);
    }

    // "|" separates alternatives, "&" joins conditions, "!" negates one condition.
    private static Predicate<Ticket> parsePredicate(String expr) {
        Predicate<Ticket> or = null;
        for (String orPart : expr.split("\\|")) {
            Predicate<Ticket> and = null;
            for (String a : orPart.split("&")) {
                Predicate<Ticket> p = atom(a);
                and = (and == null) ? p : and.and(p);
            }
            or = (or == null) ? and : or.or(and);
        }
        return or;
    }

    private static Function<Ticket, String> describerByName(String name) {
        switch (name) {
            case "PLATE": return Ticket::getPlate;
            case "PLATE_MINUTES": return t -> t.getPlate() + ":" + t.getMinutes();
            case "PRICE": return t -> "" + t.price();
            case "PLATE_PRICE": return t -> t.getPlate() + ":" + t.price();
            default: throw new IllegalStateException("unknown function " + name);
        }
    }

    private static void run(String cmd, String[] p) throws LotFullException {
        switch (cmd) {
            case "LOT":
                lot = new ParkingLot(Integer.parseInt(p[1]));
                System.out.println("Lot created (capacity " + p[1] + ")");
                break;

            case "NEW_HOURLY": {
                Ticket t = new HourlyTicket(plateArg(p[1]), Integer.parseInt(p[2]));
                System.out.println("HourlyTicket " + t.getPlate() + " " + t.getMinutes() + " " + t.price());
                break;
            }
            case "NEW_DAILY": {
                Ticket t = new DailyTicket(plateArg(p[1]), Integer.parseInt(p[2]));
                System.out.println("DailyTicket " + t.getPlate() + " " + t.getMinutes() + " " + t.price());
                break;
            }

            case "ADD_HOURLY":
                if (noLot()) break;
                lot.addTicket(new HourlyTicket(plateArg(p[1]), Integer.parseInt(p[2])));
                System.out.println("Added");
                break;
            case "ADD_DAILY":
                if (noLot()) break;
                lot.addTicket(new DailyTicket(plateArg(p[1]), Integer.parseInt(p[2])));
                System.out.println("Added");
                break;
            case "ADD_NULL":
                if (noLot()) break;
                lot.addTicket(null);
                System.out.println("Added");
                break;

            case "COUNT":
                if (noLot()) break;
                System.out.println("Count: " + lot.getTicketCount());
                break;
            case "REVENUE":
                if (noLot()) break;
                System.out.println("Revenue: " + lot.totalRevenue());
                break;

            case "RULE_ADJUST": {
                Ticket t = p[2].equals("HOURLY")
                        ? new HourlyTicket("X", Integer.parseInt(p[3]))
                        : new DailyTicket("X", Integer.parseInt(p[3]));
                System.out.println("Adjusted: " + parseRules(p[1]).adjust(t, Double.parseDouble(p[4])));
                break;
            }
            case "RULE_REVENUE":
                if (noLot()) break;
                System.out.println("Rule revenue: " + lot.revenueWithRule(parseRules(p[1])));
                break;
            case "RULE_NULL":
                if (noLot()) break;
                lot.revenueWithRule(null);
                System.out.println("No exception");
                break;
            case "RULE_CALLS": {
                if (noLot()) break;
                int[] calls = {0};
                lot.revenueWithRule((t, price) -> {
                    calls[0]++;
                    return price;
                });
                System.out.println("Calls: " + calls[0]);
                break;
            }

            case "LONGER_TEST":
                System.out.println("Longer: " + ParkingLot.longerThan(Integer.parseInt(p[1]))
                        .test(new HourlyTicket("X", Integer.parseInt(p[2]))));
                break;
            case "WHERE":
                if (noLot()) break;
                System.out.println("Where: " + lot.revenueWhere(parsePredicate(p[1])));
                break;
            case "WHERE_NULL":
                if (noLot()) break;
                lot.revenueWhere(null);
                System.out.println("No exception");
                break;

            case "FIND": {
                if (noLot()) break;
                Ticket t = lot.findFirst(parsePredicate(p[1]));
                System.out.println("Found: " + (t == null ? "none" : t.getPlate()));
                break;
            }
            case "FIND_NULL":
                if (noLot()) break;
                lot.findFirst(null);
                System.out.println("No exception");
                break;

            case "DESCRIBE": {
                if (noLot()) break;
                String[] r = lot.describeAll(describerByName(p[1]));
                System.out.println("Describe: " + r.length + " " + Arrays.toString(r));
                break;
            }
            case "DESCRIBE_NULL":
                if (noLot()) break;
                lot.describeAll(null);
                System.out.println("No exception");
                break;
            case "DESCRIBE_CALLS": {
                if (noLot()) break;
                int[] calls = {0};
                lot.describeAll(t -> {
                    calls[0]++;
                    return "x";
                });
                System.out.println("Calls: " + calls[0]);
                break;
            }

            default:
                System.out.println("Unknown command");
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] p = line.split("\\s+");
            if (p[0].equals("END")) break;
            try {
                run(p[0], p);
            } catch (LotFullException e) {
                System.out.println("LotFullException");
            } catch (IllegalArgumentException e) {
                System.out.println("IllegalArgumentException");
            }
        }
    }
}