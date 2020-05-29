package Model;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball extends GraphicsObject
{

    private DoubleProperty radius;

    public Ball(double _initXPosition, double _initYPosition) {



        super(_initXPosition, _initYPosition);
        radius = new SimpleDoubleProperty(this, "radius", 30);

        elementView = new Circle(xPosition(), yPosition(), radius(), Color.PLUM);
        elementView.setStrokeWidth(3);
        elementView.setStroke(Color.ORANGE);

        //hier werden die Properties des Objektes an die der Shape getackert
        ((Circle) elementView).centerXProperty().bindBidirectional(xPositionProperty());
        ((Circle) elementView).centerYProperty().bindBidirectional(yPositionProperty());
        ((Circle) elementView).radiusProperty().bindBidirectional(radiusProperty());

        elementView.scaleXProperty().bindBidirectional(xScaleProperty());
        elementView.scaleYProperty().bindBidirectional(yScaleProperty());

        //hier ändert sich die Farbe wenn das Objekt angeklickt wird
        isSelectedProperty().addListener((observable, oldValue, newValue) -> {
            setIsSelectedColor();
        });

        // ein Ball kann nur proportional skaliert werden.
        xScaleProperty().bindBidirectional(yScaleProperty());


    }
    public DoubleProperty radiusProperty() { return radius; }
    public final void setRadius(double _radius) { this.radius.set(_radius); }
    public final double radius() { return this.radius.get(); }

    public void moveElement(){

        setXPosition(xPosition()+4);
        setYPosition(yPosition()+4);
    }


    private void setIsSelectedColor(){
        if (getIsSelected()){

            elementView.setStroke(Color.ORANGE);
            elementView.setStrokeWidth(3);

        } else elementView.setStroke(null);
    }




}
