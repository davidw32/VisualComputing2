package Model;


import Helpers.VectorMath;
import javafx.scene.effect.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

import java.util.Locale;

import static Helpers.Config.GRAVITY;
import static Helpers.Frictions.*;

public class Ball extends GraphicsObject {

    // private DoubleProperty radius;
    private boolean collision, frictionLock;
    private double frictionCoefficient = 0.1;
    private VectorMath calculator;
    private Line directionLine;
    private double velocity;

    private boolean bounce = false;
    private boolean bounced = false;
    private String collisionMaterial = "Rubber";
    private double contactAngle = 0;
    private double bounceDirectionX = 0;
    private double bounceDirectionY = 0;
    private boolean slowed = false;
    private double bounceVelocity = 0;
    private double flexibility = 0.6;
    Text velocityText = new Text();

    private boolean windCollision = false;
    private double windX = 0;
    private double windY = 0;
    private double windAngle = 0;


    public boolean springboardCollision = false;

    public Ball(double _initXPosition, double _initYPosition) {

        super(_initXPosition, _initYPosition);

        setWeight(0.5);
        setIsMoving(true);
        setRadius(25.0);
        setWidth(2 * getRadius());
        setHeight(2 * getRadius());

        elementView = new Circle(getXPosition(), getYPosition(), radius(), Color.PLUM);
        elementView.setStrokeWidth(3);
        elementView.setStroke(Color.ORANGE);
        elementView.setStrokeType(StrokeType.INSIDE);
        elementView.setEffect(getRubberSurface());

        directionLine = new Line();
        directionLine.setStrokeWidth(3);
        directionLine.setStartX(getXPosition());
        directionLine.setStartY(getYPosition());

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
        xPositionProperty().addListener((observable -> {
            updateDirectionLine();
        }));
        yPositionProperty().addListener((observable -> {
            updateDirectionLine();
        }));

        xPositionProperty().addListener((observable -> {
            updateDirectionLine();
        }));

        yPositionProperty().addListener((observable -> {
            updateDirectionLine();
        }));
        radiusProperty().addListener((observable -> {
            setWidth(2 * getRadius());
            setHeight(2 * getRadius());
            updateDirectionLine();
        }));
        materialProperty().addListener((observable -> {
            switch (getMaterial()) {
                case "Metal":
                    this.flexibility = 0.1;
                    elementView.setEffect(getDefaultBallSurface());
                    break;
                case "Wood":
                    this.flexibility = 0.2;
                    elementView.setEffect(getWoodSurface());
                    break;
                case "Rubber":
                    this.flexibility = 0.6;
                    elementView.setEffect(getRubberSurface());
                    break;
            }
            setFriction(flexibility);
        }));
        // ein Ball kann nur proportional skaliert werden.
        xScaleProperty().bindBidirectional(yScaleProperty());

        calculator = new VectorMath();
        velocity = calculator.vectorLength(getXVelocity(), getYVelocity());
        velocityText.setText(String.format(Locale.US, "%.2f", velocity));
        velocityText.setX(getXPosition());
        velocityText.setY(getYPosition() - radius() - 10);

    }


    public final double radius() {
        return this.radius.get();
    }

    public Text getVelocityText() {
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
        velocityText.setText(String.format(Locale.US, "%.2f", velocity));
        velocityText.setX(getXPosition());
        velocityText.setY(getYPosition() - radius() - 10);
    }

    //wenn sich die Geschwindigkeit ändert
    public void updateDirectionLine() {
        velocityText.setText(String.format(Locale.US, "%.2f", velocity));
        velocityText.setX(getXPosition());
        velocityText.setY(getYPosition() - radius() - 10);
        if (getVelocity() == 0) {
            directionLine.setEndX(getXPosition());
            directionLine.setEndY(getYPosition());

        } else {
            directionLine.setEndX(getXPosition() + radius() * getXVelocity() / getVelocity());
            directionLine.setEndY(getYPosition() + radius() * getYVelocity() / getVelocity());
        }
    }


    public void moveElement() {

        move();

    }


    private void setIsSelectedColor() {
        if (getIsSelected()) {

            elementView.setStroke(Color.ORANGE);
            elementView.setStrokeType(StrokeType.INSIDE);
            elementView.setStrokeWidth(3);
            velocityText.setVisible(true);

        } else {
            elementView.setStroke(null);
            elementView.setStrokeType(StrokeType.INSIDE);
            velocityText.setVisible(false);
        }
    }

    /**
     * Berechnet die Kollision mit den Linien der GraphicScene
     *
     * @param lines
     */
    public void collisionDetection(Line[] lines) {

        for (Line line : lines) {
            // x = Px + t*Rx   y = Py + t*Ry   y = m*x + b (lineare Funktion)
            // Umformung der Parameterform der Geraden in eine lineare Funktion
            double linePx = line.getStartX();
            double linePy = line.getStartY();
            double lineRx = line.getEndX() - line.getStartX();
            double lineRy = line.getEndY() - line.getStartY();

            double lineM = lineRy / lineRx;
            double lineB = linePy + (-linePx / lineRx) * lineRy;
            double abstand;
            double schnittpunktX;
            double schnittpunktY;
            // Normalenvektor der Linie
            double nX = lineRy;
            double nY = -lineRx;

            if (lineRy == 0) { // Fallunterscheidung wenn es eine horizontale Linie ist
                abstand = Math.abs(linePy - getYPosition()) - radius() * getXScale();
                schnittpunktX = getXPosition();
                schnittpunktY = linePy;
            } else if (lineRx == 0) { // Fallunterscheidung wenn es eine vertikale Linie ist
                abstand = Math.abs(linePx - getXPosition()) - radius() * getXScale();
                schnittpunktX = linePx;
                schnittpunktY = getYPosition();
            } else {
                double ballLineM = nY / nX;
                double ballLineB = getYPosition() + (-getXPosition() / nX) * nY;

                schnittpunktX = (ballLineB - lineB) / (lineM - ballLineM);
                schnittpunktY = ballLineM * schnittpunktX + ballLineB;

                // Abstand zwischen dem Mittelpunkt des Ball und dem Schnittpunkt - den Radius des Balls
                abstand = Math.sqrt(Math.pow(getXPosition() - schnittpunktX, 2) + Math.pow(getYPosition() - schnittpunktY, 2)) - radius() * getXScale();

            }

            //Bestimmung des linken und rechten Punktes der Linie im Koordinatensystem
            double leftX = linePx;
            double rightX = linePx + lineRx;
            if (lineRx < 0) {
                leftX = line.getEndX();
                rightX = leftX - lineRx;
            }
            double topY = linePy;
            double bottomY = linePy + lineRy;
            if (lineRy < 0) {
                topY = line.getEndY();
                bottomY = topY - lineRy;
            }


            // bestimmt ob sich der Schnittpunkt zwischen dem Start- und Endpunkt der Linie befindet
            boolean onLine = leftX <= schnittpunktX && rightX >= schnittpunktX && topY <= schnittpunktY && bottomY >= schnittpunktY;

            if (abstand < 0.5 && onLine) { // wenn es kollidiert
                collision = true;

                // Laenge des Normalenvektors der Ebene
                double lengthN = calculator.vectorLength(nX, nY);

                // Normierter Normalenvektor der Ebene
                double normedNX = 1 / lengthN * nX;
                double normedNY = 1 / lengthN * nY;

                // Ball wird auf der Ebene poisitioniert
                setXPosition(schnittpunktX + normedNX * (radius() * getXScale() - 0.5));
                setYPosition(schnittpunktY + normedNY * (radius() * getXScale() - 0.5));

                // Bestimmt ob sich der Ball überhauft auf die Linie zu bewegt
                boolean contact = (nX * getXVelocity() * time + nY * getYVelocity() * time) < 0;

                //Winkel zwischen dem Bewegungsvektor und dem Normalenvektor der Linie
                double directionAngle = Math.acos(((-getXVelocity()) * nX + (-getYVelocity()) * nY) / (Math.sqrt(Math.pow(getXVelocity(), 2) + Math.pow(getYVelocity(), 2)) * Math.sqrt(Math.pow(nX, 2) + Math.pow(nY, 2)))) * (180 / Math.PI);

                // Winkel der Ebene
                double angleNew = detectAngle(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());

                if (contactAngle != angleNew) { // wenn sich der erkannte Winkel ändert wird das Springen wieder ermöglicht
                    bounced = false;
                }

                if (angleNew == 0) { // bei einer horizontalen Linie
                    if (Math.abs(getYVelocity() * time) > 2 && contact) { // wenn sich der Ball schnell genug auf die Linie zubewegt soll gesprungen werden
                        bounce = true;
                        setYVelocity(-1 * getYVelocity() * flexibility);
                        bounce = false;
                    } else {
                        if (contact) { // sonst soll sich der Ball entlang der Linie bewegen
                            bounced = true;
                            if (line instanceof Block.BlockLine) {
                                this.collisionMaterial = ((Block.BlockLine) line).getParentBlock().getMaterial();
                            }
                        }
                    }
                } else { // bei der schiefen Ebene
                    if (Math.sqrt(Math.pow(getYVelocity() * time, 2) + Math.pow(getXVelocity() * time, 2)) > 5 && contact) { // wenn sich der Ball schnell genug auf die Linie zubewegt soll gesprungen werden
                        if (!bounced) { // wenn der Ball noch nicht auf der Linie entlang rollt
                            bounce = true;

                            // Berechnungen des Ausfallwinkels
                            double cos = Math.cos(Math.toRadians(-directionAngle * 2));
                            double sin = Math.sin(Math.toRadians(-directionAngle * 2));

                            if (getXVelocity() > 0 && nY < 0 || getXVelocity() < 0 && nY > 0) {
                                cos = Math.cos(Math.toRadians(directionAngle * 2));
                                sin = Math.sin(Math.toRadians(directionAngle * 2));
                            }
                            if (getXVelocity() == 0) {
                                if (angleNew < 180) {
                                    cos = Math.cos(Math.toRadians(directionAngle * 2));
                                    sin = Math.sin(Math.toRadians(directionAngle * 2));
                                }
                            }

                            // Berechnung des neuen Richtungsvektors
                            bounceDirectionX = (getXVelocity() * cos - getYVelocity() * sin) * (-1);
                            bounceDirectionY = (getXVelocity() * sin + (getYVelocity()) * cos);

                            // Normierung des Richtungsvektors
                            double bounceDirection = Math.sqrt(Math.pow(bounceDirectionX, 2) + Math.pow(bounceDirectionY, 2));
                            bounceDirectionX = 1 / bounceDirection * bounceDirectionX;
                            bounceDirectionY = 1 / bounceDirection * bounceDirectionY;
                            bounceVelocity = Math.sqrt(Math.pow(getXVelocity(), 2) + Math.pow(getYVelocity() * flexibility, 2));

                            setXVelocity(bounceDirectionX * bounceVelocity);
                            setYVelocity(-1 * bounceDirectionY * bounceVelocity);
                        }
                    } else if (contact) { // sonst soll der Ball auf der Ebene entlang rollen
                        if (!bounced) {
                            bounced = true;
                            if (line instanceof Block.BlockLine) {
                                this.collisionMaterial = ((Block.BlockLine) line).getParentBlock().getMaterial();
                            }
                        }
                    }
                }
                contactAngle = angleNew;

            }
        }
        windCollision = calcWindCollision(lines);
    }

    /**
     * Berechnungen der Bewegungen
     */
    public void move() {
        if (windCollision) {
            windX = 0;
            windY = 0;
        }

        /*if(bounce) { // wenn der Ball vom Aufprall springen soll
            if (contactAngle != 0) {
                setXVelocity(bounceDirectionX * bounceVelocity);
            }
        }*/

        if (bounced) { // wenn der Ball an einer Ebene entlang rollt ohne zu springen
            calcAcceleration(contactAngle);
        }
        if (getXPosition() + radius() * getXScale() >= 1150 && (getXVelocity() > 0 && bounced || getXVelocity() + (getXAcceleration() + windX) * time > 0 && !bounced)) { // Kollision mit rechtem Szenen-Rand kehrt die x-Geschwindigkeit um
            setXVelocity(-1 * getXVelocity() * flexibility);
            setXPosition(1150 - radius() * getXScale());
            windX = 0;

        } else if (getXPosition() - radius() * getXScale() <= 0 && (getXVelocity() < 0 && bounced || getXVelocity() + (getXAcceleration() + windX) * time < 0 && !bounced)) { // Kollision mit linken Szenen-Rand kehrt die x-Geschwindigkeit um
            setXVelocity(-1 * getXVelocity() * flexibility);
            setXPosition(0 + radius() * getXScale());
            windX = 0;
        }
        // x
        if (!bounced) {
            setXAcceleration(getXAcceleration() + windX);
            // [m] s = s0 + v * t + 1/2 * a * t^2
            setXPosition(getXPosition() + getXVelocity() * time + 0.5f * getXAcceleration() * Math.pow(time, 2));
            // [m/s] v = v0 + a * t
            setXVelocity(getXVelocity() + getXAcceleration() * time);
        } else {
            setXPosition(getXPosition() + getXVelocity() * time);
        }


        if (!collision) { // wenn der Ball im freien Fall ist
            setYAcceleration(GRAVITY + windY);
            setXAcceleration(0 + windX);
            bounced = false;
        }
        if (bounce) { // wenn der Ball vom Aufprall springen soll
            /*if (contactAngle != 0) {
                setYVelocity(-1 * bounceDirectionY * bounceVelocity);
            }
            else {
                setYVelocity(-1 * getYVelocity() * flexibility);
            }*/
            bounce = false;
        }
        // y
        if (!bounced) {
            //[m] s = s0 + v * t + 1/2 * a * t^2
            setYPosition(getYPosition() + getYVelocity() * time + 0.5 * getYAcceleration() * Math.pow(time, 2));
            //[m/s] Geschwindigkeit v = v0 + a * t
            setYVelocity(getYVelocity() + getYAcceleration() * time);
        } else {
            setYPosition(getYPosition() + getYVelocity() * time);
        }


        collision = false;

    }

    /**
     * Berechnet die Beschleunigung und Reibung abhängig von dem Winkel der Ebene
     *
     * @param angle Winkel der Ebene
     */
    public void calcAcceleration(double angle) {
        findFrictionCoefficient();

        //[m/s^2]
        double FG = getWeight() * (GRAVITY + windY);
        // [N]
        double FH = (FG * Math.sin(Math.toRadians(angle)));
        // FR = frictionCoff  * FN
        // [N]
        double FN = (frictionCoefficient * (FG * Math.cos(Math.toRadians(angle))));

        // [N]
        double friction = FH + FN;
        if (FH < 0) {
            friction = FH - FN;
        }
        // [m/s^2]  F = m * a umgeformt zu:  a = F / m
        double acceleration = friction / getWeight();
        setXAcceleration(acceleration * Math.cos(Math.toRadians(angle)));
        setYAcceleration(acceleration * Math.sin(Math.toRadians(angle)));

        setYAcceleration(getYAcceleration() + windY);


        boolean turn = false;

        if (angle == 0) { // beim Rollen auf einer horizontalen Ebene
            setYVelocity(0);
            setYAcceleration(0);
            if (getXVelocity() > 0) {
                setXAcceleration(-1 * getXAcceleration());
            }
            if ((windX + getXAcceleration() > 0 && windX > 0 || windX + getXAcceleration() < 0 && windX < 0) && getXVelocity() != 0) {
                if (getXVelocity() + (getXAcceleration() + windX) * time > 0 && getXVelocity() < 0 || getXVelocity() + (getXAcceleration() + windX) * time < 0 && getXVelocity() > 0) {
                    turn = true;
                }
            } else {
                //Es kommt zum Stillstand,sobald Geschwindigkeit < 0.25
                if (Math.abs(getXVelocity()) < 2.5) {
                    setXVelocity(0);
                    setXAcceleration(0);
                    windX = 0;
                }
            }
        }

        setXAcceleration(getXAcceleration() + windX);

        double[] direction;
        if (angle > 180) {
            direction = calculator.rotateCCW(-1, 0, angle); // berechnet die Richtung in die der Ball rollen muss
            if (getXVelocity() + getXAcceleration() * time > 0) {
                direction[0] = -1 * direction[0];
                direction[1] = -1 * direction[1];
                if (!slowed) {
                    setXVelocity(getXVelocity() / 2);
                    setYVelocity(getYVelocity() / 2);
                    slowed = true;
                }
            } else {
                slowed = false;
            }
        } else {
            direction = calculator.rotateCCW(1, 0, angle); // berechnet die Richtung in die der Ball rollen muss
            if (getXVelocity() + getXAcceleration() * time < 0 && angle != 0) {
                direction[0] = -1 * direction[0];
                direction[1] = -1 * direction[1];
                if (!slowed) {
                    setXVelocity(getXVelocity() / 2);
                    setYVelocity(getYVelocity() / 2);
                    slowed = true;
                }
            } else {
                slowed = false;
            }
        }
        if (getXVelocity() < 0 && angle == 0) {
            direction = calculator.rotateCCW(1, 0, 180); // berechnet die Richtung in die der Ball rollen muss
        }


        if (turn) {
            direction[0] = -1 * direction[0];
            direction[1] = -1 * direction[1];
        }

        double velocity = calculator.vectorLength(getXVelocity() + getXAcceleration() * time, getYVelocity() + getYAcceleration() * time);
        setXVelocity(direction[0] * velocity); // die Geschwindigkeiten werden entsprechend des Richtungsvektors gesetzt
        setYVelocity(direction[1] * velocity);


    }

    /**
     * Bestimmt den Rollreibungskoeffizienten je nach Materialien der Kollisionsobjekte
     */
    private void findFrictionCoefficient() {
        switch (getMaterial()) {
            case "Metal":
                switch (this.collisionMaterial) {
                    case "Metal":
                        this.frictionCoefficient = METAL_ON_METAL;
                        break;
                    case "Wood":
                        this.frictionCoefficient = METAL_ON_WOOD;
                        break;
                    case "Rubber":
                        this.frictionCoefficient = METAL_ON_RUBBER;
                        break;
                }
                break;
            case "Wood":
                switch (this.collisionMaterial) {
                    case "Metal":
                        this.frictionCoefficient = WOOD_ON_METAL;
                        break;
                    case "Wood":
                        this.frictionCoefficient = WOOD_ON_WOOD;
                        break;
                    case "Rubber":
                        this.frictionCoefficient = WOOD_ON_RUBBER;
                        break;
                }
                break;
            case "Rubber":
                switch (this.collisionMaterial) {
                    case "Metal":
                        this.frictionCoefficient = RUBBER_ON_METAL;
                        break;
                    case "Wood":
                        this.frictionCoefficient = RUBBER_ON_WOOD;
                        break;
                    case "Rubber":
                        this.frictionCoefficient = RUBBER_ON_RUBBER;
                        break;
                }
                break;
        }
    }

    /**
     * Berechnet die Beschleunigung, welche der Wind auf den Ball bei einer Temperatur von ca. 20 Grad Celsius ausuebt
     */
    public void calcWind(Wind sceneWind) {
        if (sceneWind.getIsActivated()) {
            double airDensity = 1.2041 * Math.pow(10, -6); // kg/cm^3 bei 20 Grad Celsius
            double dragCoefficient = 0.47; // für eine Kugel
            double windVelocity = 0.8369 * Math.pow(sceneWind.getWindForce(), 3f / 2) * 100; // Umrechnung Bft(Beaufort) in cm/s
            this.windAngle = sceneWind.getWindDirection();

            double affectedArea = Math.PI * Math.pow(radius() * getXScale(), 2);

            double dragForce = 0.5 * airDensity * dragCoefficient * affectedArea * Math.pow(windVelocity, 2);

            double windAcceleration = dragForce / getWeight();

            windX = windAcceleration * Math.cos(Math.toRadians(windAngle));
            windY = windAcceleration * Math.sin(Math.toRadians(windAngle));
        } else {
            windX = 0;
            windY = 0;
        }

    }

    /**
     * Berechnet ob der Wind den Ball beeinflusst oder ob er bereits vorher mit einer Linie kollidiert.
     *
     * @param lines Kollisionslinien der Szene
     * @return Wahrheitswert ob der Wind vor dem Ball mit einer Linie kollidiert
     */
    public boolean calcWindCollision(Line[] lines) {
        for (int i = 0; i < lines.length -1; i++) { // Der Boden wird nicht als Windkollision betrachtet
            Line line = lines[i];
            // x = Px + t*Rx   y = Py + t*Ry   y = m*x + b (lineare Funktion)
            // Umformung der Parameterform der Geraden in eine lineare Funktion
            double linePx = line.getStartX();
            double linePy = line.getStartY();
            double lineRx = line.getEndX() - line.getStartX();
            double lineRy = line.getEndY() - line.getStartY();

            double lineM = lineRy / lineRx;
            double lineB = linePy + (-linePx / lineRx) * lineRy;
            double schnittpunktX;
            double schnittpunktY;

            // Wind-Gerade von dem Ball aus in lineare Funktion umgeformt
            double windLinePx = getXPosition();
            double windLinePy = getYPosition();
            double windLineRx = calculator.rotateCCW(1, 0, windAngle)[0];
            double windLineRy = calculator.rotateCCW(1, 0, windAngle)[1];

            double windLineM = windLineRy / windLineRx;
            double windLineB = windLinePy + (-windLinePx / windLineRx) * windLineRy;

            if (Math.abs(windLineM) != Math.abs(lineM)) { // wenn der Wind nicht parallel zu der Linie verlaeuft
                if (lineRx == 0) { // Fallunterscheidung wenn es eine vertikale Linie ist
                    schnittpunktX = linePx;
                    schnittpunktY = windLineM * linePx + windLineB;
                } else if (windLineRx == 0) { // Fallunterscheidung wenn der Wind vertikal verlaeuft
                    schnittpunktX = (linePy - windLineB) / windLineM;
                    schnittpunktY = linePy;
                } else {
                    schnittpunktX = (windLineB - lineB) / (lineM - windLineM);
                    schnittpunktY = windLineM * schnittpunktX + windLineB;
                }

                //Bestimmung des linken und rechten Punktes der Linie im Koordinatensystem
                double leftX = linePx;
                double rightX = linePx + lineRx;
                if (lineRx < 0) {
                    leftX = line.getEndX();
                    rightX = leftX - lineRx;
                }
                double topY = linePy;
                double bottomY = linePy + lineRy;
                if (lineRy < 0) {
                    topY = line.getEndY();
                    bottomY = topY - lineRy;
                }
                // bestimmt ob sich der Schnittpunkt zwischen dem Start- und Endpunkt der Linie befindet
                boolean onLine = leftX <= schnittpunktX && rightX >= schnittpunktX && topY <= schnittpunktY && bottomY >= schnittpunktY;
                if (onLine) {
                    if (windAngle > 0 && windAngle <= 180 && schnittpunktY <= getYPosition() || windAngle > 180 && schnittpunktY >= getYPosition()) { // wenn der Schnittpunkt vor dem Aufprall auf den Ball liegt
                        return true;
                    } else if (windAngle == 0 && schnittpunktX < getXPosition()) {
                        return true;
                    }
                }

            }

        }
        return false;
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

            double normalX, normalY, relX, relY;
            //die Relativgeschwindigkeit beider Kugeln
            relX = ball2.getXVelocity();
            relY = ball2.getYVelocity();
            // Berührnormale zwischen den diesem und ball2
            normalX = ball2.getXPosition() - this.getXPosition();
            normalY = ball2.getYPosition() - this.getYPosition();

            double test = normalX * relX + normalY * relY;

            double normlength = calculator.vectorLength(normalX, normalY);
            //ball2.setXPosition(this.getXPosition()+normalX/normlength *(this.getRadius()+ball2.getRadius()));
            // ball2.setYPosition(this.getYPosition()+normalY/normlength *(this.getRadius()+ball2.getRadius()));

            if (test < 0) {
                ball2.setXPosition(this.getXPosition() + normalX / normlength * (this.getRadius() + ball2.getRadius()));
                ball2.setYPosition(this.getYPosition() + normalY / normlength * (this.getRadius() + ball2.getRadius()));
                // v = v_t + v_z => v_t = v - v_z, v_z mit ParallelProjektion bestimmen
                double[] v1_z = calculator.parallelProjection(this.getXVelocity(), this.getYVelocity(), normalX, normalY);
                double[] v2_z = calculator.parallelProjection(ball2.getXVelocity(), ball2.getYVelocity(), normalX, normalY);

                if (v1_z != new double[]{0, 0} && v2_z != new double[]{0, 0}) {
                    // elastischen Stoß nur für die Anteile in Zentralrichtung berechnen
                    vneu = zentralerStoss(v1_z[0], v1_z[1], this.getWeight(), v2_z[0], v2_z[1], ball2.getWeight());
                    // Geschwindigkeit aus der neuen Zentralrichtung plus dem (unveränderten) tangentialen Anteil v_t = v - v_z
                    this.setXVelocity(vneu[0] + this.getXVelocity() - v1_z[0]);
                    this.setYVelocity(vneu[1] + this.getYVelocity() - v1_z[1]);
                    ball2.setXVelocity(vneu[2] + ball2.getXVelocity() - v2_z[0]);
                    ball2.setYVelocity(vneu[3] + ball2.getYVelocity() - v2_z[1]);
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
        double tolerance = 1;
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
     * @param weight2 - Gewicht des zweiten Objekts
     * @return _ double[] mit vx1_neu, vy1_neu, vx2_neu, vx3_neu
     */
    private double[] zentralerStoss(double vx_1, double vy_1, double weight1, double vx_2, double vy_2, double weight2) {

        double[] vnew = {0, 0, 0, 0};
        // Falls beide Objekte das gleiche Gewicht haben, werden die Geschwindigkeiten getauscht
        if (weight1 == weight2) {
            vnew[0] = vx_2;
            vnew[1] = vy_2;
            vnew[2] = vx_1;
            vnew[3] = vy_1;
        } else {
            // Berechnung gemäß der Formeln für den elastischen Stoß
            double xFactor = (weight1 * vx_1 + weight2 * vx_2) / (weight1 + weight2);
            double yFactor = (weight1 * vy_1 + weight2 * vy_2) / (weight1 + weight2);

            vnew[0] = 2 * xFactor - vx_1;
            vnew[1] = 2 * yFactor - vy_2;
            vnew[2] = 2 * xFactor - vx_2;
            vnew[3] = 2 * yFactor - vy_2;
        }

        return vnew;
    }

    /**
     * Kollisionserkennung mit der Seesaw/Wippe
     * @param seesaw
     */
    public void checkCollisionWithSeesaw(Seesaw seesaw) {
        Line[] outlines = seesaw.getOutlines();
        //Bestimmung des Schnittpunktes mit der Oberkante der Wippe
        double linePx = outlines[0].getStartX();
        double linePy = outlines[0].getStartY();
        double lineRx = outlines[0].getEndX() - outlines[0].getStartX();
        double lineRy = outlines[0].getEndY() - outlines[0].getStartY();

        double lineM = lineRy / lineRx;
        double lineB = linePy + (-linePx / lineRx) * lineRy;
        double abstand;
        double schnittpunktX;
        double schnittpunktY;
        // Normalenvektor der Linie
        double nX = lineRy;
        double nY = -lineRx;

        if (lineRy == 0) { // Fallunterscheidung wenn es eine horizontale Linie ist
            abstand = Math.abs(linePy - getYPosition()) - radius() * getXScale();
            schnittpunktX = getXPosition();
            schnittpunktY = linePy;
        } else if (lineRx == 0) { // Fallunterscheidung wenn es eine vertikale Linie ist
            abstand = Math.abs(linePx - getXPosition()) - radius() * getXScale();
            schnittpunktX = linePx;
            schnittpunktY = getYPosition();
        } else {
            double ballLineM = nY / nX;
            double ballLineB = getYPosition() + (-getXPosition() / nX) * nY;

            schnittpunktX = (ballLineB - lineB) / (lineM - ballLineM);
            schnittpunktY = ballLineM * schnittpunktX + ballLineB;

            // Abstand zwischen dem Mittelpunkt des Ball und dem Schnittpunkt - den Radius des Balls
            abstand = Math.sqrt(Math.pow(getXPosition() - schnittpunktX, 2) + Math.pow(getYPosition() - schnittpunktY, 2)) - radius() * getXScale();

        }

        //Bestimmung des linken und rechten Punktes der Linie im Koordinatensystem
        double leftX = linePx;
        double rightX = linePx + lineRx;
        if (lineRx < 0) {
            leftX = outlines[0].getEndX();
            rightX = leftX - lineRx;
        }
        double topY = linePy;
        double bottomY = linePy + lineRy;
        if (lineRy < 0) {
            topY = outlines[0].getEndY();
            bottomY = topY - lineRy;
        }


        // bestimmt ob sich der Schnittpunkt zwischen dem Start- und Endpunkt der Linie befindet
        boolean onLine = leftX <= schnittpunktX && rightX >= schnittpunktX && topY <= schnittpunktY && bottomY >= schnittpunktY;
        //v_rel * normale < 0  (bilden spitzen Winkel, also rollen aufeinander zu)
        boolean hit = (nX * this.getXVelocity() + nY * this.getYVelocity()) <= 0;

        if (onLine && abstand < 1) {
            // Laenge des Normalenvektors der Ebene
            double lengthN = calculator.vectorLength(nX, nY);

            // Normierter Normalenvektor der Ebene
            double normedNX = 1 / lengthN * nX;
            double normedNY = 1 / lengthN * nY;

            // Ball wird auf der Ebene poisitioniert
            setXPosition(schnittpunktX + normedNX * (radius() * getXScale() - 0.5));
            setYPosition(schnittpunktY + normedNY * (radius() * getXScale() - 0.5));

            //Ball rollt auf Wippe zu
            if (hit) {
                //trifft die Kugel auf der linken oder rechten Seite der Wippe auf
                if (leftX <= schnittpunktX && (leftX + rightX) / 2 >= schnittpunktX) {
                    seesaw.setLeft(true);
                    seesaw.setLeftForce(this.getWeight(), schnittpunktX, schnittpunktY);
                } else if ((leftX + rightX) / 2 < schnittpunktX && rightX >= schnittpunktX) { //oder auf der rechten Hälfte
                    seesaw.setRight(true);
                    seesaw.setRightForce(this.getWeight(), schnittpunktX, schnittpunktY);
                }

                double delX = schnittpunktX - this.getXPosition();
                double delY = schnittpunktY - this.getYPosition();
                //Bestimmung der Anteile der Geschwindigkeitsvektoren, die parallel zur  Zentralrichtung liegen
                double[] v1_z = calculator.parallelProjection(this.getXVelocity(), this.getYVelocity(), delX, delY); //Projektion der Kugelvelocity
                double[] v2_z = calculator.parallelProjection(seesaw.getXVelocity(), seesaw.getYVelocity(), delX, delY); // Projektion der spinnerVelocity


                // nur die parallelen Anteile werden verändert
                double[] vneu = zentralerStoss(v1_z[0], v1_z[1], this.getWeight(), v2_z[0], v2_z[1], seesaw.getWeight());
                // die neue Geschwindigkeit ergibte sich aus den veränderteren Anteilen + dem Tangentialan (unveränderten) Anteil
                this.setXVelocity(vneu[0] + this.getXVelocity() - v1_z[0]);
                this.setYVelocity(vneu[1] + this.getYVelocity() - v1_z[1]);
            }

        } else { // falls nicht die obere Kante getroffen wird, teste die restlichen Kanten auf Kollision
            Line[] testLines = new Line[outlines.length - 1];
            //System.out.println("ElseFall Seesaw");
            for (int i = 1; i < outlines.length; i++) {
                // alle anderen bis auf die Erste Kante
                testLines[i - 1] = outlines[i];
            }
            collisionDetection(testLines);
        }

    }

    /**
     * Kollisionserkennung Spinner
     *
     * @param spinner
     */
    public void checkCollisionWithSpinner(Spinner spinner) {

        Line[] outlines = spinner.getOutlines();

        double[] spinnerVelocity;

        if (spinner.getRotationalSpeed() == 0) {
            collisionDetection(outlines);//wenn der Spinner sich nicht dreht, behandle ihn wie Block
        } else {//ansonsten

            for (Line line : outlines) {

                double ax, ay, bx, by, deltaX, deltaY, normalX, normalY, lotX, lotY, distance;
                boolean online;
                boolean hit;

                //Anfangs und Endpunkt, der zu prüfenden Kante
                ax = line.getStartX();
                ay = line.getStartY();
                bx = line.getEndX();
                by = line.getEndY();
                deltaX = bx - ax;
                deltaY = by - ay;

                double steigung = deltaY / deltaX;
                double b = ay - ax * steigung;

                if (deltaY == 0) { //der spinner ist waagerecht
                    lotX = this.getXPosition();
                    lotY = ay;
                    distance = Math.abs(this.getYPosition() - ay);
                } else if (deltaX == 0) { // der Spinner ist senkrecht
                    lotX = ax;
                    lotY = this.getYPosition();
                    distance = Math.abs(this.getXPosition() - ax);

                } else {

                    double steigung2 = -deltaY / deltaX;
                    double b2 = this.getYPosition() - this.getXPosition() * steigung2;

                    //Berührpunkt von Kugel zur Kante
                    lotX = (b2 - b) / (steigung - steigung2);
                    lotY = steigung2 * lotX + b2;
                    //Abstand Mittelpunkt Kugel zur Kante
                    distance = calculator.vectorLength((this.getXPosition() - lotX), (this.getYPosition() - lotY));

                }

                // Normalenvektor zur Kante
                normalX = deltaY;
                normalY = -deltaX;

                //Lotfusspunkt liegt zwischen A und B
                online = (((ax <= bx && ax <= lotX && lotX <= bx) || (bx <= ax && bx <= lotX && lotX <= ax)) &&
                        ((ay <= by && ay <= lotY && lotY <= by) || (by <= ay && by <= lotY && lotY <= ay)));
                //System.out.println("online "+online+" Abstand "+Math.abs(distance - this.getRadius()));

                //Abstand Kugel / Kante klein genug
                if (online && Math.abs(distance - this.getRadius()) < 15) {

                    double normLength = calculator.vectorLength(normalX, normalY);
                    // Ball wird auf der Ebene poisitioniert
                    setXPosition(lotX + normalX / normLength * (radius() * getXScale() - 0.5));
                    setYPosition(lotY + normalY / normLength * (radius() * getXScale() - 0.5));

                    //den Richtungsvektor für die Bahngeschwindigkeit im Lotpunkt ermitteln
                    spinnerVelocity = spinner.velocityVector(lotX, lotY);

                    //v_rel * n_Spinnerkante > 0  (bilden spitzen Winkel, also rollen aufeinander zu)
                    hit = (normalX * (spinnerVelocity[0] - this.getXVelocity()) + normalY * (spinnerVelocity[1] - this.getYVelocity())) > 0;

                    if (hit) {
                        // Zentralrichtung des Stosses
                        double delX = lotX - this.getXPosition();
                        double delY = lotY - this.getYPosition();
                        //Bestimmung der Anteile der Geschwindigkeitsvektoren, die parallel zur  Zentralrichtung liegen
                        double[] v1_z = calculator.parallelProjection(this.getXVelocity(), this.getYVelocity(), delX, delY); //Projektion der Kugelvelocity
                        double[] v2_z = calculator.parallelProjection(spinnerVelocity[0], spinnerVelocity[1], delX, delY); // Projektion der spinnerVelocity

                        if (v1_z != new double[]{0, 0} && v2_z != new double[]{0, 0}) {
                            // nur die parallelen Anteile werden verändert
                            double[] vneu = zentralerStoss(v1_z[0], v1_z[1], this.getWeight(), v2_z[0], v2_z[1], spinner.getWeight());
                            // die neue Geschwindigkeit ergibte sich aus den veränderteren Anteilen + dem Tangentialan (unveränderten) Anteil
                            this.setXVelocity(vneu[0] + this.getXVelocity() - v1_z[0]);
                            this.setYVelocity(vneu[1] + this.getYVelocity() - v1_z[1]);
                        }
                    }
                }
            }//endfor
        }
    }

    public void collisionDetectionBoardSpring(Line line, Springboard.Board board) {


        // x = Px + t*Rx   y = Py + t*Ry   y = m*x + b (lineare Funktion)
        // Umformung der Parameterform der Geraden in eine lineare Funktion
        double linePx = line.getStartX();
        double linePy = line.getStartY();
        double lineRx = line.getEndX() - line.getStartX();
        double lineRy = line.getEndY() - line.getStartY();

        double lineM = lineRy / lineRx;
        double lineB = linePy + (-linePx / lineRx) * lineRy;
        double abstand;
        double schnittpunktX;
        double schnittpunktY;
        // Normalenvektor der Linie
        double nX = lineRy;
        double nY = -lineRx;

        if (lineRy == 0) { // Fallunterscheidung wenn es eine horizontale Linie ist
            abstand = Math.abs(linePy - getYPosition()) - radius() * getXScale();
            schnittpunktX = getXPosition();
            schnittpunktY = linePy;
        } else if (lineRx == 0) { // Fallunterscheidung wenn es eine vertikale Linie ist
            abstand = Math.abs(linePx - getXPosition()) - radius() * getXScale();
            schnittpunktX = linePx;
            schnittpunktY = getYPosition();
        } else {
            double ballLineM = nY / nX;
            double ballLineB = getYPosition() + (-getXPosition() / nX) * nY;

            schnittpunktX = (ballLineB - lineB) / (lineM - ballLineM);
            schnittpunktY = ballLineM * schnittpunktX + ballLineB;

            // Abstand zwischen dem Mittelpunkt des Ball und dem Schnittpunkt - den Radius des Balls
            abstand = Math.sqrt(Math.pow(getXPosition() - schnittpunktX, 2) + Math.pow(getYPosition() - schnittpunktY, 2)) - radius() * getXScale();

        }

        //Bestimmung des linken und rechten Punktes der Linie im Koordinatensystem
        double leftX = linePx;
        double rightX = linePx + lineRx;
        if (lineRx < 0) {
            leftX = line.getEndX();
            rightX = leftX - lineRx;
        }
        double topY = linePy;
        double bottomY = linePy + lineRy;
        if (lineRy < 0) {
            topY = line.getEndY();
            bottomY = topY - lineRy;
        }


        // bestimmt ob sich der Schnittpunkt zwischen dem Start- und Endpunkt der Linie befindet
        boolean onLine = leftX <= schnittpunktX && rightX >= schnittpunktX && topY <= schnittpunktY && bottomY >= schnittpunktY;

        if (abstand < 0.5 && onLine) { // wenn es kollidiert
            springboardCollision = true;
            board.getParentSpringboard().move(this);

        } else if (abstand > 1 && (xPosition.get() < line.getStartX() || xPosition.get() > line.getEndX())) {
            springboardCollision = false;
        }
    }


    // das Element wird auf die Startwerte zurückgesetzt
    @Override
    public void resetElement() {
        super.resetElement();
        this.bounced = false;
        this.collision = false;
        this.bounce = false;
        this.slowed = false;
    }

    public void setSpringboardCollision(boolean springboardCollision) {
        this.springboardCollision = springboardCollision;
    }

}
