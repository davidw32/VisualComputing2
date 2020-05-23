package Controller;

import Model.Ball;
import Model.GraphicScene;
import Model.ElementEditorModel;
import Model.GraphicsObject;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;

public class ElementEditorController {

    @FXML StartController startController;
    private GraphicScene graphicScene;


    @FXML
    private TextField textFieldXPosition;
    @FXML
    private TextField textFieldYPosition;

    private ChangeListener<Object> changeXPosition;
    private ChangeListener<Object> changeYPosition;


    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }

    public void setStartController(StartController startController) {
        this.startController = startController;
    }

    public void initialize(){
        System.out.println("Init ElementController");

    }

    public void bindActiveElement(GraphicsObject activeElement){


    }


    public void updateElement() {

    }



}
