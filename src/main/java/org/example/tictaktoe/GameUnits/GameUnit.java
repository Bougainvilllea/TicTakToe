package org.example.tictaktoe.GameUnits;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.tictaktoe.Field;

public abstract class GameUnit {
    protected Field field;
    protected double sizeX;
    protected double sizeY;
    protected double thickness;
    Color color;

    public GameUnit() {

    }

    public GameUnit(Color color, double sizeY, double sizeX, double thickness) {
        this.color = color;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.thickness = thickness;
    }

    public abstract void render(double x, double y, Pane pane);

}
