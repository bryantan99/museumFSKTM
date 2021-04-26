package Simulator;

import museum.Museum;
import museum.TicketCounter;
import runnable.SellTicket;

import java.util.Random;

public class Simulator {
    private Museum museum;
    private TicketCounter counter;
    private Random r;

    public Simulator() {
        this.r = new Random();
        this.museum = new Museum();
        this.counter = new TicketCounter();
    }

    public void startSimulate() {
        counter.startOperate();

        while (counter.isOperating() || museum.isOpen()) {
            Runnable sellTicketRunnable = new SellTicket(counter, r.nextInt(4) + 1);
            Thread sellTicketThread = new Thread(sellTicketRunnable);
            sellTicketThread.start();
        }

    }
}
