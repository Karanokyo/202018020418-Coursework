package src.model;

import java.awt.*;

public class Node {
    private final Color[] colors=new Color[]{Color.green,Color.orange,Color.gray};
    public static final String RESET="\u001B[0m";
    public static final String GREEN = "\u001B[42m";
    public static final String YELLOW ="\u001B[43m";
    private Color color;
    private char value;
    private int index;

    /**
     * init function
     * @param index index
     * @param value value
     */
    public Node(int index,char value){
        this.index=index;
        this.value=value;
        color=colors[index];
    }
    /**
     * get color
     * @return color
     */
    public Color getColor() {
        return color;
    }

    /**
     * get the value
     * @return value
     */
    public char getValue() {
        return value;
    }

    /**
     * set index
     * @param index index
     */
    public void setIndex(int index) {
        this.index = index;
        color=colors[index];
    }

    /**
     * to String for CLIApp
     * @return string
     */
    public String toString(){
        String s=RESET;
        if(index==0){
            s=GREEN;
        } else if (index==1) {
            s=YELLOW;
        }
        return s+value+RESET;
    }
}
