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
    private Line directionLine;
    private double velocity;

    private boolean bounce = false;
    private boolean bounced = false;
    private double contactAngle = 0;
    private double bounceDirectionX = 0;
    private double bounceDirectionY = 0;
    private boolean slowed = false;
    private double bounceVelocity = 0;
    Text velocityText = new Text();

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
    }

    //wenn sich die Geschwindigkeit ändert
    public void updateDirectionLine() {

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
        //moveX();
        //moveY();
        move();

    }


    private void setIsSelectedColor() {
        if (getIsSelected()) {

            elementView.setStroke(Color.ORANGE);
            elementView.setStrokeWidth(3);

        } else elementView.setStroke(null);
    }

    /**
     * Berechnet die Kollision mit den Linien der GraphicScene
     * @param lines
     */
    public void collisionDetection(Line[] lines){

        for(Line line : lines){
            // x = Px + t*Rx   y = Py + t*Ry   y = m*x + b (lineare Funktion)
            // Umformung der Parameterform der Geraden in eine lineare Funktion
            double linePx = line.getStartX();
            double linePy = line.getStartY();
            double lineRx = line.getEndX() - line.getStartX();
            double lineRy = line.getEndY() - line.getStartY();

            double lineM = lineRy / lineRx;
            double lineB = linePy + (- linePx / lineRx)*lineRy;
            double abstand;
            double schnittpunktX;
            double schnittpunktY;
            // Normalenvektor der Linie
            double nX = lineRy;
            double nY = -lineRx;

            if(lineRy == 0){ // Fallunterscheidung wenn es eine horizontale Linie ist
                abstand = Math.abs(linePy - getYPosition()) - radius()*getXScale();
                schnittpunktX = getXPosition();
                schnittpunktY = linePy;
            }
            else if(lineRx == 0){ // Fallunterscheidung wenn es eine vertikale Linie ist
                abstand = Math.abs(linePx - getYPosition()) - radius()*getXScale();
                schnittpunktX = linePx;
                schnittpunktY = getYPosition();
            }
            else {
                double ballLineM = nY / nX;
                double ballLineB = getYPosition() + (-getXPosition() / nX) * nY;

                schnittpunktX = (ballLineB - lineB) / (lineM - ballLineM);
                schnittpunktY = ballLineM * schnittpunktX + ballLineB;

                // Abstand zwischen dem Mittelpunkt des Ball und dem Schnittpunkt - den Radius des Balls
                abstand = Math.sqrt(Math.pow(getXPosition() - schnittpunktX, 2) + Math.pow(getXPosition() - schnittpunktY, 2)) - radius()*getXScale();

            }

            //Bestimmung des linken und rechten Punktes der Linie im Koordinatensystem
            double leftX = linePx;
            double rightX = linePx + lineRx;
            if(lineRx < 0){
                leftX = line.getEndX();
                rightX = leftX - lineRx;
            }
            double topY = linePy;
            double bottomY = linePy + lineRy;
            if(lineRy < 0){
                topY = line.getEndY();
                bottomY = topY - lineRy;
            }


            // bestimmt ob sich der Schnittpunkt zwischen dem Start- und Endpunkt der Linie befindet
            boolean onLine = leftX <= schnittpunktX && rightX >= schnittpunktX && topY <= schnittpunktY && bottomY >= schnittpunktY;

            if(abstand < 0.5 && onLine){ // wenn es kollidiert
                collision = true;

                // Bestimmt ob sich der Ball überhauft auf die Linie zu bewegt
                boolean contact = (nX * getXVelocity() * time + nY * getYVelocity() * time) < 0;

                //Winkel zwischen dem Bewegungsvektor und dem Normalenvektor der Linie
                double directionAngle = Math.acos(((-getXVelocity())*nX+(-getYVelocity())*nY)/(Math.sqrt(Math.pow(getXVelocity(),2)+Math.pow(getYVelocity(),2))*Math.sqrt(Math.pow(nX,2)+Math.pow(nY,2))))*(180/Math.PI);

                // Winkel der Ebene
                double angleNew = detectAngle(line.getStartX(),line.getStartY(),line.getEndX(),line.getEndY());

                if(contactAngle!= angleNew){ // wenn sich der erkannte Winkel ändert wird das Springen wieder ermöglicht
                    bounced = false;
                }

                if(angleNew == 0){ // bei einer horizontalen Linie
                    if(Math.abs(getYVelocity()*time) > 2 && contact) { // wenn sich der Ball schnell genug auf die Linie zubewegt soll gesprungen werden
                        bounce = true;
                    }
                    else {
                        if(contact) { // sonst soll sich der Ball entlang der Linie bewegen
                            bounced = true;
                            bounce = false;
                        }
                    }
                }
                else{ // bei der schiefen Ebene
                    if(Math.sqrt(Math.pow(getYVelocity()*time,2)+Math.pow(getXVelocity()*time,2)) > 5 && contact) { // wenn sich der Ball schnell genug auf die Linie zubewegt soll gesprungen werden
                        if (!bounced) { // wenn der Ball noch nicht auf der Linie entlang rollt
                            bounce = true;

                            // Berechnungen des Ausfallwinkels
                            double cos = Math.cos(Math.toRadians(-directionAngle * 2));
                            double sin = Math.sin(Math.toRadians(-directionAngle * 2));

                            if(getXVelocity() > 0 && nY < 0 || getXVelocity() < 0 && nY > 0){
                                cos = Math.cos(Math.toRadians(directionAngle * 2));
                                sin = Math.sin(Math.toRadians(directionAngle * 2));
                            }
                            if(getXVelocity() == 0){
                                if(angleNew < 180){
                                    cos = Math.cos(Math.toRadians(directionAngle * 2));
                                    sin = Math.sin(Math.toRadians(directionAngle * 2));
                                }
                            }

                            // Berechnung des neuen Richtungsvektors
                            bounceDirectionX = (getXVelocity() * cos - getYVelocity() * sin)*(-1);
                            bounceDirectionY = (getXVelocity() * sin + (getYVelocity()) * cos);

                            // Normierung des Richtungsvektors
                            double bounceDirection = Math.sqrt(Math.pow(bounceDirectionX,2)+Math.pow(bounceDirectionY,2));
                            bounceDirectionX = 1/bounceDirection * bounceDirectionX;
                            bounceDirectionY = 1/ bounceDirection * bounceDirectionY;
                            bounceVelocity = Math.sqrt(Math.pow(getXVelocity()*0.6, 2) + Math.pow(getYVelocity()*0.6, 2));
                        }
                    }
                    else if (contact){ // sonst soll der Ball auf der Ebene entlang rollen
                        if(!bounced) {
                            bounced = true;
                        }
                    }
                }
                contactAngle = angleNew;

            }
        }
    }

    /**
     * Berechnungen der Bewegungen
     */
    public void move(){

        if(bounce) { // wenn der Ball vom Aufprall springen soll
            if (contactAngle != 0) {
                setXVelocity(bounceDirectionX * bounceVelocity);
            }
        }

        if(bounced){ // wenn der Ball an einer Ebene entlang rollt ohne zu springen
            calcAcceleration(contactAngle);
        }
        if (getXPosition() + radius() * getXScale() >= 1150 && getXVelocity() > 0) { // Kollision mit rechtem Szenen-Rand kehrt die x-Geschwindigkeit um
            setXVelocity(-1 * getXVelocity());

        } else if (getXPosition() - radius() * getXScale() <= 0 && getXVelocity() < 0) { // Kollision mit linken Szenen-Rand kehrt die x-Geschwindigkeit um
            setXVelocity(-1 * getXVelocity());
        }
        // x
        // [m] s = s0 + v * t + 1/2 * a * t^2
        setXPosition(getXPosition() + getXVelocity() * time + 0.5f * getXAcceleration() * Math.pow(time,2));
        // [m/s] v = v0 + a * t
        setXVelocity(getXVelocity()+getXAcceleration()*time);
        velocityText.setX((getXPosition() -radius())/2);


        if(!collision){ // wenn der Ball im freien Fall ist
            setYAcceleration(GRAVITY);
            setXAcceleration(0);
            bounced = false;
        }
        if(bounce) { // wenn der Ball vom Aufprall springen soll
            if(contactAngle != 0) {
                setYVelocity(-1*bounceDirectionY * bounceVelocity);
            }
            else {
                setYVelocity(-1*getYVelocity() * 0.6);
            }
            bounce = false;
        }
        // y
        //[m] s = s0 + v * t + 1/2 * a * t^2
        setYPosition( getYPosition() + getYVelocity() * time + 0.5 * getYAcceleration() * Math.pow(time,2));
        //[m/s] Geschwindigkeit v = v0 + a * t
        setYVelocity(getYVelocity() +getYAcceleration()*time);

        velocityText.setY(getYPosition() - ((Circle) elementView).getRadius()-10);
        velocityText.setText(String.format("%.2f",Math.sqrt(Math.pow(getXVelocity(),2)+Math.pow(getYVelocity(),2))));

        collision = false;

    }

    /**
     * Berechnet die Beschleunigung und Reibung abhängig von dem Winkel der Ebene
     * @param angle Winkel der Ebene
     */
    public void calcAcceleration(double angle){
        //[m/s^2]
        double FG = getWeight() * GRAVITY;
        // [N]
        double FH =  ( FG * Math.sin( Math.toRadians(angle) ) );
        // FR = frictionCoff  * FN
        // [N]
        double FN =  ( frictionCoefficient *( FG * Math.cos(Math.toRadians(angle)  )  )  );

        // [N]
        double friction = FH + FN;
        if(FH < 0){
            friction = FH - FN;
        }
        // [m/s^2]  F = m * a umgeformt zu:  a = F / m
        double acceleration = friction / getWeight();
        setXAcceleration(acceleration * Math.cos(Math.toRadians(angle)));
        setYAcceleration(acceleration * Math.sin(Math.toRadians(angle)));



        if(angle == 0) { // beim Rollen auf einer horizontalen Ebene
            setYVelocity(0);
            setYAcceleration(0);
            if(getXVelocity() > 0) {
                setXAcceleration(-1 * getXAcceleration());
            }
            //Es kommt zum Stillstand,sobald Geschwindigkeit < 0.25 und Beschleunigung < 0.25f
            if(Math.abs(getXVelocity()) < 2.5)
            {
                setXVelocity(0);
                setXAcceleration(0);
            }
        }

        double[] direction;
        if(angle > 180){
            direction = calculator.rotateCCW(-1,0, angle); // berechnet die Richtung in die der Ball rollen muss
            if(getXVelocity() > 0){
                direction[0] = -1 * direction[0];
                direction[1] = -1 * direction[1];
                if(!slowed){
                    setXVelocity(getXVelocity()/2);
                    setYVelocity(getYVelocity()/2);
                    slowed = true;
                }
            }
            else{
                slowed = false;
            }
        }
        else {
            direction = calculator.rotateCCW(1, 0, angle); // berechnet die Richtung in die der Ball rollen muss
            if(getXVelocity() < 0 && angle != 0) {
                direction[0] = -1 * direction[0];
                direction[1] = -1 * direction[1];
                if(!slowed){
                    setXVelocity(getXVelocity()/2);
                    setYVelocity(getYVelocity()/2);
                    slowed = true;
                }
            }
            else{
                slowed = false;
            }
        }
        if (getXVelocity() < 0 && angle == 0) {
            direction = calculator.rotateCCW(1, 0, 180); // berechnet die Richtung in die der Ball rollen muss
        }

        double velocity = calculator.vectorLength(getXVelocity(), getYVelocity());
        setXVelocity(direction[0] * velocity); // die Geschwindigkeiten werden entsprechend des Richtungsvektors gesetzt
        setYVelocity(direction[1] * velocity);


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

    // das Element wird auf die Startwerte zurückgesetzt
    @Override
    public void resetElement(){
        super.resetElement();
        this.bounced = false;
        this.collision = false;
        this.bounce = false;
        this.slowed = false;
    }


}
