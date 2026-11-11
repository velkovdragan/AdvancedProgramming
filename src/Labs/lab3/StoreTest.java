package Labs.lab3;

import javax.print.CancelablePrintJob;
import javax.swing.plaf.ProgressBarUI;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

class InvalidProductException extends Exception {
    public InvalidProductException(String line) {
        // TODO

        super("Invalid product: " + line);
    }
}

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String category) {
        // TODO
        super("Category " + category + " not found");
    }
}

abstract class Product implements Comparable<Product> {
    // TODO: id, name, category, basePrice
    private int id;
    private String name;
    private String category;
    private double basePrice;

    public Product(int id, String name, String category, double basePrice) {
        // TODO
        this.id=id;
        this.name=name;
        this.category=category;
        this.basePrice=basePrice;
    }

    // TODO: getId(), getName(), getCategory(), getBasePrice()


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public abstract double getFinalPrice();

    public abstract char getTypeCode();

    public static Product createProduct(String line) throws InvalidProductException {

        String[] podeleno = line.split(";", -1);

        if (podeleno.length != 6) {
            throw new InvalidProductException(line);
        }

        for (int i = 0; i < podeleno.length; i++) {
            podeleno[i] = podeleno[i].trim();
        }

        String type = podeleno[0];

        if (!type.equals("D") && !type.equals("P")) {
            throw new InvalidProductException(line);
        }

        try {
            int id = Integer.parseInt(podeleno[1]);
            String name = podeleno[2];
            String category = podeleno[3];
            double basePrice = Double.parseDouble(podeleno[4]);

            if (basePrice < 0) {
                throw new InvalidProductException(line);
            }

            if (type.equals("D")) {
                int sizeMb = Integer.parseInt(podeleno[5]);

                if (sizeMb <= 0) {
                    throw new InvalidProductException(line);
                }

                return new DigitalProduct(id, name, category, basePrice, sizeMb);

            } else {
                double weight = Double.parseDouble(podeleno[5]);

                if (weight <= 0) {
                    throw new InvalidProductException(line);
                }

                return new PhysicalProduct(id, name, category, basePrice, weight);
            }

        } catch (NumberFormatException e) {
            throw new InvalidProductException(line);
        }
    }

    @Override
    public int compareTo(Product other) {

        int priceComparison = Double.compare(
                this.getFinalPrice(), other.getFinalPrice()
        );

        if (priceComparison != 0) {
            return priceComparison;
        }
        else{
            int nameComparison=this.getName().compareTo(other.getName());
            if(nameComparison!=0){
                return nameComparison;
            }
        }

        return Integer.compare(this.getId(),other.getId());
    }

    @Override
    public String toString() {

        return String.format("%c %-5d %-15s %-12s %10.2f", getTypeCode(), id, name, category, getFinalPrice());
    }
}
class NameComparator implements Comparator<Product>{


    @Override
    public int compare(Product p1,Product p2) {
        int nameComparison = p1.getName().compareToIgnoreCase(p2.getName());
        int finalpriceComparison = Double.compare(p2.getFinalPrice(), p1.getFinalPrice());
        int idComparison = Integer.compare(p1.getId(), p2.getId());

        if (nameComparison == 0) {
            if (finalpriceComparison == 0) {
                return idComparison;
            } else {
                return finalpriceComparison;
            }
        } else {
            return nameComparison;
        }
    }
}

class PhysicalProduct extends Product {

    private double weight;

    public PhysicalProduct(int id, String name, String category, double basePrice, double weight) {
        super(id, name, category, basePrice);
        this.weight=weight;
    }

    public double getWeight() {

        return weight;
    }

    public double getShippingCost() {
        if(weight<=1){
            return 60;
        }
        else{
            return 60+30*(weight-1);
        }
    }

    @Override
    public double getFinalPrice() {
        return this.getBasePrice()+this.getShippingCost();
    }

    @Override
    public char getTypeCode() {
        return 'P';
    }


    @Override
    public String toString(){
        return  super.toString()+String.format(" | %.2f kg", weight);
    }
}

class DigitalProduct extends Product {
    // TODO
    private int sizeMb;

    public DigitalProduct(int id, String name, String category, double basePrice, int sizeMb) {
        super(id, name, category, basePrice);
        this.sizeMb=sizeMb;
    }

    public int getSizeMb() {
        // TODO
        return this.sizeMb;
    }

    @Override
    public double getFinalPrice() {
        // TODO
        return this.getBasePrice()*0.9;
    }

    @Override
    public char getTypeCode() {
        return 'D';
    }

    // TODO: toString()
    @Override
    public String toString(){
        return super.toString()+String.format(" | %d MB", sizeMb);
    }
}

class Store{
    // TODO
    private List<Product> products=new ArrayList<>();
    private int invalidLines=0;



    public int readProducts(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String vlez;
        int count = 0;
        while((vlez=br.readLine())!=null){

            if(vlez.trim().isEmpty()){

                continue;
            }

            try{

                products.add(Product.createProduct(vlez));
                count++;
            } catch (InvalidProductException e) {
                this.invalidLines++;

            }


        }

        return count;

    }

    public int getInvalidLines() {
        // TODO
        return this.invalidLines;
    }

    public void printSorted(OutputStream os) {
        // TODO
        PrintWriter out=new PrintWriter(os);
        List<Product>sorted=new ArrayList<>(products);
        Collections.sort(sorted);
        for(Product product:sorted){
            out.println(product);
        }

        out.flush();



    }

    public void printByCategory(OutputStream os, String category) throws CategoryNotFoundException {
        PrintWriter out=new PrintWriter(os);

        List<Product>filtered=products.stream()
                .filter(p->p.getCategory().equals(category))
                .sorted(new NameComparator())
                .collect(Collectors.toList());


        if(filtered.isEmpty()){
            throw new CategoryNotFoundException(category);
        }

        for(Product product : filtered){
            out.println(product);
        }

        out.flush();




    }

    public void printStatistics(OutputStream os) {
        // TODO
        PrintWriter out=new PrintWriter(os);

        if(products.isEmpty()){
            out.println("No products");
            out.flush();
            return;
        }

        int total=products.size();
        int digital=0;
        int physical=0;

        double sum=0;

        for(Product product: products){

            if(product.getTypeCode()=='D'){
                digital++;
            }
            else{
                physical++;
            }

            sum+=product.getFinalPrice();

        }

        double average=sum/total;
        Product expensive=Collections.max(products);
        Product cheapest=Collections.min(products);
        out.println("Total: " + total + " (physical: " + physical + ", digital: " + digital + ")");
        out.println("Cheapest: " + cheapest);
        out.println("Most expensive: " + expensive);
        out.printf("Average price: %.2f%n", average);

        out.flush();


    }
}

public class StoreTest {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder productLines = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null && !line.trim().equals("COMMANDS")) {
            productLines.append(line).append("\n");
        }

        Store store = new Store();
        int read = store.readProducts(
                new ByteArrayInputStream(productLines.toString().getBytes(StandardCharsets.UTF_8)));
        System.out.println("Read products: " + read);
        System.out.println("Invalid lines: " + store.getInvalidLines());

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            System.out.println("=== " + line + " ===");
            switch (parts[0]) {
                case "SORTED":
                    store.printSorted(System.out);
                    break;
                case "CATEGORY":
                    try {
                        store.printByCategory(System.out, parts[1]);
                    } catch (CategoryNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "STATS":
                    store.printStatistics(System.out);
                    break;
                default:
                    System.out.println("Unknown command");
            }
        }
    }
}
