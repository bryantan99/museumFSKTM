package museum;

import turnstile.*;
import constant.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Museum {

    private int numberOfCurrentVisitor;
    private boolean isOpen;
    private Map<String, List<Turnstile>> turnstileMap;

    public Museum() {
        this.numberOfCurrentVisitor = 0;
        this.isOpen = false;
        initTurnstileMap();
    }

    private void initTurnstileMap() {
        this.turnstileMap = new HashMap<>();

        this.turnstileMap.put("ENTRANCE", new ArrayList<>());
        this.turnstileMap.put("EXIT", new ArrayList<>());

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                this.turnstileMap.get("ENTRANCE").add(new EntranceTurnstile(Constant.ENTRANCE_TURNSTILE_LIST.get(i) + (j + 1)));
                this.turnstileMap.get("EXIT").add(new ExitTurnstile(Constant.EXIT_TURNSTILE_LIST.get(i) + (j + 1)));
            }
        }
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

    public void setNumberOfCurrentVisitor(int numberOfCurrentVisitor) { this.numberOfCurrentVisitor = numberOfCurrentVisitor; }

    public void startBusiness() {
        this.isOpen = true;
    }

    public void endBusiness() {
        this.isOpen = false;
    }

    public void addVisitor() {
        numberOfCurrentVisitor++;
    }

    public void removeVisitor() {
        numberOfCurrentVisitor--;
    }

    public Map<String, List<Turnstile>> getTurnstileMap() {
        return turnstileMap;
    }

    public void setTurnstileMap(Map<String, List<Turnstile>> turnstileMap) {
        this.turnstileMap = turnstileMap;
    }
}
