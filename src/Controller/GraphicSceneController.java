package Controller;

import Model.Ball;
import Model.GraphicScene;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

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
                        // als "aktive" setzten
                        graphicScene.setActiveElement(newBall);
                        // in der Szene anzeigen
                        graphicPane.getChildren().add(newBall.getElementView());

                        success = true;
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


        //hier werden die Changelistener für die Werte des GraphicsObjects gesetzt
        returnBall.xPositionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
               // System.out.println("xPositionProperty changed. Oldvalue: "+oldValue+" new Value: " +newValue);
            }
        });
        returnBall.yPositionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //System.out.println("xPositionProperty changed. Oldvalue: "+oldValue+" new Value: " +newValue);
            }
        });

        return returnBall;
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


