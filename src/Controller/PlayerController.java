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
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

/**
 * @author Patrick Pavlenko,Pamela Trowe
 * Zustaendig fuer Bereich wo die Animation gesteuert wird (starten,pausieren..etc)
 */
public class PlayerController {

    @FXML private Button playPauseButton;
    @FXML private Label timeFactor;
    @FXML private HBox pauseIcon;
    @FXML private Polygon playIcon;
    @FXML private Label clock;

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
            pauseIcon.setVisible(true);
            playIcon.setVisible(false);
            if (!isRunning)
            {
                for (GraphicsObject graphicsObject : graphicScene.getElementsInScene())
                {
                    graphicsObject.setStartValues();
                }
            }

            timeline = new Timeline(new KeyFrame(
                    Duration.millis(16 * graphicScene.getTimeFactor()), event -> {
                            graphicScene.getTimer().updateTime(clock);
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
                pauseIcon.setVisible(false);
                playIcon.setVisible(true);
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

            pauseIcon.setVisible(false);
            playIcon.setVisible(true);
            graphicScene.getTimer().restartTime(clock);
            graphicScene.resetAllElements();
        }

    }

    @FXML
    public void resetSimulation(){
        if(isRunning){
            graphicScene.getTimer().restartTime(clock);
            graphicScene.resetAllElements();

        }

    }

    /**
     * Zeit wird hier verschnellert
     * @param actionEvent
     */
    public void setOnTimeLapse(ActionEvent actionEvent)
    {
        //Falls die Animationssgeschwindigkeit bei 10% ist
        if(graphicScene.getTimeFactor() == 10)
        {
            // Dann soll die Animation zuerst ihre normale Geschwindigkeit (100%) erreichen
            graphicScene.setTimeFactor(1);
        }
        else
        {
            // ansonsten soll die Geschwindigkeit auf 1000% eingestellt werden der Animation
            graphicScene.setTimeFactor(0.1);
        }
        timeFactor.setText( (1 / graphicScene.getTimeFactor() ) + "x" );
    }

    /**
     * Zeit wird hier verlangsamt
     * Geschwindigkeit wird runtergesetzt auf 10% (
     * @param actionEvent
     */
    public void setOnSlowMotion(ActionEvent actionEvent)
    {
        //Falls die Animationsgeschwindigkeit bei 1000% ist
        if(graphicScene.getTimeFactor() == 0.1)
        {
            // Dann soll die Animation zuerst ihre normale Geschwindigkeit (100%) erreichen
            graphicScene.setTimeFactor(1);
        }
        else
        {
            // ansonsten soll die Geschwindigkeit auf 10% eingestellt werden der Animation
            graphicScene.setTimeFactor(10);
        }

        timeFactor.setText( (1 / graphicScene.getTimeFactor() )  + "x" );
    }

    public void setGraphicScene(GraphicScene graphicScene)
    {
        this.graphicScene = graphicScene;
    }

    public HBox getPlayer(){return player;}

}
