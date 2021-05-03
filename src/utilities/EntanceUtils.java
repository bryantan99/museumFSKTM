package utilities;

import java.util.Random;

public final class EntanceUtils {
    public static final Random random = new Random();

    public static boolean judgeWhichEntranceToGo(){
        return random.nextInt() >= 0.5? true : false;
    }
}
