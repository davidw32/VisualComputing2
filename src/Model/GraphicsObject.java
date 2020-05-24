package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Shape;

/**
 * Parentklasse aller Elemente in der Szene
 *
 */
public abstract class GraphicsObject {


    protected Shape elementView;

    private double startX, startY, startVelX, startVelY, startAccX, startAccY;

    private final DoubleProperty xPosition, yPosition,
                                xAcceleration, yAcceleration,
                                xVelocity, yVelocity,
                                xScale, yScale,
                                friction, weight, radius;

    private boolean isMoving;


    public GraphicsObject(double initialX, double initialY) {
        this.xPosition = new SimpleDoubleProperty(this, "xPosition", initialX);
        this.yPosition = new SimpleDoubleProperty(this, "yPosition", initialY);
        this.xAcceleration = new SimpleDoubleProperty(this,"xAcceleration",0);
        this.yAcceleration = new SimpleDoubleProperty(this,"yAcceleration", 0);
        this.xVelocity = new SimpleDoubleProperty(this, "xVelocity",0);
        this.yVelocity = new SimpleDoubleProperty(this, "yVelocity",0);
        this.xScale = new SimpleDoubleProperty(this, "xScale", 0);
        this.yScale = new SimpleDoubleProperty(this, "yScale", 0);
        this.weight = new SimpleDoubleProperty(this,"weight",0);
        this.friction = new SimpleDoubleProperty(this, "friction", 0);
        this.radius = new SimpleDoubleProperty(this, "radius", 30);

    }
    // statt der Werte diese als Property setzen, dann lassen sie sich mit der Gui verknüpfen
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

    public DoubleProperty xAccelerationProperty() { return xAcceleration; }
    public DoubleProperty yAccelerationProperty() { return yAcceleration; }

    public final void setxAcceleration(double _xAcceleration){ this.xAcceleration.set(_xAcceleration);}
    public final void setyAcceleration(double _yAcceleration){ this.xAcceleration.set(_yAcceleration);}

    public final double getxAcceleration(){return this.xAcceleration.get();}
    public final double getyAcceleration(){return this.yAcceleration.get();}

    public DoubleProperty xVelocityProperty() { return xVelocity; }
    public DoubleProperty yVelocityProperty() { return yVelocity; }

    public final void setxVelocity(double _xVelocity){ this.xVelocity.set(_xVelocity);}
    public final void setyVelocity(double _yVelocity){ this.xVelocity.set(_yVelocity);}

    public final double getxVelocity(){return this.xVelocity.get();}
    public final double getyVelocity(){return this.yVelocity.get();}

    public DoubleProperty xScaleProperty() { return xScale; }
    public DoubleProperty yScaleProperty() { return yScale; }

    public final void setxScale(double _xScale){ this.xScale.set(_xScale);}
    public final void setyScale(double _yScale){ this.xScale.set(_yScale);}

    public final double getxScale(){return this.xScale.get();}
    public final double getyScale(){return this.yScale.get();}

    public DoubleProperty radiusProperty() { return radius; }
    public final void setRadius(double _radius) { this.radius.set(_radius); }
    public final double getRadius() { return this.radius.get(); }

    public DoubleProperty weightProperty() { return weight; }
    public final void setWeigth(double _weight){ this.weight.set(_weight); }
    public final double getWeigth() { return this.weight.get(); }


    public Shape getElementView() {
        return elementView;
    }

    public void setElementView(Shape elementView) {
        this.elementView = elementView;
    }

    // hier werden die Werte für das Reset festgelegt
    protected void setStartValues(){
        startX = this.getXPosition();
        startY = this.getYPosition();
        startAccX = this.getxAcceleration();
        startAccY =  this.getyAcceleration();
        startVelX = this.getxVelocity();
        startVelY = this.getyVelocity();

    }

    // das Element wird auf die Startwerte zurückgesetzt
    public void resetElement(){
        setXPosition(startX);
        setYPosition(startY);
        setxAcceleration(startAccX);
        setyAcceleration(startAccY);
        setxVelocity(startVelX);
        setyVelocity(startVelY);
    }

    public void moveElement(){}

    @Override
    public String toString(){
        return "xProperty: "+getXPosition() +" yProperty: "+getYPosition();
    }

}
