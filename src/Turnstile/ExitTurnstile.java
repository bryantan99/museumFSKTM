package Turnstile;

import museum.Museum;

public class ExitTurnstile extends Turnstile {

    private Museum museum;

    public ExitTurnstile(String turnstileId) {
        super(turnstileId);
    }

    public void removeVisitor() {
        museum.removeVisitor();
    }

}
