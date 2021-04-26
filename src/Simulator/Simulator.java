package Simulator;

import constant.Constant;
import museum.Museum;
import museum.Ticket;
import museum.TicketCounter;
import runnable.SellTicket;
import utilities.CalendarUtils;
import utilities.RandomizeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.MINUTE;

public class Simulator {
    private Museum museum;
    private TicketCounter counter;

    public Simulator() {
        this.museum = new Museum();
        this.counter = new TicketCounter();
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
                counter.stopOperate();
            }

            if (nextSellTicketDateTime.equals(currentTimeCal)) {
                List<Ticket> ticketSoldList = sellTicket();
                if (ticketSoldList != null && !ticketSoldList.isEmpty()) {
                    printSellTicketMsg(toTimeString(currentTimeCal), ticketSoldList);
                    nextSellTicketDateTime.add(MINUTE, RandomizeUtils.randomizeGapBetweenTicketPurchases());
                }
            }

            currentTimeCal.add(MINUTE, 1);
        }

    }

    private void printSellTicketMsg(String timestamp, List<Ticket> ticketSoldList) {
        String msg = timestamp + " - Tickets ";
        for (int i = 0; i < ticketSoldList.size() ; i++) {
            Ticket t = ticketSoldList.get(i);
            msg += t.getTicketId();
            if (i != ticketSoldList.size() - 1) {
                msg += ", ";
            }
        }
        msg += " sold.";
        System.out.println(msg);
    }

    public String toTimeString(Calendar time) {
        return String.format("%02d:%02d", time.get(Calendar.HOUR_OF_DAY), time.get(MINUTE));
    }

    public List<Ticket> sellTicket() {
        SellTicket sellTicketRunnable = new SellTicket(counter, RandomizeUtils.randomizeNumberOfTicketSold());
        Thread sellTicketThread = new Thread(sellTicketRunnable);
        sellTicketThread.start();
        try {
            sellTicketThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sellTicketRunnable.getTicketList();
    }
}
