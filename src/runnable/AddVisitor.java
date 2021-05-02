package runnable;

import museum.Museum;
import museum.Ticket;

import java.util.Calendar;

public class AddVisitor implements Runnable {

    private final Museum museum;
    private final Ticket ticket;
    private Calendar enterTimestamp;

    public AddVisitor(Museum museum, Ticket ticket, Calendar enterTimestamp) {
        this.museum = museum;
        this.ticket = ticket;
        this.enterTimestamp = enterTimestamp;
    }

    @Override
    public void run() {
        museum.addVisitor(enterTimestamp, ticket);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
