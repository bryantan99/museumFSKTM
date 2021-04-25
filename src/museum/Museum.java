package museum;

import Turnstile.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Museum {

    private int numberOfCurrentVisitor;
    private boolean isOpen;
    private Map<String, List<Turnstile>> turnstileMap;
    private TicketCounter counter;

    public Museum() {
        this.numberOfCurrentVisitor = 0;
        this.isOpen = false;
        this.counter = new TicketCounter();
        initTurnstileMap();
    }

    private void initTurnstileMap() {
        this.turnstileMap = new HashMap<>();

        this.turnstileMap.put("ENTRANCE", new ArrayList<>());
        this.turnstileMap.put("EXIT", new ArrayList<>());

        this.turnstileMap.get("ENTRANCE").add(new EntranceTurnstile("ENTRANCE_1"));
        this.turnstileMap.get("ENTRANCE").add(new EntranceTurnstile("ENTRANCE_2"));
        this.turnstileMap.get("EXIT").add(new EntranceTurnstile("EXIT_1"));
        this.turnstileMap.get("EXIT").add(new EntranceTurnstile("EXIT_2"));
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getNumberOfCurrentVisitor() {
        return numberOfCurrentVisitor;
    }

    public void setNumberOfCurrentVisitor(int numberOfCurrentVisitor) {
        this.numberOfCurrentVisitor = numberOfCurrentVisitor;
    }

    //  Set museum's operating status to true.
    public void startBusiness() {
        this.isOpen = true;
    }

    //  Set museum's operating status to false.
    public void endBusiness() {
        this.isOpen = false;
    }

    public void addVisitor() {
        numberOfCurrentVisitor++;
    }

    public void removeVisitor() {
        numberOfCurrentVisitor--;
    }
}
