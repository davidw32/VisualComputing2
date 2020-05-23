package Model;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Ball extends GraphicsObject
{

    private Shape elementView;

    public Ball(double _initXPosition, double _initYPosition) {
        super(_initXPosition, _initYPosition);
        elementView = new Circle(getXPosition(), getYPosition(), 30, Color.PLUM);
        //kann man hier die Properties der Shape bidirectional an die Poperties des Ballobjektes binden???
        ((Circle) elementView).centerXProperty().bindBidirectional(xPositionProperty());
        ((Circle) elementView).centerYProperty().bindBidirectional(yPositionProperty());
    }



    public Shape getElementView(){
        return elementView;
    }
}
