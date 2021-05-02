package Simulator;

import constant.Constant;
import museum.Museum;
import museum.Ticket;
import museum.TicketCounter;
import utilities.CalendarUtils;
import utilities.RandomizeUtils;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Simulator {

    private volatile Calendar currentTime;
    private volatile Calendar nextTicketSellTime;

    private ExecutorService es;
    private Museum museum;
    private TicketCounter counter;

    private List<Ticket> ticketPool;
    private Map<String, List<Ticket>> ticketLeavingTimeMap;

    private final Calendar TICKET_COUNTER_END_TIME = CalendarUtils.parseTimeInHHmm(Constant.TICKET_COUNTER_END_TIME);
    private final Calendar TICKET_COUNTER_START_TIME = CalendarUtils.parseTimeInHHmm(Constant.TICKET_COUNTER_START_TIME);
    private final Calendar MUSEUM_END_TIME = CalendarUtils.parseTimeInHHmm(Constant.MUSEUM_END_TIME);
    private final Calendar MUSEUM_START_TIME = CalendarUtils.parseTimeInHHmm(Constant.MUSEUM_START_TIME);
    private final Calendar MUSEUM_LAST_ENTRY_TIME = CalendarUtils.parseTimeInHHmm(Constant.MUSEUM_LAST_ENTRY_TIME);


    public Simulator() throws ParseException {
        this.es = Executors.newFixedThreadPool(4);
        this.currentTime = CalendarUtils.parseTimeInHHmm(Constant.TICKET_COUNTER_START_TIME);
        this.nextTicketSellTime = CalendarUtils.parseTimeInHHmm(Constant.TICKET_COUNTER_START_TIME);
        this.museum = new Museum();
        this.counter = new TicketCounter();
        this.ticketPool = new ArrayList<>();
        this.ticketLeavingTimeMap = new HashMap<>();
    }

    public void startSimulate() {
        es.submit(new Thread(new IncrementTime()));
        es.submit(new Thread(new SellTicket()));
        es.submit(new Thread(new EnterMuseum()));
        es.submit(new Thread(new ExitMuseum()));
        es.shutdown();
    }

    private class IncrementTime implements Runnable {
        @Override
        public void run() {
            Calendar localCurrentTime = currentTime;
            while (localCurrentTime.before(MUSEUM_END_TIME) || localCurrentTime.equals(MUSEUM_END_TIME) || !museum.getVisitorList().isEmpty()) {
                if (localCurrentTime.equals(TICKET_COUNTER_START_TIME)) {
                    counter.startOperate();
                } else if (localCurrentTime.equals(TICKET_COUNTER_END_TIME)) {
                    counter.stopOperate(localCurrentTime);
                } else if (localCurrentTime.equals(MUSEUM_START_TIME)) {
                    museum.startBusiness();
                } else if (localCurrentTime.equals(MUSEUM_END_TIME)) {
                    museum.endBusiness();
                } else if (localCurrentTime.equals(MUSEUM_LAST_ENTRY_TIME)) {
                    museum.offEntranceTurnstile(localCurrentTime);
                }

                currentTime.add(Calendar.MINUTE, 1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class SellTicket implements Runnable {
        @Override
        public void run() {
            Calendar localCurrentTime = currentTime;
            Calendar localNextSellTicketTime = nextTicketSellTime;

            while (counter.isOperating()) {
                if (!localCurrentTime.equals(currentTime)) {
                    System.out.println("Current time has changed");
                    localCurrentTime = currentTime;
                }

                if (!localNextSellTicketTime.equals(nextTicketSellTime)) {
                    System.out.println("Next ticket sell time changed.");
                    localNextSellTicketTime = nextTicketSellTime;
                }

                if (localCurrentTime.equals(TICKET_COUNTER_END_TIME) || localCurrentTime.after(TICKET_COUNTER_END_TIME)) {
                    counter.stopOperate(localCurrentTime);
                    continue;
                }

                if (localCurrentTime.equals(nextTicketSellTime) || localCurrentTime.after(nextTicketSellTime)) {
                    List<Ticket> purchasedTicketList = counter.sellTicket(localCurrentTime, RandomizeUtils.randomizeNumberOfTicketSold());
                    if (purchasedTicketList != null && !purchasedTicketList.isEmpty()) {
                        ticketPool.addAll(purchasedTicketList);
                    }
                    nextTicketSellTime.add(Calendar.MINUTE, RandomizeUtils.randomizeGapBetweenTicketPurchases());
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private class EnterMuseum implements Runnable {
        @Override
        public void run() {
            Calendar localCurrentTime = currentTime;
            Calendar localEndTime = MUSEUM_END_TIME;

            while (!localCurrentTime.after(localEndTime)) {
                if (!localCurrentTime.equals(currentTime)) {
                    System.out.println("Current time has changed.");
                    localCurrentTime = currentTime;
                }

                if (museum.isOpen() && !ticketPool.isEmpty() && museum.getVisitorList().size() < Constant.MAX_VISITOR_IN_MUSEUM && (localCurrentTime.before(MUSEUM_LAST_ENTRY_TIME) || localCurrentTime.equals(MUSEUM_LAST_ENTRY_TIME))) {
                    Ticket nextVisitor = ticketPool.remove(0);
                    museum.addVisitor(localCurrentTime, nextVisitor);
                    scheduledTicketLeaving(nextVisitor);
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void scheduledTicketLeaving(Ticket ticket) {
        String leaveTimeKey = CalendarUtils.toHHmmString(ticket.getLeaveTime());
        if (!ticketLeavingTimeMap.containsKey(leaveTimeKey)) {
            ticketLeavingTimeMap.put(leaveTimeKey, new ArrayList<>());
        }
        ticketLeavingTimeMap.get(leaveTimeKey).add(ticket);
    }

    private class ExitMuseum implements Runnable {
        @Override
        public void run() {

            Calendar localCurrentTime = currentTime;
            Calendar localEndTime = MUSEUM_END_TIME;

            while (!localCurrentTime.after(localEndTime) || !museum.getVisitorList().isEmpty()) {
                if (!localCurrentTime.equals(currentTime)) {
                    System.out.println("Current time has changed.");
                    localCurrentTime = currentTime;
                }

                if (!localCurrentTime.after(localEndTime)) {
                    String localCurrentTimeString = CalendarUtils.toHHmmString(localCurrentTime);
                    if (!museum.getVisitorList().isEmpty() && ticketLeavingTimeMap.containsKey(localCurrentTimeString)) {
                        List<Ticket> list = ticketLeavingTimeMap.get(localCurrentTimeString);
                        for (Ticket t : list) {
                            museum.removeVisitor(localCurrentTime, t);
                        }
                    }
                } else {
                    for (Map.Entry<String, List<Ticket>> entry : ticketLeavingTimeMap.entrySet()) {
                        List<Ticket> list = entry.getValue();
                        for (Ticket t : list) {
                            museum.removeVisitor(localCurrentTime, t);
                        }
                    }

                }



                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
