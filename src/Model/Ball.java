package Model;


import Helpers.VectorMath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.Locale;

import static Helpers.Config.GRAVITY;

public class Ball extends GraphicsObject {

    private DoubleProperty radius;
    private boolean collision, frictionLock;
    private double frictionCoefficient = 0.1;
    private VectorMath calculator;
    private Line directionLine;
    private double velocity;

    boolean bounce = false;
    private Text velocityText = new Text();

    public Ball(double _initXPosition, double _initYPosition) {

        super(_initXPosition, _initYPosition);
        radius = new SimpleDoubleProperty(this, "radius", 30.0);
        setWeight(10.0);
        setIsMoving(true);

        elementView = new Circle(getXPosition(), getYPosition(), radius(), Color.PLUM);
        elementView.setStrokeWidth(3);
        elementView.setStroke(Color.ORANGE);

        directionLine = new Line();
        directionLine.setStrokeWidth(3);
        directionLine.setStartX(getXPosition());
        directionLine.setStartY(getYPosition());
        //velocity = new SimpleDoubleProperty(this, "velocity");

        //hier werden die Properties des Objektes an die der Shape getackert
        ((Circle) elementView).centerXProperty().bindBidirectional(xPositionProperty());
        ((Circle) elementView).centerYProperty().bindBidirectional(yPositionProperty());
        ((Circle) elementView).radiusProperty().bindBidirectional(radiusProperty());

        directionLine.startXProperty().bindBidirectional(xPositionProperty());
        directionLine.startYProperty().bindBidirectional(yPositionProperty());
        directionLine.setEndX(getXPosition());
        directionLine.setEndY(getYPosition());


        elementView.scaleXProperty().bindBidirectional(xScaleProperty());
        elementView.scaleYProperty().bindBidirectional(yScaleProperty());
        elementView.visibleProperty().bindBidirectional(directionLine.visibleProperty());
        //hier ändert sich die Farbe wenn das Objekt angeklickt wird
        isSelectedProperty().addListener((observable, oldValue, newValue) -> {
            setIsSelectedColor();
        });

        xVelocityProperty().addListener((observable -> {
            setVelocity();
            updateDirectionLine();
        }));
        yVelocityProperty().addListener((observable -> {
            setVelocity();
            updateDirectionLine();
        }));

        // ein Ball kann nur proportional skaliert werden.
        xScaleProperty().bindBidirectional(yScaleProperty());

        calculator = new VectorMath();
        velocity = calculator.vectorLength(getXVelocity(), getYVelocity());
        velocityText.setText(String.format(Locale.US, "%.2f",velocity));
        velocityText.setX(getXPosition() );
        velocityText.setY(getYPosition()-radius() - 10);

    }

    public DoubleProperty radiusProperty() {
        return radius;
    }

    public final void setRadius(double _radius) {
        this.radius.set(_radius);
    }

    public final double radius() {
        return this.radius.get();
    }

    public Text getVelocityText(){
        return velocityText;
    }

    //Die Länge des Geschwindigkeitsvektors
    public final void setVelocity() {
        this.velocity = calculator.vectorLength(getXVelocity(), getYVelocity());
    }

    public final double getVelocity() {
        return this.velocity;
    }

    public Line getDirectionLine() {
        return directionLine;
    }

    //wenn der Ball zurückgesetz wird
    public void resetDirectionLine() {
        directionLine.setEndX(getXPosition());
        directionLine.setEndY(getYPosition());
        velocityText.setText(String.format(Locale.US,"%.2f",velocity));
        velocityText.setX(getXPosition());
        velocityText.setY(getYPosition()-radius() - 10);
    }

    //wenn sich die Geschwindigkeit ändert
    public void updateDirectionLine() {
        velocityText.setText(String.format(Locale.US, "%.2f",velocity));
        velocityText.setX(getXPosition());
        velocityText.setY(getYPosition()-radius() - 10);
        if (getVelocity() == 0) {
            directionLine.setEndX(getXPosition());
            directionLine.setEndY(getYPosition());

        } else {
            directionLine.setEndX(getXPosition() + radius() * getXVelocity() / getVelocity());
            directionLine.setEndY(getYPosition() + radius() * getYVelocity() / getVelocity());
        }
        //System.out.println("startX: "+directionLine.getStartX()+" startY: "+directionLine.getStartY()+" endX: "+directionLine.getEndX()+ " endY: "+ directionLine.getEndY());
    }


    public void moveElement() {
        moveX();
        moveY();

    }


    private void setIsSelectedColor() {
        if (getIsSelected()) {

            elementView.setStroke(Color.ORANGE);
            elementView.setStrokeWidth(3);
            velocityText.setVisible(true);

        } else {
            elementView.setStroke(null);
            velocityText.setVisible(false);
        }
    }


    public void collisionDetection(Line[] lines) {
        collision = false;
        bounce = false;

        double x = getXPosition();
        double y = getYPosition();

        for (Line line : lines) {
            // x = Px + t*Rx   y = Py + t*Ry   y = m*x + b
            double linePx = line.getStartX();
            double linePy = line.getStartY();
            double lineRx = line.getEndX() - line.getStartX();
            double lineRy = line.getEndY() - line.getStartY();

            double lineM = lineRy / lineRx;
            double lineB = linePy + (-linePx / lineRx) * lineRy;
            double abstand;
            double schnittpunktX;
            double schnittpunktY;

            if (lineRy == 0) {
                abstand = Math.abs(linePy - y) - radius();
                schnittpunktX = x;
                schnittpunktY = linePy;
            } else if (lineRx == 0) {
                abstand = Math.abs(linePx - x) - radius();
                schnittpunktX = linePx;
                schnittpunktY = y;
            } else {

                // Normalenvektor der Linie
                double nX = lineRy;
                double nY = -lineRx;

                double ballLineM = nY / nX;
                double ballLineB = y + (-x / nX) * nY;

                schnittpunktX = (ballLineB - lineB) / (lineM - ballLineM);
                schnittpunktY = ballLineM * schnittpunktX + ballLineB;

                abstand = Math.sqrt(Math.pow(x - schnittpunktX, 2) + Math.pow(y - schnittpunktY, 2)) - radius();

            }

            if (abstand < 1 && line.getStartX() <= x && line.getEndX() >= x) {
                //System.out.println("Kollision " +line.getEndX() +"  Radius: "+ x);

                collision = true;
                double angleNew = detectAngle(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());

                if (angleNew == 0) {
                    if (Math.abs(getYVelocity() * time) > 0.5) {
                        bounce = true;
                    } else {
                        frictionLock = false;
                        setYVelocity(0);
                        setYAcceleration(0);
                    }
                } else {
                    if (getAngle() != angleNew) {
                        setYVelocity(0);
                        setYVelocity(0);
                        setXAcceleration(0);
                        setYAcceleration(0);
                    }
                }
                setAngle(angleNew);

            }
        }
    }


    /**
     * Berechnungen der X-Achse
     */
    public void moveX() {
        //Reibung [m/s^2]
        double reibung = 0;

        double x = getXPosition();

        if (collision) {
            // Reibungsberechnung bei gerade Ebene
            if (getAngle() == 0) {
                if (getXVelocity() == 0) {
                    //System.out.println("HAFTREIBUNG");
                    frictionLock = haftReibung(getWeight(), GRAVITY, frictionCoefficient);
                } else if (!frictionLock) {
                    //System.out.println("GLEITREIBUNG AB HIER");
                    // Reibung gerade Ebene [m/s^2]
                    reibung += gleitReibung(getWeight(), GRAVITY, frictionCoefficient);
                }
            } else {   // Reibung auf schiefer Ebene [m/s^2]
                reibung += frictionX(getWeight(), GRAVITY, frictionCoefficient, getAngle());
            }
            //System.out.println("FORCE:  " + reibung);
        }

        // [m/s^2] Beschleunigungen werden hier summiert
        double accelerationSum = (getXAcceleration() + reibung);

        if (x + radius() >= 1130 && getXVelocity() > 0) {
            setXVelocity(-1 * getXVelocity());

        } else if (x - radius() <= 0 && getXVelocity() < 0) {
            setXVelocity(-1 * getXVelocity());
        }


        // [m] s = s0 + v * t + 1/2 * a * t^2
        setXPosition(getXPosition() + getXVelocity() * time + 0.5f * accelerationSum * Math.pow(time, 2));
        // [m/s] v = v0 + a * t
        setXVelocity(getXVelocity() + accelerationSum * time);
        //velocityText.setX((x - radius()) / 2);


    }

    /**
     * Berechnungen der Y-Achse
     */
    public void moveY() {
        // //[m/s^2] Reibung
        double y = getYPosition();
        double force = frictionY(getWeight(), GRAVITY, frictionCoefficient, getAngle());

        //[m/s^2]
        double accelerationSum = 0;
        if (!collision) {

            setYVelocity(getYVelocity() + GRAVITY * time);
            setYAcceleration(GRAVITY);
            accelerationSum = getYAcceleration();
        } else {
            accelerationSum = (getYAcceleration() + force);
        }

        if (bounce && getYVelocity() > 0) {
            setYVelocity(-1 * getYVelocity() * 0.6);
            bounce = false;
        }

        //[m] s = s0 + v * t + 1/2 * a * t^2
        setYPosition(getYPosition() + getYVelocity() * time + 0.5 * accelerationSum * Math.pow(time, 2));
        //[m/s] Geschwindigkeit v = v0 + a * t
        setYVelocity(getYVelocity() + accelerationSum * time);

        //velocityText.setY(y - ((Circle) elementView).getRadius() - 10);
        //velocityText.setText(String.format("%.2f", Math.sqrt(Math.pow(getXVelocity(), 2) + Math.pow(getYVelocity(), 2))));
    }

    /**
     * Haftreibung
     *
     * @param m            Gewicht
     * @param g            Gravität
     * @param frictionCoff Haftreibungskoeffizient
     * @return true, falls Geschwindigkeit nicht maximale Haftreibung ueberschreitet. False,falls diese uberschritten wird.
     */
    public boolean haftReibung(double m, double g, double frictionCoff) {
        //Kraft geschwindigkeit
        // [m/s^2]  a = v / t
        double VN = getXVelocity() / time;
        // [N] F = m * a
        VN = getWeight() * VN;
        //[N] Normalkraft
        double FN = m * g;
        // [N] Haftkraft bzw. Gravitation
        double FR = FN * frictionCoff;
        //System.out.println("HAFT:   " +FR);
        //System.out.println("SPEED:  "+ VN);

        if (Math.abs(VN) < Math.abs(FR) && VN != 0) {
            //System.out.println(" HAFT TRUE");
            return true;
        }

        //System.out.println(" HAFT FALSE");
        frictionLock = false;
        return false;
    }

    /**
     * Wird aktiv,wenn Haftreibung ueberschritten wird
     *
     * @param m            Gewicht
     * @param g            Gravitation
     * @param frictionCoff Gleitreibungskoeffizient
     * @return
     */
    public double gleitReibung(double m, double g, double frictionCoff) {
        //Kraft der Geschwindigkeit (VelocityX)
        // [m/s^2] a = v / t
        double VN = getXVelocity() / time;
        // [N] F = m * a
        VN = getWeight() * VN;

        // [m/s^2]
        double FN = m * g;
        // Reibung
        double FR = FN * frictionCoff;


        //Es kommt zum Stillstand,sobald Geschwindigkeit < 0.25 und Beschleunigung < 0.25f
        if (Math.abs(getXVelocity()) < 2.5 && Math.abs(getXAcceleration()) < 2.5) {
            setXVelocity(0);
            setXAcceleration(0);
            frictionLock = true;
            //System.out.println("GLEIT FALSE");
            return 0;
        }

        //System.out.println("GLEIT TRUE");
        if (getXVelocity() < 0) {
            // [m/s^2]
            double acceleration = FR / getWeight();
            return acceleration;
        } else if (getXVelocity() > 0) {
            // [m/s^2]
            double acceleration = FR / getWeight();
            return -acceleration;
        }
        return 0;
    }


    /**
     * Reibung an einer Schiefen Ebene ( X-Achse)
     *
     * @param m            Gewicht (in KG)
     * @param g            Gravität
     * @param frictionCoff Reibungskoeffizient
     * @param angle        Winkel
     * @return Beschleunigung der Reibungskraft
     * Formeln:
     * Gewichtskraft: FG = masse * gravitation
     * Hanganbtriebskraft: FH = FG * sin(a)  (a = angle)
     * Normalkraft: FN = FG * cos(a)
     * F = m * a umgeformt zu a = f/m , dann ausgegeben
     */
    public double frictionX(double m, double g, double frictionCoff, double angle) {
        //[m/s^2]
        double FG = getWeight() * g;
        // [N]
        double FH = (FG * Math.sin(Math.toRadians(angle)));
        // FR = frictionCoff  * FN
        // [N]
        double FN = (frictionCoff * (FG * Math.cos(Math.toRadians(angle))));

        // [N]
        double FHx = (FH * Math.cos(Math.toRadians(angle)));
        double FNx = (FN * Math.cos(Math.toRadians(angle)));

        // [N]
        //System.out.println(FHx+" "+FNx);
        double friction = FHx + FNx;
        if (FHx < 0) {
            friction = FHx - FNx;
        }
        //if(FH + FN <= FG * frictionCoff) return 0;

        // [m/s^2]  F = m * a umgeformt zu:  a = F / m
        double acceleration = friction / getWeight();
        return acceleration;
    }

    /**
     * Reibung (als Beschleunigung ausgegebene)
     *
     * @param m            Gewicht (in KG)
     * @param g            Gravität
     * @param frictionCoff Reibungskoeffizient
     * @param angle        Winkel
     * @return Beschleunigung der Reibungskraft
     * Formeln:
     * Gewichtskraft: FG = masse * gravitation
     * Hanganbtriebskraft: FH = FG * sin(a)  (a = angle)
     * Normalkraft: FN = FG * cos(a)
     * F = m * a umgeformt zu a = f/m , dann ausgegeben
     */
    public double frictionY(double m, double g, double frictionCoff, double angle) {
        //[m/s^2]
        double FG = m * g;
        // [N]
        double FH = ((FG * Math.sin(Math.toRadians(angle))));
        // [N]
        double FN = (frictionCoff * (FG * Math.cos(Math.toRadians(angle))));

        // [N]
        double FHy = (FH * Math.sin(Math.toRadians(angle)));
        double FNy = (FN * Math.sin(Math.toRadians(angle)));

        // [N]
        double friction = FHy + FNy;
        if (FNy < 0) {
            friction = FHy - FNy;
        }

        // [m/s^2]  F = m * a umgeformt zu:  a = F / m
        double acceleration = friction / getWeight();
        return acceleration;
    }

    /**
     * Ermitteln eines Winkel einer Geraden
     *
     * @param x1 x Startwert der Gerade
     * @param y1 y Startwert der Gerade
     * @param x2 x Endwert der Gerade
     * @param y2 y Endwert der Gerade
     * @return Winkel der Geraden ( in Grad)
     */
    public double detectAngle(double x1, double y1, double x2, double y2) {
        if (y1 == y2) return 0;
        /*x1 = Math.abs(x1);
        x2 = Math.abs(x2);
        y1 = y1;
        y2 = y2;*/
         /*
        double x = Math.sqrt(Math.pow(x2-x1,2));
        double y = Math.sqrt(Math.pow(y2-y1,2));
        */

        double x = x2 - x1;
        double y = y2 - y1;
        //System.out.println("W: "+Math.toDegrees(Math.atan(y/x)));
        if (Math.toDegrees(Math.atan(y / x)) < 0) {
            return Math.toDegrees(Math.atan(y / x)) + 360;
        }
        // [°] Grad
        return Math.toDegrees(Math.atan(y / x));
    }


    /**
     * Hier wird geprüft, ob die Kugel mit der zweiten zusammenstößt
     * falls ja, werden die Geschwindikeitsvektoren beider Kugeln neu berechnet
     *
     * @param ball2 - die zweite Kugel
     */
    public void calculateCollisionWithBall(Ball ball2) {
        //System.out.println("Ballkollsion wird geprüft");
        //zwischenspeicher für die neuen Velocityvektoren
        double[] vneu;

        //der Abstand ist klein genug um sich zu treffen
        if (intersecting(ball2)) {
           // System.out.println("Kugeln treffen sich");

            //diese Kugel rollt nach rechts oder links
            if (this.getXVelocity() != 0 && Math.abs(this.getYVelocity()) <= 100) {

                //diese Kugel befindet sich links von der anderen Kugel
                if (this.getXPosition() <= ball2.getXPosition()) {
                    //Ball2 ruht und dieser Ball rollt daruf zu
                    if (ball2.getXVelocity() == 0 && ball2.getYVelocity() == 0 && this.getXVelocity() > 0) {
                        //berechne die neuen Geschwindigkeiten
                        vneu = zentralerStoss(this.getXVelocity(), this.getYVelocity(), this.getWeight(), ball2.getXVelocity(), ball2.getYVelocity(), ball2.getWeight());
                        //setze die berechneten neuen Geschwindigkeiten ein
                        this.setXVelocity(vneu[0]);
                        this.setYVelocity(vneu[1]);
                        ball2.setXVelocity(vneu[2]);
                        ball2.setYVelocity(vneu[3]);
                    }
                    //Ball2 rollt nach rechts und dieser Ball auch, dieser ist schneller
                    else if (ball2.getXVelocity() > 0 && ball2.getXVelocity() < this.getXVelocity()) {
                        vneu = zentralerStoss(this.getXVelocity(), this.getYVelocity(), this.getWeight(), ball2.getXVelocity(), ball2.getYVelocity(), ball2.getWeight());
                        this.setXVelocity(vneu[0]);
                        this.setYVelocity(vneu[1]);
                        ball2.setXVelocity(vneu[2]);
                        ball2.setYVelocity(vneu[3]);
                    }

                }
                //andersherum
                else if (this.getXPosition() >= ball2.getXPosition()) {
                    if (ball2.getXVelocity() == 0 && ball2.getYVelocity() == 0 && this.getXVelocity() < 0) {
                        vneu = zentralerStoss(this.getXVelocity(), this.getYVelocity(), this.getWeight(), ball2.getXVelocity(), ball2.getYVelocity(), ball2.getWeight());
                        this.setXVelocity(vneu[0]);
                        this.setYVelocity(vneu[1]);
                        ball2.setXVelocity(vneu[2]);
                        ball2.setYVelocity(vneu[3]);
                    } else if (ball2.getXVelocity() > 0 && ball2.getXVelocity() > this.getXVelocity()) {
                        vneu = zentralerStoss(this.getXVelocity(), this.getYVelocity(), this.getWeight(), ball2.getXVelocity(), ball2.getYVelocity(), ball2.getWeight());
                        this.setXVelocity(vneu[0]);
                        this.setYVelocity(vneu[1]);
                        ball2.setXVelocity(vneu[2]);
                        ball2.setYVelocity(vneu[3]);
                    }

                }
            } else {
                // ansonsten berechne den schiefen elastischen Stoß

                if (this.getXPosition() <= ball2.getXPosition()) {
                    //Differenzvektor der Mittelpunkte
                    double deltaX = ball2.getXPosition() - this.getXPosition();
                    double deltaY = ball2.getYPosition() - this.getYPosition();
                    System.out.println("Schiefer Stoß");
                    double[] v1_z = calculator.parallelProjection(this.getXVelocity(), this.getYVelocity(), deltaX, deltaY);
                    double[] v2_z = calculator.parallelProjection(ball2.getXVelocity(), ball2.getYVelocity(), deltaX, deltaY);

                    if (v1_z != new double[]{0, 0} && v2_z != new double[]{0, 0}) {
                        vneu = zentralerStoss(v1_z[0], v1_z[1], this.getWeight(), v2_z[0], v2_z[1], ball2.getWeight());

                        this.setXVelocity(vneu[0] + this.getXVelocity() - v1_z[0]);
                        this.setYVelocity(vneu[1] + this.getYVelocity() - v1_z[1]);
                        ball2.setXVelocity(vneu[2] + ball2.getXVelocity() - v2_z[0]);
                        ball2.setYVelocity(vneu[3] + ball2.getYVelocity() - v2_z[1]);
                    }


                } else if (this.getXPosition() >= ball2.getXPosition()) {
                    //Differenzvektor der Mittelpunkte
                    double deltaX = this.getXPosition() - ball2.getXPosition();
                    double deltaY = this.getYPosition() - ball2.getYPosition();
                    System.out.println("Schiefer Stoß");
                    double[] v1_z = calculator.parallelProjection(this.getXVelocity(), this.getYVelocity(), deltaX, deltaY);
                    double[] v2_z = calculator.parallelProjection(ball2.getXVelocity(), ball2.getYVelocity(), deltaX, deltaY);

                    if (v1_z != new double[]{0, 0} && v2_z != new double[]{0, 0}) {
                        vneu = zentralerStoss(v1_z[0], v1_z[1], this.getWeight(), v2_z[0], v2_z[1], ball2.getWeight());

                        this.setXVelocity(vneu[0] + this.getXVelocity() - v1_z[0]);
                        this.setYVelocity(vneu[1] + this.getYVelocity() - v1_z[1]);
                        ball2.setXVelocity(vneu[2] + ball2.getXVelocity() - v2_z[0]);
                        ball2.setYVelocity(vneu[3] + ball2.getYVelocity() - v2_z[1]);
                    }


                }


            }



        }
    }

    /**
     * hier wird der Abstand zwischen dieser und der übergebenen Kugel bestimmt
     *
     * @param ball2 - die zweite Kugel
     * @return - true, falls die beiden Kugeln sich berühren.
     */

    private boolean intersecting(Ball ball2) {

        double tolerance = 5;
        // der Abstand zwichen den Mittelpunkte ist kleiner als sie Summe der Radien (+ einem Toleranzwert)
        if (calculator.computeDistance(this.getXPosition(), this.getYPosition(), ball2.getXPosition(), ball2.getYPosition())
                <= this.radius() + ball2.radius() + tolerance) {

            return true;
        } else return false;
    }

    /**
     * Berechnung gemäß der Formel für den zentralen elastischen Stoss
     *
     * @param vx_2    - x-Geschwindikeit des zweiten Objekts
     * @param vy_2    - y-Geschwindikeit des zweiten Objekts
     * @param weigth2 - Gewischt des zweiten Objekts
     * @return _ double[] mit vx1_neu, vy1_neu, vx2_neu, vx3_neu
     */
    private double[] zentralerStoss(double vx_1, double vy_1, double weigth1, double vx_2, double vy_2, double weigth2) {
        System.out.println("zentrlarer Stoss wird berechnet");
        double[] vnew = {0, 0, 0, 0};
        if (weigth1 == weigth2) {
            vnew[0] = vx_2;
            vnew[1] = vy_2;
            vnew[2] = vx_1;
            vnew[3] = vy_1;
        } else {
            double xFactor = (weigth1 * vx_1 + weigth2 * vx_2) / (weigth1 + weigth2);
            double yFactor = (weigth1 * vy_1 + weigth2 * vy_2) / (weigth1 + weigth2);

            vnew[0] = 2 * xFactor - vx_1;
            vnew[1] = 2 * yFactor - vy_2;
            vnew[2] = 2 * xFactor - vx_2;
            vnew[3] = 2 * yFactor - vy_2;
        }

        return vnew;
    }


}
