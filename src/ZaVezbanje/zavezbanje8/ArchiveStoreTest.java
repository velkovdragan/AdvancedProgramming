package ZaVezbanje.zavezbanje8;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

class NonExistingItemException extends Exception{

    public NonExistingItemException(String m){
        super(m);
    }
}
class Archive{
    private int id;
    Date date;

    public Archive(int id){
        this.id=id;

    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate(){
        return date;
    }

    public int getId(){
        return id;
    }
}
class LockedArchive extends Archive{
    private Date dateToOpen;

    public LockedArchive(int id,Date dateToOpen){
        super(id);
        this.dateToOpen=dateToOpen;
    }

    public Date getDateToOpen() {
        return dateToOpen;
    }
}
class SpecialArchive extends Archive{
    private int maxOpen;
    int counter=0;

    public SpecialArchive(int id, int maxOpen){
        super(id);
        this.maxOpen=maxOpen;

    }

    public void incrementCounter(){
        counter++;
    }

    public int getCounter(){
        return counter;
    }

    public int getMaxOpen(){
        return maxOpen;
    }
}
class ArchiveStore{
    List<Archive>archives=new ArrayList<>();
    List<String>messages=new ArrayList<>();

    public ArchiveStore(){

    }
    public void archiveItem(Archive item, Date date){
        item.setDate(date);
        archives.add(item);
        messages.add("Item "+item.getId()+" archived at "+ date);

    }

    public void openItem(int id, Date date) throws NonExistingItemException {
        Archive el=null;
        for(Archive archive:archives){
            if(archive.getId()==id){
                el=archive;
                break;
            }
        }

        if(el==null){
            throw new NonExistingItemException("Item with id "+ id +" doesn't exist");
        }

        if(el instanceof LockedArchive){
            LockedArchive lockedArchive=(LockedArchive) el;
            if(date.before(lockedArchive.getDateToOpen())){
              messages.add("Item "+ id +" cannot be opened before "+lockedArchive.getDateToOpen());
            }
            else{
                messages.add("Item "+id+" opened at "+date);
            }
        }
        else if(el instanceof SpecialArchive){
            SpecialArchive specialArchive = (SpecialArchive) el;
            ;
            if(specialArchive.getCounter()>=specialArchive.getMaxOpen()){
                messages.add("Item "+ id +" cannot be opened more than "+ specialArchive.getMaxOpen() +" times");
            }
            else{
                specialArchive.incrementCounter();
                messages.add("Item "+id+" opened at "+date);
            }
        }


    }
    public String getLog(){
        String result="";
        for(String message:messages){
            result+=message+"\n";
        }
        return result;

    }

}
public class ArchiveStoreTest {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {

        ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}

// вашиот код овде


