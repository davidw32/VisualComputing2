package Controller;

import Model.GraphicScene;
import Model.GraphicsObject;
import com.sun.jdi.Value;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.text.ParseException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;

import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.rgb;

public class ElementEditorController {


    @FXML
    private TextField textFieldXPosition;
    @FXML
    private TextField textFieldYPosition;
    @FXML
    private TextField textFieldScaleX;
    @FXML
    private TextField textFieldScaleY;
    @FXML
    private TextField textFieldVelocityX;
    @FXML
    private TextField textFieldVelocityY;
    @FXML
    private TextField textFieldAccelerationX;
    @FXML
    private TextField textFieldAccelerationY;
    @FXML
    private TextField textFieldRotate;
    @FXML
    private TextField textFieldWeight;
    @FXML
    private TextField textFieldFriction;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ChoiceBox choiceBoxPattern;
    @FXML
    private GridPane editor;
    @FXML
    private GridPane helpText;

    private LinkedList<TextField> allTextFields;
    private GraphicScene graphicScene;


    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }

    public void initialize() {
        System.out.println("Init ElementController");
    }

    /**
     * die Textfelder werden an das "Aktive Element" der Graphic Scene gebunden
     * und der Wechsel des aktiven Elements entsprechend beobachtet
     */
    public void initValues() {
        setUpValidation(textFieldXPosition);
        setUpValidation(textFieldYPosition);

        // für die Textfelder
        graphicScene.getActiveElementProperty().addListener(this::changed);


        // für den Colorpicker
        colorPicker.setValue((Color) graphicScene.getActiveElement().getElementView().getFill());
        colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            colorPicker.setValue(newValue);
            graphicScene.getActiveElement().getElementView().setFill(newValue);

        });
    }

    // die Changelistener für die Textfelder
    private void changed(ObservableValue<? extends GraphicsObject> observableValue, GraphicsObject oldValue, GraphicsObject newValue) {


        colorPicker.setValue((Color) newValue.getElementView().getFill());

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
        Bindings.unbindBidirectional(textFieldFriction.textProperty(), oldValue.frictionProperty());

        //die Textfelder mit dem neuen Element verbinden
        StringConverter<Number> converter = new NumberStringConverter(Locale.US, "#####.###") {
            @Override
            public String toString(Number object) {
                return object == null ? "" : object.toString();
            }

            @Override
            public Number fromString(String string) throws NumberFormatException {

                    try {
                        return Double.parseDouble(string);
                    } catch (NumberFormatException ex) {
                        return 0;
                    }

            }
        };

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
        Bindings.bindBidirectional(textFieldFriction.textProperty(), newValue.frictionProperty(), converter);

    }

    private void setUpValidation(final TextField tf){
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validate(tf);
            }
        });
        validate(tf);

    }
    private void validate(TextField textField){
        ObservableList<String> styleClass = textField.getStyleClass();
        if(!textField.getText().matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?")){
            System.out.println("Falsche Zahl");
            if( ! styleClass.contains("error")) {
                styleClass.add("error");
            }
        }else {
            styleClass.removeAll(Collections.singleton("error"));
        }

    }


    public GridPane getEditor() {
        return editor;
    }

    public GridPane getHelpText() {
        return helpText;
    }

    //Schließt das Hilfefenster
    public void onClose(ActionEvent actionEvent) {
        helpText.setVisible(false);
        editor.setVisible(true);
    }
}
