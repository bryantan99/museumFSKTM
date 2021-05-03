package turnstile;

import museum.Museum;
import museum.Ticket;
import utilities.CalendarUtils;
import utilities.EntanceUtils;

import java.util.Calendar;
import java.util.Queue;

public class ExitTurnstile extends Turnstile {

    public volatile Museum museum;

    public ExitTurnstile(String turnstileId) {
        super(turnstileId);
    }

    public synchronized void addExitVisitor(Calendar timestamp) {
        try{
            while (museum.getWEExitTurnstile().getQueue().size() == 0 && museum.getEEExitTurnstile().getQueue().size() == 0 && !museum.isProcessingRest()) {
                wait(100);
            }
            Ticket ticket = null;
            if(!museum.getEEExitTurnstile().getQueue().isEmpty() && !museum.getWEExitTurnstile().getQueue().isEmpty()){
                if (EntanceUtils.judgeWhichEntranceToGo()) {
                    ticket = museum.getEEExitTurnstile().getQueue().remove();
                } else {
                    ticket = museum.getWEExitTurnstile().getQueue().remove();
                }
            }else if(!museum.getEEExitTurnstile().getQueue().isEmpty()){
                ticket = museum.getEEExitTurnstile().getQueue().remove();
            }else if(!museum.getWEExitTurnstile().getQueue().isEmpty()) {
                ticket = museum.getWEExitTurnstile().getQueue().remove();
            }else{
                return;
            }


            int size = this.queue.size();
            String leaveMsg = CalendarUtils.toHHmmString(timestamp) + " - " + ticket.getTicketId() + " has left using " + "Turnstile " + turnstileId + size;
            System.out.printf("%-60s [No. of people in the " + turnstileId + " turnstile : %-3d] [No. of people in the museum : %-3d]\n", leaveMsg, size, museum.getTotalNumOfPeopleInMuseum());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
