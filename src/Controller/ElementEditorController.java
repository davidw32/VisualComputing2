package Controller;

import Model.Ball;
import Model.GraphicScene;
import Model.ElementEditorModel;
import Model.GraphicsObject;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.converter.PaintConverter;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class ElementEditorController {



    @FXML private TextField textFieldXPosition;
    @FXML private TextField textFieldYPosition;
    @FXML private TextField textFieldScaleX;
    @FXML private TextField textFieldScaleY;
    @FXML private TextField textFieldVelocityX;
    @FXML private TextField textFieldVelocityY;
    @FXML private TextField textFieldAccelerationX;
    @FXML private TextField textFieldAccelerationY;
    @FXML private TextField textFieldRotate;
    @FXML private TextField textFieldWeight;
    @FXML private ColorPicker colorPicker;
    @FXML private ChoiceBox choiceBoxPattern;


    private GraphicScene graphicScene;


    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }

    public void initialize(){
      //  System.out.println("Init ElementController");
    }

    /**
     * die Textfelder werden an das "Aktive Element" der Graphic Scene gebunden
     * und der Wechsel des aktiven Elements entsprechend beobachtet
     */
    public void initValues() {
        // für die Textfelder
        graphicScene.getActiveElementProperty().addListener(this::changed);
        // für den Colorpicker
        colorPicker.setValue((Color)graphicScene.getActiveElement().getElementView().getFill());
        colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            colorPicker.setValue(newValue);
            graphicScene.getActiveElement().getElementView().setFill(newValue);

        });
    }

    // die Changelistener für die Textfelder
    private void changed(ObservableValue<? extends GraphicsObject> observableValue, GraphicsObject oldValue, GraphicsObject newValue) {

        colorPicker.setValue((Color)newValue.getElementView().getFill());

        // die alten Bindungen löschen
        Bindings.unbindBidirectional(textFieldXPosition.textProperty(), oldValue.xPositionProperty());
        Bindings.unbindBidirectional(textFieldYPosition.textProperty(), oldValue.yPositionProperty());
        Bindings.unbindBidirectional(textFieldScaleX.textProperty(), oldValue.xScaleProperty());
        Bindings.unbindBidirectional(textFieldScaleY.textProperty(), oldValue.yScaleProperty());
        Bindings.unbindBidirectional(textFieldVelocityX.textProperty(), oldValue.xVelocityProperty());
        Bindings.unbindBidirectional(textFieldVelocityY.textProperty(), oldValue.yVelocityProperty());
        Bindings.unbindBidirectional(textFieldAccelerationX.textProperty(), oldValue.xAccelerationProperty());
        Bindings.unbindBidirectional(textFieldAccelerationY.textProperty(), oldValue.yAccelerationProperty());
        Bindings.unbindBidirectional(textFieldRotate.textProperty(), oldValue.angleProperty());
        Bindings.unbindBidirectional(textFieldWeight.textProperty(), oldValue.weightProperty());

        //die Textfelder mit dem neuen Element verbinden
        StringConverter<Number> converter = new NumberStringConverter();

        Bindings.bindBidirectional(textFieldXPosition.textProperty(), newValue.xPositionProperty(), converter);
        Bindings.bindBidirectional(textFieldYPosition.textProperty(), newValue.yPositionProperty(), converter);
        Bindings.bindBidirectional(textFieldScaleX.textProperty(), newValue.xScaleProperty(), converter);
        Bindings.bindBidirectional(textFieldScaleY.textProperty(), newValue.yScaleProperty(), converter);
        Bindings.bindBidirectional(textFieldVelocityX.textProperty(), newValue.xVelocityProperty(), converter);
        Bindings.bindBidirectional(textFieldVelocityY.textProperty(), newValue.yVelocityProperty(), converter);
        Bindings.bindBidirectional(textFieldAccelerationX.textProperty(), newValue.xAccelerationProperty(), converter);
        Bindings.bindBidirectional(textFieldAccelerationY.textProperty(), newValue.yAccelerationProperty(), converter);
        Bindings.bindBidirectional(textFieldRotate.textProperty(), newValue.angleProperty(), converter);
        Bindings.bindBidirectional(textFieldWeight.textProperty(), newValue.weightProperty(), converter);

    }
}
