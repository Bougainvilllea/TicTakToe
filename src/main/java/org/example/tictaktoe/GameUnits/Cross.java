package org.example.tictaktoe.GameUnits;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Cross extends GameUnit{

    public Cross(Color color, int sizeX, int sizeY, double thickness) {
        super(color, sizeY, sizeX, thickness);
    }

    @Override
    public void render(double x, double y, Pane pane){
        Canvas canvas = new Canvas(pane.getPrefWidth(), pane.getPrefHeight());
        pane.getChildren().add(canvas);

        drawStick(x, y, pane, -1);
        drawStick(x, y, pane, 1);
    }

    private void drawStick(double x, double y, Pane mainPane, int leftToRight){
        Canvas canvas = new Canvas(mainPane.getPrefWidth(), mainPane.getPrefHeight());
        mainPane.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(color);
        gc.fillOval(x, y, 2, 2);
        gc.translate(x + sizeX/2 , y + sizeY/2);

        double k = thickness / Math.sqrt(sizeX*sizeX + sizeY*sizeY); //similarity coefficient

        double b = sizeY * k;
        double a = sizeX * k;

        double rectSizeX = this.thickness;
        double rectSizeY = Math.sqrt(Math.pow(sizeX - b, 2) + Math.pow(sizeY - a, 2));

        double angle = Math.toDegrees(Math.atan(sizeY/sizeX));

        gc.rotate(90.0 - (int) angle * leftToRight);
        gc.fillRect(-rectSizeX/2 , -rectSizeY/2, rectSizeX, rectSizeY);
    }
}
