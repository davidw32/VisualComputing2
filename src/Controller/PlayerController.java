package Controller;

import Model.GraphicScene;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Duration;

public class PlayerController {

    @FXML
    private StartController startController;

    private GraphicSceneController graphicSceneController;
    private GraphicScene graphicScene;

    private Timeline timeline;
    private boolean isRunning = false;

    @FXML
    public void startSimulation(){
        System.out.println("Animation gestartet");
        timeline = new Timeline( new KeyFrame(
                Duration.millis(16), event -> {
                    graphicScene.updateScene();
        }));
        isRunning=true;
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }
    @FXML
    public void stopSimulation(){
        if(isRunning){
            timeline.stop();
            isRunning = false;
        }
    }
    @FXML
    public void pauseSimulation(){
        if(isRunning){
            timeline.pause();
            isRunning = false;
        }
    }
    @FXML
    public void resetSimulation(){
        if(isRunning){
            timeline.stop();
            isRunning = false;
        }
        graphicScene.resetAllElements();
    }

    public void setOnTimeLapse(ActionEvent actionEvent) {
        System.out.println("Zeitraffer-Modus");
    }

    public void setOnSlowMotion(ActionEvent actionEvent) {
        System.out.println("SlowMotion-Modus");
    }


    public void setStartController(StartController control){
        this.startController = control;
    }

    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }



}
