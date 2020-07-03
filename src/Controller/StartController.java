package Controller;

import Model.GraphicScene;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class StartController {

    @FXML ElementBarController elementBarController;
    @FXML GraphicSceneController graphicSceneController;
    @FXML OptionBarController optionBarController;
    @FXML PlayerController playerController;
    @FXML ElementEditorController elementEditorController;
    @FXML StartScreenController startScreenController;

    private GraphicScene myGraphicScene;
    //Hauptstage
    private Stage parent;

    @FXML
    public void initialize(){
        // die GraphicScene erhält Referenzen auf alle Controller
        //System.out.println("Init StartController");
        myGraphicScene = new GraphicScene();

        myGraphicScene.setElementEditorController(elementEditorController);
        myGraphicScene.setGraphicSceneController(graphicSceneController);
        myGraphicScene.setElementBarController(elementBarController);
        myGraphicScene.setPlayerController(playerController);
        myGraphicScene.setOptionBarController(optionBarController);
        myGraphicScene.setStartScreenController(startScreenController);
        myGraphicScene.setStartController(this);

        playerController.setGraphicScene(myGraphicScene);
        elementEditorController.setGraphicScene(myGraphicScene);
        elementEditorController.initValues();
        graphicSceneController.setGraphicScene(myGraphicScene);
        optionBarController.setGraphicScene(myGraphicScene);
        elementBarController.setGraphicScene(myGraphicScene);
        startScreenController.setGraphicScene(myGraphicScene);

        elementBarController.getElementBar().setVisible(false);
        elementEditorController.getEditor().setVisible(false);
        graphicSceneController.getGraphicPane().setVisible(false);
        optionBarController.getOptionBar().setVisible(false);
        playerController.getPlayer().setVisible(false);
        elementEditorController.getHelpText().setVisible(false);
        //nur die StartScreen ist sichtbar
        startScreenController.getStartScreenGrid().setVisible(true);

    }
    public void showStartScreen(){
        elementBarController.getElementBar().setVisible(false);
        elementEditorController.getEditor().setVisible(false);
        graphicSceneController.getGraphicPane().setVisible(false);
        optionBarController.getOptionBar().setVisible(false);
        playerController.getPlayer().setVisible(false);
        elementEditorController.getHelpText().setVisible(false);
        //nur die StartScreen ist sichtbar
        startScreenController.getStartScreenGrid().setVisible(true);
    }


    public Stage getParent() {
        return parent;
    }

    public void setParent(Stage parent) {
        this.parent = parent;
    }
}
