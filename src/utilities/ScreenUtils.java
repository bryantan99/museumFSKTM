package utilities;

import java.awt.*;

public class ScreenUtils {

    // get the screen width of pc
    public static int getScreenWidth(){
        return Toolkit.getDefaultToolkit().getScreenSize().width;
    }

    // get the screen height of pc
    public static int getScreenHeight(){
        return Toolkit.getDefaultToolkit().getScreenSize().height;
    }
}
