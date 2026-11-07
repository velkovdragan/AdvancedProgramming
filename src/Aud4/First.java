package Aud4;

import java.util.*;
import java.util.stream.Collectors;

class Book{
    private String title;
    private String category;
    private float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }
    public String getTitle(){
        return title;
    }
    public float getPrice(){
        return price;
    }
}
class BookTitleAndPriceComparator implements Comparator<Book>{

    @Override
    public int compare(Book o1,Book o2){
        if(o1.getTitle().compareToIgnoreCase(o2.getTitle())==0){
            return Float.compare(o1.getPrice(),o2.getPrice());
        }else{
            return o1.getTitle().compareToIgnoreCase(o2.getTitle());
        }
    }
}
class BookPriceAndTitleComparator implements Comparator<Book>{
    @Override
    public int compare(Book o1, Book o2) {
        if (Float.compare(o1.getPrice(), o2.getPrice()) == 0) {
            return o1.getTitle().compareToIgnoreCase(o2.getTitle());
        } else
            return Float.compare(o1.getPrice(), o2.getPrice());
    }
}
class BookCollection{
    private List<Book> books;
    private int numbooks=0;

    public BookCollection() {
        this.books=new ArrayList<Book>();
    }

    public void addBook(Book book){
        books.add(book);
    }

    public void printByCategory(String category){
        String CategorySmall=category.toLowerCase();
        List<Book>filtered=new ArrayList<>();

        for(Book book:books){
            if(book.getCategory().compareToIgnoreCase(category)==0){
                filtered.add(book);
            }
        }
        filtered.sort(new BookTitleAndPriceComparator());
        for(Book book: filtered){
            System.out.println(book);
        }
    }

    public List<Book>getCheapestN(int n){
        List<Book>cheapestBooks=new ArrayList<>();
        books.sort(new BookPriceAndTitleComparator());

        for(int i=0;i<n;i++){
            cheapestBooks.add(books.get(i));
        }

        return cheapestBooks;
    }

    public void printByCategoryWithStreams(String category){
        books.stream()
                .filter(book->book.getCategory().equals(category))
                .sorted(Comparator.comparing(Book::getTitle)
                        .thenComparing(Book::getPrice))
                .forEach(System.out::println);

    }

    public List<Book> getCheapestNWithStreams(int n) {
        return books.stream()
                .sorted(Comparator.comparing(Book::getPrice)
                        .thenComparing(Book::getTitle))
                .limit(n)
                .collect(Collectors.toList());
    }
}
public class First {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(
            Scanner scanner,
            BookCollection collection
    ) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}
