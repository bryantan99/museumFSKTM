package turnstile;

import constant.Constant;
import gui.ManagerInterface;
import museum.Museum;
import museum.Ticket;
import utilities.CalendarUtils;
import utilities.EntranceUtils;
import utilities.RandomizeUtils;

import java.util.Calendar;
import java.util.Queue;
import java.util.Vector;

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
            if(!museum.getEEExitTurnstile().getQueue().isEmpty() && !museum.getWEExitTurnstile().getQueue().isEmpty()){
                if (EntranceUtils.toSouthEntrance()) {
                    ticket = museum.getEEExitTurnstile().getQueue().remove();
                    tempExitTurnstile = museum.getEEExitTurnstile();
                } else {
                    ticket = museum.getWEExitTurnstile().getQueue().remove();
                    tempExitTurnstile = museum.getWEExitTurnstile();
                }
            }else if(!museum.getEEExitTurnstile().getQueue().isEmpty()){
                ticket = museum.getEEExitTurnstile().getQueue().remove();
                tempExitTurnstile = museum.getEEExitTurnstile();
            }else if(!museum.getWEExitTurnstile().getQueue().isEmpty()) {
                ticket = museum.getWEExitTurnstile().getQueue().remove();
                tempExitTurnstile = museum.getWEExitTurnstile();
            }else{
                return;
            }

            String turnstileId = tempExitTurnstile.turnstileId;

            addTicketDataToExitTable(timestamp, ticket, turnstileId);

            String leaveMsg = CalendarUtils.toHHmmString(timestamp) + " - " + ticket.getTicketId() + " has left using Turnstile " + turnstileId + ". ";
            System.out.printf("%-60s[No. of people in the museum : %-3d]\n", leaveMsg, museum.getTotalNumOfPeopleInMuseum());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void addTicketDataToExitTable(Calendar timestamp, Ticket ticket, String turnstileId) {
        Vector<String> ticketVector = new Vector<>();
        ticketVector.add(CalendarUtils.toHHmmString(timestamp));
        ticketVector.add(turnstileId);
        ticketVector.add(ticket.getTicketId());
        ticketVector.add(String.valueOf(ticket.getStayTimeInMinute()));
        ticketVector.add(CalendarUtils.toHHmmString(ticket.getLeaveTime()));
        if(turnstileId.substring(0, 2).equals(Constant.EAST_EXIT)){
            ManagerInterface.eastExitTable.getTableData().add(ticketVector);
            ManagerInterface.eastExitTable.getTableModel().fireTableDataChanged();
        }else{
            ManagerInterface.westExitTable.getTableData().add(ticketVector);
            ManagerInterface.westExitTable.getTableModel().fireTableDataChanged();
        }
    }

    private boolean judgePeopleNumOfExit() {
        return museum.getWEExitTurnstile().getQueue().size() == 0 && museum.getEEExitTurnstile().getQueue().size() == 0 && !museum.isProcessingRest();
    }

}
