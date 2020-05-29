package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block extends GraphicsObject{


    private DoubleProperty width, height;

public Block(double _initX, double _initY){
    super(_initX, _initY);
    width = new SimpleDoubleProperty(this, "width", 80);
    height = new SimpleDoubleProperty(this, "height",20);
    setIsMoving(false);
    // Initialisierung der View
    elementView = new Rectangle(_initX, _initY, 80,20);
    elementView.setFill(Color.SEAGREEN);
    elementView.setStroke(Color.ORANGE);
    elementView.setStrokeWidth(3);

    // Bindings zwischen View und Objekt
    ((Rectangle)elementView).xProperty().bindBidirectional(xPositionProperty());
    ((Rectangle)elementView).yProperty().bindBidirectional(yPositionProperty());
    ((Rectangle) elementView).widthProperty().bindBidirectional(widthProperty());
    ((Rectangle) elementView).heightProperty().bindBidirectional(heightProperty());
    elementView.scaleXProperty().bindBidirectional(xScaleProperty());
    elementView.scaleYProperty().bindBidirectional(yScaleProperty());
    elementView.rotateProperty().bindBidirectional(angleProperty());


    //hier ändert sich die Farbe wenn das Objekt angeklickt wird
    isSelectedProperty().addListener((observable, oldValue, newValue) -> {
        setIsSelectedColor();
    });

    }

    public DoubleProperty heightProperty() { return height; }
    public final void setHeight(double _height){ this.height.set(_height); }
    public final double getHeight() { return this.height.get(); }

    public DoubleProperty widthProperty() { return width; }
    public final void setWidth(double _width){ this.width.set(_width); }
    public final double getWidth() { return this.width.get(); }


    private void setIsSelectedColor(){
        if (getIsSelected()){

            elementView.setStroke(Color.ORANGE);
            elementView.setStrokeWidth(3);

        } else elementView.setStroke(null);
    }
}
