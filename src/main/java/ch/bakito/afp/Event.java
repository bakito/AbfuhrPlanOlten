package ch.bakito.afp;

/**
 * Created by marc on 25.01.2015.
 */
public class Event {
    private final String date;
    private final String info;


    public Event(String date, String info) {
        this.date = date;
        this.info = info;
    }

    @Override
    public String toString() {
        return "Event: " + date + " / " + info;
    }
}
