package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import src.model.INumberleModel;
import src.model.Node;
import src.model.NumberleModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class guessEquationTest {
    private Color[] getColor(INumberleModel numberleModel){
        List<Color> list=new ArrayList<>();
        for(Node node:numberleModel.getCurrentState()){
            list.add(node.getColor());
        }
        return list.toArray(new Color[0]);
    }

    /**
     * Normal testing
     */
    @Test
    public void test01(){
        String guess="3*4=4*3";
        String target="2+5-1=6";
        INumberleModel numberleModel=new NumberleModel();
        numberleModel.setRemainingAttempts(1);
        numberleModel.setTargetNumber(target);
        numberleModel.processInput(guess);
        Color[] except=new Color[]{Color.gray,Color.gray,Color.gray,Color.orange,Color.gray,Color.gray,Color.gray};
        Assertions.assertArrayEquals(except,getColor(numberleModel));
        Assertions.assertTrue(numberleModel.isGameOver());

        guess="1+5-1=5";
        target="2+5-1=6";
        numberleModel=new NumberleModel();
        numberleModel.setTargetNumber(target);
        numberleModel.setRemainingAttempts(1);
        numberleModel.processInput(guess);
        except=new Color[]{Color.gray,Color.green,Color.green,Color.green,Color.green,Color.green,Color.gray};
        Assertions.assertArrayEquals(except,getColor(numberleModel));

        guess="2+5-1=6";
        target="2+5-1=6";
        numberleModel=new NumberleModel();
        numberleModel.setTargetNumber(target);
        numberleModel.setRemainingAttempts(1);
        numberleModel.processInput(guess);
        except=new Color[]{Color.green,Color.green,Color.green,Color.green,Color.green,Color.green,Color.green};
        Assertions.assertArrayEquals(except,getColor(numberleModel));
        Assertions.assertTrue(numberleModel.isGameWon());
    }

    /**
     * test with RemainingAttempts=0
     */
    @Test
    public void test02(){
        String guess="3*4=4*3";
        String target="2+5-1=6";
        INumberleModel numberleModel=new NumberleModel();
        numberleModel.setRemainingAttempts(0);
        numberleModel.setTargetNumber(target);
        String msg=numberleModel.processInput(guess);
        Assertions.assertNotEquals("",msg,"RemainingAttempts is 0, there will be a msg");
    }

    /**
     * test with input===null
     */
    @Test
    public void test03(){
        String target="2+5-1=6";
        INumberleModel numberleModel=new NumberleModel();
        numberleModel.setRemainingAttempts(1);
        numberleModel.setTargetNumber(target);
        String msg=numberleModel.processInput(null);
        Assertions.assertNotEquals("",msg,"input doesn't equal null");
    }
}
