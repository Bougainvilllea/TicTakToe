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
import java.util.concurrent.BlockingQueue;

public class Client extends Communicative{

    public Client(Game game, double updateFrequency) {
        super(game, updateFrequency);
    }

    @Override
    protected void createConnection() throws IOException {
        System.out.println("Ожидаем подключение к серверу");
        clientSocket = new Socket("localhost", 4004);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
        System.out.println("Успешно подключился к серверу");
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

}