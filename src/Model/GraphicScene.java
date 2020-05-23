package Model;

import Controller.ElementEditorController;

import java.util.LinkedList;

public class GraphicScene {

    // die Elemente in der Szene
    private LinkedList<GraphicsObject> elementsInScene = new LinkedList<>();
    private GraphicsObject activeElement;
    private ElementEditorController elementEditorController;

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
}
