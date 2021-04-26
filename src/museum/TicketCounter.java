package museum;

import constant.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketCounter {

    private boolean isOperating;
    private int numberOfTicketSold;

    public TicketCounter() {
        this.isOperating = false;
        this.numberOfTicketSold = 0;
    }

    public boolean isOperating() {
        return isOperating;
    }

    public void startOperate() {
        this.isOperating = true;
    }

    public void stopOperate() {
        this.isOperating = false;
        System.out.println("Ticket counter is now closing...\nNumber of ticket(s) sold    : " + numberOfTicketSold);
    }

    public synchronized List<Ticket> sellTicket(int ticketAmount) {
        if (!isOperating()) {
            System.out.println("Ticket counter is closed.");
            return Collections.emptyList();
        }

        if (numberOfTicketSold >= Constant.MAX_VISITOR_PER_DAY) {
            System.out.println(numberOfTicketSold + " tickets has already been sold out.");
            stopOperate();
            return Collections.emptyList();
        }

        int remainingNumberOfTicket = Constant.MAX_VISITOR_PER_DAY - numberOfTicketSold;
        if (ticketAmount > remainingNumberOfTicket) {
            System.out.println("Not enough tickets for " + ticketAmount + " person(s). Remaining ticket : " + remainingNumberOfTicket);
            return Collections.emptyList();
        }

        List<Ticket> soldTicketList = new ArrayList<>();
        for (int i = 0; i < ticketAmount; i++) {
            soldTicketList.add(new Ticket(numberOfTicketSold + 1));
            numberOfTicketSold++;
        }
        return soldTicketList;
    }

}
