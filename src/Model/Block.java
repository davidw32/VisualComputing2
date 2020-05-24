package Model;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Block extends GraphicsObject{


    double[] left_down = new double[2];
    double[] right_down = new double[2];
    double[] up_left = new double[2];
    double[] up_right = new double[2];


    public Block(double x,double y)
    {
        super(x,y);
    }
    /**
     * Kollisionshülle bzw Boundingbox wird hier erstellt
     */
    public void createHull(double angle)
    {
        double width = ((Rectangle)(elementView)).getWidth();
        double height = ((Rectangle)(elementView)).getHeight();
        double scaleX = ((Rectangle)(elementView)).getScaleX();
        double scaleY = ((Rectangle)(elementView)).getScaleY();
        double x = ((Rectangle)(elementView)).getTranslateX();
        double y = ((Rectangle)(elementView)).getTranslateY();

        left_down[0] = elementView.getTranslateX() + (width * scaleX) + rotationX(angle,x,y);
        left_down[1] = elementView.getTranslateY() + (height * scaleY) + rotationY(angle,x,y);

        right_down[0] = elementView.getTranslateX() + (width * scaleX) + rotationX(angle,x,y);
        right_down[1] = elementView.getTranslateY() + (height * scaleY) + rotationY(angle,x,y);


        left_down[0] = elementView.getTranslateX() + (width * scaleX) + rotationX(angle,x,y);
        left_down[1] = elementView.getTranslateY() + (height * scaleY) + rotationY(angle,x,y);


        up_left[0] = elementView.getTranslateX() + (width * scaleX) + rotationX(angle,x,y);
        up_left[1] = elementView.getTranslateY() + (height * scaleY) + rotationY(angle,x,y);

    }

    public void drawHull(Pane pane)
    {
        Line down = new Line(left_down[0],left_down[1],right_down[0],right_down[1]);
        Line right = new Line(right_down[0],right_down[1],up_right[0],up_right[1]);
        Line left = new Line(left_down[0],left_down[1],up_left[0],up_left[1]);
        Line up = new Line(up_left[0],up_left[1],up_right[0],up_right[1]);

        pane.getChildren().add(down);
        pane.getChildren().add(right);
        pane.getChildren().add(left);
        pane.getChildren().add(up);

        down.setStrokeWidth(5);
        left.setStrokeWidth(5);
        right.setStrokeWidth(5);
        up.setStrokeWidth(5);
    }


    @Override
    protected void moveY() {

    }

    @Override
    protected void moveX() {

    }

    @Override
    protected void moveElement() {

    }

    private double rotationX(double angle, double x, double y)
    {
        return  x * Math.cos(Math.toRadians(angle)) + y * -Math.sin(Math.toRadians(angle));
    }

    private double rotationY(double angle,double x,double y)
    {
        return x * Math.sin(Math.toRadians(angle)) + y * Math.cos(Math.toRadians(angle));
    }



}
