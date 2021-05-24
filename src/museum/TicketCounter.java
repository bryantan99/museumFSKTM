package museum;

import constant.Constant;
import gui.ManagerInterface;
import utilities.CalendarUtils;

import java.util.*;

public class TicketCounter {

    private boolean isOperating;
    private int numberOfTicketSold;

    public TicketCounter() {
        this.isOperating = false;
        this.numberOfTicketSold = 0;
    }

    public boolean isOperating() {
        return isOperating;
    }

    public void startOperate(Calendar timestamp) {
        this.isOperating = true;
        String openMsg = CalendarUtils.toHHmmString(timestamp) + " - Ticket counter is now opened.";
        System.out.printf("%-60s\n", openMsg);
        ManagerInterface.jTextArea.append(openMsg+"\n");
    }

    public void stopOperate(Calendar closingTime) {
        this.isOperating = false;
        String closeMsg = CalendarUtils.toHHmmString(closingTime) + " - Ticket counter is closed.";
        System.out.printf("%-60s[No. of tickets sold : %-3d]\n", closeMsg, numberOfTicketSold);
        ManagerInterface.jTextArea.append(closeMsg+"\n");
    }

    public synchronized List<Ticket> sellTicket(Calendar sellTime, int ticketAmount) {
        if (!isOperating()) {
            String ticketCounterClosedMsg = "Ticket counter is closed.";
            System.out.println(ticketCounterClosedMsg);
            ManagerInterface.jTextArea.append(ticketCounterClosedMsg+"\n");
            return Collections.emptyList();
        }

        if (numberOfTicketSold >= Constant.MAX_VISITOR_PER_DAY) {
            String ticketSoldOutMsg = numberOfTicketSold + " tickets has already been sold out.";
            System.out.println(ticketSoldOutMsg);
            stopOperate(sellTime);
            ManagerInterface.jTextArea.append(sellTime+ " - "+ticketSoldOutMsg+"\n");
            return Collections.emptyList();
        }

        int remainingNumberOfTicket = Constant.MAX_VISITOR_PER_DAY - numberOfTicketSold;
        if (ticketAmount > remainingNumberOfTicket) {
            String notEnoughTicketMsg = "Not enough tickets for " + ticketAmount + " person(s). Remaining ticket : " + remainingNumberOfTicket;
            System.out.println(notEnoughTicketMsg);
            ManagerInterface.jTextArea.append(sellTime+ " - "+ notEnoughTicketMsg+"\n");
            return Collections.emptyList();
        }

        List<Ticket> soldTicketList = new ArrayList<>();
        for (int i = 0; i < ticketAmount; i++) {
            Ticket soldTicket = new Ticket(numberOfTicketSold + 1);
            soldTicketList.add(soldTicket);
            numberOfTicketSold++;
            emitTableDataChanged(sellTime, soldTicket);
        }
        printSellTicket(sellTime, soldTicketList);
        return soldTicketList;
    }

    private void emitTableDataChanged(Calendar currentTime, Ticket ticket) {
        Vector<String> ticketVector = new Vector<>();
        ticketVector.add(CalendarUtils.toHHmmString(currentTime));
        ticketVector.add(ticket.getTicketId());
        ticketVector.add(String.valueOf(numberOfTicketSold));
        ManagerInterface.ticketCounterTable.getTableData().add(ticketVector);
        ManagerInterface.ticketCounterTable.getTableModel().fireTableDataChanged();
    }

    private void printSellTicket(Calendar timestamp, List<Ticket> soldTicketList) {
        if (soldTicketList != null && !soldTicketList.isEmpty()) {
            StringBuilder ticketIds = new StringBuilder();
            for (int i = 0; i < soldTicketList.size(); i++) {
                ticketIds.append(soldTicketList.get(i).getTicketId());
                if (i != soldTicketList.size() - 1) {
                    ticketIds.append(", ");
                }
            }
            System.out.println(CalendarUtils.toHHmmString(timestamp) + " - " + ticketIds + " sold.");
        }
    }

    public int getNumberOfTicketSold() {
        return numberOfTicketSold;
    }
}
