package Model;

import Helpers.VectorMath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class Spinner extends GraphicsObject {

    private DoubleProperty rotationalSpeed;
    private Line[] outlines = new Line[4]; // Kanten des Rechtecks
    private double xMiddle; // X-Wert des Mittelpunkts
    private double yMiddle; // Y-Wert des Mittelpunkts
    private VectorMath calculator;

    private Circle center, pointA;

    public Spinner(double _initX, double _initY) {

        super(_initX, _initY);
        setHeight(25);
        setWidth(100);
        setWeight(100000);
        calculator = new VectorMath();
        rotationalSpeed = new SimpleDoubleProperty(this, "rotationalSpeed", 0.0);

        xMiddle = _initX + getWidth() / 2;
        yMiddle = _initY + getHeight() / 2;
        center= new Circle(xMiddle,yMiddle,2);
        pointA = new Circle(_initX,_initY,2, Color.BLUE);
        setIsMoving(true);

        // Initialisierung der View
        elementView = new Rectangle(_initX, _initY, 100, 20);
        elementView.setFill(Color.ORANGERED);
        elementView.setStroke(Color.ORANGE);
        elementView.setStrokeType(StrokeType.INSIDE);
        elementView.setStrokeWidth(3);

        // Bindings zwischen View und Objekt
        ((Rectangle) elementView).xProperty().bindBidirectional(xPositionProperty());
        ((Rectangle) elementView).yProperty().bindBidirectional(yPositionProperty());
        ((Rectangle) elementView).widthProperty().bindBidirectional(widthProperty());
        ((Rectangle) elementView).heightProperty().bindBidirectional(heightProperty());
        elementView.scaleXProperty().bindBidirectional(xScaleProperty());
        elementView.scaleYProperty().bindBidirectional(yScaleProperty());
        elementView.rotateProperty().bindBidirectional(angleProperty());



        //hier ändert sich die Farbe wenn das Objekt angeklickt wird
        isSelectedProperty().addListener((observable, oldValue, newValue) -> {
            setIsSelectedColor();
        });

        //Objekt aktualisiert die Kollisionskanten nach Translation, Rotation oder Skalierung
        xPositionProperty().addListener((observable, oldValue, newValue) -> {
            xMiddle = getXPosition() + getWidth() / 2;
            updateOutlines();
        });

        yPositionProperty().addListener((observable, oldValue, newValue) -> {
            yMiddle = getYPosition() + getHeight() / 2;
            updateOutlines();
        });
        widthProperty().addListener(((observable, oldValue, newValue) -> {
            xMiddle = getXPosition() + getWidth() / 2;
            updateOutlines();
        }));

        heightProperty().addListener(((observable, oldValue, newValue) -> {
            yMiddle = getYPosition() + getHeight() / 2;
            updateOutlines();
        }));
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


    /**
     * initalisiert die Kollisionslinien des Rechtecks an den Kanten des Rechtecks
     */
    private void initOutlines() {
        outlines[0] = new Line(getXPosition(), getYPosition(), getXPosition() + getWidth(), getYPosition());
        outlines[1] = new Line(getXPosition() + getWidth(), getYPosition(), getXPosition() + getWidth(), getYPosition() + getHeight());
        outlines[2] = new Line(getXPosition() + getWidth(), getYPosition() + getHeight(), getXPosition(), getYPosition() + getHeight());
        outlines[3] = new Line(getXPosition(), getYPosition() + getHeight(), getXPosition(), getYPosition());

    }


    /**
     * Aktualisiert die Kollisionslinien des Rechtecks nach einer Skalierung/Translation
     */
    private void updatePositionOutlines() {
        double minX = xMiddle - getWidth()  / 2; // x-Wert links
        double minY = yMiddle - getHeight() / 2; // y-Wert oben
        double maxX = xMiddle + getWidth()  / 2; //x-Wert rechts
        double maxY = yMiddle + getHeight() / 2; // y-Wert unten

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

    /**
     * Aktualisiert die Kollisionslinien nach der Rotation
     */
    public void updateOutlines() {
        updatePositionOutlines();

        double c = Math.cos(Math.toRadians(getAngle()));
        double s = Math.sin(Math.toRadians(getAngle()));
        for (int i = 0; i < 4; i++) {
            double pX = outlines[i].getStartX() - xMiddle;
            double pY = outlines[i].getStartY() - yMiddle;
            outlines[i].setStartX((pX * c - pY * s) + xMiddle);
            outlines[i].setStartY((pX * s + pY * c) + yMiddle);

            pX = outlines[i].getEndX() - xMiddle;
            pY = outlines[i].getEndY() - yMiddle;
            outlines[i].setEndX((pX * c - pY * s) + xMiddle);
            outlines[i].setEndY((pX * s + pY * c) + yMiddle);
        }


        center.setCenterX(xMiddle);
        center.setCenterY(yMiddle);
        pointA.setCenterX(outlines[0].getStartX());
        pointA.setCenterY(outlines[0].getStartY());
    }


    private void setIsSelectedColor() {
        if (getIsSelected()) {

            elementView.setStroke(Color.ORANGE);
            elementView.setStrokeType(StrokeType.INSIDE);
            elementView.setStrokeWidth(3);

        } else elementView.setStroke(null);
    }

    @Override
    public void moveElement() {
        setAngle((getAngle() + 360 * getRotationalSpeed() * time));
        updateOutlines();
    }


    public double[] velocityVector(double x, double y){
        double[] returnVector = new double[]{0,0};

        // v = omega/(2*Pi) * (2 Pi * r)   mit omega = 2 * Pi * Umdrehung/sec
        double v = getRotationalSpeed() * time * 2 * Math.PI  * 100; //Umrechung in cm/s

        //Der Vektor für die Bahngeschwindigkeit in dem Punkt (x,y) steht senkrecht auf dem Vektor r
        returnVector[0] = -(y - yMiddle) * v ;
        returnVector[1] = (x - xMiddle) * v;


        return returnVector;
    }




    public double getCenterX() {
        return xMiddle;
    }

    public double getCenterY() {
        return yMiddle;
    }

    public DoubleProperty rotationalSpeedProperty() {
        return rotationalSpeed;
    }

    public void setRotationalSpeed(double rotationalSpeed) {
        this.rotationalSpeed.set(rotationalSpeed);
    }

    public double getRotationalSpeed() {
        return this.rotationalSpeed.get();
    }

    public Line[] getOutlines() {
        return this.outlines;
    }

    public Circle getCenter(){
        return  center;
    }
    public Circle getPointA(){
        return  pointA;
    }

}
