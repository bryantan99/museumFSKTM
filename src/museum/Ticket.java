package museum;

import constant.Constant;
import utilities.RandomizeUtils;

public class Ticket {

    private String ticketId;
    private int stayTimeInMinute;

    public Ticket() {
    }

    public Ticket(int id) {
        this.ticketId = generateTicketId(id);
        this.stayTimeInMinute = RandomizeUtils.randomizeStayTimeInMuseum();
    }

    public String getTicketId() {
        return ticketId;
    }

    public int getStayTimeInMinute() {
        return stayTimeInMinute;
    }

    private String generateTicketId(int id) {
        StringBuilder ticketId = new StringBuilder(Constant.TICKET_ID_TEMPLATE);
        int numberOfZeroInFront = String.valueOf(Constant.MAX_NUMBER_OF_TICKET).length() - String.valueOf(id).length();
        ticketId.append("0".repeat(Math.max(0, numberOfZeroInFront)));
        ticketId.append(id);
        return ticketId.toString();
    }
}
