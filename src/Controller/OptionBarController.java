package Controller;

import Model.Ball;
import Model.Block;
import Model.GraphicScene;
import Model.GraphicsObject;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;

import javax.imageio.ImageIO;
import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class OptionBarController {


    @FXML
    MenuBar optionBar;

    private GraphicScene graphicScene;

    public void initialize() {
        System.out.println("Init OptionBarController");
    }


    // Die Menu_Items von "Scene"
    @FXML
    public void openScene(ActionEvent actionEvent) {
        //System.out.println("StartScreen öffnen und Szene auswählen");
        graphicScene.getStartController().showStartScreen();
    }


    @FXML
    public void clearScene(ActionEvent actionEvent) {
        //System.out.println("Alle Elemente der Szene löschen");
        graphicScene.clearScene();

    }

    @FXML
    public void closeApplication(ActionEvent actionEvent) {
        System.out.println("Auf Wiedersehen!");
        System.exit(0);
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
        graphicScene.getGraphicSceneController().addListenersToObject(copy);
        graphicScene.addElement(copy);

    }

    @FXML
    public void deleteObject(ActionEvent actionEvent) {
        //System.out.println("lösche das aktive Element, setze den Platzhaler als neues Aktives Element");
        graphicScene.deleteActiveElement();
        System.out.println("Aktives Element gelöscht!");
    }

    //Die Menu_Items von Options
    @FXML
    public void takeSnapshot(ActionEvent actionEvent) {
        //System.out.println("Mache einen Snapshot von der Szene");
        try {
            System.out.println("Snapshot in out/Snapsshots gespeichert");
            String name = ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("uuuu_MM_dd_HH_mm_SS"));
            Image snapshot = graphicScene.getGraphicSceneController().getGraphicPane().snapshot(null, null);
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("src/img/Snapshots/" + name + "_Snapshot.png"));
        } catch (Exception e) {
            System.out.println("Failed to save image. " + e);
        }
    }

    @FXML
    public void openHelp(ActionEvent actionEvent) {
        System.out.println("Zeige Hilfetext an");
        graphicScene.getElementEditorController().getEditor().setVisible(false);
        graphicScene.getElementEditorController().getHelpText().setVisible(true);

    }

    public MenuBar getOptionBar() {
        return optionBar;
    }

    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }
}
