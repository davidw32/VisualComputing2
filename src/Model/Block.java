package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Block extends GraphicsObject{


    private DoubleProperty width, height;
    private Line[] outlines = new Line[4]; // Kanten des Rechtecks
    double xMiddle; // X-Wert des Mittelpunkts
    double yMiddle; // Y-Wert des Mittelpunkts

public Block(double _initX, double _initY){
    super(_initX, _initY);
    width = new SimpleDoubleProperty(this, "width", 80);
    height = new SimpleDoubleProperty(this, "height",20);
    xMiddle = _initX + width.get()/2;
    yMiddle = _initY + height.get()/2;

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

    //Objekt aktualisiert die Kollisionskanten nach Translation, Rotation oder Skalierung
    xPositionProperty().addListener((observable, oldValue, newValue) -> {
        xMiddle = getXPosition() + getWidth()/2;
        updateOutlines();
    });

    xPositionProperty().addListener((observable, oldValue, newValue) -> {
        yMiddle = getYPosition() + getHeight()/2;
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

    /**
     * initalisiert die Kollisionslinien des Rechtecks an den Kanten des Rechtecks
     */
    private void initOutlines(){
        outlines[0] = new Line(getXPosition(),getYPosition(),getXPosition()+getWidth(),getYPosition());
        outlines[1] = new Line(getXPosition()+getWidth(),getYPosition(),getXPosition()+getWidth(),getYPosition()+getHeight());
        outlines[2] = new Line(getXPosition()+getWidth(),getYPosition()+getHeight(),getXPosition(),getYPosition()+getHeight());
        outlines[3] = new Line(getXPosition(),getYPosition()+getHeight(),getXPosition(),getYPosition());
    }

    /**
     *  Aktualisiert die Kollisionslinien des Rechtecks nach einer Skalierung/Translation
     */
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

    /**
     * Aktualisiert die Kollisionslinien nach der Rotation
     */
    public void updateOutlines(){
        updatePositionOutlines();

        double c = Math.cos(Math.toRadians(getAngle()));
        double s = Math.sin(Math.toRadians(getAngle()));
        for(int i = 0; i<4;i++){
            double pX = outlines[i].getStartX() - xMiddle;
            double pY = outlines[i].getStartY() - yMiddle;
            outlines[i].setStartX((pX*c - pY*s) + xMiddle);
            outlines[i].setStartY((pX*s + pY*c) + yMiddle);

            pX = outlines[i].getEndX() - xMiddle;
            pY = outlines[i].getEndY() - yMiddle;
            outlines[i].setEndX((pX*c - pY*s) + xMiddle);
            outlines[i].setEndY((pX*s + pY*c) + yMiddle);
        }
    }

    public Line[] getOutlines(){ return this.outlines; }


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
