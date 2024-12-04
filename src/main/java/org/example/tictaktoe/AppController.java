package org.example.tictaktoe;
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

    class Hand extends Thread{
        public void run(){
            Canvas canvas = new Canvas(mainPane.getPrefWidth(), mainPane.getPrefHeight());
            bottomPane.getChildren().add(canvas);
            while(true){
                System.out.println(bottomPane.getWidth());
                bottomPane.resize(mainPane.getWidth(), mainPane.getHeight());
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }

        }
    }
    public void initialize() {

        System.out.println(bottomPane.getWidth() + " " + bottomPane.getLayoutX() + " " + bottomPane.getLayoutY());

        Canvas canvas = new Canvas(mainPane.getPrefWidth(), mainPane.getPrefHeight());
        bottomPane.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Field field = new Field(fieldPane, Color.BLACK, Color.BISQUE, 20);
        field.insertGameUnit(1, 1, new Cross(Color.BLUE, 100, 100, 20));
        field.insertGameUnit(1, 2, new Zero(Color.RED, 100, 100, 20, field.getBackGroundColor()));
        field.updateField();

//        Hand hand = new Hand();
//        hand.start();

    }

}
