package turnstile;

import constant.Constant;
import museum.Museum;

public class EntranceTurnstile extends Turnstile {

    private Museum museum;

    public EntranceTurnstile(String turnstileId) {
        super(turnstileId);
    }

    void addVisitor() {
        if (!museum.isOpen()) {
            System.out.println("Museum is not operating.");
            return;
        }

        if (museum.getNumberOfCurrentVisitor() < Constant.MAX_VISITOR_IN_MUSEUM) {
            System.out.println("Museum is full. Current capacity : " + museum.getNumberOfCurrentVisitor() + " / " + Constant.MAX_VISITOR_IN_MUSEUM);
            return;
        }
        
        museum.addVisitor();
    }

}
