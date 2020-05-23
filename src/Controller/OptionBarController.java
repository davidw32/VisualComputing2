package Controller;

import Model.GraphicScene;
import javafx.fxml.FXML;

public class OptionBarController {
    @FXML
    private StartController startController;
    private GraphicScene graphicScene;

    @FXML
    public void loadScene(){
        System.out.println("Szene laden");
    }

    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }

    public void setStartController(StartController startController) {
        this.startController = startController;
    }
}
