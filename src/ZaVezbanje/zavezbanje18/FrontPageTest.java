package ZaVezbanje.zavezbanje18;

import java.util.*;
import java.util.stream.Collectors;

class Category{
    private String name;

    public Category(String name){
        this.name=name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Category)) return false;

        Category other = (Category) obj;
        return this.name.equals(other.name);
    }
    public String getName() {
        return name;
    }

}
abstract class NewsItem{
    private String title;
    private Date date;
    public Category category;

    public NewsItem(String title,Date date, Category category){
        this.title=title;
        this.date=date;
        this.category=category;
    }

    public abstract String getTeaser();

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }
}
class TextNewsItem extends NewsItem{
    private String text;

    public TextNewsItem(String title,Date date, Category category,String text){
        super(title,date,category);
        this.text=text;
    }

    @Override
    public String getTeaser() {
        long minutes = (System.currentTimeMillis() - this.getDate().getTime()) / 60000;

        return String.format("%s\n%d\n%s",
                this.getTitle(),
                minutes,
                text.substring(0, Math.min(80, text.length())));
    }
}
class  MediaNewsItem extends NewsItem{
    private String url;
    private int views;

    public MediaNewsItem(String title,Date date, Category category,String url,int views){
        super(title,date,category);
        this.url=url;
        this.views=views;
    }

    @Override
    public String getTeaser(){
        long minutes=(System.currentTimeMillis()-this.getDate().getTime())/60000;
        return String.format("%s\n%d\n%s\n%d",this.getTitle(),minutes,url,views);
    }
}
class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String message){
        super(message);
    }
}
class FrontPage{
    private List<NewsItem>newsItems=new ArrayList<>();
    private Category[] categories;

    public FrontPage(Category[] categories){

        this.categories=categories;
    }
    public void addNewsItem(NewsItem newsItem){
        newsItems.add(newsItem);
    }
    public List<NewsItem> listByCategory(Category category){
        List<NewsItem>filtered=newsItems.stream().filter(s->s.getCategory().equals(category))
                .collect(Collectors.toList());

        return filtered;
    }
    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {

        if(Arrays.stream(categories).noneMatch(c->c.getName().equals(category))){
            throw new CategoryNotFoundException("Category "+category+" was not found");
        }
        List<NewsItem>filtered=newsItems.stream().filter(s->s.getCategory().getName().equals(category))
                .collect(Collectors.toList());

        return filtered;

    }

    @Override
    public String toString(){
        return newsItems.stream()
                .map(s -> s.getTeaser())
                .collect(Collectors.joining("\n"))+"\n";
    }
}
public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde
