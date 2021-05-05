package turnstile;

import constant.Constant;
import museum.Museum;
import museum.Ticket;
import utilities.CalendarUtils;
import utilities.RandomizeUtils;

import java.util.Calendar;
import java.util.Queue;

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
            while (museum.getNEEntranceTurnstile().getQueue().size() >= Constant.TURNSTILE_NUM && museum.getSEEntranceTurnstile().getQueue().size() >= Constant.TURNSTILE_NUM) {
//                System.out.println(Thread.currentThread() + " is waiting... -- Entrance");
                wait(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ticket.updateLeaveTime(timestamp);
        Queue<Ticket> tempQueue = null;
        if(museum.getNEEntranceTurnstile().getQueue().size() >= Constant.TURNSTILE_NUM){
            museum.getSEEntranceTurnstile().getQueue().add(ticket);
            tempQueue = museum.getSEEntranceTurnstile().getQueue();
        }else if(museum.getSEEntranceTurnstile().getQueue().size() >= Constant.TURNSTILE_NUM){
            museum.getNEEntranceTurnstile().getQueue().add(ticket);
            tempQueue = museum.getNEEntranceTurnstile().getQueue();
        }else{
            this.queue.add(ticket);
            tempQueue = this.queue;
        }

        String turnstileId = turnstileType + RandomizeUtils.randomizeTurnstileId();
        String enterMsg = CalendarUtils.toHHmmString(timestamp) + " - " + ticket.getTicketId() + " tries to enter using Turnstile " + turnstileId + ".";
        System.out.printf("%-60s\n", enterMsg, museum.getTotalNumOfPeopleInMuseum());

    }

}
