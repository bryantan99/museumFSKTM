package utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class CalendarUtils {

    public static Calendar parseTimeInHHmm(String TimeInHHmm) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date dateObj = sdf.parse(TimeInHHmm);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateObj);
        return cal;
    }
}
