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
        // die GraphicScene erhält Referenzen auf alle Controller
        myGraphicScene = new GraphicScene();
        myGraphicScene.setElementEditorController(elementEditorController);
        myGraphicScene.setGraphicSceneController(graphicSceneController);
        myGraphicScene.setElementBarController(elementBarController);
        myGraphicScene.setPlayerController(playerController);
        myGraphicScene.setOptionBarController(optionBarController);

        playerController.setGraphicScene(myGraphicScene);
        elementEditorController.setGraphicScene(myGraphicScene);
        elementEditorController.initValues();
        graphicSceneController.setGraphicScene(myGraphicScene);
        elementBarController.setGraphicScene(myGraphicScene);
        optionBarController.setGraphicScene(myGraphicScene);



    }


}
