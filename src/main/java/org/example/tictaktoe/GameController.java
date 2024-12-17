package org.example.tictaktoe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
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

    @FXML
    private Button fieldButton;

    @FXML
    private Text fieldText;

    @FXML
    void leaveGame() throws Exception {
        game.endGame();
        loadMenu(stage);

    }

    public Field field;

    public Stage stage;

    public Game game;

    public boolean isServer;

    private Server server;

    private Client client;




    public void initialize() throws IOException {
        game = new Game(mainPane, fieldPane, fieldButton, fieldText,"zero");
        fieldPane.setOnMousePressed(this::MousePressed);
        game.start();

    }

    public void startOnlineGame(boolean isServer, String ipPortText) throws IOException {
        String ip = ipPortText.split(":")[0];
        String port = ipPortText.split(":")[1];
        if(isServer){
            server = new Server(game, port, 60);
            game.setConnection(server);
            server.start();
        }
        else {
            client = new Client(game, ip, port,60);
            game.setConnection(client);
            client.start();
        }
        this.isServer = isServer;
    }

    private void MousePressed(MouseEvent event) {
        if(event.isPrimaryButtonDown()){
            game.clickOn(event.getX(), event.getY());
        }

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadMenu(Stage stage) throws Exception {
        FXMLLoader menuLoad = new FXMLLoader(getClass().getResource("menu.fxml"));
        Scene menu = new Scene(menuLoad.load());
        stage.setScene(menu);

        MenuController menuController = menuLoad.getController();
        menuController.setStage(stage);
    }


}
