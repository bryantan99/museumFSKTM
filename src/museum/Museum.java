package museum;

import turnstile.*;
import constant.Constant;
import utilities.CalendarUtils;

import java.util.*;

public class Museum {

    private List<Ticket> visitorList;
    private boolean isOpen;
    private Map<String, List<Turnstile>> turnstileMap;

    public Museum() {
        this.visitorList = new ArrayList<>();
        this.isOpen = false;
        initTurnstileMap();
    }

    private void initTurnstileMap() {
        this.turnstileMap = new HashMap<>();

        this.turnstileMap.put("ENTRANCE", new ArrayList<>());
        this.turnstileMap.put("EXIT", new ArrayList<>());

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                this.turnstileMap.get("ENTRANCE").add(new EntranceTurnstile(Constant.ENTRANCE_TURNSTILE_LIST.get(i) + "T" +(j + 1)));
                this.turnstileMap.get("EXIT").add(new ExitTurnstile(Constant.EXIT_TURNSTILE_LIST.get(i) + "T" + (j + 1)));
            }
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public List<Ticket> getVisitorList() {
        return visitorList;
    }

    public void startBusiness() {
        this.isOpen = true;
    }

    public void endBusiness() {
        this.isOpen = false;
    }

    public synchronized void addVisitor(Calendar timestamp , Ticket ticket) {
        try {
            while (visitorList.size() >= Constant.MAX_VISITOR_IN_MUSEUM) {
                System.out.println(Thread.currentThread() + " is waiting...");
                wait(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        visitorList.add(ticket);
        ticket.updateLeaveTime(timestamp);
        System.out.println(CalendarUtils.toHHmmString(timestamp) + " - " + ticket.getTicketId() + " has entered the museum.");
    }

    public synchronized void removeVisitor(Calendar timestamp, Ticket ticket) {
        if (!visitorList.contains(ticket)) {
            return;
        }

        visitorList.remove(ticket);
        System.out.println(CalendarUtils.toHHmmString(timestamp) + " - " + ticket.getTicketId() + " has left the museum.");
        notifyAll();
    }

    public Map<String, List<Turnstile>> getTurnstileMap() {
        return turnstileMap;
    }

    public void setTurnstileMap(Map<String, List<Turnstile>> turnstileMap) {
        this.turnstileMap = turnstileMap;
    }
}
