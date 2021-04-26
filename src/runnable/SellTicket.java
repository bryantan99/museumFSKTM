package runnable;

import museum.Ticket;
import museum.TicketCounter;

import java.util.ArrayList;
import java.util.List;

public class SellTicket implements Runnable {

    private final TicketCounter counter;
    private final int ticketAmount;
    private List<Ticket> ticketList;

    public SellTicket(TicketCounter counter, int ticketAmount) {
        this.counter = counter;
        this.ticketAmount = ticketAmount;
        this.ticketList = new ArrayList<>();
    }

    @Override
    public void run() {
        ticketList = counter.sellTicket(ticketAmount);

        if (ticketList != null && ticketList.size() != 0) {
            printStatement(ticketList);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printStatement(List<Ticket> ticketList) {
        StringBuilder ticketIds = new StringBuilder();
        for (int i = 0; i < ticketList.size(); i++) {
            Ticket t = ticketList.get(i);
            ticketIds.append(t.getTicketId()).append("[").append(t.getStayTimeInMinute()).append("]");
            if (i != ticketList.size() - 1) {
                ticketIds.append(", ");
            }
        }
        System.out.println(ticketIds + " sold.");
    }

}
