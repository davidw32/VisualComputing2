package Controller;

import Model.GraphicScene;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class OptionBarController {

    private GraphicScene graphicScene;

    @FXML
    Menu sceneMenu;

    //Szenenoptionen eines Objektes

    @FXML MenuItem sceneMenuDelete;
    @FXML MenuItem sceneMenuLoad;

    //Transformationsoptionen eines Objektes

    @FXML MenuItem createObject;
    @FXML MenuItem scaleObject;
    @FXML MenuItem rotateObject;
    @FXML MenuItem translateObject;
    @FXML MenuItem copyObject;
    @FXML MenuItem deleteObject;
    @FXML MenuItem createCustomObject;


    public void initialize()
    {

    }


    /**
     * Erstellen eines neues Objektes
     * Vorerst nur ein Circle moeglich.
     * @TODO: mehrere Erstellvarianten anbieten
     */
    @FXML public void createObject()
    {

    }

    /**
     * Hier waere eigentlich der OBJ Importer fuer Komplexe Objekte.
     * Dies soll nachher zu einen so geändert werden,dass hier andere potenzielle komplexe Objekte gebildet werden.
     * (Unsere Modellierungsobjekte bzw)
     */
    @FXML public void createCustomObject()
    {


    }

    /**
     * Translatieren des ausgewaehlten Objekte innerhalb der GraphicScene
     */
    @FXML public void translateObject()
    {

    }

    /**
     * Skalieren des ausgewaehlten Objekte innerhalb der GraphicScene
     */
    @FXML public void scaleObject()
    {

    }

    /**
     * Rotieren des ausgewaehlten Objekte innerhalb der GraphicScene
     */
    @FXML public void rotateObject()
    {

    }

    /**
     * Kopieren eines Objektes
     */
    @FXML public void copyObject()
    {

    }

    /**
     * Loeschen eines Objektes innerhalb der GraphicScene
     */
    @FXML public void deleteObject()
    {

    }

    /**
     * Oeffnet die Transformationseigenschaftenfenster des ToolWindow
     */
    @FXML public void openElementWindow()
    {
        graphicScene.getElementEditorController().getAdvancedOptionsWindow().setVisible(false);
        graphicScene.getElementEditorController().getHelpWindow().setVisible(false);
        graphicScene.getElementEditorController().getTransformGrid().setVisible(true);
        graphicScene.getElementEditorController().getTransformGrid().toFront();
        graphicScene.getElementEditorController().getAdvancedOptionsWindow().toBack();
        graphicScene.getElementEditorController().getHelpWindow().toBack();
    }

    /**
     * Oeffnet die erweiterte Einstellugnen im ToolWindow
     */
    @FXML public void openAdvancedOption()
    {
        graphicScene.getElementEditorController().getTransformGrid().toBack();
        graphicScene.getElementEditorController().getHelpWindow().toBack();
        graphicScene.getElementEditorController().getAdvancedOptionsWindow().toFront();
        graphicScene.getElementEditorController().getTransformGrid().setVisible(false);
        graphicScene.getElementEditorController().getHelpWindow().setVisible(false);
        graphicScene.getElementEditorController().getAdvancedOptionsWindow().setVisible(true);
    }

    /**
     * Oeffnet das Hilfefenster im ToolWindow
     */
    @FXML public void openHelp()
    {
        graphicScene.getElementEditorController().getTransformGrid().toBack();
        graphicScene.getElementEditorController().getHelpWindow().toFront();
        graphicScene.getElementEditorController().getAdvancedOptionsWindow().toBack();
        graphicScene.getElementEditorController().getTransformGrid().setVisible(false);
        graphicScene.getElementEditorController().getAdvancedOptionsWindow().setVisible(false);
        graphicScene.getElementEditorController().getHelpWindow().setVisible(true);
    }


    /**
     * Starten der Szene
     */
    @FXML public void sceneStart()
    {
        graphicScene.getPlayerController().startSimulation();
    }


    /**
     * Neustarten einer Szene bzw Animation
     */
    @FXML public void sceneRestart()
    {
        graphicScene.getPlayerController().resetSimulation();
    }

    /**
     * pausieren der Szene
     */
    @FXML public void scenePause()
    {
        graphicScene.getPlayerController().pauseSimulation();
    }


    /**
     * Verlassen des Programmes.
     * @TODO: Speicherung der GraphicScene ( Speicherung der GraphicOBJs)
     */
    @FXML public void goBack()
    {

    }

    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }
}
