package constant;

import java.awt.*;

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

    //  Important event timestamp
    public static final String MUSEUM_START_TIME = "09:00";
    public static final String MUSEUM_END_TIME = "18:00";
    public static final String TICKET_COUNTER_END_TIME = "17:00";
    public static final String TICKET_COUNTER_START_TIME = "08:00";
    public static final String MUSEUM_LAST_ENTRY_TIME = "17:10";

    //  ThreadPool size
    public static final int THREAD_NUM = 6;

    //  Turnstile Num
    public static final int TURNSTILE_NUM = 4;

    //  Thread Time
    public static final int SLEEP_TIME = 150;

    //  Turnstile Thread Time
    public static final int TURNSTILE_SLEEP_TIME = 150;

    //  Entrance Wait Time
    public static final int Entrance_WAIT_TIME = 100;

    //  Exit Wait Time
    public static final int EXIT_WAIT_TIME = 100;

    // Museum Wait Time
    public static final int MUSEUM_WAIT_TIME = 100;

    //  GUI constants
    public static final int WINDOW_WIDTH = 1366;
    public static final int WINDOW_HEIGHT = 768;
    public static final Color WINDOW_BACKGROUND_COLOUR = new Color(203, 220, 217);

    public static final String WINDOW_TITLE = "FSKTM Museum's Information";
    public static final String BUTTON_START = "Start Simulate";
    public static final String BUTTON_SIMULATING = "Simulating...";
    public static final String LEFT_PANEL_MUSEUM = "Museum";
    public static final String LEFT_PANEL_TICKET_COUNTER = "Ticket Counter";
    public static final String LEFT_PANEL_VISITORS_ENTRANCE_EXIT_LOGS = "Visitors Entrance / Exit Logs";
    public static final String LEFT_PANEL_VISITORS_AT_SOUTH_ENTRANCE = "Visitors at South Entrance";
    public static final String LEFT_PANEL_VISITORS_AT_NORTH_ENTRANCE = "Visitors at North Entrance";
    public static final String LEFT_PANEL_VISITORS_AT_EAST_EXIT = "Visitors at East Exit";
    public static final String LEFT_PANEL_VISITORS_AT_WEST_EXIT = "Visitors at West Exit";

    // Ticket counter table title
    public static final String[] TICKET_TABLE_TITLE = {"Time", "Ticket ID", "Total No. of Tickets Sold"};

    // Museum table title
    public static final String[] MUSEUM_TABLE_TITLE = {"Time", "Ticket ID", "Description", "Current Total No. of Visitors In Museum"};

    // Entrance table title
    public static final String[] ENTRANCE_TABLE_TITLE = {"Time", "Entrance", "Ticket ID", "Duration of Stay (minute)", "Expected Leave time"};

    // Exit table title
    public static final String[] EXIT_TABLE_TITLE = {"Visitor Left Museum's Time", "Exit", "Ticket ID", "Duration of Stay (minute)", "Visitor Arrived At Exit Turnstile's Time"};
}
