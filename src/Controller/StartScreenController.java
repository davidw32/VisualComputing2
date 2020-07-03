package Controller;

import Model.*;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;


import java.util.LinkedList;

/**
 * Für das Öffnen einer Szene, wird auch beim Programmstart zuerst gezeigt
 */
public class StartScreenController {

    private GraphicScene graphicScene;

    @FXML
    private GridPane startScreenGrid;


    public void initialize() {

        //System.out.println("Init StartScreenController");
    }

    /**
     * öffnet eine leere Szene im Editormodus
     */
    @FXML
    public void openScene() {
        startScreenGrid.setVisible(false);
        graphicScene.getElementBarController().getElementBar().setVisible(true);
        graphicScene.getElementEditorController().getEditor().setVisible(true);
        graphicScene.getGraphicSceneController().getGraphicPane().setVisible(true);
        graphicScene.getOptionBarController().getOptionBar().setVisible(true);
        graphicScene.getPlayerController().getPlayer().setVisible(true);

    }
    /**
     * öffnet eine  Szene 1 im Editormodus
     */
    @FXML
    public void openScene1() {
        LinkedList<GraphicsObject> elementsInScene1 = new LinkedList<>();
        Ball ball1 = new Ball(60, 50);
        ball1.setRadius(25);
        ball1.setIsSelected(true);
        Ball ball2 = new Ball(1022, 297);
        ball2.setRadius(35);
        ball2.getElementView().setFill(Color.GOLD);
        ball2.setMaterial("Metal");
        ball2.setMaterial("Rubber");
        ball2.setIsSelected(false);
        Block block1 = new Block(35, 200);
        block1.setWidth(400);
        block1.setHeight(20);
        block1.setAngle(10);
        block1.setXPosition(40);
        block1.setIsSelected(false);
        Spinner spinner1 = new Spinner(520, 300);
        spinner1.setRotationalSpeed(-2);
        spinner1.setIsSelected(false);
        Seesaw seesaw = new Seesaw(100, 690);
        seesaw.setIsSelected(false);
        Springboard springboard = new Springboard(982, 719);

        elementsInScene1.add(ball1);
        elementsInScene1.add(ball2);
        elementsInScene1.add(block1);
        elementsInScene1.add(spinner1);
        elementsInScene1.add(seesaw);
        elementsInScene1.add(springboard);
        elementsInScene1.add(springboard.getBoard());

        graphicScene.setElementsInScene(elementsInScene1);
        graphicScene.setActiveElement(ball1);
        graphicScene.getGraphicSceneController().addListenersToObject(ball1);
        graphicScene.getGraphicSceneController().addListenersToObject(ball2);
        graphicScene.getGraphicSceneController().addListenersToObject(block1);
        graphicScene.getGraphicSceneController().addListenersToObject(spinner1);
        graphicScene.getGraphicSceneController().addListenersToObject(seesaw);
        graphicScene.getGraphicSceneController().addListenersToObject(springboard);
        graphicScene.getGraphicSceneController().addListenersToObject(springboard.getBoard());

        graphicScene.getGraphicSceneController().getGraphicPane().getChildren().addAll(ball1.getElementView(), ball1.getDirectionLine(),
                ball1.getVelocityText(), ball2.getElementView(), ball2.getDirectionLine(), ball2.getVelocityText(),
                block1.getElementView(), spinner1.getElementView(), spinner1.getCenter(), seesaw.getElementView(),
                seesaw.getTriangle(), springboard.getElementView(), springboard.getBoard().getElementView());


        startScreenGrid.setVisible(false);
        graphicScene.getElementBarController().getElementBar().setVisible(true);
        graphicScene.getElementEditorController().getEditor().setVisible(true);
        graphicScene.getGraphicSceneController().getGraphicPane().setVisible(true);
        graphicScene.getOptionBarController().getOptionBar().setVisible(true);
        graphicScene.getPlayerController().getPlayer().setVisible(true);
        System.out.println("Szene 1 geladen");

    }

    /**
     * öffnet Szene 2 im Editormodus
     */
    public void openScene2() {
        startScreenGrid.setVisible(false);
        graphicScene.getElementBarController().getElementBar().setVisible(true);
        graphicScene.getElementEditorController().getEditor().setVisible(true);
        graphicScene.getGraphicSceneController().getGraphicPane().setVisible(true);
        graphicScene.getOptionBarController().getOptionBar().setVisible(true);
        graphicScene.getPlayerController().getPlayer().setVisible(true);
        System.out.println("Szene 2 geladen");
    }

    public void setGraphicScene(GraphicScene myGraphicScene) {
        this.graphicScene = myGraphicScene;
    }

    public GridPane getStartScreenGrid() {
        return startScreenGrid;
    }
}
