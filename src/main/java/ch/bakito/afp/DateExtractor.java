package ch.bakito.afp;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by marc on 25.01.2015.
 */
public class DateExtractor {

    private static final String URL = "http://www.olten.ch/de/gewerbe/umwelt/abfall/abfalldaten/welcome.php?kreis_id=19";

    public static void main(String... args) throws Exception {

        SimpleDateFormat fmt = new SimpleDateFormat("dd. MMM yyyy", Locale.GERMAN);


        String[] shortMonths = new String[]{
                "Jan.", "Feb.", "März", "Apr.", "Mai", "Juni",
                "July", "Aug.", "Sept.", "Okt.", "Nov.", "Dez."
        };

        DateFormatSymbols symbols = fmt.getDateFormatSymbols();

        symbols.setShortMonths(shortMonths);
        fmt.setDateFormatSymbols(symbols);


        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        UidGenerator ug = new UidGenerator("1");


        List<Event> events = getEvents();
        //events .forEach(System.err::println);
        events.forEach(e -> {
            try {

                java.util.Calendar cal = java.util.Calendar.getInstance();

                cal.setTime(fmt.parse(e.getDate()));
                cal.set(java.util.Calendar.HOUR_OF_DAY, 9);
                cal.set(java.util.Calendar.MINUTE, 0);
                cal.set(java.util.Calendar.SECOND, 0);


                // initialise as an all-day event..
                VEvent event = new VEvent(new Date(cal.getTime()), new Date(cal.getTime()), e.getInfo());


                // Generate a UID for the event..
                event.getProperties().add(ug.generateUid());

                calendar.getComponents().add(event);


            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });


        FileOutputStream fout = new FileOutputStream("mycalendar.ics");

        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(calendar, fout);

    }


    private static List<Event> getEvents() throws Exception {


        List<Event> events = new ArrayList<>();

        Document doc = Jsoup.connect(URL).get();
        Element body = doc.body();

        Element div = body.getElementById("contentboxsub");
        Elements tables = div.getElementsByTag("table");
        for (Element table : tables) {

            Elements rows = table.getElementsByTag("tr");

            if (rows.size() > 50) {

                for (Element row : rows) {
                    Elements cols = row.getElementsByTag("td");
                    if (cols.size() > 1) {
                        String info = cols.get(1).getAllElements().get(0).text();
                        if (info.indexOf("/") >= 0) {
                            info = info.substring(0, info.indexOf("/"));
                        }

                        events.add(new Event(cols.get(0).getElementsByTag("a").get(0).text(), info));
                    }
                }
            }
        }

        return events;
    }
}
