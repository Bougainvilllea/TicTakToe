package org.example.tictaktoe.GameUnits;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Zero extends GameUnit implements Serializable {
    private final Color backGroundColor;
    private Canvas canvas;

    public Zero(Color color, double sizeX, double sizeY, double thickness, Color backgroundColor) {
        super(color, sizeY, sizeX, thickness);
        this.backGroundColor = backgroundColor;
        this.thickness = thickness;
        this.name = "zero";
    }

    @Override
    public HashMap<String, Object> toHashMap(int cellNumToInsert) {
        HashMap<String, Object> tmp =  new HashMap<String, Object>();
        tmp.put("color", List.of(color.getRed(), color.getGreen(), color.getBlue()));
        tmp.put("backGroundColor", List.of(backGroundColor.getRed(), backGroundColor.getGreen(), backGroundColor.getBlue()));
        tmp.put("unitClass", "zero");
        tmp.put("cellNumToInsert", cellNumToInsert);
        tmp.put("thickness", thickness);

        return tmp;
    }

    @Override
    public void render(double x, double y, Pane pane){
        deleteOldCanvas(pane);
        canvas = new Canvas(pane.getWidth(), pane.getHeight());
        pane.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillOval(x, y, sizeX, sizeY);
        gc.setFill(backGroundColor);
        gc.fillOval(x + (sizeX * thickness) / 2 , y + (sizeY * thickness) / 2, sizeX * (1 - thickness), sizeY * (1 - thickness));
    }

    @Override
    protected void deleteOldCanvas(Pane pane){
        pane.getChildren().remove(canvas);
    }

    @Override
    public void resize(double sizeX, double sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

}
