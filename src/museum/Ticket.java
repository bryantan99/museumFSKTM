package museum;

import constant.Constant;
import utilities.RandomizeUtils;

public class Ticket {

    private String ticketId;
    private int stayTimeInMinute;

    public Ticket() {
    }

    public Ticket(int id) {
        this.ticketId = Constant.TICKET_ID_TEMPLATE + String.format("%04d", id);
        this.stayTimeInMinute = RandomizeUtils.randomizeStayTimeInMuseum();
    }

    public String getTicketId() {
        return ticketId;
    }

    public int getStayTimeInMinute() {
        return stayTimeInMinute;
    }
}
