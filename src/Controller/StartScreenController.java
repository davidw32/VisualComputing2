package Controller;

import Model.*;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;


import java.util.LinkedList;


public class StartScreenController {

    private GraphicScene graphicScene;

    @FXML
    private GridPane startScreenGrid;


    public void initialize() {

        System.out.println("init startscreen");
    }

    @FXML
    public void openScene() {
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

    public void openScene1() {
        LinkedList<GraphicsObject> elementsInScene1 = new LinkedList<>();
        Ball ball1 = new Ball(60, 50);
        ball1.setIsSelected(true);
        Block block1 = new Block(35, 200);
        block1.setWidth(400);
        block1.setAngle(10);
        block1.setXPosition(40);
        block1.setIsSelected(false);
        Spinner spinner1 = new Spinner(520,300);
        spinner1.setRotationalSpeed(2);
        spinner1.setIsSelected(false);
        elementsInScene1.add(ball1);
        elementsInScene1.add(block1);
        elementsInScene1.add(spinner1);

        graphicScene.setElementsInScene(elementsInScene1);
        graphicScene.setActiveElement(ball1);
        graphicScene.getGraphicSceneController().addListenersToObject(ball1);
        graphicScene.getGraphicSceneController().addListenersToObject(block1);
        graphicScene.getGraphicSceneController().addListenersToObject(spinner1);

        graphicScene.getGraphicSceneController().getGraphicPane().getChildren().addAll(ball1.getElementView(), ball1.getDirectionLine(),
                ball1.getVelocityText(), block1.getElementView(), spinner1.getElementView(), spinner1.getCenter());


        startScreenGrid.setVisible(false);
        System.out.println("lade Szene");

        graphicScene.getElementBarController().getElementBar().setVisible(true);
        graphicScene.getElementEditorController().getEditor().setVisible(true);
        graphicScene.getGraphicSceneController().getGraphicPane().setVisible(true);
        graphicScene.getOptionBarController().getOptionBar().setVisible(true);
        graphicScene.getPlayerController().getPlayer().setVisible(true);


    }

    public void goBack() {
        startScreenGrid.setVisible(true);
        /*
        model.getGraphicScene().setVisible(false);
        model.getToolBar().setVisible(false);
        model.getToolWindow().setVisible(false);
        model.getOptionBar().setVisible(false);
        model.getPlayer().setVisible(false);

         */
    }


    public void setGraphicScene(GraphicScene myGraphicScene) {
        this.graphicScene = myGraphicScene;
    }

    public GridPane getStartScreenGrid() {
        return startScreenGrid;
    }
}
