package museumduringpandemic;

import simulator.Simulator;
import gui.ManagerInterface;

import java.text.ParseException;

public class MuseumDuringPandemic {

    public static void main(String[] args) throws Exception {
        ManagerInterface managerInterface = new ManagerInterface();
        managerInterface.init();
        Simulator sim = new Simulator();
        sim.startSimulate();
    }

}
