package view;

import control.Controller;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Kvarfordt-Tanner-Assn8
 * Created on 11/19/2016
 *
 * This extension of GridPane serves as the game board for the Minesweeperish project.
 * It contains a grid of GameNodes for display, is in charge of the game board dimensions (given in number of GameNodes
 * in the x and y directions), is in charge of the percentage of GameNodes which are to be bombs (and subsequently how
 * many bombs are in the GameBoard), and is in charge of telling GameNodes how many bombs they neighbor.
 *
 * @author Tanner Kvarfordt
 * @version 1.0
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class GameBoard extends GridPane {

    /**
     * The overruling Controller for the GameBoard to allow interaction with other aspects of the game
     */
    private Controller controller;

    /**
     * Keeps track of how many GameNodes have been disabled for the current game
     */
    private int disabledGameNodes;

    /**
     * Determines whether or not clicks will have any effect on the GameBoard
     */
    private boolean isClickable;

    /**
     * Number of columns for GameBoard
     */
    private int gridSizeX;

    /**
     * Number of rows for GameBoard
     */
    private int gridSizeY;

    /**
     * Percentage of the GameBoard that will contain bombs
     */
    private double percentBombs;

    /**
     * Number of bombs the GameBoard will contain
     */
    private int numBombs;

    /**
     * Counter for the user to know how many unmarked bombs are present in the GameBoard
     */
    private int possibleBombs;

    /**
     * A virtual grid to place the all GameNodes in their final locations
     * The way this is set up, the outermost container corresponds to rows in the grid
     * - that is, the outer ArrayList holds rows of GameNodes
     */
    private ArrayList<ArrayList<Cell>> virtualGrid;

    /**
     * ArrayList containing all bombs nodes for the game
     */
    private ArrayList<Cell> bombsAndMarkedCells;


    /**
     * Creates a new GameBoard - initializes all member variables
     * Sets the stylesheet for the class
     * Defaults:
     * gridSizeX = 20
     * gridSizeY = 20
     * 25% bombs
     */
    public GameBoard(Controller controller) {
        this.controller = controller;
        disabledGameNodes = 0;
        isClickable = false;

        // Set default grid sizes
        gridSizeX = 20;
        gridSizeY = 20;

        // Compute number of bombs
        percentBombs = 0.14; //TODO Assignment default is 0.25
        Double tempBombs = gridSizeX * gridSizeY * percentBombs;
        numBombs = tempBombs.intValue();
        possibleBombs = numBombs;
        bombsAndMarkedCells = new ArrayList<>(numBombs);

        // Set GameBoard constraints
        setGameBoardConstraints(gridSizeX, gridSizeY);

        // Create initial ArrayList to contain all GameNodes needed
        ArrayList<Cell> initContainer = new ArrayList<>(gridSizeX * gridSizeY);

        // Create however many GameNodes needed to be bombs
        for (int i = 0; i < numBombs; ++i) {
            Cell g = new Cell(true);
            initContainer.add(g);
            g.setId("bomb-button");
            setGameNodeHandler(g);
            bombsAndMarkedCells.add(g);
        }

        // Set the remaining GameNodes to not have bombs
        for (int i = numBombs; i < (gridSizeX * gridSizeY); ++i) {
            Cell g = new Cell(false);
            initContainer.add(g);
            setGameNodeHandler(g);
        }

        // Shuffle the initial container so bomb locations are randomized
        Collections.shuffle(initContainer);

        /* Create a virtual grid to place the GameNodes in their final locations
        *  The way this is set up, the outermost container corresponds to rows in the grid
        *  - that is, the outer ArrayList holds rows of GameNodes
        *  MakeRows takes the initial 1-D container and then
        *       1) Breaks it into its proper rows based on gridSizeX and gridSizeY
        *       2) Essentially creates the grid by inserting those rows into a 2-D ArrayList
        *          where the outermost ArrayList corresponds to the rows and the innermost
        *          ArrayList to the columns.
        */
        virtualGrid = makeRows(initContainer);

        // Tell each Cell its location, and update each Cell's neighboringBomb count
        for (int i = 0; i < gridSizeY; ++i) {
            for (int j = 0; j < gridSizeX; ++j) {
                virtualGrid.get(i).get(j).setGridCoords(i, j);
                if (virtualGrid.get(i).get(j).isBomb()) {
                    updateNeighbors(i, j);
                }
                this.add(virtualGrid.get(i).get(j), j, i);

                //fix issue where there is space between nodes even with hgap and vgap set to 0
                setMargin(virtualGrid.get(i).get(j), new Insets(-1));
            }
        }

    }

    /**
     * Does two things:
     * 1) Breaks initContainer into its proper rows based on gridSizeX and gridSizeY
     * 2) Essentially creates the grid by inserting those rows into a 2-D ArrayList
     * where the outermost ArrayList corresponds to the rows and the innermost
     * ArrayList to the columns.
     *
     * @param initContainer the initial container which holds however many GameNodes the GameBoard will need, but
     *                      not in any order and not in the proper grid format
     * @return a 2-D ArrayList as a grid containing all GameNodes in the proper format
     */
    private ArrayList<ArrayList<Cell>> makeRows(ArrayList<Cell> initContainer) {
        ArrayList<ArrayList<Cell>> rows = new ArrayList<>(gridSizeY);
        int j = 0;
        for (int i = 0; i < gridSizeY; ++i, j += gridSizeX) {
            // .subList(int fromIndex, int toIndex) fromIndex is inclusive, toIndex is exclusive
            ArrayList<Cell> aRow = new ArrayList<>(initContainer.subList(j, j + gridSizeX));
            rows.add(aRow);
        }
        return rows;
    }

    /**
     * Sets the number of mineGrid cols and rows
     *
     * @param numCols number of cols for mineGrid
     * @param numRows number of rows for mineGrid
     */
    private void setGameBoardConstraints(final int numCols, final int numRows) {
        this.setVgap(0);
        this.setHgap(0);
        for (int i = 0; i < numCols; ++i) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(((double) 100) / numCols);
            this.getColumnConstraints().add(colConstraints);
        }
        for (int i = 0; i < numRows; ++i) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(((double) 100) / numRows);
            this.getRowConstraints().add(rowConstraints);
        }
    }

    /**
     * @return number of columns for the GameBoard
     */
    @SuppressWarnings("unused")
    public int getGridSizeX() {
        return gridSizeX;
    }

    /**
     * @return number of rows for the GameBoard
     */
    @SuppressWarnings("unused")
    public int getGridSizeY() {
        return gridSizeY;
    }

    /**
     * Increments a Cell's count of how many bombs it is touching
     *
     * @param row row index of the Cell whose neighboringBombs count is to be updated
     * @param col column index of the Cell whose neighboringBombs count is to be updated
     */
    private void incBombsTouching(int row, int col) {
        if (!accessIsInBounds(row, col)) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            virtualGrid.get(row).get(col).incNeighboringBombs();
        }
    }

    /**
     * This method is to be called when a Cell contains a bomb.
     * It tells that Cell's neighbors that it contains a bomb
     * - that is, it increments the bomb's neighbors' counts of how many bombs each is touching.
     *
     * @param row row index of the Cell containing a bomb
     * @param col column index of the Cell containing a bomb
     */
    private void updateNeighbors(int row, int col) {
        try {
            incBombsTouching(row, col + 1);
        } catch (Exception e) { /*do nothing; the exception is handled*/ }

        try {
            incBombsTouching(row, col - 1);
        } catch (Exception e) { /*do nothing; the exception is handled*/ }

        try {
            incBombsTouching(row + 1, col);
        } catch (Exception e) { /*do nothing; the exception is handled*/ }

        try {
            incBombsTouching(row - 1, col);
        } catch (Exception e) { /*do nothing; the exception is handled*/ }

        try {
            incBombsTouching(row - 1, col - 1);
        } catch (Exception e) { /*do nothing; the exception is handled*/ }

        try {
            incBombsTouching(row + 1, col - 1);
        } catch (Exception e) { /*do nothing; the exception is handled*/ }

        try {
            incBombsTouching(row - 1, col + 1);
        } catch (Exception e) { /*do nothing; the exception is handled*/ }

        try {
            incBombsTouching(row + 1, col + 1);
        } catch (Exception e) { /*do nothing; the exception is handled*/ }
    }

    /**
     * @return the percentage the GameBoard that contains bombs
     */
    @SuppressWarnings("unused")
    public double getPercentBombs() {
        return percentBombs;
    }

    /**
     * @return whether or not the GameBoard is clickable
     */
    @SuppressWarnings({"unused"})
    public boolean isClickable() {
        return isClickable;
    }

    /**
     * @return the number of bombs the GameBoard contains
     */
    @SuppressWarnings("unused")
    public int getNumBombs() {
        return numBombs;
    }

    /**
     * @return the counter of how many bombs the user suspects are still in the GameBoard
     */
    @SuppressWarnings("unused")
    public int getPossibleBombs() {
        return possibleBombs;
    }

    /**
     * Determines the action to be taken when a Cell is clicked
     *
     * @param g the Cell whose listener is to be set
     */
    @SuppressWarnings("Convert2Lambda")
    private void setGameNodeHandler(Cell g) {

        if (g.isBomb()) { //the Cell clicked on is a bomb
            g.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (isClickable) {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            g.setId("marked-bomb-button");
                            markNode(g);
                        } else if (!Objects.equals(g.getText(), "X") && !Objects.equals(g.getText(), "?")) {
                            if (!Objects.equals(g.getText(), "X") && !Objects.equals(g.getText(), "?")) {
                                //This Cell is a bomb
                                g.setDisable(true);
                                controller.endGame(false);
                            }
                        }
                    }
                }
            });
        } else { //the Cell clicked on is not a bomb
            g.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (isClickable) {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            g.setId("marked-safe-button");
                            bombsAndMarkedCells.add(g);
                            markNode(g);
                        } else if (!Objects.equals(g.getText(), "X") && !Objects.equals(g.getText(), "?")) {
                            //This Cell is guaranteed to not be a bomb
                            openNeighbors(((Cell) event.getSource()).getRow(), ((Cell) event.getSource()).getCol());
                            if (controller.checkForWin()) {
                                controller.endGame(true);
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * Cycles through the series of markings on Cell when right clicked
     *
     * @param g the Cell to be marked or unmarked
     */
    private void markNode(Cell g) {
        if (g.getText() == null) {
            g.setText("X");
            --possibleBombs;
            controller.updatePossibleBombs(possibleBombs);
        } else if (Objects.equals(g.getText(), "X")) {
            g.setText("?");
            ++possibleBombs;
            controller.updatePossibleBombs(possibleBombs);
        } else {
            if (Objects.equals(g.getId(), "marked-safe-button")) bombsAndMarkedCells.remove(g);
            g.setId(null);
            if (g.isBomb()) g.setId("bomb-button");
            g.setText(null);
        }
    }

    /**
     * Sets whether or not clicks on the GameBoard will have any effect
     *
     * @param isClickable true if setting the GameBoard to be clickable, false if not
     */
    public void setClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    /**
     * If a Cell clicked is touching zero bombs, this method recursively opens neighboring GameNodes until
     * it reaches a Cell that is touching a bomb (in each direction)
     *
     * @param row row of the Cell to potentially be opened
     * @param col column of the Cell to potentially be opened
     */
    private void openNeighbors(int row, int col) {
        if (accessIsInBounds(row, col)) {
            Cell g = virtualGrid.get(row).get(col);
            if (!g.isBomb() && !g.isDisable() && g.getText() == null) {
                g.setDisable(true);
                ++disabledGameNodes;
                g.setText(Integer.toString(g.getNeighboringBombs()));
                if (!controller.checkForWin()) {

                    if (g.getNeighboringBombs() == 0) {
                        openNeighbors(row, col + 1);
                        openNeighbors(row, col - 1);
                        openNeighbors(row + 1, col);
                        openNeighbors(row - 1, col);
                        openNeighbors(row - 1, col - 1);
                        openNeighbors(row + 1, col - 1);
                        openNeighbors(row - 1, col + 1);
                        openNeighbors(row + 1, col + 1);
                    }
                }
            }
        }
    }

    /**
     * Determines whether an access to a point in the GameBoard grid is legal
     *
     * @param row row of Cell to be accessed
     * @param col column of Cell to be accessed
     * @return whether or not the access is in bounds of the virtualGrid array
     */
    private boolean accessIsInBounds(int row, int col) {
        return (row >= 0 && row < gridSizeY && col >= 0 && col < gridSizeX);
    }

    /**
     * @return how many GameNodes have been disabled for the current game
     */
    public int getDisabledGameNodes() {
        return disabledGameNodes;
    }

    /**
     * Disables all bombs and marked Cells - to be called at the end of the game
     */
    public void disableAllBombsAndMarkedCells() {
        for (Cell c : bombsAndMarkedCells) {
            if (c.isBomb() && c.getText() == null) c.setText("B");
            if (c.getId() != null) c.setDisable(true);
        }
    }

    /**
     * Marks all unmarked bombs in the GameBoard - to be called if the user has won
     */
    public void markBombsOnWin() {
        //noinspection Convert2streamapi
        for (Cell c : bombsAndMarkedCells) {
            if (c.isBomb()) {
                c.setId("marked-bomb-button");
                c.setText("X");
            }
        }
    }

}
