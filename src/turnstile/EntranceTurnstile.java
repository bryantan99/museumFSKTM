package turnstile;

import constant.Constant;
import gui.ManagerInterface;
import museum.Museum;
import museum.Ticket;
import utilities.CalendarUtils;
import utilities.RandomizeUtils;

import java.util.Calendar;
import java.util.Vector;

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
            String fullCapacityMsg = "Museum is full. Current capacity : " + museum.getVisitorList().size() + " / " + Constant.MAX_VISITOR_IN_MUSEUM;
            System.out.println(fullCapacityMsg);
            ManagerInterface.jTextArea.append(CalendarUtils.toHHmmString(timestamp)+" - "+ fullCapacityMsg + "\n");
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

        String tempTurnstileId = turnstileId + RandomizeUtils.randomizeTurnstileId();

        addTicketDataToEntraceTable(timestamp, ticket, tempTurnstileId);

        String enterMsg = ticket.getTicketId() + " tries to enter using Turnstile " + tempTurnstileId + ".";
        String enterMsgWithTimestamp = CalendarUtils.toHHmmString(timestamp) + " - " + enterMsg;

        addTicketDataToMuseumTable(timestamp, ticket, enterMsg);
        System.out.printf("%-60s\n", enterMsgWithTimestamp);
    }

    private void addTicketDataToEntraceTable(Calendar timestamp, Ticket ticket, String turnstileId) {
        Vector<String> ticketVector = new Vector<>();
        ticketVector.add(CalendarUtils.toHHmmString(timestamp));
        ticketVector.add(turnstileId);
        ticketVector.add(ticket.getTicketId());
        ticketVector.add(String.valueOf(ticket.getStayTimeInMinute()));
        ticketVector.add(CalendarUtils.toHHmmString(ticket.getLeaveTime()));
        if(this.turnstileId.equals(Constant.SOUTH_ENTRANCE)){
            ManagerInterface.southEntranceTable.getTableData().add(ticketVector);
            ManagerInterface.southEntranceTable.getTableModel().fireTableDataChanged();
        }else{
            ManagerInterface.northEntranceTable.getTableData().add(ticketVector);
            ManagerInterface.northEntranceTable.getTableModel().fireTableDataChanged();
        }
    }

    private void addTicketDataToMuseumTable(Calendar timestamp, Ticket ticket, String message) {
        Vector<String> ticketVector = new Vector<>();
        ticketVector.add(CalendarUtils.toHHmmString(timestamp));
        ticketVector.add(ticket.getTicketId());
        ticketVector.add(message);
        ticketVector.add(String.valueOf(museum.getTotalNumOfPeopleInMuseum()));
        ManagerInterface.museumTable.getTableData().add(ticketVector);
        ManagerInterface.museumTable.getTableModel().fireTableDataChanged();
    }

    private boolean judgeNumPeopleOfEntrance() {
        return museum.getNEEntranceTurnstile().getQueue().size() >= Constant.TURNSTILE_NUM && museum.getSEEntranceTurnstile().getQueue().size() >= Constant.TURNSTILE_NUM;
    }

}
