package utilities;

import java.util.Random;

public final class EntranceExitUtils {
    public static final Random random = new Random();

    public static boolean toSouthEntrance(){
        return random.nextInt() >= 0.5;
    }

    public static boolean toWestExit(){ return random.nextInt() >= 0.5; }
}
