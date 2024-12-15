package org.example.tictaktoe;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.tictaktoe.Internet.Client;
import org.example.tictaktoe.Internet.Server;

import java.io.IOException;

public class GameController {

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Pane fieldPane;

    @FXML
    private BorderPane bottomPane;

    public Field field;

    public Stage stage;

    public Game game;

    public boolean isServer = true;

    private Server server;

    private Client client;

    public void setServer(){
        isServer = true;
    }


    public void initialize() throws IOException {
        game = new Game(mainPane, fieldPane, "zero");
        game.start();
        fieldPane.setOnMousePressed(this::MousePressed);

        if(isServer){
            server = new Server(game, 60);
            server.start();
        }
        else {
            client = new Client(game, 60);
            client.start();
        }


    }

    private void MousePressed(MouseEvent event) {
        if(event.isPrimaryButtonDown()){
            game.clickOn(event.getX(), event.getY());
        }

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
