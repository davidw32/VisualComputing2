package Model;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import static Model.Config.GRAVITY;
import static Model.Config.TIME;

public class Ball extends GraphicsObject
{

    boolean frictionLock = true;
    boolean collision;
    boolean bounce = false;
    Text velocityText = new Text();

    double saveVelocitX = 0;
    double saveVelocityY = 0;
    double savesX = 0;
    double savesY = 0;
    double saveAccelerationX = 0;
    double saveAccelerationY = 0;

    public Ball(double _initXPosition, double _initYPosition) {
        super(_initXPosition, _initYPosition);
        elementView = new Circle(getXPosition(), getYPosition(), 30, Color.PLUM);
        //kann man hier die Properties der Shape bidirectional an die Poperties des Ballobjektes binden???
        ((Circle) elementView).centerXProperty().bindBidirectional(xPositionProperty());
        ((Circle) elementView).centerYProperty().bindBidirectional(yPositionProperty());
    }

    protected Line directionLine = new Line(0,0,1,1);



    public Ball(double x,double y,double radius,double weight)
    {
        super(x,y);
        elementView = new Circle(520,180,radius);
        sX = 520;
        sY = 180;
        directionLine.setStrokeWidth(5);

    }

    @Override
    protected void moveElement() {

    }

    public void collisionDetection(Line[] lines){
        collision = false;
        bounce = false;
        double x = ((Circle)elementView).getCenterX() + ((Circle)elementView).getTranslateX();
        double y = ((Circle)elementView).getCenterY() + ((Circle)elementView).getTranslateY();
        for(Line line : lines){
            // x = Px + t*Rx   y = Py + t*Ry   y = m*x + b
            double linePx = line.getStartX();
            double linePy = line.getStartY();
            double lineRx = line.getEndX() - line.getStartX();
            double lineRy = line.getEndY() - line.getStartY();

            double lineM = lineRy / lineRx;
            double lineB = linePy + (- linePx / lineRx)*lineRy;
            double abstand;
            double schnittpunktX;
            double schnittpunktY;

            if(lineRy == 0){
                abstand = Math.abs(linePy - y) - ((Circle) elementView).getRadius();
                schnittpunktX = x;
                schnittpunktY = linePy;
            }
            else if(lineRx == 0){
                abstand = Math.abs(linePx - x) - ((Circle) elementView).getRadius();
                schnittpunktX = linePx;
                schnittpunktY = y;
            }
            else {

                // Normalenvektor der Linie
                double nX = lineRy;
                double nY = -lineRx;

                double ballLineM = nY / nX;
                double ballLineB = y + (-x / nX) * nY;

                schnittpunktX = (ballLineB - lineB) / (lineM - ballLineM);
                schnittpunktY = ballLineM * schnittpunktX + ballLineB;

                abstand = Math.sqrt(Math.pow(x - schnittpunktX, 2) + Math.pow(y - schnittpunktY, 2)) - (((Circle) elementView).getRadius());

            }



            if(abstand < 1 && line.getStartX() <= x && line.getEndX() >= x){
                System.out.println("Kollision " +line.getEndX() +"  Radius: "+ x);

                collision = true;
                double angleNew = detectAngle(line.getStartX(),line.getStartY(),line.getEndX(),line.getEndY());


                if(angleNew == 0){
                    if(Math.abs(velocityY*TIME) > 0.5) {
                        bounce = true;
                    }
                    else {
                        frictionLock = false;
                        velocityY = 0;
                        accelerationY = 0;
                    }
                }
                else{
                    if(angle != angleNew) {
                        velocityX = 0;
                        velocityY = 0;
                        accelerationX = 0;
                        accelerationY = 0;

                    }
                }
                angle = angleNew;
            }
        }
    }


    /**
     * Berechnungen der X-Achse
     */
    public void moveX()
    {

        //Reibung [m/s^2]
        double reibung = 0;
        double x = ((Circle)elementView).getCenterX() + ((Circle)elementView).getTranslateX();
        double y = ((Circle)elementView).getCenterY() + ((Circle)elementView).getTranslateY();




        if (collision) {
            // Reibungsberechnung bei gerade Ebene
            if (angle == 0) {
                if (velocityX == 0) {
                    //System.out.println("HAFTREIBUNG");
                    frictionLock = haftReibung(weight, GRAVITY, frictionCoefficient);
                }
                else if (!frictionLock) {
                    //System.out.println("GLEITREIBUNG AB HIER");
                    // Reibung gerade Ebene [m/s^2]
                    reibung += gleitReibung(weight, GRAVITY, frictionCoefficient);
                }
            }
            else {   // Reibung auf schiefer Ebene [m/s^2]
                reibung += frictionX(weight, GRAVITY, frictionCoefficient, angle);
            }
            //System.out.println("FORCE:  " + reibung);
        }




        // [m/s^2] Beschleunigungen werden hier summiert
        double accelerationSum = (accelerationX + reibung);

        if(x+((Circle)elementView).getRadius() >= 1115 && velocityX > 0){
            velocityX = -velocityX;

        }
        else if(x-((Circle)elementView).getRadius() <= 0 && velocityX < 0){
            velocityX = -velocityX;
        }


        // [m] s = s0 + v * t + 1/2 * a * t^2
        sX = (float)(sX + velocityX * TIME + 0.5f * accelerationSum * Math.pow(TIME,2));

        // [m/s] v = v0 + a * t
        velocityX =  velocityX+accelerationSum*TIME;

        //Translation Kugel
        ((Circle)elementView).setCenterX( sX );
        velocityText.setX(x -(((Circle)elementView).getRadius())/2);


    }

    /**
     * Berechnungen der Y-Achse
     */
    public void moveY()
    {
        // //[m/s^2] Reibung

        double y = ((Circle)elementView).getCenterY() + ((Circle)elementView).getTranslateY();
        double force = frictionY(weight,GRAVITY,frictionCoefficient,angle);


        //[m/s^2]
        double accelerationSum = 0;
        if (!collision) {
            velocityY = velocityY + GRAVITY * TIME;
            accelerationY = GRAVITY;
            accelerationSum = accelerationY;
        }
        else {
            accelerationSum = (accelerationY + force);
        }



        if(bounce && velocityY > 0) {
            velocityY = (float) (-velocityY * 0.6);
            bounce = false;
        }






        //[m] s = s0 + v * t + 1/2 * a * t^2
        sY = (float)(sY + velocityY * TIME + 0.5f * accelerationSum * Math.pow(TIME,2));

        //[m/s] Geschwindigkeit v = v0 + a * t
        velocityY =  velocityY+accelerationSum*TIME;

        //Translation Kugel
        ((Circle)elementView).setCenterY( sY );

        velocityText.setY(y - ((Circle) elementView).getRadius()-10);
        velocityText.setText(String.format("%.2f",Math.sqrt(Math.pow(velocityX,2)+Math.pow(velocityY,2))));
    }


    /**
     * Haftreibung
     * @param m Gewicht
     * @param g Gravität
     * @param frictionCoff Haftreibungskoeffizient
     * @return true,falls Geschwindigkeit nicht maximale Haftreibung ueberschreitet. False,falls diese uberschritten wird.
     */
    public boolean haftReibung(double m,double g,double frictionCoff)
    {
        //Kraft geschwindigkeit
        // [m/s^2]  a = v / t
        double VN = velocityX / TIME;
        // [N] F = m * a
        VN = weight* VN;
        //[N] Normalkraft
        double FN = m*g;
        // [N] Haftkraft bzw. Gravitation
        double FR = FN * frictionCoff;
        //System.out.println("HAFT:   " +FR);
        //System.out.println("SPEED:  "+ VN);

        if(Math.abs(VN) < Math.abs(FR) && VN != 0 )
        {
            //System.out.println(" HAFT TRUE");
            return true;
        }

        //System.out.println(" HAFT FALSE");
        frictionLock = false;
        return false;
    }

    /**
     * Wird aktiv,wenn Haftreibung ueberschritten wird
     * @param m Gewicht
     * @param g Gravitation
     * @param frictionCoff Gleitreibungskoeffizient
     * @return
     */
    public float gleitReibung(double m,double g,double frictionCoff)
    {
        //Kraft der Geschwindigkeit (VelocityX)
        // [m/s^2] a = v / t
        double VN = velocityX / TIME;
        // [N] F = m * a
        VN = weight* VN;

        // [m/s^2]
        double FN = m*g;
        // Reibung
        double FR = FN * frictionCoff;


        //Es kommt zum Stillstand,sobald Geschwindigkeit < 0.25 und Beschleunigung < 0.25f
        if(Math.abs(velocityX) < 2.5f && Math.abs(accelerationX) < 2.5f )
        {
            velocityX = 0;
            accelerationX = 0;
            frictionLock = true;
            //System.out.println("GLEIT FALSE");
            return 0;
        }

        //System.out.println("GLEIT TRUE");
        if(velocityX < 0)
        {
            // [m/s^2]
            float acceleration = (float) (FR / weight);
            return acceleration;
        }
        else if(velocityX > 0)
        {
            // [m/s^2]
            float acceleration = (float) (FR / weight);
            return -acceleration;
        }
        return 0;
    }


    /**Reibung an einer Schiefen Ebene ( X-Achse)
     * @param m Gewicht (in KG)
     * @param g Gravität
     * @param frictionCoff Reibungskoeffizient
     * @param angle Winkel
     * @return Beschleunigung der Reibungskraft
     * Formeln:
     * Gewichtskraft: FG = masse * gravitation
     * Hanganbtriebskraft: FH = FG * sin(a)  (a = angle)
     * Normalkraft: FN = FG * cos(a)
     * F = m * a umgeformt zu a = f/m , dann ausgegeben
     *
     */
    public double frictionX(double m, double g, double frictionCoff, double angle)
    {
        //[m/s^2]
        double FG = weight * g;
        // [N]
        double FH =  ( FG * Math.sin( Math.toRadians(angle) ) );
        // FR = frictionCoff  * FN
        // [N]
        double FN =  ( frictionCoff *( FG * Math.cos(Math.toRadians(angle)  )  )  );

        // [N]
        double FHx =  (FH * Math.cos(Math.toRadians(angle)));
        double FNx =  (FN * Math.cos(Math.toRadians(angle)));

        // [N]
        //System.out.println(FHx+" "+FNx);
        double friction = FHx + FNx;
        if(FHx < 0){
            friction = FHx - FNx;
        }
        //if(FH + FN <= FG * frictionCoff) return 0;

        // [m/s^2]  F = m * a umgeformt zu:  a = F / m
        double acceleration = (double)(friction / weight);
        return acceleration;
    }

    /**Reibung (als Beschleunigung ausgegebene)
     * @param m Gewicht (in KG)
     * @param g Gravität
     * @param frictionCoff Reibungskoeffizient
     * @param angle Winkel
     * @return Beschleunigung der Reibungskraft
     * Formeln:
     * Gewichtskraft: FG = masse * gravitation
     * Hanganbtriebskraft: FH = FG * sin(a)  (a = angle)
     * Normalkraft: FN = FG * cos(a)
     * F = m * a umgeformt zu a = f/m , dann ausgegeben
     *
     */
    public double frictionY(double m,double g,double frictionCoff,double angle) {
        //[m/s^2]
        double FG = m * g;
        // [N]
        double FH = (( FG * Math.sin( Math.toRadians(angle)) ) );
        // [N]
        double FN = ( frictionCoff *( FG * Math.cos(Math.toRadians(angle)  )  )  );

        // [N]
        double FHy = (FH * Math.sin(Math.toRadians(angle)));
        double FNy = (FN * Math.sin(Math.toRadians(angle)));

        // [N]
        double friction = FHy + FNy;
        if(FNy < 0) {
            friction = FHy - FNy;
        }

        // [m/s^2]  F = m * a umgeformt zu:  a = F / m
        double acceleration = (float)(friction / weight );
        return acceleration;
    }


    /**
     * Schraeger Wurf
     * @param angle Winkel
     * @return zurueckgelegte Strecke
     * Weitere Formeln:
     * Beschleunigung:
     * [m/s^2] -g
     * Geschwindigkeit:
     * [m/s] v0 * sin(alpha) - g * t
     */
    public double projectileMotion(double angle)
    {
        // [m] v0 + sin(alpha) * t - 1/2 * g * t^2
        return (velocityY *Math.sin(angle) * TIME - 1/2 * GRAVITY * Math.pow(TIME,2));
    }


    /**
     * Bewegungen des visuellen Richtungsvektors der Kugel
     * @param shape die Shape der Kugel
     * Linie die auf Richtungsvektor der Kugel basiert wird in das Zentrum der Kugel Translatiert
     * Länge wird anhand des Radius der Kugel bestimmt
     */
    public void moveDirectionLine(Circle shape)
    {
        if(velocityX == 0 && velocityY == 0)
        {
            directionLine.setVisible(false);
            return;
        }
        else
        {
            directionLine.setVisible(true);
        }

        double x = shape.getCenterX() + shape.getTranslateX();
        double y = shape.getCenterY() + shape.getTranslateY();
        double normlength = ( 1 / (Math.sqrt( Math.pow(velocityX,2) + Math.pow(velocityY,2) ) ) );

        directionLine.setStartX( x );
        directionLine.setEndX(x + (  normlength  * velocityX) * shape.getRadius()  );

        directionLine.setStartY( y);
        directionLine.setEndY( y + (  normlength  * velocityY) * shape.getRadius()  );


    }

    /**
     * Ermitteln eines Winkel einer Geraden
     * @param x1 x Startwert der Gerade
     * @param y1 y Startwert der Gerade
     * @param x2 x Endwert der Gerade
     * @param y2 y Endwert der Gerade
     * @return Winkel der Geraden ( in Grad)
     */
    public double detectAngle(double x1,double y1,double x2,double y2)
    {
        if(y1 == y2) return 0;
        /*x1 = Math.abs(x1);
        x2 = Math.abs(x2);
        y1 = y1;
        y2 = y2;*/
         /*
        double x = Math.sqrt(Math.pow(x2-x1,2));
        double y = Math.sqrt(Math.pow(y2-y1,2));
        */

        double x = x2-x1;
        double y = y2-y1;
        //System.out.println("W: "+Math.toDegrees(Math.atan(y/x)));
        if(Math.toDegrees(Math.atan(y/x)) < 0){
            return Math.toDegrees(Math.atan(y/x)) +360;
        }
        // [°] Grad
        return Math.toDegrees(Math.atan(y/x));
    }


    /**
     * Berechnung von Distanz von Ball und anderes Objekt
     * @param shape2 das Objekt,welches überprüeft wird auf Kollision mit Ball
     * @return Distanz der beiden Objekte
     */
    public double calcDistance(Shape shape2)
    {
        return (double)Math.sqrt(  Math.pow(shape2.getTranslateX(),2) - Math.pow(elementView.getTranslateX(),2) +     // X
                Math.sqrt(  Math.pow(shape2.getTranslateY(),2) - Math.pow(elementView.getTranslateY(),2)  ));  // Y
    }

    public void save()
    {
       // saved = true;
        saveAccelerationX = accelerationX;
        saveAccelerationY = accelerationY;
        savesX = sX;
        savesY = sY;
        saveVelocitX = velocityX;
        saveVelocityY = velocityY;
    }

    public void reset()
    {
        accelerationX = saveAccelerationX;
        accelerationY = saveAccelerationY;
        sX = savesX;
        sY = savesY;
        velocityX = saveVelocitX;
        velocityY = saveVelocityY;

        ((Circle)elementView).setCenterX(sX);
        ((Circle)elementView).setCenterY(sY);

        double y = ((Circle)elementView).getCenterY() + ((Circle)elementView).getTranslateY();
        double x = ((Circle)elementView).getCenterX() + ((Circle)elementView).getTranslateX();
        velocityText.setX(x -(((Circle)elementView).getRadius())/2);
        velocityText.setY(y - ((Circle) elementView).getRadius()-10);

        moveDirectionLine((Circle)elementView);
    }


    public float distanceCircleY(Circle circle, Circle circle2)
    {
        return (float)(circle.getCenterY() - circle2.getCenterY());
    }


    public float distanceCircleX(Circle circle, Circle circle2)
    {
        return (float)(circle.getCenterX() - circle2.getCenterX());
    }


    public float normalsY(Circle circle, double colY)
    {
        return (float)(circle.getCenterY() - colY);
    }

    /**
     *
     * @param circle
     * @param colX
     * @return
     */
    public float normalsX(Circle circle, double colX)
    {
        return (float)(circle.getCenterY() - colX);
    }

    /**
     * Prozeduraler verlauf der Kollisionsueberpruefung
     */
    public void checkCollision(Shape shape)
    {
        calcDistance(shape);

    }

    public Line getDirectionLine() {
        return directionLine;
    }
    public Text getVelocityText() { return velocityText; }

    public void setVelocityText(Text velocityText){
        this.velocityText = velocityText;
    }

    public Shape getElementView(){
        return elementView;
    }
}
