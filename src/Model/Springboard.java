package Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;

import static Helpers.Config.GRAVITY;

/**
 * @author: Patrick Pavlenko
 * individuelles Objekt: Sprungbrett
 */

public class Springboard extends GraphicsObject
{

    // Body
    Rectangle board = new Rectangle(500,20);
    Polyline poly = new Polyline();
    Polyline collisionLine = new Polyline();

    //Kollisionslinien als Line element
    Line[] outlines = {new Line(),new Line(),new Line(),new Line()};


    double[] linesEndPoints = new double[10];
    double[] lineStretchPoints = new double[10];


    double stretchDistance;
    double flexDistance;
    double currentDistance;

    double acceleration = 0;

    // [N] Federweg
    double s = 50;

    // [N] Kraft,die auf das Objekt ausgesetzt . ( Weil bzw. Ball darauf fällt)
    double collisionImpulse = 2000;


    public Springboard(double initialX, double initialY)
    {
        super(initialX, initialY);
        poly.getPoints().addAll(new Double[]{
                200.0, 250.0,  //Viereck
                200.0, 220.0,  // 2,3
                400.0, 220.0,  // 4,5
                400.0, 250.0,
                200.0, 250.0,

                400.0, 250.0,  //ab hier Feder
                200.0, 290.0,
                400.0, 330.0,
                200.0, 370.0,
                400.0, 410.0,
                200.0, 450.0,
                400.0, 490.0,
                200.0, 530.0,
                400.0, 570.0,
                200.0, 570.0,
        });

        poly.setStrokeWidth(1);
        updateCollisionLine();
        elementView = poly;
        elementView.setFill(Color.TRANSPARENT);
        elementView.setStroke(Color.BLUE);

        stretchDistance = getDistance();
        flexDistance = getDistance() - s;

        elementView.setTranslateX(300);
        elementView.setTranslateY(200);
        updateOutliners();

    }


        private double getDistance()
        {
            double x1 = poly.getPoints().set(8,poly.getPoints().get(8));
            double y1 = poly.getPoints().set(9,poly.getPoints().get(9));
            double x2 = poly.getPoints().set(28,poly.getPoints().get(28));
            double y2 = poly.getPoints().set(29,poly.getPoints().get(29));

            return Math.sqrt(  Math.pow( x2-x1 , 2 ) + Math.pow( y2-y1 , 2 )  );
        }

        private double featherConstant()
        {
            // [N] F = m * g      m = Gewicht Ball,welches drauf gelangt auf Feder
            double Fg = (getWeight()+1) * GRAVITY;
            //[N/m] Federkonstante  D = F(Federkraft) / S delta(Eine Einheit des Weges)    Einheiten:  [N/m] = N / s(meter)
            return  (Fg / 0.1);
        }


        /**
         *
         * @param m [Kg] Gewicht des Objektes
         * @param k [N / m] Federkonstante
         * @param s [N] Federweg
         * @param impulse [N] Kraft die bei Kollision zwischen Springbaord und Kugel entsteh
         *  FG = Gewichtskraft des Springboard
         *  FS = Spannkraft des Springboard
         *  FC = Kraft die bei Impuls zwischen Springboard und Ball entsteht
         */
        private double calcForce(double impulse)
        {
            // [N] F = m * g
            double FG = GRAVITY * weight.get();
            // [N]    [N] = [N/m] * [m]
            double FS = featherConstant() * s;
            // [N] Impulse welcher Bei Kollision zwischen Ball und Objekt eintrifft
            double FC = impulse;

            double F = 0;

            System.out.println("FG   "+FG);
            System.out.println("FC:  "+FC);
            System.out.println("FS   "+FS);

            // Wenn Ball nicht auf Objekt faellt ... ( Spannkraft = Gewichtskraft , Spannkraft wirkt gegen Gewichtskraft und neutralisieren sich damit)
            if((FG + FC) == FS)
            {
                // dann Gibt es nicht genuegend Kraft,die das Springboard zum verformen bringt
                return 0;
            }
            else
            {
                //andersfall verformt es sich
                // Gewichtskraft + Kollsionskraft - Spannkraft
                F = (FG + FC) - FS;
                System.out.println("defautl");
            }


            return F / weight.get();
        }

        public void moveElement()
        {
            double f = 0;
            double amplitude = s;
            double angularFrequency = (Math.PI *2) * f;
            double phaseAngle = 0;


            double oscillation = amplitude * Math.sin(angularFrequency * time + phaseAngle);

            updateCollisionLine();
        }

        /**
         * Updaten der Kollisionslinien,da Objekt sich verformt
         */
        private void updateCollisionLine()
        {
            // Oben [0,1,2,3]
            collisionLine.getPoints().add(poly.getPoints().get(2));
            collisionLine.getPoints().add(poly.getPoints().get(3));
            collisionLine.getPoints().add(poly.getPoints().get(4));
            collisionLine.getPoints().add(poly.getPoints().get(5));

            // Rechts [4,5,6,7]
            collisionLine.getPoints().add(poly.getPoints().get(4));
            collisionLine.getPoints().add(poly.getPoints().get(5));
            collisionLine.getPoints().add(poly.getPoints().get(26));
            collisionLine.getPoints().add(poly.getPoints().get(27));

            // Unten [8,9,10,11]
            collisionLine.getPoints().add(poly.getPoints().get(26));
            collisionLine.getPoints().add(poly.getPoints().get(27));
            collisionLine.getPoints().add(poly.getPoints().get(28));
            collisionLine.getPoints().add(poly.getPoints().get(29));

            //Links [12,13,14,15]
            collisionLine.getPoints().add(poly.getPoints().get(28));
            collisionLine.getPoints().add(poly.getPoints().get(29));
            collisionLine.getPoints().add(poly.getPoints().get(2));
            collisionLine.getPoints().add(poly.getPoints().get(3));


        }

    private void updateOutliners()
    {
        outlines[0].setStartX(poly.getPoints().get(2)+elementView.getTranslateX());
        outlines[0].setStartY(poly.getPoints().get(3)+elementView.getTranslateY());
        outlines[0].setEndX(poly.getPoints().get(4)+elementView.getTranslateX());
        outlines[0].setEndY(poly.getPoints().get(5)+elementView.getTranslateY());

        outlines[1].setStartX(poly.getPoints().get(4)+elementView.getTranslateX());
        outlines[1].setStartY(poly.getPoints().get(5)+elementView.getTranslateY());
        outlines[1].setEndX(poly.getPoints().get(26)+elementView.getTranslateX());
        outlines[1].setEndY(poly.getPoints().get(27)+elementView.getTranslateY());

        outlines[2].setStartX(poly.getPoints().get(26)+elementView.getTranslateX());
        outlines[2].setStartY(poly.getPoints().get(27)+elementView.getTranslateY());
        outlines[2].setEndX(poly.getPoints().get(28)+elementView.getTranslateX());
        outlines[2].setEndY(poly.getPoints().get(29)+elementView.getTranslateY());

        outlines[3].setStartX(poly.getPoints().get(28)+elementView.getTranslateX());
        outlines[3].setStartY(poly.getPoints().get(29)+elementView.getTranslateY());
        outlines[3].setEndX(poly.getPoints().get(2)+elementView.getTranslateX());
        outlines[3].setEndY(poly.getPoints().get(3)+elementView.getTranslateY());

        System.out.println(elementView.getTranslateX());
        System.out.println(outlines[0].getStartX());
        System.out.println(outlines[0].getStartY());
        System.out.println(outlines[0].getEndX());
        System.out.println(outlines[0].getEndY());
    }


    public void save()
        {
        /*
        for(int x = 0; x <= line.length-1;x++)
        {
            savelineEndX[x] = line[x].getEndX();
            savelineEndY[x] = line[x].getEndY();
            savelineStartX[x] = line[x].getStartX();
            savelineStartY[x] = line[x].getStartY();
        }
        saveBoardX = board.getTranslateX();
        saveBoardY = board.getTranslateY(); */
        }

        public void reset()
        {
        /*
        for(int x = 0; x <= line.length-1;x++)
        {
            line[x].setEndX(savelineEndX[x]);
            line[x].setEndY(savelineEndY[x]);
            line[x].setStartX(savelineStartX[x]);
            line[x].setStartY(savelineStartY[x]);
        }
        board.setTranslateX(saveBoardX);
        board.setTranslateY(saveBoardY);  */
        }


    public Line[] getOutlines() {
        return outlines;
    }
}
