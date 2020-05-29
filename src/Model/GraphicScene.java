package Model;

import Controller.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

import java.util.LinkedList;

public class GraphicScene {

    // die Elemente in der Szene
    private LinkedList<GraphicsObject> elementsInScene ;
    // ausgewähltes Objekt in der Szene
    //private GraphicsObject activeElement;

    private ElementEditorController elementEditorController;
    private ElementBarController elementBarController;
    private GraphicSceneController graphicSceneController;
    private OptionBarController optionBarController;
    private PlayerController playerController;

    // das Aktive Element als Property
    private final SimpleObjectProperty<GraphicsObject> activeElement;


    public GraphicScene(){
        elementsInScene= new LinkedList<>();
        // placeholder um die Textfelder im ElementEditor miteinander zu verknüpfen
        Ball placeholder = new Ball(0,0);
        activeElement = new SimpleObjectProperty<>(this, "activeElement", placeholder);

    }

    // die eigendliche Animationsloop
    public void updateScene(){
        System.out.println("GraphicScene updateScene()");
        for (GraphicsObject graphicsObject: elementsInScene){
            graphicsObject.moveElement();
        }

    }

    /**
     *  hier wird ein neues GraphicsObjekt in die Liste eingefügt
     *
     * @param _graphicsObject
     */
    public void addElement(GraphicsObject _graphicsObject){
        elementsInScene.add(_graphicsObject);
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
     * @param _graphicsObject
     */
    public final void setActiveElement(GraphicsObject _graphicsObject) {

        this.activeElement.set(_graphicsObject);
        System.out.println("GraphicScene setActiveElement");
        //elementEditorController.bindActiveElement(_graphicsObject);
    }

    /**
     * gibt das aktive Element zurück
     * @return
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
        for (GraphicsObject go: elementsInScene){
            go.resetElement();
        }
    }

    /**
     * bei allen Elementen werden die Startwerte gespeichert
     */
    public void setAllStartValues(){
        for (GraphicsObject go: elementsInScene){
            go.setStartValues();
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

    public PlayerController getPlayerController() {
        return playerController;
    }

    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }
}
