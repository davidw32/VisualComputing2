package Controller;

import Model.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import javax.swing.*;

public class GraphicSceneController {
    @FXML
    Pane graphicPane;

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
        // durch "DragDropped" wird ein neues GraphicsObject erzeugt und in der Szene eingefügt
        graphicPane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                System.out.println("Drag dropped: "+ db.getString());
                boolean success = false;
                if(db.hasString()){

                    if (db.getString().equals("ballDummy")){
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
                        success = true;
                    }

                    //Platzhalter für Drag-and-Drop bis die Elemente implementiert wurden
                    if(db.getString().equals("springboardDummy")){
                        Text placeholder = createPlaceholder(event.getX(),event.getY());
                        placeholder.setText("Springboard");
                        graphicPane.getChildren().add(placeholder);
                        success = true;
                    }
                    if(db.getString().equals("spinnerDummy")){
                        Text placeholder = createPlaceholder(event.getX(), event.getY());
                        placeholder.setText("Spinner");
                        graphicPane.getChildren().add(placeholder);
                        success = true;
                    }
                    if(db.getString().equals("seasawDummy")){
                        Text placeholder = createPlaceholder(event.getX(), event.getY());
                        placeholder.setText("Seasaw");
                        graphicPane.getChildren().add(placeholder);
                        success = true;
                    }

                }
                event.setDropCompleted(success);
                event.consume();
            }
        });



    }

    private Text createPlaceholder(double x, double y){
        Text placeholder = new Text();
        placeholder.setX(x);
        placeholder.setY(y);
        placeholder.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {

            // Position Mauszeiger
            initX = event.getSceneX();
            initY = event.getSceneY();
            //Position des Elements
            initTranslateX = ((Text) (event.getSource())).getX();
            initTranslateY = ((Text) (event.getSource())).getY();
        });
        // hier wird Drag-and-Drop innerhalb der Szene durchgeführt
       placeholder.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            //Verschiebung berechnen
            double offsetX = event.getSceneX() - initX;
            double offsetY = event.getSceneY() - initY;
            double newTranslateX = initTranslateX + offsetX;
            double newTranslateY = initTranslateY + offsetY;
            //Element verschieben
            ((Text) (event.getSource())).setX(newTranslateX);
            ((Text) (event.getSource())).setY(newTranslateY);

        });
        return placeholder;

    }

    //neues Ball-Objekt anlegen und die entsprechenden Listener hinzufügen.
    private Ball createBall(double xPosition, double yPosition){

        Ball returnBall = new Ball(xPosition, yPosition);

        //hier wird der Drag-and-drop innerhalb der Szene initialisiert
        returnBall.getElementView().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            //Element als Actives Element definieren
            graphicScene.getActiveElement().setIsSelected(false);
            returnBall.setIsSelected(true);
            graphicScene.setActiveElement(returnBall);

            // Position Mauszeiger
            initX = event.getSceneX();
            initY = event.getSceneY();
            //Position des Elements
            initTranslateX = ((Circle) (event.getSource())).getCenterX();
            initTranslateY = ((Circle) (event.getSource())).getCenterY();
        });
        // hier wird Drag-and-Drop innerhalb der Szene durchgeführt
        returnBall.getElementView().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            //Verschiebung berechnen
            double offsetX = event.getSceneX() - initX;
            double offsetY = event.getSceneY() - initY;
            double newTranslateX = initTranslateX + offsetX;
            double newTranslateY = initTranslateY + offsetY;
            //Element verschieben
            ((Circle) (event.getSource())).setCenterX(newTranslateX);
            ((Circle) (event.getSource())).setCenterY(newTranslateY);

        });

        return returnBall;
    }

    //neues Block-Objekt erzeugen und Listener setzen
    private Block createBlock(double _xPosition, double _yPosition){

        Block returnBlock = new Block(_xPosition,_yPosition);

        returnBlock.getElementView().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            // Durch Mausklick als Aktives Element setzen
            graphicScene.getActiveElement().setIsSelected(false);
            returnBlock.setIsSelected(true);
            graphicScene.setActiveElement(returnBlock);
            // Werte für das Drag-and-Drop speichern
            initX = event.getSceneX();
            initY = event.getSceneY();
            initTranslateX = ((Rectangle) (event.getSource())).getX();
            initTranslateY = ((Rectangle) (event.getSource())).getY();
        });
        // hier wird Drag-and-Drop innerhalb der Szene durchgeführt
        returnBlock.getElementView().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            //Verschiebung berechnen
            double offsetX = event.getSceneX() - initX;
            double offsetY = event.getSceneY() - initY;

            double newTranslateX = initTranslateX + offsetX;
            double newTranslateY = initTranslateY + offsetY;
            //Element verschieben
            ((Rectangle) (event.getSource())).setX(newTranslateX);
            ((Rectangle) (event.getSource())).setY(newTranslateY);

        });

        return returnBlock;
    }

    private Spinner createSpinner(double _xPosition, double _yPosition){
        return new Spinner(_xPosition,_yPosition);
    }
    private Seasaw createSeasaw(double _xPosition, double _yPosition){
        return new Seasaw(_xPosition,_yPosition);
    }
    private Springboard createSpringboard(double _xPosition, double _yPosition){
        return new Springboard(_xPosition,_yPosition);
    }

    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }

}


