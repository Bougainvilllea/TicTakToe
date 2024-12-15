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

    public void updateField(){
        updateSizes();
        updatePos();
        updateCoordinatesCells();
        clearCanvas();
        drawBoard();
        drawUnits();
    }

    public Integer getNumCellContained(double x, double y){

        for(int i = 0; i < 9; i++){
            List<Double> cellXY = cellsCoordinates.get(i);
            if((x >= cellXY.getFirst() && x <= cellXY.getFirst() + cellSize) && (y >= cellXY.getLast() && y <= cellXY.getLast() + cellSize)){
                return i;
            }
        }
        return -1;
    }


    public void resize(double w, double h){
        fieldPane.resize(w, h);
    }

    public Color getBackGroundColor(){
        return backGroundColor;
    }

    public void insertGameUnit(int cellNum, GameUnit unit) {
        if (cellNum < 0 || cellNum >= 9) {
            return;
        }
        if(fieldCells.get(cellNum).getClass() == EmptyUnit.class){
            fieldCells.set(cellNum, unit);
        }

        //седлать исключение, когда пытаются поставить юнита в занятую клетку
        //седлать исключение, когда пытаются поставить юнита в несуществующ. клетку
    }

    public String getWinningTeam(){
        for(int i = 1; i < 3; i++){

//            horizontal
            if(fieldCells.get(coordinatesToNum(1, i)).getClass() != EmptyUnit.class){
                if((fieldCells.get(coordinatesToNum(1, i)).getClass() == fieldCells.get(coordinatesToNum(2, i)).getClass())
                        && fieldCells.get(coordinatesToNum(3, i)).getClass() == fieldCells.get(coordinatesToNum(1, i)).getClass()){
                    return fieldCells.get(coordinatesToNum(1, i)).name;
                }
            }

//            vertical
            if (fieldCells.get(coordinatesToNum(i, 1)).getClass() != EmptyUnit.class){
                if((fieldCells.get(coordinatesToNum(i, 1)).getClass() == fieldCells.get(coordinatesToNum(i, 2)).getClass())
                        && fieldCells.get(coordinatesToNum(i, 3)).getClass() == fieldCells.get(coordinatesToNum(i, 1)).getClass()){
                    return fieldCells.get(coordinatesToNum(i, 1)).name;
                }
            }
        }

        //              diagonal
        if (fieldCells.get(coordinatesToNum(1, 1)).getClass() != EmptyUnit.class){

            if((fieldCells.get(coordinatesToNum(1, 1)).getClass() == fieldCells.get(coordinatesToNum(2, 2)).getClass())
                    && fieldCells.get(coordinatesToNum(3, 3)).getClass() == fieldCells.get(coordinatesToNum(1, 1)).getClass()){
                return fieldCells.get(coordinatesToNum(1, 1)).name;
            }

        }

        //              diagonal2
        if (fieldCells.get(coordinatesToNum(3, 1)).getClass() != EmptyUnit.class){

            if((fieldCells.get(coordinatesToNum(3, 1)).getClass() == fieldCells.get(coordinatesToNum(2, 2)).getClass())
                    && fieldCells.get(coordinatesToNum(1, 3)).getClass() == fieldCells.get(coordinatesToNum(3, 1)).getClass()){
                return fieldCells.get(coordinatesToNum(3, 1)).name;
            }

        }

        boolean allCellsOccupied = true;
        for(int i = 0; i < 9; i++){
            if(fieldCells.get(i).getClass() == EmptyUnit.class){
                allCellsOccupied = false;
            }
        }
        if (allCellsOccupied){
            return "draw";
        }

        return "game in progress";
    }

    private void updateCoordinatesCells(){
        int count = 0;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                cellsCoordinates.put(count, List.of(x + (cellSize + borderThickness) * j + borderThickness/2,
                        y + (cellSize + borderThickness) * i + borderThickness/2));
                count++;
            }
        }
    }

    private Integer coordinatesToNum(int x, int y){
        return (y - 1) * 3 + x - 1;
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



}
