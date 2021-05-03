package turnstile;

import constant.Constant;
import museum.Ticket;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class Turnstile {
    protected String turnstileId;
    protected ReentrantReadWriteLock readWriteLock;
    protected int maxCapaticy;
    protected Lock readLock;
    protected Lock writeLock;
    protected volatile Queue<Ticket> queue;

    public Turnstile(String turnstileId) {
        this.turnstileId = turnstileId;
        this.maxCapaticy = Constant.TURNSTILE_NUM;
        this.readWriteLock = new ReentrantReadWriteLock();
        this.readLock = this.readWriteLock.readLock();
        this.writeLock = this.readWriteLock.writeLock();
        this.queue = new LinkedList<>();
    }

    public String getTurnstileId() {
        return turnstileId;
    }

    public void setTurnstileId(String turnstileId) {
        this.turnstileId = turnstileId;
    }

    public ReentrantReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    public void setReadWriteLock(ReentrantReadWriteLock readWriteLock) {
        this.readWriteLock = readWriteLock;
    }

    public Lock getReadLock() {
        return readLock;
    }

    public void setReadLock(Lock readLock) {
        this.readLock = readLock;
    }

    public Lock getWriteLock() {
        return writeLock;
    }

    public void setWriteLock(Lock writeLock) {
        this.writeLock = writeLock;
    }

    public Queue<Ticket> getQueue() { return queue; }

    public void setQueue(Queue<Ticket> queue) { this.queue = queue; }
}
