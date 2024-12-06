package org.example.tictaktoe.GameUnits;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Cross extends GameUnit{
    private final List<Canvas> canvasList;

    public Cross(Color color, int sizeX, int sizeY, double thickness) {
        super(color, sizeY, sizeX, thickness);
        canvasList = new ArrayList<>();
    }

    @Override
    public void render(double x, double y, Pane pane){
        deleteOldCanvas(pane);
        drawStick(x, y, pane, -1);
        drawStick(x, y, pane, 1);
    }

    private void drawStick(double x, double y, Pane pane, int leftToRight){

        Canvas canvas = new Canvas(pane.getWidth(), pane.getHeight());
        pane.getChildren().add(canvas);
        canvasList.add(canvas);

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

    @Override
    protected void deleteOldCanvas(Pane pane){
        for(Canvas canvas : canvasList){
            pane.getChildren().remove(canvas);
        }
        canvasList.clear();
    }

    @Override
    public void resize(double sizeX, double sizeY) {
        this.thickness = (sizeX / this.sizeX) * this.thickness;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
}
