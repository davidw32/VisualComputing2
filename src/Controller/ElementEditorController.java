package Controller;

import Model.Ball;
import Model.GraphicScene;
import Model.ElementEditorModel;
import Model.GraphicsObject;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class ElementEditorController {

    @FXML StartController startController;
    private GraphicScene graphicScene;
    private GraphicsObject activeElement;


    @FXML
    private TextField textFieldXPosition;
    @FXML
    private TextField textFieldYPosition;

    private ChangeListener<GraphicsObject> activeElementChange;


    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }

    public void setStartController(StartController startController) {
        this.startController = startController;
    }

    public void initialize(){
        System.out.println("Init ElementController");


    }

    /**
     * Hier wird das acitive Element mit den Textfeldern im Editor verknüpft
     * @param _activeElement
     */
    public void bindActiveElement(GraphicsObject _activeElement){
        System.out.println("BindActive");


        StringConverter<Number> converter = new NumberStringConverter();

        Bindings.bindBidirectional(textFieldXPosition.textProperty(), _activeElement.xPositionProperty(), converter);
        Bindings.bindBidirectional(textFieldYPosition.textProperty(), _activeElement.yPositionProperty(), converter);

    }


    public void initValues() {
        System.out.println("InitValues");
        activeElement = graphicScene.getActiveElement();

        graphicScene.getActiveElementProperty().addListener((observableValue, oldValue, newValue) -> {

            System.out.println(observableValue.getValue());
            System.out.println("old: " + oldValue);
            System.out.println("new: " +newValue);
                });
    }



}
