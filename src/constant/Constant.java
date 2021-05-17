package constant;

import java.util.Arrays;
import java.util.List;

public final class Constant {

    // Maximum number of visitors / tickets to be sold per day.
    public static final int MAX_VISITOR_PER_DAY = 900;

    // Maximum number of visitors allowed in the museum at one time.
    public static final int MAX_VISITOR_IN_MUSEUM = 100;

    // Ticket ID template
    public static final String TICKET_ID_TEMPLATE = "T";

    // Turnstile Entrance and Exit
    public static final String SOUTH_ENTRANCE = "SE";
    public static final String NORTH_ENTRANCE = "NE";
    public static final String EAST_EXIT = "EE";
    public static final String WEST_EXIT = "WE";

    public static final String MUSEUM_START_TIME = "09:00";
    public static final String MUSEUM_END_TIME = "18:00";
    public static final String TICKET_COUNTER_END_TIME = "17:00";
    public static final String TICKET_COUNTER_START_TIME = "08:00";
    public static final String MUSEUM_LAST_ENTRY_TIME = "17:10";


    // ThreadPool
    public static final int THREAD_NUM = 6;

    // Turnstile Num
    public static final int TURNSTILE_NUM = 4;

    // Thread Time
    public static final int SLEEP_TIME = 150;

    // Turnstile Thread Time
    public static final int TURNSTILE_SLEEP_TIME = 150;

    // Entrance Wait Time
    public static final int Entrance_WAIT_TIME = 100;

    // Exit Wait Time
    public static final int EXIT_WAIT_TIME = 100;

    // Museum Wait Time
    public static final int MUSEUM_WAIT_TIME = 100;

    // Ticket counter table title
    public static final String[] TICKET_TABLE_TITLE = {"Time", "Ticket IDs", "No. of tickets sold"};

    // Museum table title
    public static final String[] MUSEUM_TABLE_TITLE = {"Time", "Ticket ID", "Stay time in minutes", "Leave time", "No. of people in museum"};

    // Entrance table title
    public static final String[] ENTRANCE_TABLE_TITLE = {"Time", "Entrance", "Ticket ID", "Stay time in minutes", "Leave time"};

    // Exit table title
    public static final String[] EXIT_TABLE_TITLE = {"Time", "Exit", "Ticket ID", "Stay time in minutes", "Leave time"};
}
