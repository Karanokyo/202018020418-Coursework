
import src.model.INumberleModel;
import src.model.NumberleModel;

import java.util.Scanner;

public class CLIApp {
    /**
     * main function
     * @param args args
     */
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Welcome to the guessing equation game.");
        INumberleModel numberleModel=new NumberleModel();
        numberleModel.startNewGame();
        while(!numberleModel.isGameOver()){
            System.out.print("You still have "+numberleModel.getRemainingAttempts()+" chances. Please enter your guess:");
            String input= scanner.next();
            String msg=numberleModel.processInput(input);
            if(!"".equals(msg)){
                System.out.println(msg);
                continue;
            }

            System.out.println(numberleModel.getCurrentState2String());
            if(numberleModel.isGameWon()){
                System.out.println("you won!!!");
                return;
            }
        }
        System.out.println("you fail");
    }
}
