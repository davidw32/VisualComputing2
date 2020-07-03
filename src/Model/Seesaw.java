package Model;

import Helpers.VectorMath;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import static Helpers.Config.GRAVITY;


public class Seesaw extends GraphicsObject
{
    private double heightOfRectangle,lengthOfRectangle, heightOfTriangle, alpha, pivotX, pivotY, omega;
    private double leftForce, rightForce;
    Rectangle elementView;
    Polygon triangle;
    private boolean left, right;
    private Line[] outlines = new Line[7]; // Kanten des Rechtecks
    private VectorMath vectorMath;

    public Seesaw(double initialX, double initialY){
        super(initialX, initialY);

        heightOfRectangle = 20.0;

        setWidth(300);
        setHeight(30);
        setWeight(100000);
        pivotX=initialX + getWidth()/2; //Mittelpunkt des Rechtecks
        pivotY=initialY + heightOfRectangle/2;
        lengthOfRectangle = getWidth();
        heightOfTriangle = getHeight();

        vectorMath = new VectorMath();
        elementView = new Rectangle(initialX,initialY,getWidth(),20);
        elementView.setFill(Color.DODGERBLUE);
        elementView.setStroke(Color.ORANGE);
        elementView.setStrokeType(StrokeType.INSIDE);
        elementView.setStrokeWidth(3);

        triangle = new Polygon( pivotX - 15, pivotY + 10+heightOfTriangle, pivotX+15, pivotY + 10+heightOfTriangle, pivotX, pivotY + 10.0);
        triangle.setFill(Color.DODGERBLUE);
        triangle.setStroke(Color.ORANGE);
        triangle.setStrokeType(StrokeType.INSIDE);
        triangle.setStrokeWidth(3);


        // Bindings zwischen View und Objekt
        elementView.xProperty().bindBidirectional(xPositionProperty());
        elementView.yProperty().bindBidirectional(yPositionProperty());
        elementView.widthProperty().bindBidirectional(widthProperty());
       //elementView.heightProperty().bindBidirectional(heightProperty());
        elementView.scaleXProperty().bindBidirectional(xScaleProperty());
        elementView.scaleYProperty().bindBidirectional(yScaleProperty());
        elementView.rotateProperty().bindBidirectional(angleProperty());
        elementView.setEffect(getDefaultSurface());
        triangle.setEffect(getDefaultSurface());


        //hier ändert sich die Farbe wenn das Objekt angeklickt wird
        isSelectedProperty().addListener(observable -> {
            setIsSelectedColor();
        });

        //Objekt aktualisiert die Kollisionskanten nach Translation, Rotation oder Skalierung
        xPositionProperty().addListener((observable, oldValue, newValue) -> {
            pivotX = getXPosition() + getWidth() / 2;
            updateOutlines();
        });

        yPositionProperty().addListener(observable -> {
            pivotY = getYPosition() + heightOfRectangle / 2;
            updateOutlines();
        });
        widthProperty().addListener(observable -> {
            pivotX = getXPosition() + getWidth() / 2;
            updateOutlines();
        });

        heightProperty().addListener(observable -> {
            pivotY = getYPosition() + heightOfRectangle / 2;
            heightOfTriangle=getHeight();
            calculateKippWinkel();
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

        elementView.fillProperty().addListener(observable -> {triangle.setFill(elementView.getFill());});

        // um zu unterscheiden, in welche Richtung der Balken kippt
        left = false;
        right = false;
        //maximaler Kippwinkel des Balkens
        calculateKippWinkel();

        //Kippgeschwindigkeit omega = 360 / 60, (eine Umdrehung pro Sekunde/60 Frames)
        omega = 10;

        initOutlines();
    }
    //Für die Grafikanzeige
    public Shape getElementView(){ return elementView;}
    public Polygon getTriangle() {
        return triangle;
    }


    private void setIsSelectedColor() {
        if (getIsSelected()) {
            elementView.setStroke(Color.ORANGE);
            elementView.setStrokeWidth(3);
            elementView.setStrokeType(StrokeType.INSIDE);
            triangle.setStroke((Color.ORANGE));
            triangle.setStrokeWidth(3);
            triangle.setStrokeType(StrokeType.INSIDE);
        } else {
            elementView.setStroke(null);
            triangle.setStroke(null);
        }
    }

    /**
     * initalisiert die Kollisionslinien des Rechtecks an den Kanten des Rechtecks
     */
    private void initOutlines() {
        //für den Balken
        outlines[0] = new Line(getXPosition(), getYPosition(), getXPosition() + getWidth(), getYPosition());
        outlines[1] = new Line(getXPosition() + getWidth(), getYPosition(), getXPosition() + getWidth(), getYPosition() + heightOfRectangle);
        outlines[2] = new Line(getXPosition() + getWidth(), getYPosition() + getHeight(), getXPosition(), getYPosition() + heightOfRectangle);
        outlines[3] = new Line(getXPosition(), getYPosition() + heightOfRectangle, getXPosition(), getYPosition());
        // für das Dreieck
        outlines[4] = new Line(pivotX,pivotY+10,pivotX+15, pivotY+10+heightOfTriangle);
        outlines[5] = new Line(pivotX+15, pivotY+10+heightOfTriangle,pivotX-15, pivotY+10+heightOfTriangle);
        outlines[6] = new Line(pivotX-15, pivotY+10+heightOfTriangle,pivotX, pivotY+10);
    }
    /**
     * Aktualisiert die Kollisionslinien des Rechtecks und Dreiecks nach einer Skalierung/Translation
     */
    private void updatePositionOutlines() {
        double minX = pivotX - getWidth()  / 2; // x-Wert links
        double minY = pivotY - heightOfRectangle / 2; // y-Wert oben
        double maxX = pivotX + getWidth()  / 2; //x-Wert rechts
        double maxY = pivotY + heightOfRectangle / 2; // y-Wert unten
        //Balken
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
        //Dreieck
        outlines[4].setStartX(pivotX);
        outlines[4].setStartY(pivotY+10);
        outlines[4].setEndX(pivotX+15);
        outlines[4].setEndY(pivotY+10+heightOfTriangle);
        outlines[5].setStartX(pivotX+15);
        outlines[5].setStartY(pivotY+10+heightOfTriangle);
        outlines[5].setEndX(pivotX-15);
        outlines[5].setEndY(pivotY+10+heightOfTriangle);
        outlines[6].setStartX(pivotX-15);
        outlines[6].setStartY(pivotY+10+heightOfTriangle);
        outlines[6].setEndX(pivotX);
        outlines[6].setEndY(pivotY+10);

    }

    /**
     * Aktualisiert die Kollisionslinien nach der Rotation
     */
    public void updateOutlines() {
        updatePositionOutlines();

        triangle.getPoints().clear();
        triangle.getPoints().addAll(pivotX - 15, pivotY + 10+heightOfTriangle, pivotX+15, pivotY + 10+heightOfTriangle, pivotX, pivotY + 10.0);
        double c = Math.cos(Math.toRadians(getAngle()));
        double s = Math.sin(Math.toRadians(getAngle()));
        //nur der Balken kippt, das Dreieck bleibt stehen
        for (int i = 0; i < 4; i++) {
            double pX = outlines[i].getStartX() - pivotX;
            double pY = outlines[i].getStartY() - startY;
            outlines[i].setStartX((pX * c - pY * s) + pivotX);
            outlines[i].setStartY((pX * s + pY * c) + startY);

            pX = outlines[i].getEndX() - pivotX;
            pY = outlines[i].getEndY() - startY;
            outlines[i].setEndX((pX * c - pY * s) + pivotX);
            outlines[i].setEndY((pX * s + pY * c) + startY);
        }

    }

    public void moveElement(){

        //den Balken nach links kippen bis zum maximalen Kippwinkel
        if(leftForce>rightForce ){
            if (this.getAngle()> -alpha){
                right= false;
                rightForce=0;
                this.setAngle(this.getAngle() - omega * time);
                updateOutlines();
            } else if (this.getAngle() <= -alpha ){
                left = false;
                leftForce = 0;
                updateOutlines();
            }
        }// den Balken nach rechts kippen bis zum maximalen Kippwinkel
        else if (rightForce>leftForce ){
            if (this.getAngle() < alpha){
                left=false;
                leftForce=0;
                this.setAngle(this.getAngle() + omega * time);
                updateOutlines();
            } else if (this.getAngle() >= alpha ){
                right = false;
                rightForce = 0;
                updateOutlines();
            }
        }
    }


    public void setLeft(boolean _left){
        this.left = _left;
    }
    public void setRight(boolean _right){
        this.right = _right;
    }

    private void calculateKippWinkel(){
        //maximaler Kippwinkel
        alpha = Math.toDegrees(Math.atan( 2 * heightOfTriangle / lengthOfRectangle));

    }

    /**
     * Die Kraft die auf den Linken Hebel des Wippe wirken
     * @param mass - der Kugel
     * @param x - x-Koordinate des Auftreffpunkts
     * @param y - y-Koordinate des Auftreffpunkts
     */
    public void setLeftForce(double mass, double x, double y){
        double length = Math.abs(x-pivotX);
        leftForce = mass * GRAVITY * length;
    }
    /**
     * Die Kraft die auf den Rechten Hebel des Wippe wirken
     * @param mass - der Kugel
     * @param x - x-Koordinate des Auftreffpunkts
     * @param y - y-Koordinate des Auftreffpunkts
     */
    public void setRightForce(double mass, double x, double y){
        double length = Math.abs(x-pivotX);
        rightForce = mass * GRAVITY * length;
    }


    public Line[] getOutlines(){
        return this.outlines;
    }

    @Override
    public void resetElement() {
        super.resetElement();
       left = false;
       right = false;
       leftForce = 0;
       rightForce = 0;
    }
}
