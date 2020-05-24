package Controller;

import Model.Ball;
import Model.GraphicScene;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class GraphicSceneController {

    @FXML Pane graphicPane;

    private GraphicScene graphicScene;


    private double initX, initY, initTranslateX, initTranslateY;

    public void initialize(){

        graphicPane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(event.getGestureSource() != graphicPane && event.getDragboard().hasString()){
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
            }
        });

        graphicPane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                System.out.println("Drag dropped: "+ db.getString());
                boolean success = false;
                if(db.hasString()){

                    if (db.getString().equals("ballDummy")){
                        System.out.println(event.getX()+" "+ event.getY());
                        Ball newBall = createBall(event.getX(), event.getY());
                        graphicScene.addElement(newBall);
                        graphicScene.setActiveElement(newBall);
                        graphicPane.getChildren().add(newBall.getElementView());

                        success = true;
                    }

                }
                event.setDropCompleted(success);
                event.consume();
            }
        });



    }

    private Ball createBall(double xPosition, double yPosition){
        System.out.println("neuen Ball erzeugt");
        Ball returnBall = new Ball(xPosition, yPosition);

        returnBall.getElementView().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("mausklkick");
        });
        returnBall.getElementView().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            System.out.println("mauspressed");
            initX = event.getSceneX();
            initY = event.getSceneY();
            initTranslateX = ((Circle) (event.getSource())).getCenterX();
            initTranslateY = ((Circle) (event.getSource())).getCenterY();
        });

        returnBall.getElementView().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            System.out.println("mause dropped" );
            double offsetX = event.getSceneX() - initX;
            double offsetY = event.getSceneY() - initY;

            double newTranslateX = initTranslateX + offsetX;
            double newTranslateY = initTranslateY + offsetY;
            ((Circle) (event.getSource())).setCenterX(newTranslateX);
            ((Circle) (event.getSource())).setCenterY(newTranslateY);
            returnBall.setXPosition(newTranslateX);
            returnBall.setYPosition(newTranslateY);


        });



        returnBall.xPositionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("xPositionProperty changed. Oldvalue: "+oldValue+" new Value: " +newValue);
            }
        });


        return returnBall;
    }



    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }
}


