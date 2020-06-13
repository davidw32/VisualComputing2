package Model;

import Helpers.VectorMath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class Spinner extends GraphicsObject{

    private DoubleProperty radius;
    private double centerX,centerY;

    private VectorMath calculator;



    private DoubleProperty width, height, rotationalSpeed;

    private Line[] outlines;

    private double[] pointA, pointB, pointC;
    private Circle border;


    public Spinner(double initialX, double initialY) {
        super(initialX, initialY);
        setIsMoving(true);

        centerX=initialX+50;
        centerY=initialY+5;
        radius = new SimpleDoubleProperty(this, "radius", 50);
        width = new SimpleDoubleProperty(this, "width", 100);
        height = new SimpleDoubleProperty(this, "height",10);
        rotationalSpeed = new SimpleDoubleProperty(this, "rotationalSpeed", 0);
        setWeight(3000);

        border = new Circle(centerX, centerY, (getRadius()+3) );
        border.setStroke(Color.GREY);
        border.setStrokeWidth(2);
        border.setFill(null);


        //rotationalSpeed wird in grad/sec angegeben
        elementView = new Rectangle(initialX - 50, initialY-5,100,10);
        elementView.setFill(Color.ORANGERED);
        elementView.setStroke(Color.ORANGE);
        elementView.setStrokeWidth(3);

        ((Rectangle)elementView).xProperty().bindBidirectional(xPositionProperty());
        ((Rectangle)elementView).yProperty().bindBidirectional(yPositionProperty());
        ((Rectangle) elementView).widthProperty().bindBidirectional(widthProperty());
        ((Rectangle) elementView).heightProperty().bindBidirectional(heightProperty());
        elementView.scaleXProperty().bindBidirectional(xScaleProperty());
        elementView.scaleYProperty().bindBidirectional(yScaleProperty());
        elementView.rotateProperty().bindBidirectional(angleProperty());

        outlines = new Line[4];

        //hier ändert sich die Farbe wenn das Objekt angeklickt wird
        isSelectedProperty().addListener((observable, oldValue, newValue) -> {
            setIsSelectedColor();
        });

        xPositionProperty().addListener((observable, oldValue, newValue) -> {
            centerX = getXPosition() + getWidth()/2;
            border.setCenterX(centerX);
            updateOutlines();
        });

        xPositionProperty().addListener((observable, oldValue, newValue) -> {
            centerY = getYPosition() + getHeight()/2;
            border.setCenterY(centerY);
            updateOutlines();
        });

        xScaleProperty().addListener((observable, oldValue, newValue) -> {
            updateOutlines();
        });

        yScaleProperty().addListener((observable, oldValue, newValue) -> {
            updateOutlines();
        });

        angleProperty().addListener((observable, oldValue, newValue) -> {
            updateOutlines();
        });

        initOutlines();




    }


    private void initOutlines(){
        outlines[0] = new Line(getXPosition(),getYPosition(),getXPosition()+getWidth(),getYPosition());
        outlines[1] = new Line(getXPosition()+getWidth(),getYPosition(),getXPosition()+getWidth(),getYPosition()+getHeight());
        outlines[2] = new Line(getXPosition()+getWidth(),getYPosition()+getHeight(),getXPosition(),getYPosition()+getHeight());
        outlines[3] = new Line(getXPosition(),getYPosition()+getHeight(),getXPosition(),getYPosition());
    }

    private void updatePositionOutlines(){
        double minX = getXPosition() -getWidth()*((elementView.getScaleX()-1)/2); // x-Wert links
        double minY = getYPosition() -getHeight()*((elementView.getScaleY()-1)/2); // y-Wert oben
        double maxX = getXPosition()+getWidth() +getWidth()*((elementView.getScaleX()-1)/2); //x-Wert rechts
        double maxY = getYPosition()+getHeight() +getHeight()*((elementView.getScaleY()-1)/2); // y-Wert unten

        outlines[0].setStartX(minX);
        outlines[0].setStartY(minY);
        outlines[0].setEndX(maxX);
        outlines[0].setEndY(minY);
        outlines[1].setStartX(maxX);
        outlines[1].setStartY(minY);
        outlines[1].setEndX(maxX);
        outlines[1].setEndY(maxY);
        outlines[2].setStartX(maxX);
        outlines[2].setStartY(maxY);
        outlines[2].setEndX(minX);
        outlines[2].setEndY(maxY);
        outlines[3].setStartX(minX);
        outlines[3].setStartY(maxY);
        outlines[3].setEndX(minX);
        outlines[3].setEndY(minY);
    }

    public void updateOutlines(){
        updatePositionOutlines();

        double c = Math.cos(Math.toRadians(getAngle()));
        double s = Math.sin(Math.toRadians(getAngle()));
        for(int i = 0; i<4;i++){
            double pX = outlines[i].getStartX() - centerX;
            double pY = outlines[i].getStartY() - centerY;
            outlines[i].setStartX((pX*c - pY*s) + centerX);
            outlines[i].setStartY((pX*s + pY*c) + centerY);

            pX = outlines[i].getEndX() - centerX;
            pY = outlines[i].getEndY() - centerY;
            outlines[i].setEndX((pX*c - pY*s) + centerX);
            outlines[i].setEndY((pX*s + pY*c) + centerY);
        }
    }

    public Line[] getOutlines(){ return this.outlines; }

    public DoubleProperty heightProperty() { return height; }
    public final void setHeight(double _height){ this.height.set(_height); }
    public final double getHeight() { return this.height.get(); }

    public DoubleProperty widthProperty() { return width; }
    public final void setWidth(double _width){ this.width.set(_width); }
    public final double getWidth() { return this.width.get(); }

    public DoubleProperty rotationalSpeedProperty() { return rotationalSpeed; }
    public final void setRotationalSpeed(double rotationalSpeed){ this.rotationalSpeed.set(rotationalSpeed); }
    public final double getRotationalSpeed() { return this.rotationalSpeed.get(); }

    public DoubleProperty radiusProperty(){ return radius;}
    public final void setRadius(double radius){ this.radius.set(radius);}
    public final double getRadius(){return this.radius.get();}

    private void setIsSelectedColor() {
        if (getIsSelected()) {

            elementView.setStroke(Color.ORANGE);
            elementView.setStrokeWidth(3);

        } else elementView.setStroke(null);
    }

    public void moveElement(){

        setAngle((getAngle()+ 360*getRotationalSpeed()*time)%360 );
        updateOutlines();

    }

    public Circle getBorder() {
        return border;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }
}
