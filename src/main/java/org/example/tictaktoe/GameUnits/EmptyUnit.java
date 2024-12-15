package org.example.tictaktoe.GameUnits;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.HashMap;

public class EmptyUnit extends GameUnit implements Serializable {

    public EmptyUnit(){
        this.name = "gameUnit";
    }

    @Override
    public HashMap<String, Object> toHashMap(int cellNumToInsert) {
        HashMap<String, Object> tmp =  new HashMap<String, Object>();
        tmp.put("unitClass", "emptyUnit");
        tmp.put("cellNumToInsert", cellNumToInsert);
        return tmp;
    }

    @Override
    public void render(double x, double y, Pane pane) {

    }

    @Override
    protected void deleteOldCanvas(Pane pane){

    }

    @Override
    public void resize(double sizeX, double sizeY) {

    }

}
