package Model;

import Controller.*;
import Helpers.SceneTime;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.shape.Line;

import javax.swing.*;
import java.util.ArrayList;
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
    private StartScreenController startScreenController;
    private StartController startController;
    private Ball placeholder;
    private final Wind wind;

    // das Aktive Element als Property
    private final SimpleObjectProperty<GraphicsObject> activeElement;

    //für die erste Szene
    Line line1 = new Line(0,0,0,0);
    Line line2 = new Line(0,0,0,0);
    Line line3 = new Line(0,0,0,0);
    Line line4 = new Line(0,0,0,0);
    Line[] lines = new Line[5];

    public GraphicScene(){
        System.out.println("klasse graphicScene");
        elementsInScene= new LinkedList<>();
        // placeholder um die Textfelder im ElementEditor miteinander zu verknüpfen
        placeholder = new Ball(0,0);
        placeholder.setWeight(0);
        placeholder.setRadius(0);

        activeElement = new SimpleObjectProperty<>(this, "activeElement", placeholder);

        //für die erste Szene
        lines[0] = line1;
        lines[1] = line2;
        lines[2] = line3;
        lines[3] = line4;
        //Unterer Rand
        lines[4] = new Line(0,820,1145,820);

        wind = new Wind();
    }

    // die eigentliche Animationsloop
    public void updateScene(){
        //
        Line[] collisionLines = findCollisionLines();
        for (GraphicsObject graphicsObject: elementsInScene){

            if ( graphicsObject instanceof Ball){
                //Prüfe ob der Ball mit weiteren Elementen kollidiert
                for(GraphicsObject secondObject: elementsInScene){
                    // alle anderen Elemente
                    if (!graphicsObject.equals(secondObject)){
                        //falls das zweite ein Ball ist
                        if (secondObject instanceof Ball) ((Ball) graphicsObject).calculateCollisionWithBall((Ball)secondObject);
                    }
                    if(secondObject instanceof Spinner){
                        ((Ball)graphicsObject).checkCollisionWithSpinner((Spinner)(secondObject));
                    }
                    if(secondObject instanceof Springboard)
                    {
                        System.out.println();
                        ((Ball)graphicsObject).collisionDetection(((Springboard) secondObject).getOutlines());
                    }
                }
                ((Ball)graphicsObject).collisionDetection(collisionLines);

            }
            graphicsObject.moveElement();
        }

    }

    /**
     * Findet alle Linien in der Szene mit denen kollidiert werden kann und gibt diese zurueck
     * @return Kollisionslinien der Szene
     */
    public Line[] findCollisionLines(){
        ArrayList<Line> collisionLines = new ArrayList<>();
        for(Line line : lines){
            collisionLines.add(line);
        }
        for (GraphicsObject graphicsObject: elementsInScene){
            if(graphicsObject instanceof Block){
                for (Line line: ((Block) graphicsObject).getOutlines()) {
                    collisionLines.add(line);
                }
            }
        }
        Line[] lineArray = new Line[collisionLines.size()];
        for(int i = 0; i < collisionLines.size(); i++){
            lineArray[i] = collisionLines.get(i);
        }
        return lineArray;
    }

    /**
     *  hier wird ein neues GraphicsObjekt in die Liste eingefügt
     *
     * @param _graphicsObject - das GraphicsObjekt, dass neu eingefügt werden soll
     */
    public void addElement(GraphicsObject _graphicsObject){
        elementsInScene.add(_graphicsObject);
        _graphicsObject.setStartValues();
        setActiveElement(_graphicsObject);
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
        this.getActiveElement().setIsSelected(false);
        _graphicsObject.setIsSelected(true);
        this.activeElement.set(_graphicsObject);
        //System.out.println("GraphicScene setActiveElement");

    }

    /**
     * gibt das aktive Element zurück
     * @return das aktuelle aktive (ausgewählte) Element
     */
    public final GraphicsObject getActiveElement(){
        return activeElement.get();
    }


    /**
     * Hier wird das aktive Element aus der Liste gelöscht
     */

    public void deleteActiveElement(){

        //getActiveElement().getElementView().setVisible(false);
        graphicSceneController.getGraphicPane().getChildren().remove(getActiveElement().getElementView());
        if (getActiveElement() instanceof Ball) {
            graphicSceneController.getGraphicPane().getChildren().remove(((Ball)getActiveElement()).getDirectionLine());
            graphicSceneController.getGraphicPane().getChildren().remove(((Ball)getActiveElement()).getVelocityText());
        }
        setActiveElement(placeholder);
        elementsInScene.remove(getActiveElement());
    }

    /**
     * alle Elemente werden auf ihre Startwerte zurückgesetzt
     */
    public void resetAllElements(){
        for (GraphicsObject graphicsObject: elementsInScene){
            graphicsObject.resetElement();
            if (graphicsObject instanceof Ball){
                ((Ball) graphicsObject).resetDirectionLine();
            }
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
        for (GraphicsObject graphicsObject: elementsInScene){
            graphicSceneController.getGraphicPane().getChildren().remove(graphicsObject.getElementView());
            if (graphicsObject instanceof Ball) {
                graphicSceneController.getGraphicPane().getChildren().remove(((Ball)graphicsObject).getDirectionLine());
                graphicSceneController.getGraphicPane().getChildren().remove(((Ball)graphicsObject).getVelocityText());
            }
        }
        elementsInScene.clear();
        setActiveElement(placeholder);

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

    public void setStartScreenController(StartScreenController startScreenController) {
    }
    public StartScreenController getStartScreenController(){ return startScreenController; }

    public void setStartController(StartController startController) {
        this.startController = startController;
    }

    public StartController getStartController() {
        return startController;
    }

    public Ball getPlaceholder() {
        return placeholder;
    }

    public Wind getWind(){
        return  this.wind;
    }

}
