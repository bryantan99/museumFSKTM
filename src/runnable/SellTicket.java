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

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    @Override
    public void run() {
        ticketList = counter.sellTicket(ticketAmount);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
