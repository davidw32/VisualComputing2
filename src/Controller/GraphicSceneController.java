package Controller;

import Model.Ball;
import Model.Block;
import Model.GraphicScene;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class GraphicSceneController {
    @FXML
    Pane graphicPane;
    @FXML
    private StartController startController;

    private GraphicScene graphicScene;


    private double initX, initY, initTranslateX, initTranslateY;

    public void initialize(){
        // hier wird es ermöglicht ein Objekt per Drag-and-Drop in die Szene zu ziehen
        graphicPane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(event.getGestureSource() != graphicPane && event.getDragboard().hasString()){
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
            }
        });
        // durch "DragDropped" wird ein neues GraphicsObject angelegt und in der Szene eingefügt
        graphicPane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                System.out.println("Drag dropped: "+ db.getString());
                boolean success = false;
                if(db.hasString()){

                    if (db.getString().equals("ballDummy")){
                        System.out.println(event.getX()+" "+ event.getY());
                        //neues Element erzeugen
                        Ball newBall = createBall(event.getX(), event.getY());
                        // in der Liste einfügen
                        graphicScene.addElement(newBall);
                        // das aktive Element wechseln
                        graphicScene.getActiveElement().setIsSelected(false);
                        graphicScene.setActiveElement(newBall);

                        // in der Szene anzeigen
                        graphicPane.getChildren().add(newBall.getElementView());

                        success = true;
                    }
                    if(db.getString().equals("blockDummy")){
                        Block newBlock = createBlock(event.getX(), event.getY());
                        graphicScene.addElement(newBlock);
                        graphicScene.getActiveElement().setIsSelected(false);
                        graphicScene.setActiveElement(newBlock);
                        graphicPane.getChildren().add(newBlock.getElementView());
                    }

                }
                event.setDropCompleted(success);
                event.consume();
            }
        });



    }
    //neues Ball-Objekt anlegen und die entsprechenden Listener hinzufügen.
    private Ball createBall(double xPosition, double yPosition){

        System.out.println("neuen Ball erzeugt");

        Ball returnBall = new Ball(xPosition, yPosition);


        //hier wird der Drag-and-drop innerhalb der Szene initialisiert
        returnBall.getElementView().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            graphicScene.getActiveElement().setIsSelected(false);
            returnBall.setIsSelected(true);
            graphicScene.setActiveElement(returnBall);

            initX = event.getSceneX();
            initY = event.getSceneY();
            initTranslateX = ((Circle) (event.getSource())).getCenterX();
            initTranslateY = ((Circle) (event.getSource())).getCenterY();
        });
        // hier wird Drag-and-Drop innerhalb der Szene durchgeführt
        returnBall.getElementView().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {

            double offsetX = event.getSceneX() - initX;
            double offsetY = event.getSceneY() - initY;

            double newTranslateX = initTranslateX + offsetX;
            double newTranslateY = initTranslateY + offsetY;
            ((Circle) (event.getSource())).setCenterX(newTranslateX);
            ((Circle) (event.getSource())).setCenterY(newTranslateY);
            returnBall.setXPosition(newTranslateX);
            returnBall.setYPosition(newTranslateY);
        });


        return returnBall;
    }
    private Block createBlock(double _xPosition, double _yPosition){
        Block returnBlock = new Block(_xPosition,_yPosition);
        returnBlock.getElementView().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            graphicScene.getActiveElement().setIsSelected(false);
            returnBlock.setIsSelected(true);
            graphicScene.setActiveElement(returnBlock);

            initX = event.getSceneX();
            initY = event.getSceneY();
            initTranslateX = ((Rectangle) (event.getSource())).getX();
            initTranslateY = ((Rectangle) (event.getSource())).getY();
        });
        // hier wird Drag-and-Drop innerhalb der Szene durchgeführt
        returnBlock.getElementView().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {

            double offsetX = event.getSceneX() - initX;
            double offsetY = event.getSceneY() - initY;

            double newTranslateX = initTranslateX + offsetX;
            double newTranslateY = initTranslateY + offsetY;
            ((Rectangle) (event.getSource())).setX(newTranslateX);
            ((Rectangle) (event.getSource())).setY(newTranslateY);
            returnBlock.setXPosition(newTranslateX);
            returnBlock.setYPosition(newTranslateY);
        });



        return returnBlock;
    }




    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }

    public void setStartController(StartController startController) {
        this.startController = startController;
    }

    public void setElementEditorController(ElementEditorController elementEditorController) {
    }
}


