package Model;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Ball extends GraphicsObject
{



    public Ball(double _initXPosition, double _initYPosition) {

        super(_initXPosition, _initYPosition);

        elementView = new Circle(getXPosition(), getYPosition(), getRadius(), Color.PLUM);

        //hier werden die Properties des Objektes an die der Shape getackert
        ((Circle) elementView).centerXProperty().bindBidirectional(xPositionProperty());
        ((Circle) elementView).centerYProperty().bindBidirectional(yPositionProperty());
        ((Circle) elementView).radiusProperty().bindBidirectional(radiusProperty());

        // ein Ball kann nur proportional skaliert werden.
        xScaleProperty().bindBidirectional(yScaleProperty());


    }




}
