package Controller;

import Model.GraphicScene;
import Model.SceneTime;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import java.text.DecimalFormat;

public class PlayerController {

    private GraphicScene graphicScene;
    private boolean isRunning = false;

    //Icon zur Pausierung des Timers
    @FXML private HBox pauseIcon;
    //Icon zum Starten des Timers
    @FXML private Polygon playIcon;
    @FXML private Label clock;
    @FXML private Label timeFactorText;

    @FXML
    public void startSimulation()
    {
        if(!isRunning)
        {
            graphicScene.setTimeline(new Timeline(
                    new KeyFrame( Duration.millis(16.66 * graphicScene.getTimer().getTimeFactor()), event ->
                    {
                        graphicScene.updateScene();
                        graphicScene.getTimer().updateTime(clock);
                    })
            ));
            isRunning=true;
            graphicScene.getTimeline().setCycleCount(Animation.INDEFINITE);
            graphicScene.getTimeline().play();

            // Objekte werden kopiert zum zurücksetzen der Objekte bei neustart
            if(graphicScene.getTimer().getMilSecond() == 0 && graphicScene.getTimer().getSecond() == 0 && graphicScene.getTimer().getMinute() == 0)
            {
           //     Abspeicherung für Neustart...
            //    model.getGraphicSceneController().saveOBJS();
            }
            isRunning = true;
            pauseIcon.setVisible(true);
            playIcon.setVisible(false);
            pauseIcon.toFront();
            playIcon.toBack();
            graphicScene.getTimeline().play();
        }
        else
        {
            stopSimulation();
        }
    }




    @FXML
    public void stopSimulation()
    {
        if(isRunning)
        {
            isRunning = false;
            pauseIcon.setVisible(false);
            playIcon.setVisible(true);
            pauseIcon.toBack();
            playIcon.toFront();
            graphicScene.getTimeline().stop();
        }
    }


    @FXML
    public void pauseSimulation()
    {
        if(isRunning)
        {
            isRunning = false;
            pauseIcon.setVisible(false);
            playIcon.setVisible(true);
            pauseIcon.toBack();
            playIcon.toFront();
            graphicScene.getTimeline().stop();
        }
    }


    @FXML
    public void resetSimulation()
    {
        if(isRunning)
        {
            graphicScene.getTimeline().stop();
            isRunning = false;

            pauseSimulation();
            graphicScene.getTimer().restartTime(clock);
            startSimulation();
        }

        graphicScene.resetAllElements();
    }


    @FXML
    public void decreaseAnimationSpeed()
    {
        SceneTime timer = graphicScene.getTimer();
        if(!isRunning)
        {
            if(timer.getTimeFactor() >= 4)
            {
                timer.setTimeFactor(4);
                DecimalFormat df = new DecimalFormat("#.#");
                timeFactorText.setText( df.format(1/timer.getTimeFactor())  + "x" );
                return;
            }
            DecimalFormat df = new DecimalFormat("#.#");
            timer.setTimeFactor(timer.getTimeFactor()+0.4);
   //         timer.getStarterController().swapKeyframe(timer.getTimeFactor());
            timeFactorText.setText( df.format(1/timer.getTimeFactor())  + "x" );
        }
        System.out.println(timer.getTimeFactor());
    }

    @FXML
    public void increaseAnimationSpeed()
    {
        SceneTime timer = graphicScene.getTimer();
        if(!isRunning)
        {
            if(timer.getTimeFactor() <= 0.4)
            {
                timer.setTimeFactor(0.4);
                DecimalFormat df = new DecimalFormat("#.#");
                timeFactorText.setText( df.format(1/timer.getTimeFactor())  + "x" );
                return;
            }
            DecimalFormat df = new DecimalFormat("#.#");
            graphicScene.getTimer().setTimeFactor(timer.getTimeFactor()-0.4);
    //        model.getStarterController().swapKeyframe(timer.getTimeFactor());
            timeFactorText.setText(df.format(1/timer.getTimeFactor())  + "x" );
        }
        System.out.println(timer.getTimeFactor());
    }

    public void setGraphicScene(GraphicScene graphicScene) { this.graphicScene = graphicScene; }



}
