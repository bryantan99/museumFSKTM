package museum;

import constant.Constant;
import gui.ManagerInterface;
import turnstile.EntranceTurnstile;
import turnstile.ExitTurnstile;
import turnstile.Turnstile;
import utilities.CalendarUtils;
import utilities.EntranceUtils;

import java.util.*;

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

    public List<Ticket> getVisitorList() {
        return visitorList;
    }

    public void startBusiness(Calendar timestamp) {
        this.isOpen = true;
        String openMsg = CalendarUtils.toHHmmString(timestamp) + " - Museum is now opened.";
        System.out.printf("%-60s\n", openMsg);
        ManagerInterface.jTextArea.append(openMsg+"\n");
    }

    public void endBusiness(Calendar timestamp) {
        this.isOpen = false;
        String closeMsg = CalendarUtils.toHHmmString(timestamp) + " - Museum is closing now.";
        System.out.printf("%-60s\n", closeMsg);
        ManagerInterface.jTextArea.append(closeMsg+"\n");
    }

    public synchronized void addVisitor(Calendar timestamp, Ticket ticket) {
        try {
            while (ifAddVisitorToMuseum()) {
                wait(Constant.MUSEUM_WAIT_TIME);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ticket.updateLeaveTime(timestamp);
        visitorList.add(ticket);

        String enterMsg = ticket.getTicketId() + " has entered museum.";
        String enterMsgWithTimestamp = CalendarUtils.toHHmmString(timestamp) + " - " + enterMsg;

        addTicketDataToMuseumTable(timestamp, ticket, enterMsg);
        System.out.printf("%-60s[No. of people in the museum : %-3d] [Leaving time: %-5s] [Staying time: %-3s minutes]\n", enterMsgWithTimestamp, getTotalNumOfPeopleInMuseum(), CalendarUtils.toHHmmString(ticket.getLeaveTime()), ticket.getStayTimeInMinute());
    }

    private void addTicketDataToMuseumTable(Calendar timestamp, Ticket ticket, String message) {
        Vector<String> ticketVector = new Vector<>();
        ticketVector.add(CalendarUtils.toHHmmString(timestamp));
        ticketVector.add(ticket.getTicketId());
        ticketVector.add(message);
        ticketVector.add(String.valueOf(this.getTotalNumOfPeopleInMuseum()));
        ManagerInterface.museumTable.getTableData().add(ticketVector);
        ManagerInterface.museumTable.getTableModel().fireTableDataChanged();
    }

    private boolean ifAddVisitorToMuseum() {
        return visitorList.size() >= Constant.MAX_VISITOR_IN_MUSEUM ||
                (SEEntranceTurnstile.getQueue().size() == 0 && NEEntranceTurnstile.getQueue().size() == 0);
    }

    public synchronized void removeVisitor(Calendar timestamp, Ticket ticket) {
        if (!visitorList.contains(ticket)) {
            return;
        }
        try{
            while (this.EEExitTurnstile.getQueue().size() >= Constant.TURNSTILE_NUM
                    && this.WEExitTurnstile.getQueue().size() >= Constant.TURNSTILE_NUM) {
                wait(Constant.MUSEUM_WAIT_TIME);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.visitorList.remove(ticket);
        decideExitTurnstile(ticket);

        String leaveMsg = ticket.getTicketId() + " wants to leave the museum.";
        String leaveMsgWithTimestamp = CalendarUtils.toHHmmString(timestamp) + " - " + leaveMsg;

        addTicketDataToMuseumTable(timestamp, ticket, leaveMsg);
        System.out.printf("%-60s\n", leaveMsgWithTimestamp);

        notifyAll();
    }

    public int getTotalNumOfPeopleInMuseum(){
        int numOfPeopleAtTurnStile = this.getEEExitTurnstile().getQueue().size() + this.getWEExitTurnstile().getQueue().size();
        int numOfPeopleAtMainMeseum = this.getVisitorList().size();
        return numOfPeopleAtTurnStile + numOfPeopleAtMainMeseum;
    }

    public void decideExitTurnstile(Ticket ticket){
        // judge which exit should go
        if(judgePeopleNumOfExit()){
            if (EntranceUtils.toSouthEntrance()) {
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

    private boolean judgePeopleNumOfExit() {
        return this.EEExitTurnstile.getQueue().size() < Constant.TURNSTILE_NUM && this.WEExitTurnstile.getQueue().size() < Constant.TURNSTILE_NUM;
    }

    public void offEntranceTurnstile(Calendar localCurrentTime) {
        String lastEntryMsg = CalendarUtils.toHHmmString(localCurrentTime) + " - Museum's last entry time has reached. Entrance turnstiles has been shut down.";
        System.out.println(lastEntryMsg);
        ManagerInterface.jTextArea.append(lastEntryMsg+"\n");
    }

    public String getMessage(String message){
        return message;
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
