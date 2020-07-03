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

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;

import static javafx.scene.paint.Color.rgb;

public class ElementEditorController {


    @FXML private Label labelRotate;
    @FXML private Label labelWeight;
    @FXML private Label labelMovement;
    @FXML private Label labelVelocity;
    @FXML private Label labelAcceleration;
    @FXML private Label labelSpinner;
    @FXML private Label labelFlexSlider;
    @FXML
    private HBox spinnerEditor;
    @FXML
    private Slider spinnerSlider;
    @FXML
    private Slider flexibilitySlider;
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
    private StringConverter<Number> converter;
    private LinkedList<TextField> allTextFields;
    private GraphicScene graphicScene;


    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }

    //Die Textfleder werden initialisiert und mit den Listenern versehen
    public void initialize() {
        System.out.println("Init ElementEditorController");
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
        spinnerSlider.setVisible(false);
        labelSpinner.setVisible(false);
        windEditor.setOpacity(0.6);
        windDirectionSlider.setDisable(true);
        windForceSlider.setDisable(true);
        textFieldElasticity.setVisible(false);

        windDirectionSlider.valueProperty().addListener(((observable, oldValue, newValue) -> graphicScene.getWind().setWindDirection((Double) newValue)));
        windForceSlider.valueProperty().addListener(((observable, oldValue, newValue) -> graphicScene.getWind().setWindForce((Double) newValue)));

        //Converter für die Textfelder
        converter = new NumberStringConverter(Locale.US, "######.##") {

            @Override
            public Number fromString(String string) throws NumberFormatException {
                try {
                    return Double.parseDouble(string);
                } catch (NumberFormatException ex) {
                    return 0;
                }
            }
        };


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
        //tf.setDisable(true);
    }

    private void validate(TextField tf) {
        ObservableList<String> styleClass = tf.getStyleClass();
        // Falls die Eingabe leer ist oder keine gültige Zahl, wird eine Fehlernachricht angezeigt
        if (tf.getText().trim().length() == 0 || !tf.getText().matches("[-+]?[0-9]*\\.?[0-9]*")) {
            if (!styleClass.contains("error")) {
                styleClass.add("error");
                infoLabel.setText("Insert a number, please!");
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
        textFieldXPosition.setDisable(false);
        textFieldYPosition.setDisable(false);
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
        // Die Eingabe-Masken für die unterschiedlichen Elemente
        if (newValue instanceof Spinner) {
            spinnerSlider.setDisable(false);
            spinnerSlider.setVisible(true);
            flexibilitySlider.setDisable(true);
            labelFlexSlider.setOpacity(0.6);
            labelSpinner.setVisible(true);
            textFieldWidth.setDisable(false);
            scaleFactor.setText("Width/Height");
            scaleFactor.setOpacity(1.0);
            textFieldWidth.setVisible(true);
            textFieldHeight.setDisable(false);
            textFieldHeight.setVisible(true);
            textFieldAccelerationX.setDisable(true);
            labelAcceleration.setOpacity(0.6);
            textFieldAccelerationY.setDisable(true);
            textFieldVelocityX.setDisable(true);
            textFieldVelocityY.setDisable(true);
            labelVelocity.setOpacity(0.6);
            textFieldRotate.setDisable(true);
            labelRotate.setOpacity(0.6);
            textFieldWeight.setDisable(true);
            labelWeight.setOpacity(0.6);
            textFieldRadius.setDisable(true);
            textFieldRadius.setVisible(false);

        } else if (newValue instanceof Ball) {
            spinnerSlider.setDisable(true);
            spinnerSlider.setVisible(false);
            labelSpinner.setVisible(false);
            flexibilitySlider.setDisable(false);
            labelFlexSlider.setOpacity(1.0);
            scaleFactor.setText("Radius");
            scaleFactor.setOpacity(1.0);
            textFieldWidth.setDisable(true);
            textFieldWidth.setVisible(false);
            textFieldHeight.setDisable(true);
            textFieldHeight.setVisible(false);
            textFieldRadius.setVisible(true);
            textFieldRadius.setDisable(false);
            labelAcceleration.setOpacity(1.0);
            textFieldAccelerationX.setDisable(false);
            textFieldAccelerationY.setDisable(false);
            labelVelocity.setOpacity(1.0);
            textFieldVelocityX.setDisable(false);
            textFieldVelocityY.setDisable(false);
            labelRotate.setOpacity(0.6);
            textFieldRotate.setDisable(true);
            labelWeight.setOpacity(1.0);
            textFieldWeight.setDisable(false);

        } else if (newValue instanceof Block &&  !(newValue instanceof Springboard) &&  !(newValue instanceof Springboard.Board)){
            spinnerSlider.setDisable(true);
            spinnerSlider.setVisible(false);
            labelSpinner.setVisible(false);
            flexibilitySlider.setDisable(true);
            labelFlexSlider.setOpacity(0.6);
            scaleFactor.setOpacity(1.0);
            scaleFactor.setText("Width/Height");
            textFieldWidth.setDisable(false);
            textFieldWidth.setVisible(true);
            textFieldHeight.setDisable(false);
            textFieldHeight.setVisible(true);
            textFieldRadius.setDisable(true);
            textFieldRadius.setVisible(false);
            labelAcceleration.setOpacity(0.6);
            textFieldAccelerationX.setDisable(true);
            textFieldAccelerationY.setDisable(true);
            labelVelocity.setOpacity(0.6);
            textFieldVelocityX.setDisable(true);
            textFieldVelocityY.setDisable(true);
            labelRotate.setOpacity(1.0);
            textFieldRotate.setDisable(false);
            labelWeight.setOpacity(0.6);
            textFieldWeight.setDisable(true);

        }else if (newValue instanceof Seesaw){
            spinnerSlider.setDisable(true);
            spinnerSlider.setVisible(false);
            labelSpinner.setVisible(false);
            flexibilitySlider.setDisable(true);
            labelFlexSlider.setOpacity(0.6);
            scaleFactor.setText("Width/Height");
            scaleFactor.setOpacity(1.0);
            textFieldWidth.setDisable(false);
            textFieldWidth.setVisible(true);
            textFieldHeight.setDisable(false);
            textFieldHeight.setVisible(true);
            textFieldRadius.setDisable(true);
            textFieldRadius.setVisible(false);
            labelAcceleration.setOpacity(0.6);
            textFieldAccelerationX.setDisable(true);
            textFieldAccelerationY.setDisable(true);
            labelVelocity.setOpacity(0.6);
            textFieldVelocityX.setDisable(true);
            textFieldVelocityY.setDisable(true);
            labelRotate.setOpacity(0.6);
            textFieldRotate.setDisable(true);
            labelWeight.setOpacity(0.6);
            textFieldWeight.setDisable(true);

        }else if (newValue instanceof Springboard || newValue instanceof Springboard.Board){
            spinnerSlider.setDisable(true);
            spinnerSlider.setVisible(false);
            labelSpinner.setVisible(false);
            flexibilitySlider.setDisable(true);
            labelFlexSlider.setOpacity(0.6);
            scaleFactor.setText("Width/Height");
            scaleFactor.setOpacity(0.6);
            textFieldWidth.setDisable(true);
            textFieldWidth.setVisible(true);
            textFieldHeight.setDisable(true);
            textFieldHeight.setVisible(true);
            textFieldRadius.setDisable(true);
            textFieldRadius.setVisible(false);
            labelAcceleration.setOpacity(0.6);
            textFieldAccelerationX.setDisable(true);
            textFieldAccelerationY.setDisable(true);
            labelVelocity.setOpacity(0.6);
            textFieldVelocityX.setDisable(true);
            textFieldVelocityY.setDisable(true);
            labelRotate.setOpacity(0.6);
            textFieldRotate.setDisable(true);
            labelWeight.setOpacity(0.6);
            textFieldWeight.setDisable(true);
        }

        if(!(newValue  instanceof Springboard))
        {
            colorPicker.setValue((Color) newValue.getElementView().getFill());
        }

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
        Bindings.unbindBidirectional(choiceBoxPattern.valueProperty(),oldValue.materialProperty());
        Bindings.unbindBidirectional(flexibilitySlider.valueProperty(), oldValue.frictionProperty());
        if (oldValue instanceof Spinner) {
            Bindings.unbindBidirectional(spinnerSlider.valueProperty(), ((Spinner) oldValue).rotationalSpeedProperty());
        }
        // die neuen Bindings setzen
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
        Bindings.bindBidirectional(choiceBoxPattern.valueProperty(), newValue.materialProperty());
        Bindings.bindBidirectional(flexibilitySlider.valueProperty(), newValue.frictionProperty());

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
            windEditor.setOpacity(1.0);
            windDirectionSlider.setDisable(false);
            windForceSlider.setDisable(false);
            graphicScene.getWind().setIsActivated(true);

        } else {
            windEditor.setOpacity(0.6);
            windForceSlider.setDisable(true);
            windDirectionSlider.setDisable(true);
            graphicScene.getWind().setIsActivated(false);
        }
    }

    public GridPane getEditor() {
        return editor;
    }
    public GridPane getHelpText() {
        return helpText;
    }
}
