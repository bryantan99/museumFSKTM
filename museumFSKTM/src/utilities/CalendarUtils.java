package utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.MINUTE;

public final class CalendarUtils {

    public static Calendar parseTimeInHHmm(String TimeInHHmm) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date dateObj = sdf.parse(TimeInHHmm);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateObj);
        return cal;
    }

    public static String toHHmmString(Calendar time) {
        return String.format("%02d:%02d", time.get(Calendar.HOUR_OF_DAY), time.get(MINUTE));
    }
}
