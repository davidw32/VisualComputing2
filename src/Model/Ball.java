package Model;


import Helpers.VectorMath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import static Helpers.Config.GRAVITY;

public class Ball extends GraphicsObject {

    private DoubleProperty radius;
    private boolean collision, frictionLock;
    private double frictionCoefficient = 0.1;
    private VectorMath calculator;

    boolean bounce = false;
    Text velocityText = new Text();

    public Ball(double _initXPosition, double _initYPosition) {

        super(_initXPosition, _initYPosition);
        radius = new SimpleDoubleProperty(this, "radius", 30);
        setWeight(10);
        setIsMoving(true);

        elementView = new Circle(getXPosition(), getYPosition(), radius(), Color.PLUM);
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

        calculator = new VectorMath();
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

    public void moveElement() {
        moveX();
        moveY();

    }


    private void setIsSelectedColor() {
        if (getIsSelected()) {

            elementView.setStroke(Color.ORANGE);
            elementView.setStrokeWidth(3);

        } else elementView.setStroke(null);
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
        velocityText.setX((x - radius()) / 2);


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

        velocityText.setY(y - ((Circle) elementView).getRadius() - 10);
        velocityText.setText(String.format("%.2f", Math.sqrt(Math.pow(getXVelocity(), 2) + Math.pow(getYVelocity(), 2))));
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
        //zwischenspeicher für die neuen Velocityvektoren
        double[] vneu;
        //der Abstand ist klein genug um sich zu treffen
        if (intersecting(ball2)) {
            // die Kugeln bewegen sich auf einander zu
            if (moveTowardsEachOther(ball2)) {
                // die Geschwindigkeitsvektoren v1 und v2 sind parallel zur Zentralrichtung
                if (moveParallel(ball2)) {
                    //berechne Zentraler elastischer Stoss
                    vneu = zentralerStoss(ball2.getXVelocity(), ball2.getYVelocity(), ball2.getWeight());
                    this.setXVelocity(vneu[0]);
                    this.setYVelocity(vneu[1]);
                    ball2.setXVelocity(vneu[2]);
                    ball2.setYVelocity(vneu[3]);

                } else {
                    // v1 und v2 nicht parallel, berechne dezentralen/realen Stoss
                    dezentralerStoss(ball2);
                }
            }
        }
    }

    /**
     * hier wird der Abstand zwischen dieser und der übergebenen Kugel bestimmt
     *
     * @param ball2  - die zweite Kugel
     * @return - true, falls die beiden Kugeln sich berühren.
     */

    private boolean intersecting(Ball ball2) {

        double tolerance = 5;
        // der Abstand zwichen den Mittelpunkte ist kleiner als sie Summe der Radien (+ einem Toleranzwert)
        if (calculator.computeDistance(this.getXPosition(), this.getYPosition(), ball2.getXPosition(),ball2.getYPosition())
                <= this.radius() + ball2.radius() + tolerance) {

            return true;
        }
        else return false;
    }

    /**
     * Diese Methode überprüft anhand der Positionen und der Geschwindikeiten, ob zwei Bälle aufeinander zu rollen
     * bisher in x-Richtung
     * @param ball2
     * @return - true falls die Bälle aufeinander zu rollen
     */

    private boolean moveTowardsEachOther(Ball ball2) {

        boolean returnBoolean=false;

        if(this.getXPosition()< ball2.getXPosition()){
            // dieser Ball befindet sich links von Ball2
            if(this.getXVelocity()>=0 && ball2.getXVelocity()<=0){
                // sie rollen auf einander zu (dieser Ball rollt nach rechts, ball2 nach links)
                returnBoolean = true;
            } else if ( this.getXVelocity()>ball2.getXVelocity() && ball2.getXVelocity()>=0){
                //beide Rollen nach rechts, dieser ist schneller
                returnBoolean = true;
            }
            else if ( this.getXVelocity()<ball2.getXVelocity()&& this.getXVelocity()<=0){
                //beide Rollen nach links, der andere ist schneller
                returnBoolean = true;
            }
        }

        else if (this.getXPosition()> ball2.getXPosition()){
            //dieser Ball liegt rechts von Ball2
            if(this.getXVelocity()<=0 && ball2.getXVelocity()>=0){
                // sie rollen auf einander zu (dieser Ball rollt nach links, ball2 nach rechts)
                returnBoolean = true;
            } else if ( this.getXVelocity()>ball2.getXVelocity() && this.getXVelocity()<=0){
                //beide Rollen nach links, dieser ist schneller
                returnBoolean = true;
            }
            else if ( this.getXVelocity()<ball2.getXVelocity()&& this.getXVelocity()>=0){
                //beide Rollen nach rechts, der andere ist schneller
                returnBoolean = true;
            }
        }
        return returnBoolean;
    }

    /**
     * Prüft ob die beiden Ball sich parallel zur Zentralrichtung bewegen
     * @param ball2
     * @return - true falls sie sich parallel zur Zentralrichtugn bewegen
     */
    private boolean moveParallel(Ball ball2) {

        double x_0 = this.getXPosition();
        double y_0 = this.getYPosition();
        double x_1 = ball2.getXPosition();
        double y_1 = ball2.getYPosition();

        if (calculator.areParallel(x_0, y_0, x_1-x_0, y_1-y_0)
                && calculator.areParallel(x_1, y_1, x_1-x_0, y_1-y_0)) return true;

        else return false;
    }

    /**
     * Berechnung gemäß der Formel für den zentralen elastischen Stoss
     *
     * @param vx_2 - x-Geschwindikeit des zweiten Objekts
     * @param vy_2 - y-Geschwindikeit des zweiten Objekts
     * @param weigth2 - Gewischt des zweiten Objekts
     * @return _ double[] mit vx1_neu, vy1_neu, vx2_neu, vx3_neu
     */
    private double[] zentralerStoss(double vx_2, double vy_2, double weigth2) {
        double[] vnew = {0,0,0,0};
        if (this.getWeight() == weigth2) {
            vnew[0] = vx_2;
            vnew[1] = vy_2;
            vnew[2] = this.getXVelocity();
            vnew[3] = this.getYVelocity();
        }
        else {
            double xFactor = (this.getWeight() * this.getXVelocity() + weigth2 * vx_2) /(this.getWeight()+weigth2);
            double yFactor = (this.getWeight() * this.getYVelocity() + weigth2 * vy_2) /(this.getWeight()+weigth2);

            vnew[0] = 2 * xFactor - this.getXVelocity();
            vnew[1] = 2 * yFactor - this.getYVelocity();
            vnew[2] = 2 * xFactor - vx_2;
            vnew[3] = 2 * yFactor - vy_2;
        }

        return vnew;
    }

    private void dezentralerStoss(Ball ball2) {
    }


/*
    private void elastischerStoss2D(Ball ball2) {
        // gemäß Formeln auf Wikipedia
        //bestimme v1_neu und v2_neu (Geschwindigkeiten von Ball1 und Ball2)

        //Steigungen der Zentralrichtung und der Berührtangente
        double s_z = calculator.directionCosine(ball2.getYPosition() - this.getYPosition(), ball2.getXPosition() - this.getXPosition());
        double s_t = 1 / (-1 * s_z);

        //Richtungskosinus von v1 bestimmen
        double s_v1 = calculator.directionCosine(this.getXVelocity(), this.getYVelocity());
        //Zerlegung von v1 in Komponente parallel zur Berührtangenten
        double xt_v1 = this.getXVelocity() * (s_z - s_v1) / (s_z - s_t);
        double yt_v1 = this.getXVelocity() * s_t;
        //Komponente parallel zur Zentralrichtung
        double xz_v1 = this.getXVelocity() * (s_t - s_v1) / (s_t - s_z);
        double yz_v1 = xz_v1 * s_z;

        //Richtungskosinus von v2 bestimmen
        double s_v2 = calculator.directionCosine(ball2.getXVelocity(), ball2.getYVelocity());
        //Zerlegung von v2 in Komponente parallel zur Berührtangenten
        double xt_v2 = ball2.getXVelocity() * (s_z - s_v2) / (s_z - s_t);
        double yt_v2 = ball2.getXVelocity() * s_t;
        //Komponente parallel zur Zentralrichtung
        double xz_v2 = ball2.getXVelocity() * (s_t - s_v2) / (s_t - s_z);
        double yz_v2 = xz_v2 * s_z;
        // für die Zentralkomponente den elastischen Stoß berechnen
        // falls die Kugeln gleichschwer sind, werden die Werte von v1 und v2 vertauscht
        if (this.getWeight() == ball2.getWeight()) {
            double tempX = xz_v1;
            double tempY = yz_v1;

            xz_v1 = xz_v2;
            yz_v1 = yz_v2;

            xz_v2 = tempX;
            yz_v2 = tempY;
        } else {
            //elastischen Stoß für die Zentralkomponente berechnen
            //v1_neu = 2 * (m_1*v_1 + m_2*v_2)/(m_1+m_2) - v_1
            //v2_neu = 2 * (m_1*v_1 + m_2*v_2)/(m_1+m_2) - v_2
            double xz_faktor = 2 * (this.getWeight() * xz_v1 + ball2.getWeight() * xz_v2) / (this.getWeight() + ball2.getWeight());
            double yz_faktor = 2 * (this.getWeight() * yz_v1 + ball2.getWeight() * yz_v2) / (this.getWeight() + ball2.getWeight());
            xz_v1 = xz_faktor - xz_v1;
            yz_v1 = yz_faktor - yz_v1;
            xz_v2 = xz_faktor - xz_v2;
            yz_v2 = yz_faktor - yz_v2;
        }

        // jetzt die neuen Geschwindigkeiten v1_neu und v2_neu aus vt und vz wieder zusammensetzen.
        this.setXVelocity(xt_v1 + xz_v1);
        this.setYVelocity(yt_v1 + yz_v1);
        ball2.setXVelocity(xt_v2 + xz_v2);
        ball2.setYVelocity(yt_v2 + yz_v2);
        // jetzt noch bei Ball2 einen Wert setzen, dass die Kollision schon bestimmt wurde!!!


    }


 */

}
