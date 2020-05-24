package Controller;

import Model.GraphicScene;
import Model.GraphicsObject;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ElementEditorController {

    private GraphicScene graphicScene;


    @FXML private TextField textFieldXPosition;
    @FXML private TextField textFieldYPosition;

    // GridPane welches Objekteigenschaften anzeigt
    @FXML private GridPane transformGrid;
    //Parent von toolWindow,advancedOptionsWindow,helpWindow
    @FXML private ScrollPane elementWindow;
    // GridPane der erweiterte Einstellung
    @FXML private GridPane advancedOptionsWindow;
    // GridPane des Hilfebereiches
    @FXML private GridPane helpWindow;

    private ChangeListener<Object> changeXPosition;
    private ChangeListener<Object> changeYPosition;


    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }

    public void initialize(){
        System.out.println("Init ElementController");
    }

    public void bindActiveElement(GraphicsObject activeElement){


    }


    public void updateElement() {

    }


    public ScrollPane getElementWindow() {
        return elementWindow;
    }

    public GridPane getTransformGrid() {
        return transformGrid;
    }

    public GridPane getHelpWindow() {
        return helpWindow;
    }

    public GridPane getAdvancedOptionsWindow() {
        return advancedOptionsWindow;
    }
}
