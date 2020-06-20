package Controller;

import Model.*;
import Model.Spinner;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;

import static javafx.scene.paint.Color.rgb;

public class ElementEditorController {


    @FXML
    private HBox spinnerEditor;
    @FXML
    private Slider spinnerSlider;
    @FXML
    private VBox windEditor;
    @FXML
    private CheckBox checkBoxWind;
    @FXML
    private Slider windDirectionSlider;
    @FXML
    private Slider windForceSlider;
    @FXML
    private TextField textFieldXPosition;
    @FXML
    private TextField textFieldYPosition;
    @FXML
    private TextField textFieldWidth;
    @FXML
    private TextField textFieldHeight;
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
    private TextField textFieldElasticity;
    @FXML
    private TextField textFieldRadius;

    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ChoiceBox choiceBoxPattern;
    @FXML
    private GridPane editor;
    @FXML
    private GridPane helpText;
    @FXML private Label infoLabel;

    @FXML private Label scaleFactor;

    private LinkedList<TextField> allTextFields;
    private GraphicScene graphicScene;


    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }

    public void initialize() {
        setUpValidation(textFieldXPosition);
        setUpValidation(textFieldYPosition);
        setUpValidation(textFieldVelocityX);
        setUpValidation(textFieldVelocityY);
        setUpValidation(textFieldAccelerationX);
        setUpValidation(textFieldAccelerationY);
        setUpValidation(textFieldWidth);
        setUpValidation(textFieldHeight);
        setUpValidation(textFieldElasticity);
        setUpValidation(textFieldRotate);
        setUpValidation(textFieldWeight);
        setUpValidation(textFieldRadius);

        spinnerSlider.setDisable(true);
        windDirectionSlider.setDisable(true);
        windForceSlider.setDisable(true);

        windDirectionSlider.valueProperty().addListener(((observable, oldValue, newValue) -> graphicScene.getWind().setWindDirection((Double) newValue)));
        windForceSlider.valueProperty().addListener(((observable, oldValue, newValue) -> graphicScene.getWind().setWindForce((Double) newValue)));


    }

    //überprüft, ob die Eingabe eine Zahl ist
    private void setUpValidation(final TextField tf) {
        tf.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                validate(tf);
            }

        });

        validate(tf);
        tf.setDisable(true);
    }

    private void validate(TextField tf) {
        ObservableList<String> styleClass = tf.getStyleClass();
        if (tf.getText().trim().length() == 0 && tf.getText().matches("[-+]?[0-9]*\\.?[0-9]*")) {
            if (!styleClass.contains("error")) {
                styleClass.add("error");
                infoLabel.setText("Bitte eine Zahl eingeben!");
                infoLabel.setStyle("-fx-text-fill: red;");
            }
        } else {
            // remove all occurrences:
            infoLabel.setText("");
            styleClass.removeAll(Collections.singleton("error"));
        }
    }

    /**
     * die Textfelder werden an das "Aktive Element" der Graphic Scene gebunden
     * und der Wechsel des aktiven Elements entsprechend beobachtet
     */
    public void initValues() {


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

        if (newValue instanceof Spinner) {
            spinnerSlider.setDisable(false);
            textFieldXPosition.setDisable(false);
            textFieldYPosition.setDisable(false);
            textFieldWidth.setDisable(false);
            textFieldWidth.setVisible(true);
            textFieldHeight.setDisable(false);
            textFieldHeight.setVisible(true);
            textFieldAccelerationX.setDisable(true);
            textFieldAccelerationY.setDisable(true);
            textFieldVelocityX.setDisable(true);
            textFieldVelocityY.setDisable(true);
            textFieldRotate.setDisable(true);
            textFieldElasticity.setDisable(true);
            textFieldWeight.setDisable(true);
            textFieldRadius.setDisable(true);
            textFieldRadius.setVisible(false);

            scaleFactor.setText("Width/Height");


        } else if (newValue instanceof Ball) {
            spinnerSlider.setDisable(true);
            textFieldXPosition.setDisable(false);
            textFieldYPosition.setDisable(false);
            textFieldWidth.setDisable(true);
            textFieldWidth.setVisible(false);
            textFieldHeight.setDisable(true);
            textFieldHeight.setVisible(false);
            textFieldRadius.setVisible(true);
            textFieldRadius.setDisable(false);
            textFieldAccelerationX.setDisable(false);
            textFieldAccelerationY.setDisable(false);
            textFieldVelocityX.setDisable(false);
            textFieldVelocityY.setDisable(false);
            textFieldRotate.setDisable(true);
            textFieldElasticity.setDisable(false);
            textFieldWeight.setDisable(false);
            scaleFactor.setText("Radius");

        } else if (newValue instanceof Block){
            spinnerSlider.setDisable(true);
            textFieldXPosition.setDisable(false);
            textFieldYPosition.setDisable(false);
            textFieldWidth.setDisable(false);
            textFieldWidth.setVisible(true);
            textFieldHeight.setDisable(false);
            textFieldHeight.setVisible(true);
            textFieldRadius.setDisable(true);
            textFieldRadius.setVisible(false);
            textFieldAccelerationX.setDisable(true);
            textFieldAccelerationY.setDisable(true);
            textFieldVelocityX.setDisable(true);
            textFieldVelocityY.setDisable(true);
            textFieldRotate.setDisable(false);
            textFieldElasticity.setDisable(true);
            textFieldWeight.setDisable(true);
            scaleFactor.setText("Width/Height");
        }

        colorPicker.setValue((Color) newValue.getElementView().getFill());

        // die alten Bindungen löschen
        Bindings.unbindBidirectional(textFieldXPosition.textProperty(), oldValue.xPositionProperty());
        Bindings.unbindBidirectional(textFieldYPosition.textProperty(), oldValue.yPositionProperty());
        Bindings.unbindBidirectional(textFieldWidth.textProperty(), oldValue.widthProperty());
        Bindings.unbindBidirectional(textFieldHeight.textProperty(), oldValue.heightProperty());
        Bindings.unbindBidirectional(textFieldRadius.textProperty(), oldValue.radiusProperty());
        Bindings.unbindBidirectional(textFieldVelocityX.textProperty(), oldValue.xVelocityProperty());
        Bindings.unbindBidirectional(textFieldVelocityY.textProperty(), oldValue.yVelocityProperty());
        Bindings.unbindBidirectional(textFieldAccelerationX.textProperty(), oldValue.xAccelerationProperty());
        Bindings.unbindBidirectional(textFieldAccelerationY.textProperty(), oldValue.yAccelerationProperty());
        Bindings.unbindBidirectional(textFieldRotate.textProperty(), oldValue.angleProperty());
        Bindings.unbindBidirectional(textFieldWeight.textProperty(), oldValue.weightProperty());
        Bindings.unbindBidirectional(textFieldElasticity.textProperty(), oldValue.frictionProperty());


        if (oldValue instanceof Spinner) {
            Bindings.unbindBidirectional(spinnerSlider.valueProperty(), ((Spinner) oldValue).rotationalSpeedProperty());
        }

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
        Bindings.bindBidirectional(textFieldWidth.textProperty(), newValue.widthProperty(), converter);
        Bindings.bindBidirectional(textFieldHeight.textProperty(), newValue.heightProperty(), converter);
        Bindings.bindBidirectional(textFieldRadius.textProperty(), newValue.radiusProperty(), converter);
        Bindings.bindBidirectional(textFieldVelocityX.textProperty(), newValue.xVelocityProperty(), converter);
        Bindings.bindBidirectional(textFieldVelocityY.textProperty(), newValue.yVelocityProperty(), converter);
        Bindings.bindBidirectional(textFieldAccelerationX.textProperty(), newValue.xAccelerationProperty(), converter);
        Bindings.bindBidirectional(textFieldAccelerationY.textProperty(), newValue.yAccelerationProperty(), converter);
        Bindings.bindBidirectional(textFieldRotate.textProperty(), newValue.angleProperty(), converter);
        Bindings.bindBidirectional(textFieldWeight.textProperty(), newValue.weightProperty(), converter);
        Bindings.bindBidirectional(textFieldElasticity.textProperty(), newValue.frictionProperty(), converter);

        if (newValue instanceof Spinner) {
            Bindings.bindBidirectional(spinnerSlider.valueProperty(), ((Spinner) newValue).rotationalSpeedProperty());
        }


    }


    //Schließt das Hilfefenster
    public void onClose(ActionEvent actionEvent) {
        helpText.setVisible(false);
        editor.setVisible(true);
    }

    //schaltet den Wind an
    public void setOnWind(ActionEvent actionEvent) {
        if (checkBoxWind.isSelected()) {
            windDirectionSlider.setDisable(false);
            windForceSlider.setDisable(false);
            graphicScene.getWind().setIsActivated(true);

        } else {
            windForceSlider.setDisable(true);
            windDirectionSlider.setDisable(true);
            graphicScene.getWind().setIsActivated(false);
        }
    }


    public TextField getTextFieldWidth() {
        return textFieldWidth;
    }

    public TextField getTextFieldHeight() {
        return textFieldHeight;
    }

    public GridPane getEditor() {
        return editor;
    }
    public GridPane getHelpText() {
        return helpText;
    }
}
