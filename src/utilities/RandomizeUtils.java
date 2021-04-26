package utilities;

import java.util.Random;

public final class RandomizeUtils {

    public static int randomizeStayTimeInMuseum() {
        Random r = new Random();
        return r.nextInt(101) + 50;
    }

    public static int randomizeGapBetweenTicketPurchases() {
        Random r = new Random();
        return r.nextInt(4) + 1;
    }

}
