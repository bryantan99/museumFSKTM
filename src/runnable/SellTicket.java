package runnable;

import museum.Ticket;
import museum.TicketCounter;
import utilities.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SellTicket implements Runnable {

    private final TicketCounter counter;
    private final int ticketAmount;
    private List<Ticket> ticketList;
    private Calendar sellTicketTime;

    public SellTicket(TicketCounter counter, int ticketAmount, Calendar sellTicketTime) {
        this.counter = counter;
        this.ticketAmount = ticketAmount;
        this.sellTicketTime = sellTicketTime;
        this.ticketList = new ArrayList<>();
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    @Override
    public void run() {
        ticketList = counter.sellTicket(sellTicketTime, ticketAmount);

        if (ticketList != null && !ticketList.isEmpty()) {
            printSellTicketStmt();
        }

        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printSellTicketStmt() {
        String ticketIds = "";
        for (int i = 0; i < ticketList.size(); i++) {
            ticketIds += ticketList.get(i).getTicketId();
            if (i != ticketList.size() - 1) {
                ticketIds += ", ";
            }
        }
        System.out.println(CalendarUtils.toHHmmString(sellTicketTime) + " : " + ticketIds + " sold.");
    }

}
