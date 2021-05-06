package utilities;

import constant.Constant;

import java.util.Random;

public final class RandomizeUtils {
    public static Random r = new Random();

    public static int randomizeStayTimeInMuseum() {
        return r.nextInt(101) + 50;
    }

    public static int randomizeGapBetweenTicketPurchases() {
        return r.nextInt(4) + 1;
    }

    public static int randomizeNumberOfTicketSold() {
        return r.nextInt(4) + 1;
    }

    public static int randomizeTurnstileId() {
        return r.nextInt(Constant.TURNSTILE_NUM) + 1;
    }
}
