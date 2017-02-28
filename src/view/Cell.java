package view;

import javafx.scene.control.Button;

/**
 * Kvarfordt-Tanner-Assn8
 * Created on 11/20/2016
 *
 * GameNodes will be used to populate the GameBoard for the minesweeperish game.
 * Each Cell knows its grid location in the GameBoard, and whether or not it is a mine/bomb.
 *
 * @author Tanner Kvarfordt
 * @version 1.0
 */
@SuppressWarnings("WeakerAccess")
public class Cell extends Button {

    /**
     * Row of the GameBoard in which the Cell is located
     */
    private int row;

    /**
     * Column of the GameBoard in which the Cell is located
     */
    private int col;

    /**
     * Flag stating whether or not this Cell contains a mine/bomb
     */
    private boolean isBomb;

    /**
     * The number of bombs/mines the Cell touches
     */
    private int neighboringBombs;

    /**
     * Cell Constructor - initializes all member variables
     * Sets button text to null
     *
     * @param isBomb flags the new Cell as a bomb if true
     */
    public Cell(boolean isBomb) {
        this();
        this.setText(null);
        neighboringBombs = 0;
        this.isBomb = isBomb;
        this.setMinWidth(32);
        this.setMinHeight(32);
    }

    private Cell() {
        this.getStylesheets().addAll("view/GameNodeStyles.css");
    }

    /**
     * Setter for row
     *
     * @param row the Cell's row index in the grid
     */
    @SuppressWarnings("unused")
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Setter for col
     *
     * @param column the Cell's column index in the grid
     */
    @SuppressWarnings("unused")
    public void setCol(int column) {
        this.col = column;
    }

    /**
     * Setter for both row and column indices of a Cell
     *
     * @param row    the Cell's row index in the grid
     * @param column the Cell's column index in the grid
     */
    public void setGridCoords(int row, int column) {
        this.row = row;
        this.col = column;
    }

    /**
     * @return the Cell's row index in the grid
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the Cell's column index in the grid
     */
    public int getCol() {
        return col;
    }

    /**
     * Increments neighboringBombs count by 1
     */
    public void incNeighboringBombs() {
        ++neighboringBombs;
    }

    /**
     * @return true if the Cell is a bomb, false otherwise
     */
    public boolean isBomb() {
        return isBomb;
    }

    /**
     * @return the number of bombs/mines the Cell touches
     */
    public int getNeighboringBombs() {
        return neighboringBombs;
    }

}
