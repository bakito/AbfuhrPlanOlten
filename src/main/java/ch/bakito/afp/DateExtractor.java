package ch.bakito.afp;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by marc on 25.01.2015.
 */
public class DateExtractor {

    private static final String URL = "http://www.olten.ch/de/gewerbe/umwelt/abfall/abfalldaten/welcome.php?kreis_id=19";

    public static void main(String... args) throws Exception {
        URL url = new URL(URL);

        String content;
        try (InputStream is = url.openStream()) {
            content = IOUtils.toString(is);
        }

        System.err.println(content);
    }
}
