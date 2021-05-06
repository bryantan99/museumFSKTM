package museum;

import constant.Constant;
import utilities.RandomizeUtils;

import java.util.Calendar;

public class Ticket {

    private String ticketId;
    private int stayTimeInMinute;
    private Calendar leaveTime;

    public Ticket(int id) {
        this.ticketId = Constant.TICKET_ID_TEMPLATE + String.format("%04d", id);
        this.stayTimeInMinute = RandomizeUtils.randomizeStayTimeInMuseum();
    }

    public String getTicketId() {
        return ticketId;
    }

    public Calendar getLeaveTime() {
        return leaveTime;
    }

    public int getStayTimeInMinute() {
        return stayTimeInMinute;
    }

    public void updateLeaveTime(Calendar enterTime) {
        leaveTime = Calendar.getInstance();
        leaveTime.setTime(enterTime.getTime());
        leaveTime.add(Calendar.MINUTE, stayTimeInMinute);
    }
}
