package constant;

import java.util.Arrays;
import java.util.List;

public final class Constant {

    public static final int MAX_NUMBER_OF_TICKET = 9999;

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
    public static final List<String> ENTRANCE_TURNSTILE_LIST = Arrays.asList(SOUTH_ENTRANCE, NORTH_ENTRANCE);
    public static final List<String> EXIT_TURNSTILE_LIST = Arrays.asList(EAST_EXIT, WEST_EXIT);

    public static final String MUSEUM_START_TIME = "09:00";
    public static final String MUSEUM_END_TIME = "18:00";
    public static final String TICKET_COUNTER_END_TIME = "17:00";
    public static final String TICKET_COUNTER_START_TIME = "08:00";
    public static final String MUSEUM_LAST_ENTRY_TIME = "17:10";
}
