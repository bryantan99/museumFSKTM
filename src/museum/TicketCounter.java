package museum;

import constant.Constant;
import java.util.LinkedList;
import java.util.Queue;

public class TicketCounter {

    private boolean isOperating;
    private Queue<Ticket> ticketQueue;

    public TicketCounter() {
        this.isOperating = false;
        this.ticketQueue = new LinkedList<Ticket>();
    }

    public Queue<Ticket> getTicketQueue() {
        return ticketQueue;
    }

    public void setTicketQueue(Queue<Ticket> ticketQueue) {
        this.ticketQueue = ticketQueue;
    }

    public boolean getIsOperating() {
        return isOperating;
    }

    public void setIsOperating(boolean isOperating) {
        this.isOperating = isOperating;
    }

    public int getNumberOfRemainingTicket() {
        return this.ticketQueue.size();
    }

    public void startOperate() {
        this.isOperating = true;
        this.initTicketQueue();
    }

    public void stopOperate() {
        this.isOperating = false;
        int numberOfTicketSold = Constant.MAX_VISITOR_PER_DAY - this.ticketQueue.size();
        System.out.println("Ticket counter is now closing...\nNumber of ticket(s) sold    : " + numberOfTicketSold);
    }

    public synchronized void sellTicket(int ticketAmount) {
        if (!isOperating) {
            System.out.println("Ticket counter is not operating.");
            return;
        }

        if (ticketQueue.size() <= 0) {
            System.out.println("Tickets had been sold out. Remaining ticket : " + ticketQueue.size());
            stopOperate();
            return;
        }

        if (ticketAmount > ticketQueue.size()) {
            System.out.println("Not enough tickets for " + ticketAmount + " person(s). Remaining ticket : " + ticketQueue.size());
            return;
        }

        for (int i = 0; i < ticketAmount; i++) {
            Ticket ticketSold = ticketQueue.remove();
            System.out.println(ticketSold.getTicketId() + " sold.");
        }
        System.out.println("\n\n");

    }

    private void initTicketQueue() {
        this.ticketQueue.clear();
        for (int i = 1; i <= Constant.MAX_VISITOR_PER_DAY; i++) {
            String ticketId = Constant.TICKET_ID_TEMPLATE + i;
            this.ticketQueue.add(new Ticket(ticketId));
        }
        System.out.println(ticketQueue.size() + " ticket(s) are ready.");
    }

}
