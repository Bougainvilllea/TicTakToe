package org.example.tictaktoe;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.example.tictaktoe.GameUnits.Cross;
import org.example.tictaktoe.GameUnits.EmptyUnit;
import org.example.tictaktoe.GameUnits.GameUnit;
import org.example.tictaktoe.GameUnits.Zero;
import org.example.tictaktoe.Internet.Client;
import org.example.tictaktoe.Internet.Communicative;
import org.example.tictaktoe.Internet.Server;
import org.example.tictaktoe.Internet.Update;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Game {
    private Thread gameThread;
    private final AnchorPane mainPane;
    private String team;
    private boolean yourTurn;
    public final Field field;
    public BlockingQueue<Update> updateQueue;
    public Communicative connection;

    public Game(AnchorPane mainPane, Pane fieldPane, Button fieldButton, Text fieldText, String team){
        this.mainPane = mainPane;
        this.team = team;
        field = new Field(fieldPane, fieldButton, fieldText, Color.BLACK, Color.BISQUE, 20);
        yourTurn = false;
        updateQueue = new LinkedBlockingQueue<>();
    }

    public void start(){
        initGameThread(60);
    }

    private GameUnit getUnitByTeam(){
        if(Objects.equals(team, "cross")){
            return new Cross(Color.color(1, 0, 0), 100, 100, 20);
        }

        else if(Objects.equals(team, "zero")){
            return new Zero(Color.color(0, 0, 1), 100, 100, (20.0 / 100.0) * 2, field.getBackGroundColor());
        }

        return null;
    }

    public void setFieldText(String text){
        field.fieldText.setText(text);
    }

    public void clickOn(double x, double y){
        int cellNumToInsert = field.getNumCellContained(x, y);
        System.out.println(yourTurn);
        if(cellNumToInsert != -1 && yourTurn && field.fieldCells.get(cellNumToInsert).name.equals("emptyUnit")){
            GameUnit unit = getUnitByTeam();
            field.insertGameUnit(cellNumToInsert, unit);

            assert unit != null;
            updateQueue.add(new Update(unit.toHashMap(cellNumToInsert), "insert unit"));
            yourTurn = false;
        }
    }

    public void endGame(){
        connection.close();
    }

    private void PrintIfSomebodyWin(){
        String winner = field.getWinningTeam();

        if (!winner.equals("game in progress")){
            if(!winner.equals("draw")){
                setFieldText("Победила команда: " + winner);
            }
            else {
                setFieldText("Ничья!");
            }
            yourTurn = false;
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
                        if(connection.getState().equals("connected")){
                            if(yourTurn) {
                                setFieldText("Ваш ход!");
                            }
                            else{
                                setFieldText("Ход противника");
                            }
                        }


                        field.resize(w, h * 0.8);
                        field.updateField();

                        PrintIfSomebodyWin();
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

    public void setConnection(Communicative connection){
        this.connection = connection;
    }
}
