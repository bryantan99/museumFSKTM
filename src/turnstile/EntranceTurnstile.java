package turnstile;

import constant.Constant;
import museum.Museum;
import museum.Ticket;
import utilities.CalendarUtils;
import utilities.RandomizeUtils;

import java.util.Calendar;

public class EntranceTurnstile extends Turnstile {

    public volatile Museum museum;

    public EntranceTurnstile(String turnstileId) {
        super(turnstileId);
    }

    public synchronized void addVisitor(Calendar timestamp, Ticket ticket) {

        if (!museum.isOpen()) {
            System.out.println("Museum is not operating.");
            return;
        }

        if (museum.getVisitorList().size() >= Constant.MAX_VISITOR_IN_MUSEUM) {
            System.out.println("Museum is full. Current capacity : " + museum.getVisitorList().size() + " / " + Constant.MAX_VISITOR_IN_MUSEUM);
            return;
        }

        try {
            while (judgeNumPeopleOfEntrance()) {
                wait(Constant.Entrance_WAIT_TIME);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ticket.updateLeaveTime(timestamp);
            this.queue.add(ticket);

        String enterMsg = CalendarUtils.toHHmmString(timestamp) + " - " + ticket.getTicketId() + " tries to enter using Turnstile " + turnstileId + RandomizeUtils.randomizeTurnstileId() + ".";
        System.out.printf("%-60s [No. of people in the  " + turnstileId + " turnstile : %-3d]\n", enterMsg, queue.size());
    }

    private boolean judgeNumPeopleOfEntrance() {
        return museum.getNEEntranceTurnstile().getQueue().size() >= Constant.TURNSTILE_NUM && museum.getSEEntranceTurnstile().getQueue().size() >= Constant.TURNSTILE_NUM;
    }

}
