package Controller;

import Model.GraphicScene;
import javafx.fxml.FXML;

public class StartController {

    @FXML ElementBarController elementBarController;
    @FXML GraphicSceneController graphicSceneController;
    @FXML OptionBarController optionBarController;
    @FXML PlayerController playerController;
    @FXML ElementEditorController elementEditorController;

    private GraphicScene myGraphicScene;

    @FXML public void initialize(){

        myGraphicScene = new GraphicScene();
        myGraphicScene.setElementEditorController(elementEditorController);
        myGraphicScene.setGraphicSceneController(graphicSceneController);
        myGraphicScene.setElementBarController(elementBarController);
        myGraphicScene.setPlayerController(playerController);
        myGraphicScene.setOptionBarController(optionBarController);

        playerController.setGraphicScene(myGraphicScene);
        elementEditorController.setGraphicScene(myGraphicScene);
        optionBarController.setGraphicScene(myGraphicScene);
        graphicSceneController.setGraphicScene(myGraphicScene);
        elementBarController.setGraphicScene(myGraphicScene);

    }


}
