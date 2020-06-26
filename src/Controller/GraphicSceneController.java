package Controller;

import Model.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class GraphicSceneController {
    @FXML
    private Pane graphicPane;

    private GraphicScene graphicScene;

    private double initX, initY, initTranslateX, initTranslateY;

    public void initialize(){

        System.out.println("Init GraphicSceneController");
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
                        //neues Element erzeugen und Listener hinzufügen
                        Ball newBall = new Ball(event.getX(), event.getY());
                        addListenersToObject(newBall);
                        // in der Liste einfügen
                        graphicScene.addElement(newBall);
                        // in der Szene anzeigen
                        graphicPane.getChildren().addAll(newBall.getElementView(), newBall.getDirectionLine(), newBall.getVelocityText());

                        success = true;
                    }
                    if(db.getString().equals("blockDummy")){
                        Block newBlock = new Block(event.getX(), event.getY());
                        addListenersToObject(newBlock);
                        graphicScene.addElement(newBlock);
                        graphicPane.getChildren().add(newBlock.getElementView());
                        success = true;
                    }

                    //Platzhalter für Drag-and-Drop bis die Elemente implementiert wurden
                    if(db.getString().equals("springboardDummy")){
                        Springboard springboard = new Springboard(event.getX(),event.getY());
                        addListenersToObject(springboard);
                        graphicScene.addElement(springboard);
                        graphicPane.getChildren().addAll(springboard.getElementView());
                        success = true;
                    }

                    if(db.getString().equals("spinnerDummy")){

                        Spinner newSpinner = new Spinner(event.getX(), event.getY());
                        addListenersToObject(newSpinner);

                        graphicScene.addElement(newSpinner);

                        graphicPane.getChildren().addAll(newSpinner.getElementView(), newSpinner.getCenter());
                        success = true;
                    }
                    if(db.getString().equals("seesawDummy")){

                        Seesaw newSeesaw = new Seesaw(event.getX(), event.getY());
                        addListenersToObject(newSeesaw);

                        graphicScene.addElement(newSeesaw);
                        graphicPane.getChildren().addAll(newSeesaw.getElementView(), newSeesaw.getTriangle());
                        success = true;
                    }

                }
                event.setDropCompleted(success);
                event.consume();
            }
        });

    }


    public void addListenersToObject(GraphicsObject _graphicsObject){
        _graphicsObject.getElementView().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    //Element als Actives Element definieren

                    graphicScene.setActiveElement(_graphicsObject);
        });

        _graphicsObject.getElementView().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {

            // Position Mauszeiger
            initX =(int) event.getSceneX();
            initY =(int) event.getSceneY();
            //Position des Elements
            initTranslateX=_graphicsObject.getXPosition();
            initTranslateY=_graphicsObject.getYPosition();
        });
        // hier wird Drag-and-Drop innerhalb der Szene durchgeführt
        _graphicsObject.getElementView().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            //Verschiebung berechnen
            double offsetX = event.getSceneX() - initX;
            double offsetY = event.getSceneY() - initY;
            double newTranslateX = initTranslateX + offsetX;
            double newTranslateY = initTranslateY + offsetY;
            //Element verschieben
            _graphicsObject.setXPosition((int)newTranslateX);
            _graphicsObject.setYPosition((int)newTranslateY);
            if(_graphicsObject instanceof Ball){
                ((Ball) _graphicsObject).updateDirectionLine();
            }
            if(_graphicsObject instanceof Spinner){
                ((Spinner)_graphicsObject).updateOutlines();
            }
            if(_graphicsObject instanceof Seesaw){
                ((Seesaw)_graphicsObject).updateOutlines();
            }
        });

    }
    private Text createPlaceholder(double x, double y){
        Text placeholder = new Text();
        placeholder.setX(x);
        placeholder.setY(y);
        placeholder.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {

            // Position Mauszeiger
            initX = (int)event.getSceneX();
            initY = (int)event.getSceneY();
            //Position des Elements
            initTranslateX = ((Text)event.getSource()).getX();
            initTranslateY = ((Text)event.getSource()).getY();
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


    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }

    public Pane getGraphicPane() {
        return graphicPane;
    }
}


