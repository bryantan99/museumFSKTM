package turnstile;

import museum.Ticket;

import java.util.LinkedList;
import java.util.Queue;

public abstract class Turnstile {

    protected String turnstileId;
    protected volatile Queue<Ticket> queue;

    public Turnstile(String turnstileId) {
        this.turnstileId = turnstileId;
        this.queue = new LinkedList<>();
    }

    public Queue<Ticket> getQueue() { return queue; }

}
