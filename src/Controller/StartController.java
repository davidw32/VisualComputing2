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

    @FXML
    public void initialize(){

        myGraphicScene = new GraphicScene();
        myGraphicScene.setElementEditorController(elementEditorController);
        myGraphicScene.setGraphicSceneController(graphicSceneController);
        myGraphicScene.setElementBarController(elementBarController);
        myGraphicScene.setPlayerController(playerController);
        myGraphicScene.setOptionBarController(optionBarController);

        playerController.setStartController(this);
        playerController.setGraphicScene(myGraphicScene);
        elementEditorController.setStartController(this);
        elementEditorController.setGraphicScene(myGraphicScene);
        optionBarController.setStartController(this);
        optionBarController.setGraphicScene(myGraphicScene);
        graphicSceneController.setStartController(this);
        graphicSceneController.setGraphicScene(myGraphicScene);

        elementBarController.setStartController(this);
        elementBarController.setGraphicScene(myGraphicScene);



    }


}
