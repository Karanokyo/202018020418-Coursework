import src.controller.NumberleController;
import src.model.INumberleModel;
import src.model.NumberleModel;
import src.view.NumberleView;

public class GUIApp {
    /**
     * the main function
     * @param args args
     */
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        createAndShowGUI();
                    }
                }
        );
    }

    /**
     * First, it instantiates a NumberleModel object as the game's data model.
     * Next, it creates a NumberleController object that passes the model as a parameter so that the controller can manage the game logic.
     * Finally, it creates a NumberleView object that passes both the model and the controller so that the view can display the game interface and interact with the user.
     */
    public static void createAndShowGUI() {
        INumberleModel model = new NumberleModel();
        NumberleController controller = new NumberleController(model);
        NumberleView view = new NumberleView(model, controller);
    }
}