package Labs.lab4;

import java.awt.print.Book;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;
import java.util.stream.Collectors;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String line) {
        // TODO: порака / message: "Invalid booking: <line>"
        super("Invalid booking: " + line);
    }
}

class BookingConflictException extends Exception {
    public BookingConflictException(Booking booking, Booking existing) {
        // TODO: порака / message: "Conflict: booking <id> overlaps with booking <id> in room <room>"
        super("Conflict: booking " + booking.getId() + " overlaps with booking " + existing.getId() + " in room " + booking.getRoom());
    }
}

class RoomNotFoundException extends Exception {
    public RoomNotFoundException(String room) {
        // TODO: порака / message: "Room <room> not found"
        super("Room " + room+ " not found");
    }
}

abstract class Booking implements Comparable<Booking> {
    // ДАДЕНО - не менувај / GIVEN - do not change
    public static final DateTimeFormatter DATE_TIME =
            DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);
    public static final DateTimeFormatter DATE =
            DateTimeFormatter.ofPattern("dd.MM.uuuu").withResolverStyle(ResolverStyle.STRICT);
    public static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");
    public static final LocalTime WORK_START = LocalTime.of(8, 0);
    public static final LocalTime WORK_END = LocalTime.of(20, 0);

    // TODO: id, room, organizer, start (LocalDateTime), duration (Duration)
    private  int id;
    private String room;
    private String organizer;
    private LocalDateTime start;
    private Duration duration;


    public Booking(int id, String room, String organizer, LocalDateTime start, Duration duration) {
        this.id = id;
        this.room = room;
        this.organizer = organizer;
        this.start = start;
        this.duration = duration;
    }

    public int getId() {
        // TODO
        return id;
    }

    public String getRoom() {
        // TODO
        return room;
    }

    public String getOrganizer() {
        // TODO
        return organizer;
    }

    public LocalDateTime getStart() {
        // TODO
        return start;
    }

    public Duration getDuration() {
        // TODO
        return duration;
    }

    public LocalDateTime getEnd() {
        // TODO
        return start.plus(duration);
    }

    public LocalDate getDate() {
        // TODO
        return start.toLocalDate();
    }

    public abstract Duration getBuffer();

    public abstract char getTypeCode();

    public LocalDateTime getBlockedFrom() {
        // TODO
        return start.minus(getBuffer());
    }

    public LocalDateTime getBlockedTo() {
        // TODO
        return getEnd().plus(getBuffer());
    }

    public boolean overlaps(Booking other) {
        if(!room.equals(other.room)){
            return false;
        }

        return getBlockedFrom().isBefore(other.getBlockedTo()) &&
                other.getBlockedFrom().isBefore(getBlockedTo());
    }

    public static String formatDuration(Duration d) {
        // TODO: "1h 30m", "0h 45m", "12h 00m"
        return String.format("%dh %02dm",d.toHours(),d.toMinutesPart());
    }

    public static Booking createBooking(String line) throws InvalidBookingException {
        String[] parts = line.split(";", -1);
        if (parts.length != 7) {
            throw new InvalidBookingException(line);
        }
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        String type = parts[0];
        String room = parts[2];
        String organizer = parts[3];
        String extra = parts[6];
        if (room.isEmpty() || organizer.isEmpty() || extra.isEmpty()) {
            throw new InvalidBookingException(line);
        }

        try {
            int id = Integer.parseInt(parts[1]);
            LocalDateTime start = LocalDateTime.parse(parts[4], DATE_TIME);
            Duration duration = Duration.parse(parts[5]);


            if (duration.isZero() || duration.isNegative()
                    || !duration.equals(Duration.ofMinutes(duration.toMinutes()))) {
                throw new InvalidBookingException(line);
            }


            LocalDateTime end = start.plus(duration);
            if (start.toLocalTime().isBefore(WORK_START)
                    || !end.toLocalDate().equals(start.toLocalDate())
                    || end.toLocalTime().isAfter(WORK_END)) {
                throw new InvalidBookingException(line);
            }

            switch (type) {
                case "L":
                    return new Lecture(id, room, organizer, start, duration, extra);
                case "E":
                case "M":
                    int number = Integer.parseInt(extra);
                    if (number <= 0) {
                        throw new InvalidBookingException(line);
                    }
                    if (type.equals("E")) {
                        return new Exam(id, room, organizer, start, duration, number);
                    }
                    return new Meeting(id, room, organizer, start, duration, number);
                default:
                    throw new InvalidBookingException(line);
            }
        } catch (NumberFormatException | DateTimeException | ArithmeticException e) {

            throw new InvalidBookingException(line);
        }
    }

    @Override
    public int compareTo(Booking other) {
        int compare=this.start.compareTo(other.getStart());
        if(compare ==0){
            int compare2=this.room.compareTo(other.getRoom());
            if(compare2==0){
                return Integer.compare(this.id,other.getId());
            }
            return compare2;
        }
        return compare;
    }

    @Override
    public String toString() {
        return String.format("%c %-3d %-6s %s %s-%s %-10s %s",
                getTypeCode(), id, room,
                start.format(DATE), start.format(TIME), getEnd().format(TIME),
                organizer, formatDuration(duration));
    }
}

class Lecture extends Booking {
    // TODO: courseCode
    private String courseCode;

    public Lecture(int id, String room, String organizer, LocalDateTime start, Duration duration,
                   String courseCode) {
        super(id, room, organizer, start, duration);
        // TODO
        this.courseCode=courseCode;
    }

    @Override
    public Duration getBuffer() {
        // TODO
        return Duration.ZERO;
    }

    @Override
    public char getTypeCode() {
        return 'L';
    }

    // TODO: toString()
    @Override
    public String toString(){
        return super.toString() + " | course: " + courseCode;
    }
}

class Exam extends Booking {
    // TODO: students
    int students;

    public Exam(int id, String room, String organizer, LocalDateTime start, Duration duration, int students) {
        super(id, room, organizer, start, duration);
        // TODO
        this.students=students;
    }

    @Override
    public Duration getBuffer() {
        if(students<=60){
            return Duration.ofMinutes(15);
        }
        return Duration.ofMinutes(30);
    }

    @Override
    public char getTypeCode() {
        return 'E';
    }

    // TODO: toString()
    @Override
    public String toString(){
        return super.toString()+ " | students: " + students + ", buffer: " + this.getBuffer().toMinutes() + "m";
    }
}

class Meeting extends Booking {
    // TODO: participants
    private int participants;

    public Meeting(int id, String room, String organizer, LocalDateTime start, Duration duration,
                   int participants) {
        super(id, room, organizer, start, duration);
        // TODO
        this.participants=participants;
    }

    @Override
    public Duration getBuffer() {
        // TODO
        return Duration.ofMinutes(10);
    }

    @Override
    public char getTypeCode() {
        return 'M';
    }

    // TODO: toString()
    @Override
    public String toString(){
        return super.toString()+" | participants: "+participants+", buffer: "+this.getBuffer().toMinutes()+"m";
    }
}

class BookingSystem {
    // TODO: листа од резервации, бројачи / list of bookings, counters
    private final List<Booking>bookings=new ArrayList<>();
    private int invalidLines=0;
    private int conflicts=0;

    public void addBooking(Booking booking) throws BookingConflictException {
        // TODO
        for(Booking b : bookings){
            if(b.overlaps(booking)){
                throw new BookingConflictException(booking,b);
            }
        }
        bookings.add(booking);
    }

    public int readBookings(InputStream is, OutputStream log) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        PrintWriter printWriter = new PrintWriter(log);
        int counter=0;
        String line;

        while((line=bufferedReader.readLine())!=null){
            if(line.trim().isEmpty()){
                continue;
            }
            try{
                Booking booking = Booking.createBooking(line);
                addBooking(booking);
                counter++;
            } catch (InvalidBookingException e) {
                printWriter.println(e.getMessage());
                invalidLines++;
            } catch (BookingConflictException e) {
                printWriter.println(e.getMessage());
                conflicts++;
            }
        }
        printWriter.flush();
        return counter;
    }

    public int getInvalidLines() {
        // TODO
        return invalidLines;
    }

    public int getConflicts() {
        // TODO
        return conflicts;
    }

    public void printSchedule(OutputStream os) {
        // TODO
        List<Booking>sorted=new ArrayList<>(bookings);
        Collections.sort(sorted);
        PrintWriter printWriter=new PrintWriter(os);
        for(Booking booking:sorted){
            printWriter.println(booking);
        }

        printWriter.flush();
    }

    public void printDay(OutputStream os, LocalDate date) {
        // TODO
        PrintWriter printWriter=new PrintWriter(os);
        List<Booking>included=new ArrayList<>();
        for(Booking booking:bookings){
            if(booking.getDate().equals(date)){
                included.add(booking);
            }
        }
        if(included.isEmpty()){
            printWriter.println("No bookings");
        }
        else{
            included.sort(Comparator.comparing(Booking::getRoom)
                    .thenComparing(Booking::getStart));
            for(Booking booking:included){
                printWriter.println(booking);
            }
        }

        printWriter.flush();
    }

    public void printRoom(OutputStream os, String room) throws RoomNotFoundException {
        // TODO
        PrintWriter printWriter = new PrintWriter(os);
        List<Booking> included = new ArrayList<>();
        for(Booking booking:bookings){
            if(booking.getRoom().equals(room)){
                included.add(booking);
            }
        }
        if(included.isEmpty()){
            throw new RoomNotFoundException(room);
        }

        Comparator<Booking> byDate = Comparator.comparing(Booking::getDate);
        included.sort(byDate.reversed()
                .thenComparing(b -> b.getStart().toLocalTime())
                .thenComparing(Booking::getId));

        for(Booking booking:included){
            printWriter.println(booking);
        }
        printWriter.flush();
    }

    public void printStatistics(OutputStream os) {
        PrintWriter printWriter = new PrintWriter(os);
        if (bookings.isEmpty()) {
            printWriter.println("No bookings");
            printWriter.flush();
            return;
        }


        int lectures = 0;
        int exams = 0;
        int meetings = 0;
        Duration total = Duration.ZERO;
        Map<LocalDate, Duration> perDay = new TreeMap<>();

        for (Booking booking : bookings) {
            if (booking.getTypeCode() == 'L') {
                lectures++;
            } else if (booking.getTypeCode() == 'E') {
                exams++;
            } else {
                meetings++;
            }
            total = total.plus(booking.getDuration());
            perDay.merge(booking.getDate(), booking.getDuration(), Duration::plus);
        }


        Comparator<Booking> byDuration = Comparator.comparing(Booking::getDuration)
                .thenComparing(Comparator.naturalOrder());
        Booking shortest = Collections.min(bookings, byDuration);
        Booking longest = Collections.max(bookings, byDuration);

        double average = (double) total.toMinutes() / bookings.size();


        Map.Entry<LocalDate, Duration> busiest = null;
        for (Map.Entry<LocalDate, Duration> entry : perDay.entrySet()) {
            if (busiest == null || entry.getValue().compareTo(busiest.getValue()) > 0) {
                busiest = entry;
            }
        }

        printWriter.println(String.format("Total: %d (lectures: %d, exams: %d, meetings: %d)",
                bookings.size(), lectures, exams, meetings));
        printWriter.println("Total booked: " + Booking.formatDuration(total));
        printWriter.println("Shortest: " + shortest);
        printWriter.println("Longest: " + longest);
        printWriter.println(String.format(Locale.US, "Average duration: %.2f min", average));
        printWriter.println("Busiest day: " + busiest.getKey().format(Booking.DATE)
                + " (" + busiest.getKey().getDayOfWeek() + ") "
                + Booking.formatDuration(busiest.getValue()));
        printWriter.flush();
    }
}

public class BookingSystemTest {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder bookingLines = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null && !line.trim().equals("COMMANDS")) {
            bookingLines.append(line).append("\n");
        }

        BookingSystem system = new BookingSystem();
        int accepted = system.readBookings(
                new ByteArrayInputStream(bookingLines.toString().getBytes(StandardCharsets.UTF_8)), System.out);
        System.out.println("Accepted: " + accepted);
        System.out.println("Invalid: " + system.getInvalidLines());
        System.out.println("Conflicts: " + system.getConflicts());

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            System.out.println("=== " + line + " ===");
            switch (parts[0]) {
                case "SCHEDULE":
                    system.printSchedule(System.out);
                    break;
                case "DAY":
                    system.printDay(System.out, LocalDate.parse(parts[1], Booking.DATE));
                    break;
                case "ROOM":
                    try {
                        system.printRoom(System.out, parts[1]);
                    } catch (RoomNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "STATS":
                    system.printStatistics(System.out);
                    break;
                default:
                    System.out.println("Unknown command");
            }
        }
    }
}
