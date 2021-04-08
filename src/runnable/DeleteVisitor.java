package runnable;

import museum.Museum;
import museum.Ticket;

public class DeleteVisitor implements Runnable {

    private Museum museum;
    private Ticket ticket;

    @Override
    public void run() {
        if (museum.getNumberOfCurrentVisitor() <= 0) {
            return;
        }

        int newTotal = museum.getNumberOfCurrentVisitor() - 1;
        museum.setNumberOfCurrentVisitor(newTotal);
        System.out.println("Visitor with ticketId (" + ticket.getTicketId() + ") has left the museum. Current total number of visitors in the museum : " + museum.getNumberOfCurrentVisitor());
        Thread thread = Thread.currentThread();
        thread.notify();
    }

}
