package ch.bakito.afp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marc on 25.01.2015.
 */
public class DateExtractor {

    private static final String URL = "http://www.olten.ch/de/gewerbe/umwelt/abfall/abfalldaten/welcome.php?kreis_id=19";

    public static void main(String... args) throws Exception {

        getEvents().forEach(System.err::println);
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
