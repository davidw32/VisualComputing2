package Controller;

import Model.GraphicScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class OptionBarController {

    GraphicScene graphicScene;

    @FXML
    public void loadScene(){
        System.out.println("Szene laden");
    }


    public void goBack(ActionEvent actionEvent)
    {

    }

    public void scenePause(ActionEvent actionEvent)
    {
        graphicScene.getPlayerController().stopSimulation();
    }

    public void sceneRestart(ActionEvent actionEvent)
    {
        graphicScene.getPlayerController().resetSimulation();
    }

    public void sceneStart(ActionEvent actionEvent)
    {
        graphicScene.getPlayerController().startSimulation();
    }

    public void copyObject(ActionEvent actionEvent) {
    }

    public void deleteObject(ActionEvent actionEvent) {
    }

    public void opentoolWindow(ActionEvent actionEvent) {
    }

    public void openHelp(ActionEvent actionEvent) {
    }

    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }

    public GraphicScene getGraphicScene() {
        return graphicScene;
    }
}
