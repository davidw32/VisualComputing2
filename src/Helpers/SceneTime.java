package Helpers;

import javafx.scene.control.Label;


/**
 * @author Patrick Pavlenko
 * Timer der Animation
 */
public class SceneTime
{
    private boolean playing = false;
    //timeInterval als Millisekunden
    private float timeInterval = 16.66f;
    private float milSecond = 0;
    private float second = 0;
    private float minute = 0;


    /**
     * zuruecksetzen bzw neustarten der Zeit
     * @param text
     */
    public void restartTime(Label text)
    {
        second = 0;
        minute = 0;
        milSecond = 0;
        text.setText("00:00:00");
    }

    /**
     * Konstante Update des Timers oben rechts
     * @param text
     */
    public void updateTime(Label text)
    {
        if(milSecond >= 1000)
        {
            milSecond -= 1000;
            second++;
        }
        if(second >= 60)
        {
            second = 0;
            minute++;
        }
        if(minute >= 999)
        {
            minute = 999;
        }
        milSecond += 16.66;

        if(minute == 0 && second < 10)
        {
            text.setText("0" + Integer.toString((int)minute) + ":" + "0" + Integer.toString((int)second) + ":" + Integer.toString((int)milSecond));
        }
        else if(second < 10)
        {
            text.setText(Integer.toString((int)minute) + ":" + "0" + Integer.toString((int)second) + ":" + Integer.toString((int)milSecond));
        }
        else if(minute == 0)
        {
            text.setText("0" + Integer.toString((int)minute) + ":" + Integer.toString((int)second) + ":" + Integer.toString((int)milSecond));
        }
        else
        {
            text.setText(Integer.toString((int)minute) + ":" + Integer.toString((int)second) + ":" + Integer.toString((int)milSecond));
        }


    }


    public float getMilSecond() { return milSecond; }
    public float getMinute() { return minute; }
    public float getSecond() { return second; }

    public boolean isPlaying() { return playing; }

    public void setPlaying(boolean playing) { this.playing = playing; }

}
