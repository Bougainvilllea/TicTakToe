package org.example.tictaktoe;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.tictaktoe.GameUnits.EmptyUnit;
import org.example.tictaktoe.GameUnits.GameUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Field {
    private final List<GameUnit> fieldCells = new ArrayList<>();
    private HashMap<Integer, List<Double>> cellsCoordinates = new HashMap<>();
    private final Pane fieldPane;
    private final Canvas canvas;
    private final Color bordersColor;
    private final Color backGroundColor;
    private final double borderThickness;
    public double width;
    public double height;
    private double x;
    private double y;
    private double smallestSize;
    private double biggestSize;



    public Field(Pane fieldPane, Color bordersColor, Color backGroundColor, double borderThickness){
        for(int i = 0; i < 9; i++){
            fieldCells.add(new EmptyUnit());
        }

        this.fieldPane = fieldPane;

        updateSizes();

        this.width = smallestSize;
        this.height = smallestSize;

        this.x = (getPaneWidth() - smallestSize) / 2;
        this.y = (getPaneHeight() - smallestSize) / 2;


        this.canvas = new Canvas(getPaneWidth(), getPaneHeight());
        fieldPane.getChildren().add(canvas);

        this.backGroundColor = backGroundColor;
        this.bordersColor = bordersColor;

        this.borderThickness = borderThickness;

        double cellWidthHeight = width / 3 - borderThickness;

        int count = 0;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                cellsCoordinates.put(count, List.of(x + (cellWidthHeight + borderThickness) * i + borderThickness/2,
                        y + (cellWidthHeight + borderThickness) * j + borderThickness/2));
                count++;
            }
        }
        for (int i = 0; i < 9; i++){
            System.out.println(cellsCoordinates.get(i));
        }
    }

    private Integer coordinatesToNum(int x, int y){
        return (y - 1) * 3 + x - 1;
    }

    public void insertGameUnit(int x, int y, GameUnit unit) {
        if(fieldCells.get(coordinatesToNum(x, y)).getClass() == EmptyUnit.class){
            fieldCells.set(coordinatesToNum(x, y), unit);
        }

        //седлать исключение, когда пытаеся поставить юнита в занятую клетку
    }

    public void updateField(){

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(backGroundColor);
        gc.fillRect(x, y, width, height);

        gc.setFill(bordersColor);

        for (int i = 0; i <= 3; i++){
            gc.fillRect( x + (width / 3) * i - borderThickness / 2, y, borderThickness, height);
        }

        for (int i = 0; i <= 3; i++){
            gc.fillRect(x, y + (height / 3) * i - borderThickness / 2, width, borderThickness);
        }

        for (int i = 0; i < 9; i++) {
            List<Double> coordinatesOnPane = cellsCoordinates.get(i);
            fieldCells.get(i).render(coordinatesOnPane.getFirst(), coordinatesOnPane.getLast(), fieldPane);
        }



    }

    private double getPaneWidth(){
        double tmpWidth = fieldPane.getWidth();

        if (tmpWidth == 0){
            tmpWidth = fieldPane.getPrefWidth();
        }

        return tmpWidth;
    }

    private double getPaneHeight(){
        double tmpHeight = fieldPane.getHeight();

        if (tmpHeight == 0){
            tmpHeight = fieldPane.getPrefHeight();
        }

        return tmpHeight;
    }


    private void updateSizes(){
        if(getPaneWidth() >= getPaneHeight()){
            smallestSize = getPaneHeight();
            biggestSize = getPaneWidth();
        }
        else{
            smallestSize = getPaneWidth();
            biggestSize = getPaneHeight();
        }

    }

    public Color getBackGroundColor(){
        return backGroundColor;
    }

}
