package Controller;

import Model.GraphicScene;
import Model.GraphicsObject;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class PlayerController {

    @FXML
    private StartController startController;
    @FXML private Button play_pause;

    private GraphicSceneController graphicSceneController;
    private GraphicScene graphicScene;

    private Timeline timeline;
    private boolean isRunning = false;
    private boolean isPaused = true;

    @FXML
    public void startSimulation(){
        System.out.println("Animation gestartet");
        if(isPaused) {
            play_pause.setText("Pause");
            if (!isRunning) {
                for (GraphicsObject graphicsObject : graphicScene.getElementsInScene()) {
                    graphicsObject.setStartValues();
                }
            }

            timeline = new Timeline(new KeyFrame(
                    Duration.millis(16), event -> {

                graphicScene.updateScene();
            }));

            timeline.setCycleCount(Animation.INDEFINITE);
            isRunning = true;
            isPaused = false;

            System.out.println(isRunning);
            timeline.play();
        } else {
            if (isRunning){
                timeline.pause();
                isPaused = true;
                play_pause.setText("Play");
            }
        }
    }
    @FXML
    public void stopSimulation(){

        if(isRunning){
            timeline.stop();
            isRunning = false;
            isPaused = true;
            play_pause.setText("Play");
            graphicScene.resetAllElements();
        }

    }

    @FXML
    public void resetSimulation(){
        if(isRunning){

            graphicScene.resetAllElements();

        }

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
