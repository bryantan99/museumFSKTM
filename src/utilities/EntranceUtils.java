package utilities;

import java.util.Random;

public final class EntranceUtils {
    public static final Random random = new Random();

    public static boolean toSouthEntrance(){
        return random.nextBoolean();
    }
}
