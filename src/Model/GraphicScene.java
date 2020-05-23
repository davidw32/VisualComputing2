package Model;

import Controller.*;

import java.util.LinkedList;

public class GraphicScene {

    // die Elemente in der Szene
    private LinkedList<GraphicsObject> elementsInScene = new LinkedList<>();
    private GraphicsObject activeElement;
    private ElementEditorController elementEditorController;
    private ElementBarController elementBarController;
    private GraphicSceneController graphicSceneController;
    private OptionBarController optionBarController;
    private PlayerController playerController;


    // die eigendliche Animationsloop
    public void updateScene(){
        System.out.println("GraphicScene updateScen()");
    }

    public void addElement(GraphicsObject _graphicsObject){
        System.out.println("Element hinzugefügt");
        elementsInScene.add(_graphicsObject);
    }

    public void setActiveElement(GraphicsObject _graphicsObject) {
        activeElement = _graphicsObject;
        System.out.println("GraphicScene setActiveElement");
        elementEditorController.bindActiveElement(activeElement);
    }

    public void removeElement(GraphicsObject _graphicsObject){

    }

    public void resetAllElements(){

    }

    public void clearScene(){

    }


    public LinkedList<GraphicsObject> getElementsInScene() {
        return elementsInScene;
    }

    public void setElementsInScene(LinkedList<GraphicsObject> _elements) {
        this.elementsInScene = _elements;
    }

    public GraphicsObject getActiveElement() {
        return activeElement;
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
