package src.view;// NumberleView.java
import src.controller.NumberleController;
import src.model.INumberleModel;
import src.model.NumberleModel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class NumberleView implements Observer {
    private final INumberleModel model;
    private final NumberleController controller;
    private final JFrame frame = new JFrame("Numberle");
    private final JTextField inputTextField = new JTextField(3);;
    private final JLabel attemptsLabel = new JLabel("Attempts remaining: ");
    private JPanel labelPanel;
    private List<JButton> buttonList;

    public NumberleView(INumberleModel model, NumberleController controller) {
        this.controller = controller;
        this.model = model;
        this.controller.startNewGame();
        ((NumberleModel)this.model).addObserver(this);
        initializeFrame();
        this.controller.setView(this);
        update((NumberleModel)this.model, null);
    }

    /**
     * It creates a window (JFrame), sets the size and position, and defines a grid layout.
     * In this layout, it adds a center panel for placing input fields and submit/restart buttons, as well as a keyboard panel containing number and operator buttons.
     * The Submit button is used to process user input and update the state of the game, while the restart button allows the user to restart the game.
     * In addition, there is a TAB that displays the number of remaining attempts.
     * The entire interface is designed to provide users with an interactive gaming experience.
     */
    public void initializeFrame() {
        buttonList=new ArrayList<>();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setLocation(500,300);
        frame.setLayout(new GridLayout(3, 1));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));

        labelPanel=new JPanel();
        frame.add(labelPanel);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 1));
        inputPanel.add(inputTextField);

        JButton submitButton = new JButton("Submit");
        JButton restartButton = new JButton("Restart");
        restartButton.setEnabled(false);
        restartButton.addActionListener(e->{
            int choice=JOptionPane.showConfirmDialog(null,"Would you to restart game?","info",JOptionPane.YES_NO_OPTION);
            if(choice==JOptionPane.YES_OPTION){
                controller.startNewGame();
                restartButton.setEnabled(false);
                for(JButton button:buttonList){
                    button.setBackground(Color.WHITE);
                }
            }
        });
        submitButton.setSize(100,20);
        restartButton.setSize(100,20);
        submitButton.addActionListener(e -> {
            String msg=controller.processInput(inputTextField.getText());
            if(!msg.equals("")){
                JOptionPane.showMessageDialog(null,msg);
                return;
            }
            restartButton.setEnabled(true);
            inputTextField.setText("");
            if(controller.isGameWon()){
                int choice=JOptionPane.showConfirmDialog(null,"You won!\nWould you to restart game?","info",JOptionPane.YES_NO_OPTION);
                if(choice==JOptionPane.YES_OPTION){
                    controller.startNewGame();
                    for(JButton button:buttonList){
                        button.setBackground(Color.WHITE);
                    }
                }else{
                    System.exit(0);
                }
            }
            if(controller.isGameOver()){
                int choice=JOptionPane.showConfirmDialog(null,"You fail!\nWould you to restart game?","info",JOptionPane.YES_NO_OPTION);
                if(choice==JOptionPane.YES_OPTION){
                    controller.startNewGame();
                    for(JButton button:buttonList){
                        button.setBackground(Color.WHITE);
                    }
                }else{
                    System.exit(0);
                }
            }
        });
        inputPanel.add(submitButton);
        inputPanel.add(restartButton);

        attemptsLabel.setText("Attempts remaining: " + controller.getRemainingAttempts());
        inputPanel.add(attemptsLabel);

        center.add(new JPanel());
        center.add(inputPanel);
        center.add(new JPanel());
        frame.add(center);

        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new BoxLayout(keyboardPanel, BoxLayout.X_AXIS));
        keyboardPanel.add(new JPanel());
        JPanel numberPanel = new JPanel();
        numberPanel.setLayout(new GridLayout(2, 5));
        keyboardPanel.add(numberPanel);

        for (int i = 0; i < 10; i++) {
            JButton button = new JButton(Integer.toString(i));
            button.setEnabled(true);
            button.addActionListener(e -> {
                inputTextField.setText(inputTextField.getText() + button.getText());
            });
            buttonList.add(button);
            button.setPreferredSize(new Dimension(50, 50));
            numberPanel.add(button);
        }
        char[] ch=new char[]{'+','-','*','/','='};
        for(char c:ch){
            JButton button = new JButton(Character.toString(c));
            button.setEnabled(true);
            button.addActionListener(e -> {
                inputTextField.setText(inputTextField.getText() + button.getText());
            });
            buttonList.add(button);
            button.setPreferredSize(new Dimension(50, 50));
            numberPanel.add(button);
        }
        JButton button = new JButton("<-");
        button.setEnabled(true);
        button.setBackground(Color.WHITE);
        button.addActionListener(e -> {
            if(inputTextField.getText().length()!=0) {
                inputTextField.setText(inputTextField.getText().substring(0, inputTextField.getText().length() - 1));
            }
        });
        button.setPreferredSize(new Dimension(50, 50));
        numberPanel.add(button);
        keyboardPanel.add(new JPanel());

        frame.add(keyboardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * update the state to display in view
     */
    private void updateState(){
        labelPanel.removeAll();
        List<JLabel> labelList=controller.getCurrentState2View();
        for(JLabel label:labelList){
            labelPanel.add(label);
        }
        Map<String,Color> map=new HashMap<>();
        for(JLabel label:labelList){
            if(label.getBackground()==Color.GRAY) map.put(label.getText(),Color.GRAY);
        }
        for(JLabel label:labelList){
            if(label.getBackground()==Color.ORANGE) map.put(label.getText(),Color.ORANGE);
        }
        for(JLabel label:labelList){
            if(label.getBackground()==Color.GREEN) map.put(label.getText(),Color.GREEN);
        }
        for(JButton button:buttonList){
            button.setBackground(map.getOrDefault(button.getText(),Color.WHITE));
        }
        labelPanel.repaint();
    }

    /**
     * update
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers}
     *                 method.
     */
    @Override
    public void update(java.util.Observable o, Object arg) {
        attemptsLabel.setText("Attempts remaining: " + controller.getRemainingAttempts());
        updateState();

    }
}