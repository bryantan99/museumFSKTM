package runnable;

import museum.TicketCounter;

public class SellTicket implements Runnable {

    private TicketCounter counter;
    private int ticketAmount;

    public SellTicket(TicketCounter counter, int ticketAmount) {
        this.counter = counter;
        this.ticketAmount = ticketAmount;
    }

    @Override
    public void run() {
        counter.sellTicket(ticketAmount);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

}
