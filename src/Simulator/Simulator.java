package Simulator;

import constant.Constant;
import museum.Museum;
import museum.Ticket;
import museum.TicketCounter;
import runnable.AddVisitor;
import runnable.DeleteVisitor;
import runnable.SellTicket;
import utilities.CalendarUtils;
import utilities.RandomizeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Calendar.MINUTE;

public class Simulator {
    private Museum museum;
    private TicketCounter counter;
    private List<Ticket> ticketPool;
    private Map<String, List<Ticket>> leaveScheduledEventMap;

    public Simulator() {
        this.museum = new Museum();
        this.counter = new TicketCounter();
        this.ticketPool = new ArrayList<>();
        this.leaveScheduledEventMap = new HashMap<>();
    }

    public void startSimulate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        Date startTimeDate = sdf.parse(Constant.TICKET_COUNTER_START_TIME);
        Date endTimeDate = sdf.parse(Constant.MUSEUM_END_TIME);

        Calendar currentTimeCal = Calendar.getInstance();
        Calendar endTimeCal = Calendar.getInstance();
        currentTimeCal.setTime(startTimeDate);
        endTimeCal.setTime(endTimeDate);

        counter.startOperate();
        Calendar nextSellTicketDateTime = Calendar.getInstance();
        nextSellTicketDateTime.setTime(startTimeDate);

        while (!currentTimeCal.after(endTimeCal)) {

            if (currentTimeCal.equals(CalendarUtils.parseTimeInHHmm(Constant.TICKET_COUNTER_END_TIME))) {
                counter.stopOperate(currentTimeCal);
            }

            if (nextSellTicketDateTime.equals(currentTimeCal) && counter.isOperating()) {
                boolean hasTicketSold = sellTicket(currentTimeCal);
                if (hasTicketSold) {
                    nextSellTicketDateTime.add(MINUTE, RandomizeUtils.randomizeGapBetweenTicketPurchases());
                }
            }

            if(!ticketPool.isEmpty()) {
                Ticket visitor = ticketPool.remove(0);
                enterMuseum(currentTimeCal, visitor);
                scheduleLeaveEvent(visitor);
            }

            if (leaveScheduledEventMap.containsKey(CalendarUtils.toHHmmString(currentTimeCal))) {
                List<Ticket> leavingVisitorList = leaveScheduledEventMap.get(CalendarUtils.toHHmmString(currentTimeCal));
                for (Ticket visitor : leavingVisitorList) {
                    leaveMuseum(currentTimeCal, visitor);
                }
            }

            currentTimeCal.add(MINUTE, 1);
        }

    }

    private void leaveMuseum(Calendar currentTimeCal, Ticket visitor) {
        DeleteVisitor deleteVisitor = new DeleteVisitor(museum, visitor, currentTimeCal);
        Thread t = new Thread(deleteVisitor);

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void scheduleLeaveEvent(Ticket visitor) {
        String leaveTime = CalendarUtils.toHHmmString(visitor.getLeaveTime());
        if (!leaveScheduledEventMap.containsKey(leaveTime)) {
            leaveScheduledEventMap.put(leaveTime, new ArrayList<>());
        }
        leaveScheduledEventMap.get(leaveTime).add(visitor);
    }

    private void enterMuseum(Calendar timestamp, Ticket visitor) {
        AddVisitor addVisitorRunnable = new AddVisitor(museum, visitor, timestamp);
        Thread t = new Thread(addVisitorRunnable);

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean sellTicket(Calendar sellTicketTime) {
        SellTicket sellTicketRunnable = new SellTicket(counter, RandomizeUtils.randomizeNumberOfTicketSold(), sellTicketTime);
        Thread sellTicketThread = new Thread(sellTicketRunnable);
        sellTicketThread.start();
        try {
            sellTicketThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (sellTicketRunnable.getTicketList() != null && !sellTicketRunnable.getTicketList().isEmpty()) {
            ticketPool.addAll(sellTicketRunnable.getTicketList());
            return true;
        }

        return false;
    }
}
