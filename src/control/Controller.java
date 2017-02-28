package control;

import javafx.scene.control.Alert;
import view.GameBoard;
import view.ScoreBoard;
import view.Start;

/**
 * Kvarfordt-Tanner-Assn8
 * Created on 11/24/2016
 *
 * Controller class for the Minesweeperish project.
 *
 * @author Tanner Kvarfordt
 * @version 1.0
 */
@SuppressWarnings("WeakerAccess")
public class Controller {

    /**
     * The GameBoard for the current game being controlled
     */
    private GameBoard gameBoard;

    /**
     * The ScoreBoard for the current game being controlled
     */
    private ScoreBoard scoreBoard;

    /**
     * The class containing the start method for the current game being controlled
     */
    private Start start;

    /**
     * Decrement the ScoreBoard's bombsLeft counter
     */
    public void updatePossibleBombs(int possibleBombs) {
        scoreBoard.updateBombsLeft(possibleBombs);
        start.setScoreBoard(scoreBoard);
    }

    /**
     * Checks the win condition to see if the player has won
     *
     * @return whether or not the player has won
     */
    public boolean checkForWin() {
        return (gameBoard.getDisabledGameNodes()
                == ((gameBoard.getGridSizeX() * gameBoard.getGridSizeY()) - gameBoard.getNumBombs()));
    }

    /**
     * Ends the game in the appropriate manner depending on a win or a loss
     *
     * @param isWin true if the game was won by the player, false if otherwise
     */
    public void endGame(boolean isWin) {
        if (isWin) {
            gameBoard.setClickable(false);
            scoreBoard.stopTimer();
            gameBoard.markBombsOnWin();
            gameBoard.disableAllBombsAndMarkedCells();
            Alert winner = new Alert(Alert.AlertType.INFORMATION);
            winner.setTitle("Winner!");
            winner.setGraphic(null);
            winner.setHeaderText(null);
            winner.setContentText("Congratulations! It took you " + Integer.toString(scoreBoard.getTime())
                    + " seconds to win!");
            winner.showAndWait();
            restartGame();
        } else {
            gameBoard.setClickable(false);
            scoreBoard.stopTimer();
            gameBoard.disableAllBombsAndMarkedCells();
            Alert loser = new Alert(Alert.AlertType.INFORMATION);
            loser.setGraphic(null);
            loser.setHeaderText(null);
            loser.setTitle("You Lose!");
            loser.setContentText("Sorry, you lose! It took you " + Integer.toString(scoreBoard.getTime())
                    + " seconds to lose!");
            loser.showAndWait();
            restartGame();
        }
    }

    /**
     * Sets gameBoard
     *
     * @param gameBoard the GameBoard for the current game being controlled
     */
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    /**
     * Sets scoreBoard
     *
     * @param scoreBoard the ScoreBoard for the current game being controlled
     */
    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    /**
     * sets start
     *
     * @param start the class containing the start method for the current game being controlled
     */
    public void setStart(Start start) {
        this.start = start;
    }

    /**
     * Empty constructor to create an instance of Controller
     */
    public Controller() {
    }

    /**
     * Restarts the game
     */
    public void restartGame() {
        gameBoard = new GameBoard(this);
        scoreBoard = new ScoreBoard(gameBoard.getPossibleBombs(), this);
        start.setGameBoard(gameBoard);
        start.setScoreBoard(scoreBoard);
    }

}

