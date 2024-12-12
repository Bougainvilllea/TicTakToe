package org.example.tictaktoe;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class Client {
    private static Socket clientSocket;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private Thread clientThread;
    private Game game;

    public Client(Game game) {
        this.game = game;
    }

    private void createConnection() throws IOException {
        System.out.println("Ожидаем подключение к серверу");
        clientSocket = new Socket("localhost", 4004);
        System.out.println("Успешно подключился к серверу");

        in = new ObjectInputStream(clientSocket.getInputStream());
        out = new ObjectOutputStream(clientSocket.getOutputStream());

    }

    private void setStartGameSettings() throws IOException, ClassNotFoundException {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> settings = (HashMap<String, Object>) in.readObject();

        System.out.println("я получил настройки" + settings);
        out.writeObject("клиент принял настройки" + settings);
        out.flush();

        game.setTurn((boolean) settings.get("turn"));
        game.setTeam((String) settings.get("team"));

        System.out.println("finish init settings");
    }

    public void start() throws IOException {
        initClientThread(120);
    }

    private void initClientThread(double clientTickRate){
        clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    createConnection();
                    setStartGameSettings();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                };

                while (true) {
                    try {
                        Thread.sleep((int) Math.ceil(1000 / clientTickRate));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    Platform.runLater(updater);
                }
            }
        });

        clientThread.start();
    }

    private void sendUpdates() throws IOException {

    }


    private void update() {
        try {
            try {

                out.flush();

                String serverWord = in.readLine();
                System.out.println(serverWord);
            } finally {
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}