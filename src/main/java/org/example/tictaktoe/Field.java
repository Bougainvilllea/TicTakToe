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
    private final HashMap<Integer, List<Double>> cellsCoordinates = new HashMap<>();
    private final Pane fieldPane;
    private final Color bordersColor;
    private final Color backGroundColor;

    private double borderThickness;
    private double borderThicknessProportion;
    private double x;
    private double y;
    private double smallestSize;
    private Canvas canvas = null;
    private double cellSize;

    public double width;
    public double height;





    public Field(Pane fieldPane, Color bordersColor, Color backGroundColor, double borderThickness){

        for(int i = 0; i < 9; i++){
            fieldCells.add(new EmptyUnit());
        }

        this.fieldPane = fieldPane;
        updateSizes();

        this.canvas = new Canvas(getPaneWidth(), getPaneHeight());
        this.backGroundColor = backGroundColor;
        this.bordersColor = bordersColor;
        this.borderThickness = borderThickness;
        this.borderThicknessProportion = borderThickness / width;

        fieldPane.getChildren().add(canvas);

        updateField();
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
        updateSizes();
        updatePos();
        updateCoordinatesCells();
        clearCanvas();
        drawBoard();
        drawUnits();
    }

    private void updateCoordinatesCells(){

        int count = 0;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                cellsCoordinates.put(count, List.of(x + (cellSize + borderThickness) * i + borderThickness/2,
                        y + (cellSize + borderThickness) * j + borderThickness/2));
                count++;
            }
        }
    }

    private void clearCanvas(){
        if(this.canvas != null){
            fieldPane.getChildren().remove(canvas);
            canvas = new Canvas(getPaneWidth(), getPaneHeight());
            fieldPane.getChildren().add(canvas);

            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, getPaneWidth(), getPaneHeight());
        }

    }

    private void updatePos(){
        this.x = (getPaneWidth() - smallestSize) / 2;
        this.y = (getPaneHeight() - smallestSize) / 2;
    }

    private void updateSizes(){
        if(getPaneWidth() >= getPaneHeight()){
            smallestSize = getPaneHeight();
        }
        else{
            smallestSize = getPaneWidth();
        }
        this.width = smallestSize;
        this.height = smallestSize;

        borderThickness = width * borderThicknessProportion;
        cellSize = width / 3 - borderThickness;

    }

    private void drawBoard(){
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
    }

    private void drawUnits(){
        for (int i = 0; i < 9; i++) {
            List<Double> coordinatesOnPane = cellsCoordinates.get(i);
            fittingUnits(fieldCells.get(i));
            fieldCells.get(i).render(coordinatesOnPane.getFirst(), coordinatesOnPane.getLast(), fieldPane);
        }
    }

    private void fittingUnits(GameUnit unit){
        unit.resize(cellSize, cellSize);
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


    public Color getBackGroundColor(){
        return backGroundColor;
    }

}
