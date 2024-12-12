package org.example.tictaktoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private MenuController menuController;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader menuLoad = new FXMLLoader(getClass().getResource("menu.fxml"));
        Scene menu = new Scene(menuLoad.load());

        menuController = menuLoad.getController();
        menuController.setStage(stage);


        stage.setTitle("TicTakToe");
        stage.setScene(menu);
        stage.setMinWidth(650);
        stage.setMinHeight(425);
        stage.show();

    }

    public static void main(String[] args) {launch(args);}

}
