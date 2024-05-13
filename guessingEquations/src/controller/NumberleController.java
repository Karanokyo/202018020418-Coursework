package src.controller;

import src.model.INumberleModel;
import src.model.Node;
import src.view.NumberleView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// NumberleController.java
public class NumberleController {
    private INumberleModel model;
    private NumberleView view;

    public NumberleController(INumberleModel model) {
        this.model = model;
    }

    /**
     * set a view
     * @param view view
     */
    public void setView(NumberleView view) {
        this.view = view;
    }

    /**
     * proceess input
     * @param input input
     * @return success or fail
     */
    public String processInput(String input) {
        return model.processInput(input);
    }

    /**
     * is game over
     * @return true or false
     */
    public boolean isGameOver() {
        return model.isGameOver();
    }

    /**
     * is won
     * @return true or false
     */
    public boolean isGameWon() {
        return model.isGameWon();
    }

    /**
     * get remaining attempts
     * @return remaining attempts
     */
    public int getRemainingAttempts() {
        return model.getRemainingAttempts();
    }

    /**
     * start a new game
     */
    public void startNewGame() {
        model.startNewGame();
    }

    /**
     * get current state to View
     * @return List<JLabel>
     */
    public List<JLabel> getCurrentState2View(){
        List<JLabel> re=new ArrayList<>();
        List<Node> list=model.getCurrentState();
        if(list==null||list.size()==0){
            for(int i=0;i<model.getINPUT_SIZE();i++){
                JLabel label=new JLabel();
                label.setText("");
                label.setPreferredSize(new Dimension(50,50));
                label.setBackground(Color.WHITE);
                label.setOpaque(true);
                re.add(label);
            }
        }else{
            for(Node node: list){
                JLabel label=new JLabel();
                label.setText(Character.toString(node.getValue()));
                label.setPreferredSize(new Dimension(50,50));
                label.setBackground(node.getColor());
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setOpaque(true);
                re.add(label);
            }
        }
        return re;
    }
}