package org.example.tictaktoe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button buttonCreateGame;

    @FXML
    private Button buttonJoinGame;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Label menuText;

    private Stage stage;

    @FXML
    void createGameButtonController(ActionEvent event) throws IOException {
        if(event.getSource() == buttonCreateGame) {
            loadGame(true, stage);
        }
    }

    @FXML
    void joinGameButtonController(ActionEvent event) throws IOException {
        if(event.getSource() == buttonJoinGame) {
            loadGame(false, stage);
        }
    }

    @FXML
    void initialize() {
        assert buttonCreateGame != null : "fx:id=\"buttonCreateGame\" was not injected: check your FXML file 'menu.fxml'.";
        assert buttonJoinGame != null : "fx:id=\"buttonJoinGame\" was not injected: check your FXML file 'menu.fxml'.";
        assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'menu.fxml'.";
        assert menuText != null : "fx:id=\"menuText\" was not injected: check your FXML file 'menu.fxml'.";

    }

    private void loadGame(boolean isServer, Stage stage) throws IOException {
        FXMLLoader gameLoad = new FXMLLoader(getClass().getResource("field.fxml"));
        Scene gameScene = new Scene(gameLoad.load());
        stage.setScene(gameScene);

        GameController controller = gameLoad.getController();
        controller.setStage(stage);
        controller.startOnlineGame(isServer);
        stage.setResizable(true);
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
