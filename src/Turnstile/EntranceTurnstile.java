package Turnstile;

import museum.Museum;

public class EntranceTurnstile extends Turnstile {

    private Museum museum;

    public EntranceTurnstile(String turnstileId) {
        super(turnstileId);
    }

    void addVisitor() {
        museum.addVisitor();
    }

}
