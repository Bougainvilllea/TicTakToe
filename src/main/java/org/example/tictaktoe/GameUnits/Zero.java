package org.example.tictaktoe.GameUnits;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Zero extends GameUnit{
    private final Color backGroundColor;

    public Zero(Color color, double sizeX, double sizeY, double thickness, Color backgroundColor) {
        super(color, sizeY, sizeX, thickness);
        this.backGroundColor = backgroundColor;
        this.thickness = (thickness / 100) * 2;
        System.out.println(this.thickness);
    }

    @Override
    public void render(double x, double y, Pane pane){
        Canvas canvas = new Canvas(pane.getPrefWidth(), pane.getPrefHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);
        gc.setFill(color);
        gc.fillOval(x, y, sizeX, sizeY);
        gc.setFill(backGroundColor);
        gc.fillOval(x + (sizeX * thickness) / 2 , y + (sizeY * thickness) / 2, sizeX * (1 - thickness), sizeY * (1 - thickness));
    }

}
