package Model;

import Helpers.VectorMath;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;


public class Seesaw extends GraphicsObject
{
    private double heightOfRectangle,lengthOfRectangle, heightOfTriangle, alpha, pivotX, pivotY, omega;

    Rectangle elementView;
    Polygon triangle;
    private boolean left, right;
    private Line[] outlines = new Line[7]; // Kanten des Rechtecks
    private VectorMath vectorMath;

    public Seesaw(double initialX, double initialY){
        super(initialX, initialY);


        setWidth(300);
        setHeight(20);
        setWeight(100000);
        pivotX=initialX + getWidth()/2; //Mittelpunkt des Rechtecks
        pivotY=initialY + getHeight()/2;

        vectorMath = new VectorMath();
        elementView = new Rectangle(initialX,initialY,getWidth(),20.0);
        elementView.setFill(Color.DODGERBLUE);
        elementView.setStroke(Color.ORANGE);
        elementView.setStrokeWidth(3);

        triangle = new Polygon( pivotX - 15, pivotY + 30.0, pivotX+15, pivotY + 30.0, pivotX, pivotY + 10.0);
        triangle.setFill(Color.DODGERBLUE);
        triangle.setStroke(Color.ORANGE);
        triangle.setStrokeWidth(3);


        // Bindings zwischen View und Objekt
        elementView.xProperty().bindBidirectional(xPositionProperty());
        elementView.yProperty().bindBidirectional(yPositionProperty());
        elementView.widthProperty().bindBidirectional(widthProperty());
        elementView.heightProperty().bindBidirectional(heightProperty());
        elementView.scaleXProperty().bindBidirectional(xScaleProperty());
        elementView.scaleYProperty().bindBidirectional(yScaleProperty());
        elementView.rotateProperty().bindBidirectional(angleProperty());
        elementView.setEffect(getDefaultSurface());
        triangle.setEffect(getDefaultSurface());


        //hier ändert sich die Farbe wenn das Objekt angeklickt wird
        isSelectedProperty().addListener((observable, oldValue, newValue) -> {
            setIsSelectedColor();
        });

        //Objekt aktualisiert die Kollisionskanten nach Translation, Rotation oder Skalierung
        xPositionProperty().addListener((observable, oldValue, newValue) -> {
            pivotX = getXPosition() + getWidth() / 2;

            updateOutlines();
        });

        yPositionProperty().addListener((observable, oldValue, newValue) -> {
            pivotY = getYPosition() + getHeight() / 2;

            updateOutlines();
        });
        widthProperty().addListener(((observable, oldValue, newValue) -> {
            pivotX = getXPosition() + getWidth() / 2;
            updateOutlines();
        }));

        heightProperty().addListener(((observable, oldValue, newValue) -> {
            pivotY = getYPosition() + getHeight() / 2;
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
        elementView.fillProperty().addListener(observable -> {triangle.setFill(elementView.getFill());});

        initOutlines();


        heightOfRectangle = 20.0;
        lengthOfRectangle = getWidth();
        heightOfTriangle = 20;
        // um zu unterscheiden, in welche Richtung der Balken kippt
        left = false;
        right = false;
        //maximaler Kippwinkel des Balkens
        alpha = Math.toDegrees(Math.atan( 2* heightOfTriangle / lengthOfRectangle));
        //Kippgeschwindigkeit omega = 360 / 60, (eine Umdrehung pro Sekunde/60 Frames)
        omega = 60;
    }

    public Shape getElementView(){ return elementView;}

    public Polygon getTriangle() {
        return triangle;
    }


    private void setIsSelectedColor() {
        if (getIsSelected()) {

            elementView.setStroke(Color.ORANGE);
            elementView.setStrokeWidth(3);
            triangle.setStroke((Color.ORANGE));
            triangle.setStrokeWidth(3);

        } else {
            elementView.setStroke(null);
            triangle.setStroke(null);
        }
    }

    /**
     * initalisiert die Kollisionslinien des Rechtecks an den Kanten des Rechtecks
     */
    private void initOutlines() {
        outlines[0] = new Line(getXPosition(), getYPosition(), getXPosition() + getWidth(), getYPosition());
        outlines[1] = new Line(getXPosition() + getWidth(), getYPosition(), getXPosition() + getWidth(), getYPosition() + getHeight());
        outlines[2] = new Line(getXPosition() + getWidth(), getYPosition() + getHeight(), getXPosition(), getYPosition() + getHeight());
        outlines[3] = new Line(getXPosition(), getYPosition() + getHeight(), getXPosition(), getYPosition());
        outlines[4] = new Line(pivotX,pivotY+10,pivotX+15, pivotY+30);
        outlines[5] = new Line(pivotX+15, pivotY+30,pivotX-15, pivotY+30);
        outlines[6] = new Line(pivotX-15, pivotY+30,pivotX, pivotY+10);
    }
    /**
     * Aktualisiert die Kollisionslinien des Rechtecks und Dreiecks nach einer Skalierung/Translation
     */
    private void updatePositionOutlines() {
        double minX = pivotX - getWidth()  / 2; // x-Wert links
        double minY = pivotY - getHeight() / 2; // y-Wert oben
        double maxX = pivotX + getWidth()  / 2; //x-Wert rechts
        double maxY = pivotY + getHeight() / 2; // y-Wert unten
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
        outlines[4].setEndY(pivotY+30);
        outlines[5].setStartX(pivotX+15);
        outlines[5].setStartY(pivotY+30);
        outlines[5].setEndX(pivotX-15);
        outlines[5].setEndY(pivotY+30);
        outlines[6].setStartX(pivotX-15);
        outlines[6].setStartY(pivotY+30);
        outlines[6].setEndX(pivotX);
        outlines[6].setEndY(pivotY+10);

    }

    /**
     * Aktualisiert die Kollisionslinien nach der Rotation
     */
    public void updateOutlines() {
        updatePositionOutlines();

        triangle.getPoints().clear();
        triangle.getPoints().addAll(pivotX - 15, pivotY + 30.0, pivotX+15, pivotY + 30.0, pivotX, pivotY + 10.0);
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
        //System.out.println("Alpha: "+alpha+" left "+left+" right "+right);
        //den Balken nach links kippen bis zum maximalen Kippwinkel
        if(left){
            if (this.getAngle()> -alpha){
                this.setAngle(this.getAngle() - omega * time);
                updateOutlines();
            } else if (this.getAngle() <= -alpha ){
                left = false;
                updateOutlines();
            }
        }// den Balken nach rechts kippen bis zum maximalen Kippwinkel
        else if (right){
            if (this.getAngle() < alpha){
                this.setAngle(this.getAngle() + omega * time);
                updateOutlines();
            } else if (this.getAngle() >= alpha ){
                right = false;
                updateOutlines();
            }
        }

    }


    public Line[] getOutlines(){
        return this.outlines;
    }



    public void setLeft(boolean _left){
        this.left = _left;
    }
    public void setRight(boolean _right){
        this.right = _right;
    }

    private void kippWinkel(){
        //maximaler Kippwinkel
        alpha = Math.atan( 2* heightOfTriangle / lengthOfRectangle);
    }

    public void setOmega(double velocity, double x, double y){

        double radius = vectorMath.vectorLength(x-pivotX, y-pivotY);
        this.omega= Math.toDegrees(velocity/radius);
        System.out.println("Radius: "+radius+" Omega: "+omega+" Velocity: "+velocity);
    }
    private void setKippGeschwindigkeit(double ballWeight, double ballAccX, double ballAccY){

        double forceX = ballWeight * ballAccX;
        double forceY = ballWeight * ballAccY;

        this.setXAcceleration( forceX / this.getWeight() );
        this.setYAcceleration( forceY / this.getWeight() );

        this.setXVelocity(this.getXVelocity() + this.getXAcceleration() * time * Math.toRadians(alpha));
        this.setYVelocity(this.getYVelocity() + this.getYAcceleration() * time * Math.toRadians(alpha));

    }


}
