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
        Ball ball1 = new Ball(55, 145);
        ball1.setRadius(25);
        ball1.setIsSelected(true);
        ball1.setMaterial("Rubber");
        Ball ball2 = new Ball(55, 245);
        ball2.setRadius(25);
        ball2.setMaterial("Rubber");
        ball2.setIsSelected(false);
        Ball ball3 = new Ball(55, 336);
        ball3.setRadius(25);
        ball3.setMaterial("Rubber");
        ball3.setIsSelected(false);
        Ball ball4 = new Ball(791, 53);
        ball4.setRadius(25);
        ball4.setMaterial("Metal");
        ball4.setIsSelected(false);
        ball4.getElementView().setFill(Color.GREY);
        ball4.setWeight(1);
        Block block1 = new Block(30, 190);
        block1.setWidth(150);
        block1.setHeight(20);
        block1.setAngle(10);
        block1.setMaterial("Rubber");
        block1.setIsSelected(false);
        Block block2 = new Block(30, 288);
        block2.setWidth(150);
        block2.setHeight(20);
        block2.setAngle(10);
        block2.setMaterial("Rubber");
        block2.setIsSelected(false);
        Block block3 = new Block(30, 376);
        block3.setWidth(150);
        block3.setHeight(20);
        block3.setAngle(10);
        block3.setMaterial("Rubber");
        block3.setIsSelected(false);
        Block block4 = new Block(180, 203);
        block4.setWidth(500);
        block4.setHeight(20);
        block4.setAngle(0);
        block4.setMaterial("Metal");
        block4.setIsSelected(false);
        block4.getElementView().setFill(Color.GREY);
        Block block5 = new Block(180, 301);
        block5.setWidth(420);
        block5.setHeight(20);
        block5.setAngle(0);
        block5.setMaterial("Wood");
        block5.setIsSelected(false);
        block5.getElementView().setFill(Color.web("#b3661a"));
        Block block6 = new Block(180, 390);
        block6.setWidth(340);
        block6.setHeight(20);
        block6.setAngle(0);
        block6.setMaterial("Rubber");
        block6.setIsSelected(false);
        Block block7 = new Block(423, 518);
        block7.setWidth(390);
        block7.setHeight(20);
        block7.setAngle(-15);
        block7.setMaterial("Wood");
        block7.setIsSelected(false);
        block7.getElementView().setFill(Color.web("#b3661a"));
        Block block8 = new Block(1035, 440);
        block8.setWidth(30);
        block8.setHeight(20);
        block8.setAngle(-20);
        block8.setMaterial("Wood");
        block8.setIsSelected(false);
        block8.getElementView().setFill(Color.web("#b3661a"));
        Block block9 = new Block(1037, 698);
        block9.setWidth(100);
        block9.setHeight(20);
        block9.setAngle(-30);
        block9.setMaterial("Wood");
        block9.setIsSelected(false);
        block9.getElementView().setFill(Color.web("#b3661a"));
        Spinner spinner1 = new Spinner(741, 92);
        spinner1.setRotationalSpeed(-2);
        spinner1.setIsSelected(false);
        Seesaw seesaw = new Seesaw(199, 604);
        seesaw.setIsSelected(false);
        Seesaw seesaw2 = new Seesaw(-1, 707);
        seesaw2.setIsSelected(false);
        Springboard springboard = new Springboard(865, 610);
        springboard.getBoard().getElementView().setFill(Color.web("#b3661a"));

        elementsInScene1.add(ball1);
        elementsInScene1.add(ball2);
        elementsInScene1.add(ball3);
        elementsInScene1.add(ball4);
        elementsInScene1.add(block1);
        elementsInScene1.add(block2);
        elementsInScene1.add(block3);
        elementsInScene1.add(block4);
        elementsInScene1.add(block5);
        elementsInScene1.add(block6);
        elementsInScene1.add(block7);
        elementsInScene1.add(block8);
        elementsInScene1.add(block9);
        elementsInScene1.add(spinner1);
        elementsInScene1.add(seesaw);
        elementsInScene1.add(seesaw2);
        elementsInScene1.add(springboard);
        elementsInScene1.add(springboard.getBoard());

        graphicScene.setElementsInScene(elementsInScene1);
        graphicScene.setActiveElement(ball1);
        graphicScene.getGraphicSceneController().addListenersToObject(ball1);
        graphicScene.getGraphicSceneController().addListenersToObject(ball2);
        graphicScene.getGraphicSceneController().addListenersToObject(ball3);
        graphicScene.getGraphicSceneController().addListenersToObject(ball4);
        graphicScene.getGraphicSceneController().addListenersToObject(block1);
        graphicScene.getGraphicSceneController().addListenersToObject(block2);
        graphicScene.getGraphicSceneController().addListenersToObject(block3);
        graphicScene.getGraphicSceneController().addListenersToObject(block4);
        graphicScene.getGraphicSceneController().addListenersToObject(block5);
        graphicScene.getGraphicSceneController().addListenersToObject(block6);
        graphicScene.getGraphicSceneController().addListenersToObject(block7);
        graphicScene.getGraphicSceneController().addListenersToObject(block8);
        graphicScene.getGraphicSceneController().addListenersToObject(block9);
        graphicScene.getGraphicSceneController().addListenersToObject(spinner1);
        graphicScene.getGraphicSceneController().addListenersToObject(seesaw);
        graphicScene.getGraphicSceneController().addListenersToObject(seesaw2);
        graphicScene.getGraphicSceneController().addListenersToObject(springboard);
        graphicScene.getGraphicSceneController().addListenersToObject(springboard.getBoard());

        graphicScene.getGraphicSceneController().getGraphicPane().getChildren().addAll(ball1.getElementView(), ball1.getDirectionLine(),
                ball1.getVelocityText(), ball2.getElementView(), ball2.getDirectionLine(), ball2.getVelocityText(),
                ball3.getElementView(), ball3.getDirectionLine(), ball3.getVelocityText(),
                ball4.getElementView(), ball4.getDirectionLine(), ball4.getVelocityText(),
                block1.getElementView(), block2.getElementView(), block3.getElementView(), block4.getElementView(),
                block5.getElementView(), block6.getElementView(), block7.getElementView(), block8.getElementView(),
                block9.getElementView(), spinner1.getElementView(), spinner1.getCenter(), seesaw.getElementView(),
                seesaw.getTriangle(), seesaw2.getElementView(), seesaw2.getTriangle(), springboard.getElementView(),
                springboard.getBoard().getElementView());


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
