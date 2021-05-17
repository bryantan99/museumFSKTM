package utilities;

import java.util.Random;

public class ExitUtils {
    public static final Random random = new Random();

    public static boolean toEastExit(){
        return random.nextBoolean();
    }
}
