package museum;

import constant.Constant;
import utilities.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

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
    }

    public void stopOperate(Calendar closingTime) {
        this.isOperating = false;
        String closeMsg = CalendarUtils.toHHmmString(closingTime) + " - Ticket counter is closed.";
        System.out.printf("%-60s[No. of tickets sold : %-3d]\n", closeMsg, numberOfTicketSold);
    }

    public synchronized List<Ticket> sellTicket(Calendar sellTime, int ticketAmount) {
        if (!isOperating()) {
            System.out.println("Ticket counter is closed.");
            return Collections.emptyList();
        }

        if (numberOfTicketSold >= Constant.MAX_VISITOR_PER_DAY) {
            System.out.println(numberOfTicketSold + " tickets has already been sold out.");
            stopOperate(sellTime);
            return Collections.emptyList();
        }

        int remainingNumberOfTicket = Constant.MAX_VISITOR_PER_DAY - numberOfTicketSold;
        if (ticketAmount > remainingNumberOfTicket) {
            System.out.println("Not enough tickets for " + ticketAmount + " person(s). Remaining ticket : " + remainingNumberOfTicket);
            return Collections.emptyList();
        }

        List<Ticket> soldTicketList = new ArrayList<>();
        for (int i = 0; i < ticketAmount; i++) {
            soldTicketList.add(new Ticket(numberOfTicketSold + 1));
            numberOfTicketSold++;
        }
        printSellTicket(sellTime, soldTicketList);
        return soldTicketList;
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

}
