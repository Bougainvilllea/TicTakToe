package org.example.tictaktoe.GameUnits;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.tictaktoe.Field;

public abstract class GameUnit {
    protected Field field;
    protected double sizeX;
    protected double sizeY;
    protected double thickness;
    protected Color color;
    protected Canvas canvas;

    public GameUnit() {

    }

    public GameUnit(Color color, double sizeY, double sizeX, double thickness) {
        this.color = color;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.thickness = thickness;
        this.canvas = new Canvas(sizeX, sizeY);
    }

    public abstract void render(double x, double y, Pane pane);
    protected abstract void deleteOldCanvas(Pane pane);
    public abstract void resize(double sizeX, double sizeY);

}
