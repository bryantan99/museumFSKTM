package visitor;

import java.util.Random;

public class Visitor {

    private int visitDurationInMin;

    public int getVisitDurationInMin() {
        return visitDurationInMin;
    }

    public void randomizeVisitDurationInMin() {
        Random r = new Random();
        this.visitDurationInMin = r.nextInt(101) + 50;
    }

}
