package Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Wind {



    private double windForce, windDirection;
    private boolean isActivated;

    public Wind(){

    }


    public void setWindForce(double windForce) {
        this.windForce = windForce;
    }

    public double getWindForce() {
        return windForce;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection= windDirection;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public boolean getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }
}
