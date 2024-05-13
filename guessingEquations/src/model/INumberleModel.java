package src.model;

import java.util.List;

public interface INumberleModel {
    // max attempts
    int MAX_ATTEMPTS = 6;
    // input size
    int INPUT_SIZE=7;
    //One flag should, if set, cause an error message to be displayed if the equation is not valid; this will not then count as one of the tries.
    boolean equationValidCheck=true;
    //display the target equation for testing purposes.
    boolean targetCheck=true;
    //cause the equation to be randomly selected. If unset, the equation will be fixed.
    boolean randomCheck=true;

    /**
     * init
     */
    void initialize();

    /**
     * process the input
     * @param input s string
     * @return true/false
     */
    String processInput(String input);

    /**
     * is game over?
     * @return true/false
     */
    boolean isGameOver();
    /**
     * set remainingAttempts for junit
     * @param remainingAttempts remainingAttempts
     */
    void setRemainingAttempts(int remainingAttempts);
    /**
     * whether won
     * @return true/false
     */
    boolean isGameWon();

    /**
     * get the target equation
     * @return target equation
     */
    String getTargetNumber();

    /**
     * get current guess
     * @return guess
     */
    StringBuilder getCurrentGuess();

    /**
     * get remaining attempts
     * @return remaining attempts
     */
    int getRemainingAttempts();

    /**
     * start a new game
     */
    void startNewGame();

    /**
     * getCurrentState2String for CLIAPP
     * @return string
     */
    String getCurrentState2String();

    /**
     * get input size
     * @return
     */
    int getINPUT_SIZE();

    /**
     * get current state
     * @return list<node>
     */
    List<Node> getCurrentState();

    /**
     * set the target equation
     * @param s equation
     */
    void setTargetNumber(String s);
}