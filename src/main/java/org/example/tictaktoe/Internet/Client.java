package org.example.tictaktoe.Internet;

import org.example.tictaktoe.Game;
import org.example.tictaktoe.GameUnits.Cross;
import org.example.tictaktoe.GameUnits.Zero;
import org.example.tictaktoe.MyThread;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Client extends Communicative{

    public Client(Game game, double updateFrequency) {
        super(game, updateFrequency);
    }

    @Override
    protected String createConnection() throws IOException {
        game.setFieldText("Ожидаем подключение к серверу");
        System.out.println("Ожидаем подключение к серверу");

        while (true){
            System.out.println("1");
            try {
                clientSocket = new Socket("localhost", 4004);
                break;
            }
            catch (ConnectException | RuntimeException e) {
                game.setFieldText("Указанный сервер не запущен");
                System.out.println("Указанный сервер не запущен");
                return "error";
            }
        }

        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());

        this.setState("connected");
        game.setFieldText("Успешно подключился к серверу");
        System.out.println("Успешно подключился к серверу");
        return "ok";
    }

    @Override
    protected void setStartGameSettings() throws IOException, ClassNotFoundException {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> settings = (HashMap<String, Object>) in.readObject();

        System.out.println("я получил настройки" + settings);
        out.writeObject("клиент принял настройки" + settings);
        out.flush();

        game.setTurn((Boolean) settings.get("yourTurn"));
        game.setTeam((String) settings.get("team"));

        System.out.println("finish init settings");
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