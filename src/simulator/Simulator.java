package simulator;

import constant.Constant;
import gui.ManagerInterface;
import museum.Museum;
import museum.Ticket;
import museum.TicketCounter;
import utilities.CalendarUtils;
import utilities.ExitUtils;
import utilities.RandomizeUtils;
import utilities.EntranceUtils;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Simulator {
    public static volatile Calendar currentTime;
    public static volatile Calendar nextTicketSellTime;

    private ExecutorService es;
    private volatile Museum museum;
    private volatile TicketCounter counter;

    private volatile List<Ticket> turnStilePool;
    private volatile Map<String, List<Ticket>> ticketLeavingTimeMap;

    private final Calendar TICKET_COUNTER_END_TIME = CalendarUtils.parseTimeInHHmm(Constant.TICKET_COUNTER_END_TIME);
    private final Calendar TICKET_COUNTER_START_TIME = CalendarUtils.parseTimeInHHmm(Constant.TICKET_COUNTER_START_TIME);
    private final Calendar MUSEUM_END_TIME = CalendarUtils.parseTimeInHHmm(Constant.MUSEUM_END_TIME);
    private final Calendar MUSEUM_START_TIME = CalendarUtils.parseTimeInHHmm(Constant.MUSEUM_START_TIME);
    private final Calendar MUSEUM_LAST_ENTRY_TIME = CalendarUtils.parseTimeInHHmm(Constant.MUSEUM_LAST_ENTRY_TIME);


    public Simulator() throws ParseException {
        this.es = Executors.newFixedThreadPool(Constant.THREAD_NUM);
        this.currentTime = CalendarUtils.parseTimeInHHmm(Constant.TICKET_COUNTER_START_TIME);
        this.nextTicketSellTime = CalendarUtils.parseTimeInHHmm(Constant.TICKET_COUNTER_START_TIME);
        this.museum = new Museum();
        this.counter = new TicketCounter();
        this.turnStilePool = new ArrayList<>();
        this.ticketLeavingTimeMap = new Hashtable<>();
    }

    public void startSimulate() {
        es.submit(new Thread(new IncrementTime()));
        es.submit(new Thread(new SellTicket()));
        es.submit(new Thread(new EnterMuseum()));
        es.submit(new Thread(new EnterTurnstile()));
        es.submit(new Thread(new ExitMuseum()));
        es.submit(new Thread(new ExitTurnstile()));
        es.shutdown();
    }

    private class IncrementTime implements Runnable {
        @Override
        public void run() {
            Calendar localCurrentTime = currentTime;
            while (incrementCondition(localCurrentTime)) {
                if (localCurrentTime.equals(TICKET_COUNTER_START_TIME)) {
                    counter.startOperate(localCurrentTime);
                } else if (localCurrentTime.equals(TICKET_COUNTER_END_TIME)) {
                    counter.stopOperate(localCurrentTime);
                } else if (localCurrentTime.equals(MUSEUM_START_TIME)) {
                    museum.startBusiness(localCurrentTime);
                } else if (localCurrentTime.equals(MUSEUM_END_TIME)) {
                    museum.endBusiness(localCurrentTime);
                } else if (localCurrentTime.equals(MUSEUM_LAST_ENTRY_TIME)) {
                    museum.offEntranceTurnstile(localCurrentTime);
                }

                currentTime.add(Calendar.MINUTE, 1);
                try {
                    Thread.sleep(Constant.SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean incrementCondition(Calendar localCurrentTime) {
        return localCurrentTime.before(MUSEUM_END_TIME) || localCurrentTime.equals(MUSEUM_END_TIME) || !museum.getVisitorList().isEmpty();
    }

    private class SellTicket implements Runnable {
        @Override
        public void run() {
            Calendar localCurrentTime = currentTime;
            Calendar localNextSellTicketTime = nextTicketSellTime;

            while (localCurrentTime.before(TICKET_COUNTER_END_TIME)) {
                if (!localCurrentTime.equals(currentTime)) {
                    System.out.println("Current time has changed");
                    localCurrentTime = currentTime;
                }

                if (!localNextSellTicketTime.equals(nextTicketSellTime)) {
                    System.out.println("Next ticket sell time changed.");
                    localNextSellTicketTime = nextTicketSellTime;
                }

                if ((localCurrentTime.equals(nextTicketSellTime) || localCurrentTime.after(nextTicketSellTime)) && counter.isOperating()) {
                    List<Ticket> purchasedTicketList = counter.sellTicket(localCurrentTime, RandomizeUtils.randomizeNumberOfTicketSold());
                    if (purchasedTicketList != null && !purchasedTicketList.isEmpty()) {
                        turnStilePool.addAll(purchasedTicketList);
                    }
                    nextTicketSellTime.add(Calendar.MINUTE, RandomizeUtils.randomizeGapBetweenTicketPurchases());
                }

                try {
                    Thread.sleep(Constant.SLEEP_TIME);
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

                if (enterMuseumCondition(localCurrentTime)) {
                    Ticket nextVisitor = null;

                    if(!museum.getSEEntranceTurnstile().getQueue().isEmpty() && !museum.getNEEntranceTurnstile().getQueue().isEmpty()){
                        if (EntranceUtils.toSouthEntrance()) {
                            nextVisitor = museum.getSEEntranceTurnstile().getQueue().remove();
                        } else {
                            nextVisitor = museum.getNEEntranceTurnstile().getQueue().remove();
                        }
                    }else if(museum.getSEEntranceTurnstile().getQueue().isEmpty()){
                        nextVisitor = museum.getNEEntranceTurnstile().getQueue().remove();
                    }else if(museum.getNEEntranceTurnstile().getQueue().isEmpty()){
                        nextVisitor = museum.getSEEntranceTurnstile().getQueue().remove();
                    }

                    if (nextVisitor != null) {
                        museum.addVisitor(localCurrentTime, nextVisitor);
                        scheduledTicketLeaving(nextVisitor);
                    }
                }

                try {
                    Thread.sleep(Constant.SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean enterMuseumCondition(Calendar localCurrentTime) {
        return museum.isOpen() && (!museum.getNEEntranceTurnstile().getQueue().isEmpty() || !museum.getSEEntranceTurnstile().getQueue().isEmpty())
                && museum.getTotalNumOfPeopleInMuseum() < Constant.MAX_VISITOR_IN_MUSEUM && localCurrentTime.before(MUSEUM_LAST_ENTRY_TIME);
    }

    private void scheduledTicketLeaving(Ticket ticket) {
        String leaveTimeKey = CalendarUtils.toHHmmString(ticket.getLeaveTime());
        if (!ticketLeavingTimeMap.containsKey(leaveTimeKey)) {
            ticketLeavingTimeMap.put(leaveTimeKey, new ArrayList<Ticket>());
        }
        ticketLeavingTimeMap.get(leaveTimeKey).add(ticket);
    }

    private class ExitMuseum implements Runnable {
        @Override
        public void run() {

            Calendar localCurrentTime = currentTime;
            Calendar localEndTime = MUSEUM_END_TIME;
            Map<String, List<Ticket>> localTicketLeavingTimeMap = ticketLeavingTimeMap;

            while (!localCurrentTime.after(localEndTime) || !museum.getVisitorList().isEmpty()) {
                if(localCurrentTime.after(localEndTime) && !museum.getVisitorList().isEmpty()){
                    museum.setProcessingRest(true);
                }

                if (!localCurrentTime.equals(currentTime)) {
                    System.out.println("Current time has changed.");
                    localCurrentTime = currentTime;
                }

                if (!localTicketLeavingTimeMap.equals(ticketLeavingTimeMap)) {
                    System.out.println("Map has updated");
                    localTicketLeavingTimeMap = ticketLeavingTimeMap;
                }

                if (!localCurrentTime.after(localEndTime)) {
                    String localCurrentTimeString = CalendarUtils.toHHmmString(localCurrentTime);
                    if (!museum.getVisitorList().isEmpty() && ticketLeavingTimeMap.containsKey(localCurrentTimeString)) {
                        List<Ticket> list = ticketLeavingTimeMap.get(localCurrentTimeString);
                        for (Ticket t : list) {
                            museum.removeVisitor(localCurrentTime, t);
                        }
                    }
                }else{
                    for (Map.Entry<String, List<Ticket>> entry : ticketLeavingTimeMap.entrySet()) {
                        List<Ticket> list = entry.getValue();
                        for (Ticket t : list) {
                            museum.removeVisitor(localCurrentTime, t);
                        }
                    }
                }
            }
        }
    }

    private class EnterTurnstile implements Runnable {
        @Override
        public void run() {
            Calendar localCurrentTime = currentTime;
            Calendar localEndTime = MUSEUM_END_TIME;

            while (!localCurrentTime.after(localEndTime)) {
                if (!localCurrentTime.equals(currentTime)) {
                    System.out.println("Current time has changed.");
                    localCurrentTime = currentTime;
                }

                if (enterTurnstileCondition(localCurrentTime)) {
                    Ticket nextVisitor = turnStilePool.remove(0);
                    if(museum.getNEEntranceTurnstile().getQueue().size() < Constant.TURNSTILE_NUM && museum.getNEEntranceTurnstile().getQueue().size() < Constant.TURNSTILE_NUM){
                        if (EntranceUtils.toSouthEntrance()) {
                            museum.getSEEntranceTurnstile().addVisitor(localCurrentTime, nextVisitor);
                        } else {
                            museum.getNEEntranceTurnstile().addVisitor(localCurrentTime, nextVisitor);
                        }
                    }else if(museum.getNEEntranceTurnstile().getQueue().size() < Constant.TURNSTILE_NUM){
                        museum.getNEEntranceTurnstile().addVisitor(localCurrentTime, nextVisitor);
                    }else if(museum.getSEEntranceTurnstile().getQueue().size() < Constant.TURNSTILE_NUM){
                        museum.getSEEntranceTurnstile().addVisitor(localCurrentTime, nextVisitor);
                    }

                }

                try {
                    Thread.sleep(Constant.SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean enterTurnstileCondition(Calendar localCurrentTime) {
        return  museum.isOpen() &&
                !turnStilePool.isEmpty() &&
                (museum.getNEEntranceTurnstile().getQueue().size() < Constant.TURNSTILE_NUM || museum.getSEEntranceTurnstile().getQueue().size() < Constant.TURNSTILE_NUM) &&
                localCurrentTime.before(MUSEUM_LAST_ENTRY_TIME);
    }

    private class ExitTurnstile implements Runnable {
        @Override
        public void run() {
            Calendar localCurrentTime = currentTime;
            Calendar localEndTime = MUSEUM_END_TIME;

            while (exitMuseumCondition(localCurrentTime, localEndTime)) {
                if(!museum.getEEExitTurnstile().getQueue().isEmpty() && !museum.getWEExitTurnstile().getQueue().isEmpty()){
                    if (ExitUtils.toEastExit()) {
                        museum.getEEExitTurnstile().addExitVisitor(localCurrentTime);
                    } else {
                        museum.getWEExitTurnstile().addExitVisitor(localCurrentTime);
                    }
                }else if(museum.getEEExitTurnstile().getQueue().isEmpty()){
                    museum.getWEExitTurnstile().addExitVisitor(localCurrentTime);
                }else if(museum.getWEExitTurnstile().getQueue().isEmpty()){
                    museum.getEEExitTurnstile().addExitVisitor(localCurrentTime);
                }

                try {
                    Thread.sleep(Constant.TURNSTILE_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            printProgramEnd();
        }
    }

    private void printProgramEnd() {
        ManagerInterface.jTextArea.append("-----------------------------------------------------------------------------\n");
        ManagerInterface.jTextArea.append("No visitors in museum. Museum ends operating.\n");
        ManagerInterface.jTextArea.append("Total number of ticket sold: " + counter.getNumberOfTicketSold() + "\n");
        ManagerInterface.jTextArea.append("-----------------------------------------------------------------------------");
    }

    private boolean exitMuseumCondition(Calendar localCurrentTime, Calendar localEndTime) {
        return !localCurrentTime.after(localEndTime) || !museum.getEEExitTurnstile().getQueue().isEmpty() || !museum.getWEExitTurnstile().getQueue().isEmpty() || !museum.getVisitorList().isEmpty();
    }
}
