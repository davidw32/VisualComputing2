package Controller;

import Model.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class OptionBarController {


    @FXML MenuBar optionBar;

    Button continue_btn = new Button("Continue");
    Button cancel_btn = new Button("Cancel");
    Label text = new Label("Do you really want to exit?");
    VBox Vbox_Modal = new VBox();
    HBox HBox_Modal = new HBox();
    Scene modalScene;
    Stage warningWindow;
    String eventType = "";

    private GraphicScene graphicScene;

    public void initialize()
    {
       // System.out.println("Init OptionBarController");
        text.setStyle("-fx-font-size: 20");
    }


    // Die Menu_Items von "Scene"
    @FXML
    public void openScene(ActionEvent actionEvent) {
        //System.out.println("StartScreen öffnen und Szene auswählen");
        eventType = "open";
        showWarning();
    }


    @FXML
    public void clearScene(ActionEvent actionEvent) {
        //System.out.println("Alle Elemente der Szene löschen");
        eventType = "clear";
        showWarning();
    }

    @FXML
    public void closeApplication(ActionEvent actionEvent) {
        System.out.println("Auf Wiedersehen!");
        eventType = "close";
        showWarning();
    }

    //Die Menu-Items von Object
    @FXML
    public void showBoundingBox(ActionEvent actionEvent) {
        System.out.println("BoundingBox des aktiven Elements anzeigen");
    }

    @FXML
    public void copyObject(ActionEvent actionEvent) {
        // clone() funktionert nicht mit den ganzen Listenern
        System.out.println("Aktives Element kopiert");
        GraphicsObject copy = null;
        GraphicsObject active = graphicScene.getActiveElement();

        //Prüfe welches neue Element erzeugt werden soll
        if (active instanceof Ball) {
            copy = new Ball(40, 40);
            ((Ball) copy).setRadius(((Ball) active).radius());
            copy.setIsMoving(true);

        }
        if (active instanceof Block) {
            copy = new Block(40, 40);
            ((Block) copy).setHeight(((Block) active).getHeight());
            ((Block) copy).setWidth(((Block) active).getWidth());
            copy.setIsMoving(false);
        }
        if(active instanceof Spinner){
            copy = new Spinner(40,40);
            ((Spinner) copy).setHeight(((Spinner) active).getHeight());
            ((Spinner) copy).setWidth(((Spinner) active).getWidth());
            ((Spinner) copy).setRotationalSpeed(((Spinner) active).getRotationalSpeed());
        }
        if(active instanceof Seesaw){
            copy = new Seesaw(40,40);
            ((Seesaw) copy).setHeight(((Seesaw) active).getHeight());
            ((Seesaw) copy).setWidth(((Seesaw) active).getWidth());
        }

        //kopiere die Werte
        copy.setWeight(active.getWeight());
        copy.setAngle(active.getAngle());
        copy.setXAcceleration(active.getXAcceleration());
        copy.setYAcceleration(active.getYAcceleration());
        copy.setXScale(active.getXScale());
        copy.setYScale(active.getYScale());
        copy.setFriction(active.getFriction());
        copy.getElementView().setFill(active.getElementView().getFill());

        //füge die Kopie in der Szene und in er Liste ein
        graphicScene.getGraphicSceneController().getGraphicPane().getChildren().add(copy.getElementView());
        if ( copy instanceof Ball){
            graphicScene.getGraphicSceneController().getGraphicPane().getChildren().addAll(((Ball) copy).getDirectionLine(), ((Ball) copy).getVelocityText());
        }

        if (copy instanceof Spinner){
            graphicScene.getGraphicSceneController().getGraphicPane().getChildren().addAll(((Spinner)copy).getCenter());
        }
        if (copy instanceof Seesaw){
            graphicScene.getGraphicSceneController().getGraphicPane().getChildren().addAll(((Seesaw)copy).getTriangle());
        }

        graphicScene.getGraphicSceneController().addListenersToObject(copy);
        graphicScene.addElement(copy);

    }

    @FXML
    public void deleteObject(ActionEvent actionEvent)
    {
        //System.out.println("lösche das aktive Element, setze den Platzhaler als neues Aktives Element");
        graphicScene.deleteActiveElement();
        System.out.println("Aktives Element gelöscht!");
    }

    //Die Menu_Items von Options
    @FXML
    public void takeSnapshot(ActionEvent actionEvent)
    {
        //System.out.println("Mache einen Snapshot von der Szene");
        try {
            System.out.println("Snapshot in img/Snapsshots gespeichert");
            String name = ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("uuuu_MM_dd_HH_mm_SS"));
            Image snapshot = graphicScene.getGraphicSceneController().getGraphicPane().snapshot(null, null);
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("src/img/Snapshots/" + name + "_Snapshot.png"));
        } catch (Exception e) {
            System.out.println("Failed to save image. " + e);
        }
    }

    @FXML
    public void openHelp(ActionEvent actionEvent)
    {
        //System.out.println("Zeige Hilfetext an");
        graphicScene.getElementEditorController().getEditor().setVisible(false);
        graphicScene.getElementEditorController().getHelpText().setVisible(true);

    }


    private void showWarning()
    {

        VBox Vbox_Modal = new VBox();
        Vbox_Modal.setAlignment(Pos.CENTER);
        Vbox_Modal.setSpacing(20);

        HBox HBox_Modal = new HBox();
        HBox_Modal.getChildren().addAll(continue_btn,cancel_btn);
        HBox_Modal.setSpacing(20);
        HBox_Modal.setAlignment(Pos.BASELINE_CENTER);

        Vbox_Modal.getChildren().addAll(text,HBox_Modal);

        Scene modalScene = new Scene(Vbox_Modal, 300, 150);
        Stage warningWindow = new Stage();

        warningWindow.setResizable(false);
        warningWindow.setTitle("Hinweis");
        warningWindow.setScene(modalScene);
        warningWindow.initModality(Modality.WINDOW_MODAL);
        //Sucht die momentane Stage ( im dessen Sinne die Parent Stage)
        warningWindow.initOwner(Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null));
        warningWindow.setX(750);
        warningWindow.setY(450);

        warningWindow.show();

        cancel_btn.setOnMouseClicked(e ->
        {
            warningWindow.hide();
        });

        continue_btn.setOnMouseClicked(e ->
        {
            if(eventType.equals("clear"))
            {
                graphicScene.clearScene();
                warningWindow.hide();
            }
            else if(eventType.equals("close"))
            {
                System.exit(0);
                warningWindow.hide();
            }
            else if(eventType.equals("open"))
            {
                graphicScene.clearScene();
                graphicScene.getStartController().showStartScreen();
                warningWindow.hide();

            }
        });

    }

    public MenuBar getOptionBar() {
        return optionBar;
    }

    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }
}

