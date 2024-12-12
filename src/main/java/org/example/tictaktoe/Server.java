package org.example.tictaktoe;

import javafx.application.Platform;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Server {
    private Game game;
    private static Socket clientSocket;
    private static ServerSocket server;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private MyThread serverThread;
    private MyThread inputServerThread;
    private MyThread outputServerThread;
    private List<Update> sendUpdateQueue;

    public Server(Game game) {
        this.game = game;
        System.out.println("Сервер создан!");
    }

    public void start() throws IOException{
        server = new ServerSocket(4004);
        initServerThreads(60);
    }

    private HashMap<String, Object> getRandomStartGameSettings(){
        Random random = new Random();
        HashMap<String, Object> startSettings = new HashMap<>();

        boolean yourTurn = random.nextBoolean();
        String team = List.of("cross", "zero").get(random.nextInt(2));

        startSettings.put("team", team);
        startSettings.put("yourTurn", yourTurn);
        return startSettings;
    }

    private void createClientConnection() throws IOException {
        System.out.println("Ожидаем подключение 2-го игрока");
        try {
            clientSocket = server.accept();
            System.out.println("2-ой игрок подключился!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        in = new ObjectInputStream(clientSocket.getInputStream());
        out = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    private String reverseTeam(String team){
        if (team.equals("cross")){
            return "zero";
        }
        else if (team.equals("zero")){
            return "cross";
        }
        return null;
    }

    private void setStartGameSettings() throws IOException, ClassNotFoundException {
        HashMap<String, Object> settings = getRandomStartGameSettings();

        game.setTurn((Boolean) settings.get("yourTurn"));
        game.setTeam((String) settings.get("team"));

        @SuppressWarnings("unchecked")
        HashMap<String, Object> settingsForClient = (HashMap<String, Object>) settings.clone();
        settingsForClient.put("yourTurn", !(Boolean) settings.get("yourTurn"));
        settingsForClient.put("team", reverseTeam((String) settings.get("team")));

        System.out.println("client:" + settingsForClient + " me:" + settings);


        out.writeObject(settingsForClient);
        out.flush();

        System.out.println("Resp from client:" + (String) in.readObject());
        System.out.println("finish init settings");
    }

    private void initServerThreads(double tickRate){
        serverThread = new MyThread(new Runnable() {
            @Override
            public void run() {
                update();
            }
        }, new Runnable() {
            @Override
            public void run() {
                System.out.println("Сервер запущен!");

                try {
                    createClientConnection();
                    System.out.println("пробую отдать настройки");
                    setStartGameSettings();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }, tickRate);

        inputServerThread = new MyThread(new Runnable() {
            @Override
            public void run() {

            }
        }, tickRate);

        outputServerThread = new MyThread(new Runnable() {
            @Override
            public void run() {

            }
        }, tickRate);

        serverThread.start();
        inputServerThread.start();
        outputServerThread.start();
    }

    private void sendUpdates(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
//        сделать отправку hashMap-ом, чтобы дальше было удобно парсить данные
        if(!sendUpdateQueue.isEmpty()) {
            out.writeObject(sendUpdateQueue);
            out.flush();
            sendUpdateQueue.clear();
        }
    }

    private void takeUpdates(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        @SuppressWarnings("unchecked")
        List<Update> updateQueue = (List<Update>) in.readObject();
        out.writeObject((String) "Server received update");
        out.flush();
    }

    private void makeUpdate(List<Update> updates){

    }

    private void update() {
        try {
            try {
                try {

                    out.flush();
                }

                finally {
                    clientSocket.close();
                    in.close();
                    out.close();
                }
            } finally {
                System.out.println("Сервер закрыт!");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}