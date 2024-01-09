package dk.dtu.mtd;

import dk.dtu.mtd.controller.Controller;
import dk.dtu.mtd.view.Gui;

public class App {
    public static void main(String[] args) {
        Gui.initGui();
        Controller.initController();
    }
   
}
