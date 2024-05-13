package src.model;// NumberleModel.java

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Stack;

public class NumberleModel extends Observable implements INumberleModel {
    // target equation
    private String targetNumber;
    /**
     * current guess
     */
    private StringBuilder currentGuess;
    /**
     * remainingAttempts
     */
    private int remainingAttempts;
    /**
     * won?
     */
    private boolean gameWon;
    /**
     * current state
     */
    private List<Node> currentState;

    /**
     * get input size
     * @return input size
     */
    public int getINPUT_SIZE(){
        return INPUT_SIZE;
    }

    /**
     * setter
     * @param s equation
     */
    public void setTargetNumber(String s){
        targetNumber=s;
    }

    /**
     * @ensures
     *  // If randomCheck is true, targetNumber is set by IOUtil.getAnEquation()
     *  (randomCheck ==> targetNumber.equals(IOUtil.getAnEquation())) &&
     *  // If randomCheck is false, targetNumber is set to "2*3+2=8".
     *  (!randomCheck ==> targetNumber.equals("2*3+2=8")) &&
     *  // currentGuess is initialized to a space string of length 7
     *  currentGuess.toString().equals("       ") &&
     *  // remainingAttempts is set to MAX_ATTEMPTS
     *  remainingAttempts == MAX_ATTEMPTS &&
     *  // gameWon is set to false
     *  gameWon == false &&
     *  // currentState is initialized to an empty ArrayList
     *  currentState.isEmpty();
     *
     * @signals (Exception e)
     *  // If an exception occurs during initialization, an exception is thrown
     *  false;
     */
    @Override
    public void initialize() {
        if(randomCheck){
            targetNumber = IOUtil.getAnEquation();
        }else{
            targetNumber="2*3+2=8";
        }
        currentGuess = new StringBuilder("       ");
        remainingAttempts = MAX_ATTEMPTS;
        gameWon = false;
        currentState=new ArrayList<>();
        setChanged();
        notifyObservers();
    }
    private static final int PRECEDENCE_ADD_SUB = 1;
    private static final int PRECEDENCE_MUL_DIV = 2;

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    /**
     * set remainingAttempts for junit
     * @param remainingAttempts remainingAttempts
     */
    public void setRemainingAttempts(int remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
    }

    private static int getPrecedence(char op) {
        if (op == '+' || op == '-') {
            return PRECEDENCE_ADD_SUB;
        } else if (op == '*' || op == '/') {
            return PRECEDENCE_MUL_DIV;
        }
        return 0;
    }

    /**
     * @requires
     *  // Arguments a and b are integers, and op is a valid operator
     *  (\exists char operator; operator == '+' || operator == '-' ||
     *   operator == '*' || operator == '/'; op == operator);
     *
     * @ensures
     *  // Returns the correct result based on the operator
     *  (op == '+' ==> \result == a + b) &&
     *  (op == '-' ==> \result == a - b) &&
     *  (op == '*' ==> \result == a * b) &&
     *  (op == '/' ==> (b != 0 ==> \result == a / b) &&
     *                 (b == 0 ==> \result == Integer.MIN_VALUE));
     *
     * @signals (IllegalArgumentException e)
     *  // If the operator is invalid, throw an IllegalArgumentException
     *  !(op == '+' || op == '-' || op == '*' || op == '/');
     */
    private static int applyOp(int a, int b, char op) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                return b != 0 ? a / b : Integer.MIN_VALUE;
            default:
                throw new IllegalArgumentException("Invalid operator: " + op);
        }
    }


    /**
     *
     * @param input
     * @requires
     * //The input string is not null and should contain only numbers and operators after removing Spaces
     * input != null && input.matches("[0-9\\+\\-\\*\]+");
     *
     * @ensures
     * //Returns the computed integer result
     * \result == \eval(input);
     *
     * @signals (IllegalArgumentException e)
     * //If the input contains illegal characters, an exception is thrown
     * !input.matches("[0-9\\+\\-\\*\]+");
     */
    public int getValue(String input) {
        String trimmedInput = input.replaceAll("\\s+", "");
        char[] chars = trimmedInput.toCharArray();

        Stack<Integer> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if (Character.isDigit(c)) {
                int val = 0;
                while (i < chars.length && Character.isDigit(chars[i])) {
                    val = val * 10 + (chars[i] - '0');
                    i++;
                }
                i--;
                values.push(val);
            } else if (isOperator(c)) {
                while (!ops.isEmpty() && getPrecedence(ops.peek()) >= getPrecedence(c)) {
                    int val2 = values.pop();
                    int val1 = values.pop();
                    char op = ops.pop();
                    values.push(applyOp(val1, val2, op));
                }
                ops.push(c);
            } else {
                throw new IllegalArgumentException("Invalid character: " + c);
            }
        }

        while (!ops.isEmpty()) {
            int val2 = values.pop();
            int val1 = values.pop();
            char op = ops.pop();
            values.push(applyOp(val1, val2, op));
        }

        return values.pop();
    }

    /**
     * @requires
     *  // The input is not null and the length is INPUT_SIZE
     *  input != null && input.length() == INPUT_SIZE;
     *
     * @ensures
     *  // If the input does not comply with the rules, an error message is displayed
     *  (\result.equals("Equation can only contain 0-9,+,-,*,or /") ||
     *   \result.equals("Continuous operators") ||
     *   \result.equals("0 cannot be divided") ||
     *   \result.equals("Input must be 7 characters!") ||
     *   \result.equals("Input must contain '='") ||
     *   \result.equals("Input must contain '+','-','*', or '/'") ||
     *   \result.equals("Input must contain one '='") ||
     *   \result.equals("The equations on both sides are not equal.")) ||
     *  // Returns an empty string if the input matches the rules
     *  \result.equals("");
     *
     * @signals (Exception e)
     *  // If the input is null or of incorrect length, an exception is thrown
     *  input == null || input.length() != INPUT_SIZE;
     */
    private String isValidInput(String input){
        if(!equationValidCheck){
            return "";
        }
        int i=0;
        int k=-2;
        for(char c:input.toCharArray()){
            if(!(c>='0'&&c<='9')&&c!='+'&&c!='-'&&c!='*'&&c!='/'&&c!='='){
                return "Equation can only contain 0-9,+,-,*,or /";
            }
            if(c=='+'||c=='-'||c=='*'||c=='/'||c=='='){
                if(k+1==i){
                    return "Continuous operators";
                }
                k=i;
            }
            i++;
        }
        if(input.contains("/0")){
            return "0 cannot be divided";
        }
        if(input.length()!=INPUT_SIZE){
            return "Input must be 7 characters!";
        }
        if(!input.contains("=")){
            return "Input must contain '='";
        }
        if(!input.contains("+")&&!input.contains("-")&&!input.contains("*")&&!input.contains("/")){
            return "Input must contain '+','-','*', or '/'";
        }
        String[] strings=input.split("=");
        if(strings.length!=2){
            return "Input must contain one '='";
        }
        String left=strings[0],right=strings[1];
        if(getValue(left)!=getValue(right)){
            return "The equations on both sides are not equal.";
        }
        return "";
    }
    /**
     * @requires
     *  // input should not be null and remainingAttempts should be greater than 0
     *  input != null && remainingAttempts > 0;
     *
     * @ensures
     *  // If input is invalid, the corresponding error message is returned
     *  (\result.equals("input doesn't equal null") ||
     *   \result.equals("There's no chance to try anymore") ||
     *   \result.equals(isValidInput(input))) &&
     *  // If the input is valid, remainingAttempts are reduced by one
     *  (\old(remainingAttempts) - 1 == remainingAttempts) &&
     *  // If the guess is correct, the game-winning flag is set to true
     *  (correctNumber == INPUT_SIZE ==> gameWon == true) &&
     *  // If the guess is not completely correct, return the empty string and update the game state
     *  (correctNumber != INPUT_SIZE ==> \result.equals(""));
     *
     * @signals (NullPointerException e)
     *  // If input is null, a NullPointerException is thrown
     *  input == null;
     *
     * @signals (IllegalStateException e)
     *  // If there are no more opportunities to try, throw an IllegalStateException
     *  remainingAttempts <= 0;
     */
    // JML
    // ///@ requires input != null
    // ///@ ensures msg!="" if there are same problems else ""
    @Override
    public String processInput(String input) {
        if(input==null){
            return "input doesn't equal null";
        }
        if(remainingAttempts<=0){
            return "There's no chance to try anymore";
        }
        if(targetCheck){
            System.out.println(targetNumber+" is the target.");
        }
        String msg=isValidInput(input);
        if(!"".equals(msg)){
            return msg;
        }
        remainingAttempts--;
        currentGuess=new StringBuilder(input);
        StringBuilder currentGuessCopy=new StringBuilder(input);
        StringBuilder targetNumberCopy=new StringBuilder(targetNumber);
        currentState=new ArrayList<>();
        // Initialize all as errors, i.e. gray
        for(int i=0;i<currentGuessCopy.length();i++){
            currentState.add(new Node(2,currentGuessCopy.charAt(i)));
        }
        // Number of correct
        int correctNumber=0;
        // the correct
        for(int i=0;i<Math.min(currentGuessCopy.length(),targetNumberCopy.length());i++){
            if(currentGuessCopy.charAt(i)==targetNumberCopy.charAt(i)){
                currentGuessCopy.setCharAt(i,' ');
                targetNumberCopy.setCharAt(i,' ');
                correctNumber++;
                currentState.get(i).setIndex(0);  // green
            }
        }
        if(correctNumber==INPUT_SIZE){
            gameWon=true;
            setChanged();
            notifyObservers();
            return "";
        }
        // the number or sign is in the equation, but in the wrong place
        for(int i=0;i<currentGuessCopy.length();i++){
            for(int j=0;j<targetNumber.length();j++){
                if(currentGuessCopy.charAt(i)==' '||targetNumberCopy.charAt(j)==' '){
                    continue;
                }
                if(currentGuessCopy.charAt(i)==targetNumberCopy.charAt(j)){
                    targetNumberCopy.setCharAt(j,' ');
                    currentState.get(i).setIndex(1);  // orange
                    break;
                }
            }
        }
        setChanged();
        notifyObservers();

        return "";
    }

    /**
     * current state
     * @return current state
     * @ensures result==list<Node>
     */
    // JML
    // ///@ ensures result==list<Node>
    public List<Node> getCurrentState(){
        return currentState;
    }

    /**
     * getCurrentState2String for CLIApp
     * @return string
     * @ensures result!=null
     */
    // JML
    // ///@ ensures result!=null
    public String getCurrentState2String(){
        StringBuilder stringBuilder=new StringBuilder();
        for(Node node:currentState){
            stringBuilder.append(node.toString());
        }
        return stringBuilder.toString();
    }

    /**
     * is game over
     * @return true ore false
     * @ensures result==true or false
     */
    // JML
    // ///@ ensures result==true or false
    @Override
    public boolean isGameOver() {
        return remainingAttempts <= 0 || gameWon;
    }

    /**
     * is game won
     * @return true or false
     * @ensures result is true or false
     */
    // JML
    // ///@ ensures result is true or false
    @Override
    public boolean isGameWon() {
        return gameWon;
    }

    /**
     * return target equation
     * @return equation
     */
    @Override
    public String getTargetNumber() {
        return targetNumber;
    }

    /**
     * get current guess
     * @return current guess
     */
    @Override
    public StringBuilder getCurrentGuess() {
        return currentGuess;
    }

    /**
     * remaining attempts
     * @return remaining attempts
     */
    @Override
    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    /**
     * start a new game
     */
    @Override
    public void startNewGame() {
        initialize();
    }
}
