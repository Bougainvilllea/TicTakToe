package org.example.tictaktoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public void loadMenu(Stage stage) throws Exception {

        FXMLLoader menuLoad = new FXMLLoader(getClass().getResource("menu.fxml"));
        Scene menu = new Scene(menuLoad.load());
        stage.setScene(menu);

        MenuController menuController = menuLoad.getController();
        menuController.setStage(stage);
    }


    @Override
    public void start(Stage stage) throws Exception {
        loadMenu(stage);

        stage.setTitle("TicTakToe");
        stage.setMinWidth(650);
        stage.setMinHeight(425);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {launch(args);}

}
