package Model;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import static javafx.scene.paint.Color.BLUE;

/**
 * Parentklasse aller Elemente in der Szene
 *
 */
public abstract class GraphicsObject {


    protected double weight = 1, frictionCoefficient = 0;
    protected double velocityX = 0, velocityY = 0, accelerationX = 0, accelerationY = 0;

    protected double startX = 0, startY = 0, startVelX = 0, startVelY = 0, startAccX = 0, startAccY = 0;

    protected final DoubleProperty xPosition, yPosition;

    // zurueckgelegte Strecken
    protected double sX = 0;
    protected double sY = 0;

    protected double angle = 0;

    protected boolean isMoving;

    protected Shape elementView;

    protected Color defaultMaterial = BLUE;

    public GraphicsObject(double initialX, double initialY) {
        this.xPosition = new SimpleDoubleProperty(this, "xPosition",initialX);
        this.yPosition = new SimpleDoubleProperty(this,"yPosition", initialY);

    }

    public DoubleProperty xPositionProperty(){return xPosition;}
    public DoubleProperty yPositionProperty(){return yPosition;}



    @Override
    public String toString(){
        return "xProperty: "+getXPosition() +" yProperty: "+getYPosition();
    }


    public void saveElement()
    {

    }

    abstract protected void moveElement();

    public void resetElement(){

    }

    public void activateSelectionColor()
    {
        elementView.setFill(Color.YELLOW);
    }
    public void deactivateSelectionColor()
    {
        elementView.setFill(defaultMaterial);
    }


    abstract protected void moveX();
    abstract protected void moveY();


    public final double getXPosition(){ return xPosition.get(); }
    public final double getYPosition(){ return yPosition.get(); }
    public Shape getShape() { return elementView; }
    public double getFrictionCoefficient() { return frictionCoefficient; }
    public double getAccelerationX() { return accelerationX; }
    public double getAccelerationY() { return accelerationY; }
    public double getsX() { return sX; }
    public double getsY() { return sY; }
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    public double getWeight() { return weight; }
    public double getAngle() { return angle; }

    public void setWeight(double weight) { this.weight = weight; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }
    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }
    public void setAccelerationY(double accelerationY) { this.accelerationY = accelerationY; }
    public void setAccelerationX(double accelerationX) { this.accelerationX = accelerationX; }
    public void setsY(double sY) { this.sY = sY; }
    public void setsX(double sX) { this.sX = sX; }
    public final void setXPosition(double _xPosition){ this.xPosition.set(_xPosition); }
    public final void setYPosition(double _yPosition){ this.yPosition.set(_yPosition); }

}
