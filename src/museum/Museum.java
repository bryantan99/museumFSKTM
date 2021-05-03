package museum;

import constant.Constant;
import turnstile.EntranceTurnstile;
import turnstile.ExitTurnstile;
import turnstile.Turnstile;
import utilities.CalendarUtils;
import utilities.EntanceUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class Museum {

    private volatile List<Ticket> visitorList;
    private volatile boolean isOpen;
    private volatile boolean isProcessingRest;
    private volatile Map<String, List<Turnstile>> turnstileMap;

    /*
        Turnstile part
        Two Entrances: South Entrance (SE) and North Entrance (NE)
        Two Exits: East Exit (EE) and West Exit (WE)
     */
    private volatile EntranceTurnstile SEEntranceTurnstile;
    private volatile EntranceTurnstile NEEntranceTurnstile;

    private volatile ExitTurnstile EEExitTurnstile;
    private volatile ExitTurnstile WEExitTurnstile;

    public Museum() {
        this.visitorList = new ArrayList<>();
        this.isOpen = false;
        this.isProcessingRest = false;
        initTurnstileMap();
    }

    private void initTurnstileMap() {
        this.SEEntranceTurnstile = new EntranceTurnstile(Constant.SOUTH_ENTRANCE);
        this.SEEntranceTurnstile.museum = this;
        this.NEEntranceTurnstile = new EntranceTurnstile(Constant.NORTH_ENTRANCE);
        this.NEEntranceTurnstile.museum = this;
        this.EEExitTurnstile = new ExitTurnstile(Constant.EAST_EXIT);
        this.EEExitTurnstile.museum = this;
        this.WEExitTurnstile = new ExitTurnstile(Constant.WEST_EXIT);
        this.WEExitTurnstile.museum = this;
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

    public synchronized void addVisitor(Calendar timestamp, Ticket ticket) {
        try {
            while (visitorList.size() >= Constant.MAX_VISITOR_IN_MUSEUM || (SEEntranceTurnstile.getQueue().size() == 0 && NEEntranceTurnstile.getQueue().size() == 0)) {
//                System.out.println(Thread.currentThread() + " is waiting... --Museum");
                wait(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ticket.updateLeaveTime(timestamp);
        visitorList.add(ticket);
        String enterMsg = CalendarUtils.toHHmmString(timestamp) + " - " + ticket.getTicketId() + " has entered museum. Leave time : " + CalendarUtils.toHHmmString(ticket.getLeaveTime());
        System.out.printf("%-60s [No. of people in the museum : %-3d] [Leaving time: %-5s] [Staying time: %-3s minutes]\n", enterMsg, getTotalNumOfPeopleInMuseum(), CalendarUtils.toHHmmString(ticket.getLeaveTime()), ticket.getStayTimeInMinute());
    }

    public synchronized void removeVisitor(Calendar timestamp, Ticket ticket) {
        if (!visitorList.contains(ticket)) {
            return;
        }
        try{
            while (this.EEExitTurnstile.getQueue().size() >= Constant.TURNSTILE_NUM && this.WEExitTurnstile.getQueue().size() >= Constant.TURNSTILE_NUM) {
//                System.out.println(Thread.currentThread() + " is waiting... --Museum");
                wait(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.visitorList.remove(ticket);
        decideExitTurnstile(ticket);
        String leaveMsg = CalendarUtils.toHHmmString(timestamp) + " - " + ticket.getTicketId() + " wants to leave museum";
        System.out.printf("%-60s [No. of people in the museum : %-3d]\n", leaveMsg, getTotalNumOfPeopleInMuseum());
        notifyAll();
    }

    public int getTotalNumOfPeopleInMuseum(){
        int numOfPeopleAtTurnStile = this.getEEExitTurnstile().getQueue().size() + this.getWEExitTurnstile().getQueue().size();
        int numOfPeopleAtMainMeseum = this.getVisitorList().size();
        return numOfPeopleAtTurnStile + numOfPeopleAtMainMeseum;
    }

    public void decideExitTurnstile(Ticket ticket){
        // judge which exit should go
        if(this.EEExitTurnstile.getQueue().size() < Constant.TURNSTILE_NUM && this.WEExitTurnstile.getQueue().size() < Constant.TURNSTILE_NUM){
            if (EntanceUtils.judgeWhichEntranceToGo()) {
                this.EEExitTurnstile.getQueue().add(ticket);
            } else {
                this.WEExitTurnstile.getQueue().add(ticket);
            }
        }else if(this.EEExitTurnstile.getQueue().size() < Constant.TURNSTILE_NUM){
            this.EEExitTurnstile.getQueue().add(ticket);
        }else if(this.WEExitTurnstile.getQueue().size() < Constant.TURNSTILE_NUM){
            this.WEExitTurnstile.getQueue().add(ticket);
        }
    }

    public Map<String, List<Turnstile>> getTurnstileMap() {
        return turnstileMap;
    }

    public void setTurnstileMap(Map<String, List<Turnstile>> turnstileMap) {
        this.turnstileMap = turnstileMap;
    }

    public void offEntranceTurnstile(Calendar localCurrentTime) {
        System.out.println(CalendarUtils.toHHmmString(localCurrentTime) + " - Museum's last entry time has reached. Entrance turnstiles has been shut down.");
    }

    public EntranceTurnstile getSEEntranceTurnstile() {
        return SEEntranceTurnstile;
    }

    public EntranceTurnstile getNEEntranceTurnstile() {
        return NEEntranceTurnstile;
    }

    public ExitTurnstile getEEExitTurnstile() {
        return EEExitTurnstile;
    }

    public ExitTurnstile getWEExitTurnstile() {
        return WEExitTurnstile;
    }

    public boolean isProcessingRest() {
        return isProcessingRest;
    }

    public void setProcessingRest(boolean processingRest) {
        isProcessingRest = processingRest;
    }
}
