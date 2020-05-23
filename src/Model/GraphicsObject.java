package Model;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.shape.Shape;

/**
 * Parentklasse aller Elemente in der Szene
 *
 */
public abstract class GraphicsObject {


    private double weight, frictionCoefficient;
    private double velocityX, velocityY, accelerationX, accelerationY;

    private double startX, startY, startVelX, startVelY, startAccX, startAccY;

    private final DoubleProperty xPosition, yPosition;

    private boolean isMoving;

    private Shape elementView;

    public GraphicsObject(double initialX, double initialY) {
        this.xPosition = new SimpleDoubleProperty(this, "xPosition",initialX);
        this.yPosition = new SimpleDoubleProperty(this,"yPosition", initialY);


    }

    public DoubleProperty xPositionProperty(){return xPosition;}
    public DoubleProperty yPositionProperty(){return yPosition;}

    public final void setXPosition(double _xPosition){
        this.xPosition.set(_xPosition);
    }
    public final void setYPosition(double _yPosition){
        this.yPosition.set(_yPosition);
    }
    public final double getXPosition(){
            return xPosition.get();
    }
    public final double getYPosition(){
            return yPosition.get();
    }

    @Override
    public String toString(){
        return "xProperty: "+getXPosition() +" yProperty: "+getYPosition();
    }
    public void initElement(){
    }

    public void moveElement(){}

    public void resetElement(){

    }


}
