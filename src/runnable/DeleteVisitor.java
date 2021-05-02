package runnable;

import museum.Museum;
import museum.Ticket;

import java.util.Calendar;

public class DeleteVisitor implements Runnable {

    private Museum museum;
    private Ticket ticket;
    private Calendar timestamp;

    public DeleteVisitor(Museum museum, Ticket ticket, Calendar timestamp) {
        this.museum = museum;
        this.ticket = ticket;
        this.timestamp = timestamp;
    }

    @Override
    public void run() {
        museum.removeVisitor(timestamp, ticket);

        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
