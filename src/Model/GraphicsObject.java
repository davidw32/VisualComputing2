package Model;

import javafx.beans.property.*;
import javafx.scene.shape.Shape;

/**
 * Parentklasse aller Elemente in der Szene
 *
 */
public abstract class GraphicsObject {


    protected Shape elementView;

    // Werte für das Reset
    private double startX, startY, startVelX, startVelY, startAccX, startAccY;

    private final DoubleProperty xPosition, yPosition,
                                 xAcceleration, yAcceleration,
                                 xVelocity, yVelocity,
                                 xScale, yScale,
                                 friction, weight, angle;

    protected boolean isMoving;
    private final BooleanProperty isSelected;


    public GraphicsObject(double initialX, double initialY) {

        this.xPosition = new SimpleDoubleProperty(this, "xPosition", initialX);
        this.yPosition = new SimpleDoubleProperty(this, "yPosition", initialY);
        this.xAcceleration = new SimpleDoubleProperty(this,"xAcceleration",0);
        this.yAcceleration = new SimpleDoubleProperty(this,"yAcceleration", 0);
        this.xVelocity = new SimpleDoubleProperty(this, "xVelocity",0);
        this.yVelocity = new SimpleDoubleProperty(this, "yVelocity",0);
        this.xScale = new SimpleDoubleProperty(this, "xScale", 1);
        this.yScale = new SimpleDoubleProperty(this, "yScale", 1);
        this.weight = new SimpleDoubleProperty(this,"weight",0);
        this.friction = new SimpleDoubleProperty(this, "friction", 0);

        this.angle = new SimpleDoubleProperty(this,"angle", 0);
        this.isSelected = new SimpleBooleanProperty(this, "isSelected", true);
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

    public final double xPosition(){
            return xPosition.get();
    }
    public final double yPosition(){
            return yPosition.get();
    }

    public DoubleProperty xAccelerationProperty() { return xAcceleration; }
    public DoubleProperty yAccelerationProperty() { return yAcceleration; }

    public final void setXAcceleration(double _xAcceleration){ this.xAcceleration.set(_xAcceleration);}
    public final void setYAcceleration(double _yAcceleration){ this.xAcceleration.set(_yAcceleration);}

    public final double xAcceleration(){return this.xAcceleration.get();}
    public final double yAcceleration(){return this.yAcceleration.get();}

    public DoubleProperty xVelocityProperty() { return xVelocity; }
    public DoubleProperty yVelocityProperty() { return yVelocity; }

    public final void setXVelocity(double _xVelocity){ this.xVelocity.set(_xVelocity);}
    public final void setYVelocity(double _yVelocity){ this.xVelocity.set(_yVelocity);}

    public final double xVelocity(){return this.xVelocity.get();}
    public final double yVelocity(){return this.yVelocity.get();}

    public DoubleProperty xScaleProperty() { return xScale; }
    public DoubleProperty yScaleProperty() { return yScale; }

    public final void setXScale(double _xScale){ this.xScale.set(_xScale);}
    public final void setYScale(double _yScale){ this.xScale.set(_yScale);}

    public final double xScale(){return this.xScale.get();}
    public final double yScale(){return this.yScale.get();}



    public DoubleProperty weightProperty() { return weight; }
    public final void setWeight(double _weight){ this.weight.set(_weight); }
    public final double getWeight() { return this.weight.get(); }

    public DoubleProperty angleProperty() { return angle;}
    public final void setAngle(double _angle){this.angle.set(_angle);}
    public final double angle(){ return this.angle.get();}

    public BooleanProperty isSelectedProperty(){ return isSelected;}
    public final void setIsSelected(Boolean _isSelected){ this.isSelected.set(_isSelected); }
    public final boolean getIsSelected(){ return isSelected.get(); }


    public Shape getElementView() {
        return elementView;
    }
    public void setElementView(Shape elementView) {
        this. elementView = elementView;
    }

    public void setIsMoving(boolean _isMoving){ this.isMoving = _isMoving; }
    public boolean isMoving() { return isMoving; }

    // hier werden die Werte für das Reset festgelegt
    public void setStartValues(){

        startX = this.xPosition();
        startY = this.yPosition();
        startAccX = this.xAcceleration();
        startAccY =  this.yAcceleration();
        startVelX = this.xVelocity();
        startVelY = this.yVelocity();
        System.out.println("Starx: "+startX +" startY: "+startY);

    }

    // das Element wird auf die Startwerte zurückgesetzt
    public void resetElement(){
        setXPosition(startX);
        setYPosition(startY);
        setXAcceleration(startAccX);
        setYAcceleration(startAccY);
        setXVelocity(startVelX);
        setYVelocity(startVelY);
    }

    public void moveElement(){}

    @Override
    public String toString(){
        return "xProperty: "+ xPosition() +" yProperty: "+ yPosition();
    }

}
