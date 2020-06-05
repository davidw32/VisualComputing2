package Model;

import Controller.*;
import Helpers.SceneTime;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.shape.Line;

import java.util.LinkedList;

/**
 * @author Patrick Pavlenko,Pamela Trowe
 * Globale Klasse, welche über mehrere Klassen gepassed wird,um essenzielle
 * Werte / Objekte zuzugreifen zu können
 */
public class GraphicScene {

    // die Elemente in der Szene
    private LinkedList<GraphicsObject> elementsInScene ;
    // ausgewähltes Objekt in der Szene
    //private GraphicsObject activeElement;

    // Zeit bzw Timer der unten im Programm gesehen werden kann
    private SceneTime timer = new SceneTime();
    //Faktor fuer den Zeitraffer,wecher die Anzahl der Frames bestimmt
    private double timeFactor = 1;

    private ElementEditorController elementEditorController;
    private ElementBarController elementBarController;
    private GraphicSceneController graphicSceneController;
    private OptionBarController optionBarController;
    private PlayerController playerController;

    // das Aktive Element als Property
    private final SimpleObjectProperty<GraphicsObject> activeElement;

    //für die erste Szene
    Line line1 = new Line(0,220,400,220);
    Line line2 = new Line(400,220,500,200);
    Line line3 = new Line(500,200,700,300);
    Line line4 = new Line(700,300,900,300);
    Line[] lines = new Line[5];

    public GraphicScene(){
        elementsInScene= new LinkedList<>();
        // placeholder um die Textfelder im ElementEditor miteinander zu verknüpfen
        Ball placeholder = new Ball(0,0);
        activeElement = new SimpleObjectProperty<>(this, "activeElement", placeholder);

        //für die erste Szene
        lines[0] = line1;
        lines[1] = line2;
        lines[2] = line3;
        lines[3] = line4;
        //Unterer Rand
        lines[4] = new Line(0,820,1115,820);


    }

    // die eigentliche Animationsloop
    public void updateScene(){

        for (GraphicsObject graphicsObject: elementsInScene){

            if ( graphicsObject instanceof Ball){
                //wie sollen wir das allgemein lösen?????
                ((Ball)graphicsObject).collisionDetection(lines);
            }
            graphicsObject.moveElement();
        }

    }

    /**
     *  hier wird ein neues GraphicsObjekt in die Liste eingefügt
     *
     * @param _graphicsObject - das GraphicsObjekt, dass neu eingefügt werden soll
     */
    public void addElement(GraphicsObject _graphicsObject){
        elementsInScene.add(_graphicsObject);
        _graphicsObject.setStartValues();
    }

    /**
     * getter für die Property "activeElement"
     * @return ativeElement-Property
     */
    public SimpleObjectProperty<GraphicsObject> getActiveElementProperty(){
        return activeElement;
    }

    /**
     * hier wird das angeklickte Element als Aktiv gesetzt, dabei werden die Werte im ElementEditor angepasst
     * die Listener vom Vorgänger müssen entfernt und auf das neue Objekt gesetzt werden.
     * @param _graphicsObject - das per Mausklick ausgewählte Element
     */
    public final void setActiveElement(GraphicsObject _graphicsObject) {

        this.activeElement.set(_graphicsObject);
        System.out.println("GraphicScene setActiveElement");
        //elementEditorController.bindActiveElement(_graphicsObject);
    }

    /**
     * gibt das aktive Element zurück
     * @return das aktuelle aktive (ausgewählte) Element
     */
    public final GraphicsObject getActiveElement(){
        return activeElement.get();
    }


    /**
     * Hier wird das übergebene Element aus der Liste gelöscht, und die Listener werden entfernt
     * @param _graphicsObject
     */

    public void removeElement(GraphicsObject _graphicsObject){

    }

    /**
     * alle Elemente werden auf ihre Startwerte zurückgesetzt
     */
    public void resetAllElements(){
        for (GraphicsObject graphicsObject: elementsInScene){
            graphicsObject.resetElement();
        }
    }

    /**
     * bei allen Elementen werden die Startwerte gespeichert
     */
    public void setAllStartValues(){
        for (GraphicsObject graphicsObject: elementsInScene){
            graphicsObject.setStartValues();
        }
    }

    /**
     * hier wird die gesammte Szene gelöscht
     */
    public void clearScene(){

    }

    /**
     * um die Liste speichern zu können
     * @return
     */
    public LinkedList<GraphicsObject> getElementsInScene() {
        return elementsInScene;
    }

    /**
     * wird aufgerufen eine vorhande Szene geladen wird
     * @param _elements
     */
    public void setElementsInScene(LinkedList<GraphicsObject> _elements) {
        this.elementsInScene = _elements;
    }


    public SceneTime getTimer() {
        return timer;
    }

    public double getTimeFactor() {
        return timeFactor;
    }

    public ElementEditorController getElementEditorController() {
        return elementEditorController;
    }

    public void setElementEditorController(ElementEditorController elementEditorController) {
        this.elementEditorController = elementEditorController;
    }

    public ElementBarController getElementBarController() {
        return elementBarController;
    }

    public void setElementBarController(ElementBarController elementBarController) {
        this.elementBarController = elementBarController;
    }

    public GraphicSceneController getGraphicSceneController() {
        return graphicSceneController;
    }

    public void setGraphicSceneController(GraphicSceneController graphicSceneController) {
        this.graphicSceneController = graphicSceneController;
    }

    public OptionBarController getOptionBarController() {
        return optionBarController;
    }

    public void setOptionBarController(OptionBarController optionBarController) {
        this.optionBarController = optionBarController;
    }

    public void setTimeFactor(double timeFactor) {
        this.timeFactor = timeFactor;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }
}
