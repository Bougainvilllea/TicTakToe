package org.example.tictaktoe;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.tictaktoe.GameUnits.Cross;
import org.example.tictaktoe.GameUnits.EmptyUnit;
import org.example.tictaktoe.GameUnits.GameUnit;
import org.example.tictaktoe.GameUnits.Zero;

import java.util.List;
import java.util.Objects;

public class Game {
    private Thread gameThread;
    private final AnchorPane mainPane;
    private final Field field;
    private String team;
    private boolean yourTurn;
    public List<Update> updateQueue;

    public Game(AnchorPane mainPane, Pane fieldPane, String team){
        this.mainPane = mainPane;
        this.team = team;
        field = new Field(fieldPane, Color.BLACK, Color.BISQUE, 20);
        yourTurn = false;
    }

    public void start(){
        initGameThread(60);
    }

    private GameUnit getUnitTeam(){
        if(Objects.equals(team, "cross")){
            return new Cross(Color.RED, 100, 100, 20);
        }
        else if(Objects.equals(team, "zero")){
            return new Zero(Color.BLUE, 100, 100, 20, field.getBackGroundColor());
        }
        return null;
    }

    public void clickOn(double x, double y){
        int cellNumToInsert = field.getNumCellContained(x, y);

        if(cellNumToInsert != -1 && yourTurn){
            field.insertGameUnit(cellNumToInsert, getUnitTeam());
        }
    }
    private void endGame(){
        System.out.println("Ending game");
    }

    private void endGameIfSomebodyWin(){
        Class winner = field.getWinningTeam();
        if (winner != EmptyUnit.class){
            endGame();
        }
    }

    private void initGameThread(double fps){
        gameThread = new Thread(new Runnable() {
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

                        field.resize(w, h * 0.8);
                        field.updateField();

                        endGameIfSomebodyWin();
                    }
                };

                while (true) {
                    try {
                        Thread.sleep((int) Math.ceil(1000 / fps));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    Platform.runLater(updater);
                }
            }
        });

        gameThread.start();
    }

    public void setTurn(boolean yourTurn){
        this.yourTurn = yourTurn;
    }

    public void setTeam(String team){
        this.team = team;
    }
}
