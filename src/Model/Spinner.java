package Model;

import Helpers.VectorMath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Spinner extends GraphicsObject{

    private DoubleProperty radius;
    private double centerX,centerY;
    private double[] pointA, pointB, pointC;
    private VectorMath calculator;
    private int timer=0;
    private Polygon triangle;

    public Spinner(double initialX, double initialY) {
        super(initialX, initialY);
        centerX=initialX;
        centerY=initialY;
        radius = new SimpleDoubleProperty(this, "radius", 30);
        setWeight(10);
        setIsMoving(true);

        //die Eckpunkte eines gleichseitigen Dreiecks
        pointA = new double[]{centerX, centerY + getRadius() * 2};
        pointB = new double[]{centerX + (getRadius() * Math.sqrt(3)) , centerY - getRadius()};
        pointC = new double[]{centerX - (getRadius() * Math.sqrt(3)) , centerY - getRadius()};

        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                pointA[0],pointA[1],
                pointB[0],pointB[1],
                pointC[0],pointC[1]);
        // ein Kreis für den Drehpunkt in der Mitte
        Circle center = new Circle(centerX,centerY,5);
        // die entgültige Form für den Spinner
        elementView = Shape.subtract(triangle,center);
        elementView.setFill(Color.ORANGERED);
        elementView.setStrokeWidth(3);
        elementView.setStroke(Color.ORANGE);


        //hier werden die Properties des Objektes an die der Shape getackert
        //da der Spinner nicht verschoben wird, sondern sich nur um die eigene Achse dreht
        elementView.translateXProperty().bindBidirectional(xPositionProperty());
        elementView.translateYProperty().bindBidirectional(yPositionProperty());
        elementView.rotateProperty().bindBidirectional(angleProperty());

        elementView.scaleXProperty().bindBidirectional(xScaleProperty());
        elementView.scaleYProperty().bindBidirectional(yScaleProperty());

        //hier ändert sich die Farbe wenn das Objekt angeklickt wird
        isSelectedProperty().addListener((observable, oldValue, newValue) -> {
            setIsSelectedColor();
        });

        // ein Ball kann nur proportional skaliert werden.
        xScaleProperty().bindBidirectional(yScaleProperty());

        calculator = new VectorMath();

    }
    public DoubleProperty radiusProperty() { return radius; }
    public final void setRadius(double _radius) { this.radius.set(_radius); }
    public final double getRadius() { return this.radius.get(); }


    private void setIsSelectedColor() {
        if (getIsSelected()) {

            elementView.setStroke(Color.ORANGE);
            elementView.setStrokeWidth(3);

        } else elementView.setStroke(null);
    }

    public void moveElement(){
        timer++;
        elementView.setRotate(6*timer);
    }

}
