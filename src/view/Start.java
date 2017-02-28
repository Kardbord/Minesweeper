package view;

import control.Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Kvarfordt-Tanner-Assn8
 * Created on 11/21/2016
 *
 * Contains the start method for the Minesweeperish program.
 *
 * @author Tanner Kvarfordt
 * @version 1.0
 */
public class Start extends Application {

    /**
     * The overruling Controller for the game
     */
    private Controller controller;

    /**
     * GameBoard for Minesweeperish
     */
    private GameBoard gameBoard;

    /**
     * ScoreBoard for Minesweeperish
     */
    @SuppressWarnings("FieldCanBeLocal")
    private ScoreBoard scoreBoard;

    /**
     * Main pane for the program - will contain scoreBoard and gameBoard
     */
    private BorderPane mainPane;

    /**
     * Empty constructor
     */
    public Start() {
    }

    /**
     * Start method for the GUI
     *
     * @param primaryStage primaryStage for the GUI
     * @throws Exception in case something breaks lol
     */
    @SuppressWarnings("Convert2Lambda")
    @Override
    public void start(Stage primaryStage) throws Exception {
        controller = new Controller();
        controller.setStart(this);

        mainPane = new BorderPane();

        gameBoard = new GameBoard(controller);

        scoreBoard = new ScoreBoard(gameBoard.getPossibleBombs(), controller);
        scoreBoard.setAlignment(Pos.CENTER);
        scoreBoard.setStartBtnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameBoard = new GameBoard(controller);
                mainPane.setCenter(gameBoard);
                gameBoard.setClickable(true);
                scoreBoard.setStartBtnDisable(true);
                scoreBoard.startTimer();
                controller.setGameBoard(gameBoard);
                controller.setScoreBoard(scoreBoard);
            }
        });

        mainPane.setTop(scoreBoard);
        mainPane.setCenter(gameBoard);

        controller.setGameBoard(gameBoard);
        controller.setScoreBoard(scoreBoard);

        Scene scene1 = new Scene(mainPane);
        primaryStage.setScene(scene1);
        primaryStage.setTitle("Minesweeperish");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Stop method override
     * Executes before the program is shut down
     */
    @Override
    public void stop() {
        if (!scoreBoard.timerIsStopped()) scoreBoard.stopTimer();
    }

    /**
     * Sets ScoreBoard for the game
     *
     * @param scoreBoard ScoreBoard for the game
     */
    @SuppressWarnings("Convert2Lambda")
    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
        scoreBoard.setStartBtnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scoreBoard.startTimer();
                gameBoard = new GameBoard(controller);
                mainPane.setCenter(gameBoard);
                gameBoard.setClickable(true);
                scoreBoard.setStartBtnDisable(true);
                controller.setGameBoard(gameBoard);
            }
        });
        mainPane.setTop(scoreBoard);
    }

    /**
     * Sets GameBoard for the game
     *
     * @param gameBoard GameBoard for the game
     */
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        mainPane.setCenter(gameBoard);
    }

}
