package museum;

public class Museum {

    private int numberOfCurrentVisitor;
    private boolean isOpen;

    public Museum() {
        this.numberOfCurrentVisitor = 0;
        this.isOpen = false;
    }

    public boolean isIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public int getNumberOfCurrentVisitor() {
        return numberOfCurrentVisitor;
    }

    public void setNumberOfCurrentVisitor(int numberOfCurrentVisitor) {
        this.numberOfCurrentVisitor = numberOfCurrentVisitor;
    }

    public void startBusiness() {
        this.isOpen = true;
    }

    public void endBusuiness() {
        this.isOpen = false;
    }

    public void addVisitor() {

    }

    public void removeVisitor() {

    }
}
