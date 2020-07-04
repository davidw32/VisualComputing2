package Model;

import Controller.*;
import Helpers.SceneTime;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Schnittstelle zwischen der Gui und Datenklassen der Simulation
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

    Line ground = new Line(0,820,1250,820);

    public GraphicScene(){

        elementsInScene= new LinkedList<>();
        // placeholder um die Textfelder im ElementEditor miteinander zu verknüpfen
        placeholder = new Ball(0,0);
        placeholder.setWeight(0);
        placeholder.setRadius(0);
        //das aktuell ausgewählte Element
        activeElement = new SimpleObjectProperty<>(this, "activeElement", placeholder);

        wind = new Wind();
    }

    // die eigentliche Animationsloop
    public void updateScene(){
        //
        Line[] collisionLines = findCollisionLines();
        for (GraphicsObject graphicsObject: elementsInScene){

            if ( graphicsObject instanceof Ball){
                //Prüfe ob der Ball mit weiteren Elementen kollidiert
                ((Ball) graphicsObject).calcWind(getWind());
                for(GraphicsObject secondObject: elementsInScene){
                    // alle anderen Elemente
                    if (!graphicsObject.equals(secondObject)){
                        //falls das zweite ein Ball ist
                        if (secondObject instanceof Ball) ((Ball) graphicsObject).calculateCollisionWithBall((Ball)secondObject);
                    }
                    if(secondObject instanceof Spinner){
                        ((Ball)graphicsObject).checkCollisionWithSpinner((Spinner)(secondObject));
                    }
                    if(secondObject instanceof Springboard.Board){
                        ((Ball)graphicsObject).collisionDetectionBoardSpring(((Springboard.Board) secondObject).getOutlines()[0],(Springboard.Board) secondObject);
                    }
                    if(secondObject instanceof Seesaw){
                        ((Ball)graphicsObject).checkCollisionWithSeesaw((Seesaw) (secondObject));
                    }
                }
                ((Ball)graphicsObject).collisionDetection(collisionLines);

            }
            if(graphicsObject instanceof  Springboard)
            {
                ((Springboard)graphicsObject).moveElement(elementsInScene);
            }
            else
            {
                graphicsObject.moveElement();
            }
        }

    }

    /**
     * Findet alle Linien in der Szene mit denen kollidiert werden kann und gibt diese zurueck
     * @return Kollisionslinien der Szene
     */
    public Line[] findCollisionLines(){
        ArrayList<Line> collisionLines = new ArrayList<>();

        for (GraphicsObject graphicsObject: elementsInScene){
            if(graphicsObject instanceof Block && !(graphicsObject instanceof Springboard.Board)){
                for (Line line: ((Block) graphicsObject).getOutlines()) {
                    collisionLines.add(line);
                }
            }
            else if(graphicsObject instanceof Springboard.Board)
            {

                collisionLines.add( ( (Springboard.Board)graphicsObject).getOutlines()[1]);
                collisionLines.add( ( (Springboard.Board)graphicsObject).getOutlines()[2]);
                collisionLines.add( ( (Springboard.Board)graphicsObject).getOutlines()[3]);
            }
        }
        collisionLines.add(ground);
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

        graphicSceneController.getGraphicPane().getChildren().remove(getActiveElement().getElementView());
        if (getActiveElement() instanceof Ball) {
            graphicSceneController.getGraphicPane().getChildren().remove(((Ball)getActiveElement()).getDirectionLine());
            graphicSceneController.getGraphicPane().getChildren().remove(((Ball)getActiveElement()).getVelocityText());
        }
        if (getActiveElement() instanceof Spinner){
            graphicSceneController.getGraphicPane().getChildren().remove(((Spinner)getActiveElement()).getCenter());
        }
        if(getActiveElement() instanceof Seesaw){
            getGraphicSceneController().getGraphicPane().getChildren().remove(((Seesaw)getActiveElement()).getTriangle());
        }
        if(getActiveElement() instanceof  Springboard)
        {
            getGraphicSceneController().getGraphicPane().getChildren().remove(((Springboard)getActiveElement()).getBoard().getElementView());
            elementsInScene.remove(((Springboard)getActiveElement()).getBoard());
        }

        elementsInScene.remove(getActiveElement());

        setActiveElement(placeholder);
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

        graphicSceneController.getGraphicPane().getChildren().clear();

        elementsInScene.clear();

        setActiveElement(placeholder);

    }

    /**
     * @return die Liste aller Elemente in der Szene
     */
    public LinkedList<GraphicsObject> getElementsInScene() {
        return elementsInScene;
    }

    /**
     * Eine komplette Szene laden
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
