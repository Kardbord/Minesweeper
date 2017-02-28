package view;

import control.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Kvarfordt-Tanner-Assn8
 * Created on 11/19/2016
 *
 * This class is the score board for the game minesweeperish.
 * It also contains the start button to start a new game.
 *
 * @author Tanner Kvarfordt
 * @version 1.0
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class ScoreBoard extends HBox {

    /**
     * The overruling Controller for the ScoreBoard to allow interaction with other aspects of the game
     */
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Controller controller;

    private Timer timer;

    /**
     * Time in seconds since the start button was pressed
     */
    private int time;

    /**
     * Counter of how many bombs the user thinks are still left in the minefield.
     */
    private int bombsLeft;

    /**
     * Button to start the next round
     */
    private Button startBtn;

    /**
     * Label corresponding to the amount of time in seconds since the start button has been pressed
     */
    private Label timerBottom;

    /**
     * Label corresponding to how many bombs the user believes is left
     */
    private Label bombsLeftLabel;

    /**
     * True if the Timer is stopped, false if not
     */
    private boolean timerIsStopped;

    /**
     * ScoreBoard constructor - initializes all member variables and adds them to the pane (this)
     */
    public ScoreBoard(int bombsLeft, Controller controller) {
        timer = new Timer(true);
        timerIsStopped = false;
        this.controller = controller;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(50);
        this.setPadding(new Insets(10, 0, 10, 0));
        this.bombsLeft = bombsLeft;
        startBtn = new Button("Start");
        time = 0;

        /*
      Timer to keep track of how long the current game has gone on
     */
        Label timerTop = new Label("Time");
        timerBottom = new Label("0");

        /*
      VBox to hold Labels informing the user about how many seconds have passed since the game began
     */
        VBox timerBox = new VBox();
        timerBox.getChildren().addAll(timerTop, timerBottom);

        /*
      VBox to hold Labels reminding the user how many bombs they think remain
     */
        VBox bombsLeftBox = new VBox();
        bombsLeftLabel = new Label(Integer.toString(bombsLeft));
        bombsLeftBox.getChildren().addAll(new Label("Bombs Left"), bombsLeftLabel);

        timerBox.setAlignment(Pos.CENTER);
        bombsLeftBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(bombsLeftBox, startBtn, timerBox);
    }

    /**
     * @return the time in seconds since the start button was pressed
     */
    public int getTime() {
        return time;
    }

    /**
     * Set the start button action
     *
     * @param e the listener for startBtn
     */
    public void setStartBtnAction(EventHandler<ActionEvent> e) {
        startBtn.setOnAction(e);
    }

    /**
     * Update the bombsLeft counter
     *
     * @param bombsLeft the number of bombs the user thinks are still left
     */
    public void updateBombsLeft(int bombsLeft) {
        this.bombsLeft = bombsLeft;
        bombsLeftLabel.setText(Integer.toString(this.bombsLeft));
    }

    /**
     * Update the game timer every second
     */
    private void updateTimer() {
        ++time;
        timerBottom.setText(Integer.toString(time));
    }

    /**
     * Starts the game timer
     */
    public void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(ScoreBoard.this::updateTimer);
            }
        }, 0, 1000);
    }

    /**
     * Stops the game timer
     */
    public void stopTimer() {
        timer.cancel();
        timerIsStopped = true;
    }

    /**
     * @return whether or not the Timer is stopped
     */
    public boolean timerIsStopped() {
        return timerIsStopped;
    }

    /**
     * Enables or disables startBtn
     *
     * @param isDisable true to disable startBtn, false to enable startBtn
     */
    @SuppressWarnings("SameParameterValue")
    public void setStartBtnDisable(boolean isDisable) {
        startBtn.setDisable(isDisable);
    }

}


