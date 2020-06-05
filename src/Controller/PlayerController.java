package Controller;

import Model.GraphicScene;
import Model.GraphicsObject;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class PlayerController {

    @FXML private Button playPauseButton;
    @FXML  private HBox player;
    private GraphicScene graphicScene;

    private Timeline timeline;
    private boolean isRunning = false;
    private boolean isPaused = true;

    public void initialize(){
        System.out.println("init player");
    }

    @FXML
    public void startSimulation(){
        //starte die Simulation
        if(isPaused) {
            playPauseButton.setText("Pause");
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
        } else { //mache Pause
            if (isRunning){
                timeline.pause();
                isPaused = true;
                playPauseButton.setText("Play");
            }
        }
    }


    @FXML
    public void stopSimulation(){
        // stop und reset
        if(isRunning){
            timeline.stop();
            isRunning = false;
            isPaused = true;
            playPauseButton.setText("Play");
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

    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }

    public HBox getPlayer(){return player;}

}
