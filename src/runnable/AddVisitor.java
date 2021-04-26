package runnable;

import constant.Constant;
import museum.Museum;
import museum.Ticket;

public class AddVisitor implements Runnable {

    private final Museum museum;
    private final Ticket ticket;

    public AddVisitor(Museum museum, Ticket ticket) {
        this.museum = museum;
        this.ticket = ticket;
    }

    @Override
    public void run() {
        Thread incomingVisitorThread = Thread.currentThread();
        try {
            if (museum.getNumberOfCurrentVisitor() >= Constant.MAX_VISITOR_IN_MUSEUM) {
                incomingVisitorThread.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int newTotal = museum.getNumberOfCurrentVisitor() + 1;
        museum.setNumberOfCurrentVisitor(newTotal);
        System.out.println("Visitor with ticketId (" + ticket.getTicketId() + ") has entered the museum. Current total number of visitors in the museum : " + museum.getNumberOfCurrentVisitor());
    }

}
