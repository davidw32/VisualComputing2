package Model;

import javafx.beans.property.*;
import javafx.scene.effect.ImageInput;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import javax.swing.event.ChangeListener;

/**
 * Parentklasse aller Elemente in der Szene
 *
 */
public abstract class GraphicsObject {


    protected Shape elementView;
    protected boolean isMoving;
    protected double time = 0.01666;
    protected StringProperty material;

    private Lighting woodSurface, metalSurface, rubberSurface, defaultSurface, defaultBallSurface;

    protected ImageInput woodImage, metalImage, rubberImage;




    // Werte für das Reset
    protected double  startX, startY, startVelX, startVelY, startAccX, startAccY, startAngle, startScaleX, startScaleY, startWeight, startWidth, startHeight;


    protected final DoubleProperty xPosition, yPosition,
                                 xAcceleration, yAcceleration,
                                 xVelocity, yVelocity,
                                 xScale, yScale,
                                 width, height, radius,
                                 friction, weight, angle;

    protected final BooleanProperty isSelected;




    public GraphicsObject(double initialX, double initialY) {
        //die Properties des GraphicsObject werden erzeugt
        this.xPosition = new SimpleDoubleProperty(this, "xPosition", initialX);
        this.yPosition = new SimpleDoubleProperty(this, "yPosition", initialY);
        this.xAcceleration = new SimpleDoubleProperty(this,"xAcceleration",0.0);
        this.yAcceleration = new SimpleDoubleProperty(this,"yAcceleration", 0.0);
        this.xVelocity = new SimpleDoubleProperty(this, "xVelocity",0.0);
        this.yVelocity = new SimpleDoubleProperty(this, "yVelocity",0.0);
        this.xScale = new SimpleDoubleProperty(this, "xScale", 1.0);
        this.yScale = new SimpleDoubleProperty(this, "yScale", 1.0);
        this.weight = new SimpleDoubleProperty(this,"weight",0.0);
        this.friction = new SimpleDoubleProperty(this, "friction", 0.6);
        this.angle = new SimpleDoubleProperty(this,"angle", 0.0);
        this.isSelected = new SimpleBooleanProperty(this, "isSelected", true);
        this.width = new SimpleDoubleProperty(this, "width", 0.0);
        this.height = new SimpleDoubleProperty(this, "height", 0.0);
        this.radius = new SimpleDoubleProperty(this, "radius", 0.0);
        this.material = new SimpleStringProperty(this,"material","Rubber");

        woodImage = new ImageInput(new Image("img/Patterns/wood.png",400,400,false,true));
        metalImage = new ImageInput(new Image("img/Patterns/metal.png",360,200,false,true));
        rubberImage = new ImageInput(new Image("img/Patterns/rubber.png",350,350,false, true));
        woodImage.setX(getXPosition()-50);
        metalImage.setX(getXPosition()-100);
        rubberImage.setX(getXPosition()-100);
        woodImage.setY(getYPosition()-50);
        metalImage.setY(getYPosition()-50);
        rubberImage.setY(getYPosition()-100);
        xPositionProperty().addListener(observable -> {
            woodImage.setX(getXPosition() - getWidth());
            metalImage.setX(getXPosition() - getWidth());
            rubberImage.setX(getXPosition() - getWidth());
        });
        yPositionProperty().addListener(observable -> {
            woodImage.setY(getYPosition() - getHeight());
            metalImage.setY(getYPosition() - getHeight());
            rubberImage.setY(getYPosition() - getHeight());
        });
        createSurfaces();

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

    public final void setXAcceleration(double _xAcceleration){ this.xAcceleration.set(_xAcceleration);}
    public final void setYAcceleration(double _yAcceleration){ this.yAcceleration.set(_yAcceleration);}

    public final double getXAcceleration(){return this.xAcceleration.get();}
    public final double getYAcceleration(){return this.yAcceleration.get();}

    public DoubleProperty xVelocityProperty() { return xVelocity; }
    public DoubleProperty yVelocityProperty() { return yVelocity; }

    public final void setXVelocity(double _xVelocity){ this.xVelocity.set(_xVelocity);}
    public final void setYVelocity(double _yVelocity){ this.yVelocity.set(_yVelocity);}

    public final double getXVelocity(){return this.xVelocity.get();}
    public final double getYVelocity(){return this.yVelocity.get();}

    public DoubleProperty xScaleProperty() { return xScale; }
    public DoubleProperty yScaleProperty() { return yScale; }

    public final void setXScale(double _xScale){ this.xScale.set(1);}
    public final void setYScale(double _yScale){ this.xScale.set(1);}

    public final double getXScale(){return 1;}
    public final double getYScale(){return 1;}

    public DoubleProperty widthProperty(){
        return this.width;
    }
    public double getWidth(){ return width.get(); }
    public void setWidth(double _width){ this.width.set(_width);}

    public DoubleProperty heightProperty() {
        return this.height;
    }
    public double getHeight(){return height.get();}
    public void setHeight(double _height){ this.height.set(_height);}

    public DoubleProperty radiusProperty(){ return radius;}
    public double getRadius(){ return this.radius.get();}
    public  void setRadius(double _radius){ this.radius.set(_radius);}

    public DoubleProperty frictionProperty() { return friction; }
    public final void setFriction(double _friction){ this.friction.set(_friction); }
    public final double getFriction() { return this.friction.get(); }

    public DoubleProperty weightProperty() { return weight; }
    public final void setWeight(double _weight){ this.weight.set(_weight); }
    public final double getWeight() { return this.weight.get(); }

    public DoubleProperty angleProperty() { return angle;}
    public final void setAngle(double _angle){this.angle.set(_angle);}
    public final double getAngle(){ return this.angle.get();}

    public StringProperty materialProperty () { return material;}
    public final void setMaterial(String _material){this.material.set(_material);}
    public final String getMaterial(){ return this.material.get();}

    public BooleanProperty isSelectedProperty(){ return isSelected;}
    public final void setIsSelected(Boolean _isSelected){ this.isSelected.set(_isSelected); }
    public final boolean getIsSelected(){ return isSelected.get(); }

    public Shape getElementView(){ return this.elementView; }
    public void setElementView(Shape elementView) {
        this. elementView = elementView;
    }

    public void setIsMoving(boolean _isMoving){ this.isMoving = _isMoving; }
    public boolean isMoving() { return isMoving; }

    // hier werden die Werte für das Reset festgelegt
    public final void setStartValues(){
        startX = this.getXPosition();
        startY = this.getYPosition();
        startAccX = this.getXAcceleration();
        startAccY =  this.getYAcceleration();
        startVelX = this.getXVelocity();
        startVelY = this.getYVelocity();
        startAngle = this.getAngle();
        startScaleX = this.getXScale();
        startScaleY = this.getYScale();
        startWeight = this.getWeight();
        startHeight = this.getHeight();
        startWidth = this.getWidth();
    }

    // das Element wird auf die Startwerte zurückgesetzt
    public void resetElement(){
        setXPosition(startX);
        setYPosition(startY);
        setXAcceleration(startAccX);
        setYAcceleration(startAccY);
        setXVelocity(startVelX);
        setYVelocity(startVelY);
        setAngle(startAngle);
        //setXScale(startScaleX);
        //setYScale(startScaleY);
        setWeight(startWeight);
        setWidth(startWidth);
        setHeight(startHeight);
    }

    private void createSurfaces(){

        Light.Point pointLight = new Light.Point(30, 20, 50, Color.WHITE);
        Light.Distant distantLight = new Light.Distant();
        distantLight.setAzimuth(225);
        distantLight.setElevation(90);

        defaultSurface = new Lighting(distantLight);
        defaultSurface.setSurfaceScale(10);

        defaultBallSurface = new Lighting(pointLight);
        defaultBallSurface.setSpecularExponent(5);
        defaultBallSurface.setSurfaceScale(5);

        metalSurface = new Lighting(pointLight);
        metalSurface.setSurfaceScale(5);
        metalSurface.setSpecularExponent(5);
        metalSurface.setBumpInput(metalImage);

        rubberSurface = new Lighting(pointLight);
        rubberSurface.setSurfaceScale(20);
        rubberSurface.setSpecularExponent(5);
        rubberSurface.setBumpInput(rubberImage);

        woodSurface = new Lighting(pointLight);
        woodSurface.setSurfaceScale(10);
        woodSurface.setSpecularExponent(5);
        woodSurface.setBumpInput(woodImage);

    }
    public Lighting getDefaultSurface(){ return defaultSurface;}
    public Lighting getMetalSurface(){return metalSurface;}
    public Lighting getDefaultBallSurface(){ return defaultBallSurface;}
    public Lighting getRubberSurface(){return rubberSurface;}
    public Lighting getWoodSurface(){return woodSurface;}

    public void moveElement(){//diese Methode muss von den Objekten jeweils selbst implementiert werden
    }


    @Override
    public String toString(){
        return "xProperty: "+ getXPosition() +" yProperty: "+ getYPosition();
    }

}
