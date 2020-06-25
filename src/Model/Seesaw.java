package Model;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;


public class Seesaw extends GraphicsObject
{
    private double heightOfRectangle,lengthOfRectangle, heightOfTriangle, alpha, pivotX, pivotY, weight;
    private Group seesaw;
    Rectangle elementView;
    Polygon triangle;

    private Line[] outlines = new Line[4]; // Kanten des Rechtecks


    public Seesaw(double initialX, double initialY){
        super(initialX, initialY);


        setWidth(120);
        setHeight(20);
        pivotX=initialX + getWidth()/2; //Mittelpunkt des Rechtecks
        pivotY=initialY + getHeight()/2;

        elementView = new Rectangle(initialX,initialY,120.0,20.0);
        elementView.setFill(Color.DODGERBLUE);
        elementView.setStroke(Color.ORANGE);
        elementView.setStrokeWidth(3);

        triangle = new Polygon( pivotX - 15, pivotY + 30.0, pivotX+15, pivotY + 30.0, pivotX, pivotY + 10.0);
        triangle.setFill(Color.DODGERBLUE);
        triangle.setStroke(Color.ORANGE);
        triangle.setStrokeWidth(3);


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

        initOutlines();




        heightOfRectangle = 20.0;
        lengthOfRectangle = 120.0;
        heightOfTriangle = 0;

        weight = 120.0 * 20.0 * 1; //Volumen * Dichte

    }

    public Shape getElementView(){ return elementView;}

    public Polygon getTriangle() {
        return triangle;
    }


    private void setIsSelectedColor() {
        if (getIsSelected()) {

            elementView.setStroke(Color.ORANGE);
            elementView.setStrokeWidth(3);

        } else elementView.setStroke(null);
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
        double minX = pivotX - getWidth()  / 2; // x-Wert links
        double minY = pivotY - getHeight() / 2; // y-Wert oben
        double maxX = pivotX + getWidth()  / 2; //x-Wert rechts
        double maxY = pivotY + getHeight() / 2; // y-Wert unten

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
        triangle.setTranslateX(pivotX);
        triangle.setTranslateY(pivotY);
        double c = Math.cos(Math.toRadians(getAngle()));
        double s = Math.sin(Math.toRadians(getAngle()));
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





    private void kippWinkel(){

        alpha = Math.atan( 2* heightOfTriangle / lengthOfRectangle);
    }


    private void kippGeschwindigkeit(){
        double v_0 = 0;
        double time = 1/60;
        double F_Kugel = 1; // F = m * a    Masse * Beschleunigung
        double m_Balken;
        double a = F_Kugel / weight;

        a = F_Kugel / (lengthOfRectangle/2 * heightOfRectangle * 1);

        double v_kipp = v_0 + a * time * alpha;
    }


}
