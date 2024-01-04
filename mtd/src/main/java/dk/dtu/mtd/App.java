package dk.dtu.mtd;

import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import dk.dtu.mtd.controller.Controller;
import dk.dtu.mtd.view.Gui;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App {

    public static void main(String[] args) {
        Gui.initGui();
        Controller.initController();
    }
   
}
