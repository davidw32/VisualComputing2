package Controller;

import Model.GraphicScene;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;



public class StartScreenController {

    private GraphicScene graphicScene;

    @FXML
    private GridPane startScreenGrid;


    public void initialize() {

        System.out.println("init startscreen");
    }

    @FXML
    public void openScene()
    {
        startScreenGrid.setVisible(false);
        System.out.println("lade Szene");

        graphicScene.getElementBarController().getElementBar().setVisible(true);
        graphicScene.getElementEditorController().getEditor().setVisible(true);
        graphicScene.getGraphicSceneController().getGraphicPane().setVisible(true);
        graphicScene.getOptionBarController().getOptionBar().setVisible(true);
        graphicScene.getPlayerController().getPlayer().setVisible(true);

        /*
        model.getGraphicScene().setVisible(true);
        model.getToolBar().setVisible(true);
        model.getToolWindow().setVisible(true);
        model.getOptionBar().setVisible(true);
        model.getPlayer().setVisible(true);

         */
    }


    public void goBack()
    {
        startScreenGrid.setVisible(true);
        /*
        model.getGraphicScene().setVisible(false);
        model.getToolBar().setVisible(false);
        model.getToolWindow().setVisible(false);
        model.getOptionBar().setVisible(false);
        model.getPlayer().setVisible(false);

         */
    }


    public void setGraphicScene(GraphicScene myGraphicScene) { this.graphicScene = myGraphicScene;
    }

    public GridPane getStartScreenGrid(){ return startScreenGrid;}
}
