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
        System.out.println("neue Szene geladen");
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

        LinkedList<GraphicsObject> elementsInScene2 = new LinkedList<>();

        Block block1 = new Block(7,86);
        block1.setWidth(430);
        block1.setAngle(2);
        block1.setMaterial("Rubber");
        block1.getElementView().setFill(Color.web("#2e8b57"));
        block1.setIsSelected(false);

        Block block2 = new Block(828,86);
        block2.setWidth(300);
        block2.setAngle(-4);
        block2.setMaterial("Metal");
        block2.getElementView().setFill(Color.GREY);
        block2.setIsSelected(false);

        Block block3 = new Block(235, 581);
        block3.setWidth(150);
        block3.setMaterial("Wood");
        block3.getElementView().setFill(Color.web("#b3661a"));
        block3.setIsSelected(false);

        Block block4 = new Block(450, 664);
        block4.setWidth(300);
        block4.setMaterial("Wood");
        block4.getElementView().setFill(Color.web("#b3661a"));
        block4.setIsSelected(false);

        Seesaw seesaw1 = new Seesaw(322,267);
        seesaw1.setWidth(500);
        seesaw1.setHeight(30);
        seesaw1.getElementView().setFill(Color.web("#1e90ff"));
        seesaw1.setIsSelected(false);

        Spinner spinner1 = new Spinner(932,443);
        spinner1.setWidth(200);
        spinner1.setHeight(25);
        spinner1.getElementView().setFill(Color.web("ff4500"));
        spinner1.setRotationalSpeed(-1);
        spinner1.setIsSelected(false);

        Springboard springboard1 = new Springboard(28,718);
        springboard1.getBoard().getElementView().setFill(Color.web("#b3661a"));
        springboard1.setIsSelected(false);

        Ball ball1 = new Ball(1091, 31);
        ball1.setRadius(25);
        ball1.setWeight(6);
        ball1.setMaterial("Rubber");
        ball1.getElementView().setFill(Color.web("#dda0dd"));
        ball1.setIsSelected(false);

        Ball ball2 = new Ball(40,31);
        ball2.setRadius(25);
        ball2.setWeight(2);
        ball2.setMaterial("Rubber");
        ball2.getElementView().setFill(Color.web("#ffff4d"));
        ball2.setIsSelected(false);

        elementsInScene2.add(block1);
        elementsInScene2.add(block2);
        elementsInScene2.add(block3);
        elementsInScene2.add(block4);
        elementsInScene2.add(seesaw1);
        elementsInScene2.add(spinner1);
        elementsInScene2.add(springboard1);
        elementsInScene2.add(springboard1.getBoard());
        elementsInScene2.add(ball1);
        elementsInScene2.add(ball2);

        graphicScene.setElementsInScene(elementsInScene2);
        graphicScene.setActiveElement(ball1);

        for (GraphicsObject graphicsObject : elementsInScene2){
            graphicScene.getGraphicSceneController().addListenersToObject(graphicsObject);
            graphicScene.getGraphicSceneController().getGraphicPane().getChildren().add(graphicsObject.getElementView());
        }
        graphicScene.getGraphicSceneController().addListenersToObject(springboard1.getBoard());
        graphicScene.getGraphicSceneController().getGraphicPane().getChildren().addAll(ball1.getDirectionLine(),
                ball1.getVelocityText(), ball2.getDirectionLine(), ball2.getVelocityText(), spinner1.getCenter(),
                seesaw1.getTriangle());

        // Wechsel zum Editormodus
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
