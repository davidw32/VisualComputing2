package Controller;

import Model.Ball;
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
    public void loadScene(){
        System.out.println("Szene laden");
    }
    @FXML
    MenuBar optionBar;

    private GraphicScene graphicScene;

    public void initialize(){
        System.out.println("init OptionBar");
    }


    @FXML
    public void closeApplication(ActionEvent actionEvent) {
        System.out.println("Auf Wiedersehen!");
        System.exit(0);
    }

    @FXML
    public void openScene(ActionEvent actionEvent) {
        //System.out.println("StartScreen öffnen und Szene auswählen");
        graphicScene.getStartController().showStartScreen();
    }
    @FXML
    public void saveScene(ActionEvent actionEvent) {
        System.out.println("Szene speichern");
    }
    @FXML
    public void clearScene(ActionEvent actionEvent) {
        System.out.println("Alle Elemente der Szene löschen");
        graphicScene.clearScene();

    }
    @FXML
    public void showBoundingBox(ActionEvent actionEvent) {
        System.out.println("BoundingBox des aktiven Elements anzeigen");
    }

    @FXML
    public void copyObject(ActionEvent actionEvent) {
        System.out.println("Kopiere das aktive Element und füge es in der Liste ein, setze seine Position auf linke obere Ecke, setze es als aktives Element");
        GraphicsObject clone = graphicScene.getActiveElement().clone();

        graphicScene.getActiveElement().setIsSelected(false);

        if (graphicScene.getActiveElement() instanceof Ball){
            Ball active = (Ball)graphicScene.getActiveElement();
            Ball copy = new Ball(active.getXPosition(), active.getYPosition());
            copy.setRadius(active.radius());
            copy.setXVelocity(active.getXVelocity());
            copy.setYVelocity(active.getYVelocity());
            copy.setWeight(active.getWeight());
            copy.setAngle(active.getAngle());
            copy.setXAcceleration(active.getXAcceleration());
            copy.setYAcceleration(active.getYAcceleration());
            copy.setXScale(active.getXScale());
            copy.setYScale(active.getYScale());
            copy.setFriction(active.getFriction());
            //copy.setElementView(active.getElementView());
            copy.setIsMoving(true);
            copy.setXPosition(40);
            copy.setYPosition(40);
            copy.setIsSelected(true);

            active.setIsSelected(false);

            graphicScene.getGraphicSceneController().getGraphicPane().getChildren().add((Circle)copy.getElementView());
            graphicScene.setActiveElement(copy);
            graphicScene.addElement(copy);

        }

/*
        graphicScene.setActiveElement(graphicScene.getPlaceholder());
        clone.setIsSelected(true);
        clone.setXPosition(40);
        clone.setYPosition(40);
        graphicScene.addElement(clone);
        graphicScene.setActiveElement(clone);


 */


    }

    @FXML
    public void deleteObject(ActionEvent actionEvent) {
        //System.out.println("lösche das aktive Element, setze den Platzhaler als neues Aktives Element");
        graphicScene.deleteActiveElement();
        System.out.println("Aktives Element gelöscht!");
    }

    @FXML
    public void takeSnapshot(ActionEvent actionEvent) {
        //System.out.println("Mache einen Snapshot von der Szene");
        try {
            System.out.println("Snapshot in out/Snapsshots gespeichert");
            String name = ZonedDateTime.now( ZoneId.systemDefault() ).format( DateTimeFormatter.ofPattern( "uuuu_MM_dd_HH_mm_SS" ) );
            Image snapshot = graphicScene.getGraphicSceneController().getGraphicPane().snapshot(null,null);
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("out/Snapshots/"+name+"_Snapshot.png"));
        } catch(Exception e){
            System.out.println("Failed to save image. "+e);
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
