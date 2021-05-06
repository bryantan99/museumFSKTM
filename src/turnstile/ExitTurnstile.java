package turnstile;

import constant.Constant;
import museum.Museum;
import museum.Ticket;
import utilities.CalendarUtils;
import utilities.EntranceUtils;
import utilities.RandomizeUtils;

import java.util.Calendar;
import java.util.Queue;

public class ExitTurnstile extends Turnstile {

    public volatile Museum museum;

    public ExitTurnstile(String turnstileId) {
        super(turnstileId);
    }

    public synchronized void addExitVisitor(Calendar timestamp) {
        try{
            while (judgePeopleNumOfExit()) {
                wait(Constant.EXIT_WAIT_TIME);
            }
            ExitTurnstile tempExitTurnstile = null;
            Ticket ticket = null;
            Queue<Ticket> tempQueue = null;
            if(!museum.getEEExitTurnstile().getQueue().isEmpty() && !museum.getWEExitTurnstile().getQueue().isEmpty()){
                if (EntranceUtils.toSouthEntrance()) {
                    ticket = museum.getEEExitTurnstile().getQueue().remove();
                    tempQueue = museum.getEEExitTurnstile().getQueue();
                    tempExitTurnstile = museum.getEEExitTurnstile();
                } else {
                    ticket = museum.getWEExitTurnstile().getQueue().remove();
                    tempQueue = museum.getWEExitTurnstile().getQueue();
                    tempExitTurnstile = museum.getWEExitTurnstile();
                }
            }else if(!museum.getEEExitTurnstile().getQueue().isEmpty()){
                ticket = museum.getEEExitTurnstile().getQueue().remove();
                tempQueue = museum.getEEExitTurnstile().getQueue();
                tempExitTurnstile = museum.getEEExitTurnstile();
            }else if(!museum.getWEExitTurnstile().getQueue().isEmpty()) {
                ticket = museum.getWEExitTurnstile().getQueue().remove();
                tempQueue = museum.getWEExitTurnstile().getQueue();
                tempExitTurnstile = museum.getWEExitTurnstile();
            }else{
                return;
            }

            String turnstileId = tempExitTurnstile.turnstileId;
            String leaveMsg = CalendarUtils.toHHmmString(timestamp) + " - " + ticket.getTicketId() + " has left using Turnstile " + turnstileId + RandomizeUtils.randomizeTurnstileId() + ". ";
            System.out.printf("%-60s[No. of people in the museum : %-3d]\n", leaveMsg, museum.getTotalNumOfPeopleInMuseum());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private boolean judgePeopleNumOfExit() {
        return museum.getWEExitTurnstile().getQueue().size() == 0 && museum.getEEExitTurnstile().getQueue().size() == 0 && !museum.isProcessingRest();
    }

}
