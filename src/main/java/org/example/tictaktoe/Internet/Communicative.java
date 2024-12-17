package org.example.tictaktoe.Internet;

import javafx.scene.paint.Color;
import org.example.tictaktoe.Game;
import org.example.tictaktoe.GameUnits.Cross;
import org.example.tictaktoe.GameUnits.Zero;
import org.example.tictaktoe.MyThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Communicative {
    protected Game game;
    protected static Socket clientSocket;
    protected static ServerSocket server;
    protected ObjectInputStream in;
    protected ObjectOutputStream out;
    protected MyThread mainThread;
    protected MyThread inputThread;
    protected MyThread outputThread;
    protected final BlockingQueue<Update> sendUpdateQueue = new LinkedBlockingQueue<Update>();
    protected final double updateFrequency;
    private String state = "not connected";

    public Communicative(Game game, double updateFrequency) { //ow server
        this.game = game;
        this.updateFrequency = updateFrequency;
    }

    public void start() throws IOException { //ow server
        initThreads();
    }

    protected String reverseTeam(String team) {
        if (team.equals("cross")) {
            return "zero";
        } else if (team.equals("zero")) {
            return "cross";
        }
        return null;
    }

    protected String createConnection() throws IOException {
        return "ok";
    }

    protected synchronized void setStartGameSettings() throws IOException, ClassNotFoundException { //ow

    }

    protected void sendUpdates() throws IOException, ClassNotFoundException {
        if (!sendUpdateQueue.isEmpty()) {
            List<Update> dataList = new ArrayList<Update>(sendUpdateQueue);
            out.writeObject(dataList);
            out.flush();
            sendUpdateQueue.clear();
        }
    }

    protected void takeUpdates() throws IOException, ClassNotFoundException {
        @SuppressWarnings("unchecked")
        List<Update> updatesQueueFromRemotePlayer = (List<Update>) in.readObject();

        for (Update update : updatesQueueFromRemotePlayer) {
            HashMap<String, Object> data = update.data;
            if (update.codeName.equals("insert unit")) {

                 String team = (String) data.get("unitClass");

                @SuppressWarnings("unchecked")
                 List<Double> rgb = (List<Double>) data.get("color");

                if (team.equals("cross")) {
                    Cross unit = new Cross(Color.color(rgb.getFirst(), rgb.get(1), rgb.getLast()), 100, 100,
                            (double) data.get("thickness"));
                    game.field.insertGameUnit((int) data.get("cellNumToInsert"), unit);

                } else if (team.equals("zero")) {
                    @SuppressWarnings("unchecked")
                    List<Double> rgbBackGround = (List<Double>) data.get("backGroundColor");

                    Zero unit = new Zero(Color.color(rgb.getFirst(), rgb.get(1), rgb.getLast()), 100, 100,
                            (double) data.get("thickness"), Color.color(rgbBackGround.getFirst(), rgbBackGround.get(1), rgbBackGround.getLast()));
                    game.field.insertGameUnit((int) data.get("cellNumToInsert"), unit);
                }
                game.setTurn(true);

            }

        }
    }

    protected void createInputThread() throws IOException {
        inputThread = new MyThread(new Runnable() {
            @Override
            public void run() {
                try {
                    takeUpdates();
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("inputThread: " + e.getMessage());
                }
            }

        }, updateFrequency);
    }

    protected void createOutputThread() throws IOException {
        outputThread = new MyThread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendUpdates();
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("outputThread: " + e.getMessage());
                }
            }
        }
                , updateFrequency);
    }


    protected void initThreads() {
        mainThread = new MyThread(new Runnable() {
            @Override
            public void run() {
                update();
            }
        }, new Runnable() {
            @Override
            public void run() {
                System.out.println("Основной поток запущен!");
                try {
                    if(createConnection().equals("ok")){
                        createOutputThread();
                        createInputThread();
                        setStartGameSettings();
                        outputThread.start();
                        inputThread.start();
                    };

                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }, updateFrequency);

        mainThread.start();
    }


    private void takeUpdateQueueFromGame() {
        if (sendUpdateQueue.isEmpty()) {
            sendUpdateQueue.addAll(game.updateQueue);
        }
        game.updateQueue.clear();
    }

    private void update() {
        takeUpdateQueueFromGame();
    }


    public void close() {
        System.out.println("close");
    }

    public String getState(){
        return state;
    }

    protected void setState(String state){
        this.state = state;
    }
}