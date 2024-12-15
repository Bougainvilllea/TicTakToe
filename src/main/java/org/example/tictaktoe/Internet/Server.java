package org.example.tictaktoe.Internet;

import org.example.tictaktoe.Game;
import org.example.tictaktoe.GameUnits.Cross;
import org.example.tictaktoe.GameUnits.Zero;
import org.example.tictaktoe.MyThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Server extends Communicative{

    public Server(Game game, double updateFrequency) {
        super(game, updateFrequency);
        System.out.println("Сервер создан!");
    }

    @Override
    public void start() throws IOException{ //ow
        server = new ServerSocket(4004);
        initThreads();
    }

    @Override
    protected void createConnection(){
        System.out.println("Ожидаем подключение 2-го игрока");
        try {
            clientSocket = server.accept();
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("2-ой игрок подключился!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setStartGameSettings() throws IOException, ClassNotFoundException {
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

    private HashMap<String, Object> getRandomStartGameSettings(){ // only server
        Random random = new Random();
        HashMap<String, Object> startSettings = new HashMap<>();

        boolean yourTurn = random.nextBoolean();
        String team = List.of("cross", "zero").get(random.nextInt(2));

        startSettings.put("team", team);
        startSettings.put("yourTurn", yourTurn);
        return startSettings;
    }
}