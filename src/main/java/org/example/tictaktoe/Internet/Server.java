package org.example.tictaktoe.Internet;

import org.example.tictaktoe.Game;
import org.example.tictaktoe.GameUnits.Cross;
import org.example.tictaktoe.GameUnits.Zero;
import org.example.tictaktoe.MyThread;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Server extends Communicative{
    String port;
    public Server(Game game, String port, double updateFrequency) {
        super(game, updateFrequency);
        this.port = port;

        System.out.println("Сервер создан!");
    }

    @Override
    public void start() throws IOException{ //ow
        try{server = new ServerSocket(Integer.parseInt(port), 50, InetAddress.getByName("0.0.0.0"));}
        catch (BindException e){
            server = new ServerSocket(0, 50, InetAddress.getByName("0.0.0.0"));
        }
        initThreads();
    }

    @Override
    protected String createConnection(){
        System.out.println("Ожидаем подключение 2-го игрока");
        game.setFieldText("Ожидаем подключение 2-го игрока");
        try {
            clientSocket = server.accept();
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.setState("connected");
            game.setFieldText("2-ой игрок подключился!");
            System.out.println("2-ой игрок подключился!");
        } catch (IOException e) {
            System.out.println("сокет закрыт");
            return "error";
        }
        return "ok";
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

    @Override
    public void close(){
        try {
            if(mainThread != null && inputThread != null && outputThread != null) {
                mainThread.interrupt();
                inputThread.interrupt();
                outputThread.interrupt();
            }

            if(server != null){
                server.close();
            }
            if(clientSocket != null){
                clientSocket.close();
            }
            if(in != null){
                in.close();
            }
            if(out != null){
                out.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}