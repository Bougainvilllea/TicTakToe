package org.example.tictaktoe;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.tictaktoe.GameUnits.Cross;
import org.example.tictaktoe.GameUnits.Zero;

public class AppController {

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Pane fieldPane;

    @FXML
    private BorderPane bottomPane;

    public Field field;

    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            Runnable updater = new Runnable() {

                @Override
                public void run() {
                    double w = mainPane.getWidth();
                    double h = mainPane.getHeight();
                    if (w == 0) {
                        w = mainPane.getPrefWidth();
                    }
                    if (h == 0) {
                        h = mainPane.getPrefHeight();
                    }

                    fieldPane.resize(w, h * 0.8);
                    System.out.println(w + " " + h);
                    field.updateField();
                }
            };

            while (true) {
                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException ex) {
                }

                Platform.runLater(updater);
            }
        }

    });


    public void initialize() {
        field = new Field(fieldPane, Color.BLACK, Color.BISQUE, 20);
        field.insertGameUnit(1, 1, new Cross(Color.BLUE, 100, 100, 20));
        field.insertGameUnit(1, 2, new Zero(Color.RED, 100, 100, 20, field.getBackGroundColor()));
        field.insertGameUnit(2, 2, new Zero(Color.GREEN, 100, 100, 20, field.getBackGroundColor()));
        field.updateField();

        thread.start();

    }

}
