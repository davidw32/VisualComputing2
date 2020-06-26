package Model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;

import static Helpers.Config.GRAVITY;

/**
 * @author: Patrick Pavlenko
 * individuelles Objekt: Sprungbrett
 */

public class Springboard extends Block
{

    private double acceleration = 0;
    private double startHeight;
    private boolean down = false;
    private boolean activated = false;
    private double s = 0;

    private Board board;

    public Springboard(double initialX, double initialY)
    {
        super(initialX, initialY);

        elementView.setStroke(Color.BLUE);
        ((Rectangle)elementView).setHeight(100);
        ((Rectangle)elementView).setWidth(100);
        board = new Board(0,0,this);
        initOutlines();
        updateOutlines();
        board.initOutlines();
        board.updateOutlines();




        startHeight = getHeight();
        Image img = new Image(getClass().getClassLoader().getResourceAsStream("img/Model_IMG/springboard.png"));
        elementView.setFill(new ImagePattern(img));
    }

    /**
     * Hier werden die Bewegungne + Kollisione aussgeführt
     * @param ball Der Ball,mit dem die Kollsionen + Bewegungen verglichen werden
     */
    public void move(Ball ball)
        {

            if(!activated)
            {
                s = energy(ball.getYVelocity(), ball.getWeight());
                setYVelocity(ball.getYVelocity());
                activated = true;
            }
            flex(ball);


        }

        /**
         * @param velocityY Geschwindigkeit der Y-Achse
         * @param weight gewicht
         *
         * @return
         */
        public double energy(double velocityY,double weight)
        {
            // [N/m] Federkonstante
            double k = 2.7;
            // [m] Federweg
            s = (-1*(-weight*GRAVITY)+ Math.sqrt(Math.pow(weight*GRAVITY,2)-4*((0.5)*k*(-0.5)*weight*Math.pow(velocityY,2)))/(k));

            System.out.println(s);
            //[N] Federkraft F = m * a  als: F = k * s
            double Fk = k * s;

            // [m/s^2] Beschleunigung a = F / m
            double acceleration = Fk/weight;

            setYAcceleration(-acceleration);
            return s;
        }

        /**
         * Deformation Feder und Translation des Springbrett
         */
        private void flex(Ball ball)
        {
            double difference = ball.getYPosition()+ball.radius() - board.getYPosition();
            if(!down)
            {
                if (getHeight() > startHeight - s && getYVelocity() > 1)
                {
                    setYVelocity(getYVelocity() + getYAcceleration() * time);
                    ((Rectangle) elementView).setHeight(getHeight() - difference);
                    setYPosition(getYPosition() + difference);
                    board.setYPosition(getYPosition() - board.getHeight() );
                }
                else
                {
                    down = true;
                    setYVelocity(0);
                }
                ball.setYVelocity(this.getYVelocity());
            }
            else
            {
                // Kugel geht wieder hoch
                if (getHeight() < startHeight)
                {
                    setYVelocity(getYVelocity() - getYAcceleration() * time);
                    ((Rectangle) elementView).setHeight(getHeight() + getYVelocity() * time);
                    setYPosition(getYPosition() - getYVelocity() * time);
                    board.setYPosition(getYPosition() - board.getHeight() );
                    ball.setYVelocity(-getYVelocity());
                }
                // Kugel springt ab von Feder ,nachdem sie wieder hochgedrueckt wird
                else
                {
                    activated = false;
                    down = false;
                    ball.setYVelocity(-getYVelocity());
                    ball.setYPosition(board.getYPosition()-ball.radius()-3);
                    ball.setSpringboardCollision(false);
                }

            }
            this.initOutlines();
            this.updateOutlines();
            board.initOutlines();
            board.updateOutlines();
        }

    @Override
    public void resetElement() {
        super.resetElement();
        this.activated = false;
        this.down = false;
        setHeight(startHeight);
        this.setYVelocity(0);
    }






    /**
     * Klasse des Brettes des Springboards
     * Dadurch kann gezielter beide Teile des Springboards bei Kollisionen verglichen werden
     */
    public class Board extends Block
    {

        private Springboard parentSpringboard;

        public Board(double x,double y,Springboard springboard)
        {
            super(x,y);
            setWidth(((Rectangle) springboard.getElementView()).getWidth());
            setHeight(20);
            elementView.setFill(Color.GREY);
            elementView.setStrokeWidth(3);
            elementView.setStroke(Color.BLACK);
            setXPosition(springboard.getXPosition());
            setYPosition(springboard.getYPosition() - getHeight() );
            parentSpringboard = springboard;
        }

        public Springboard getParentSpringboard() { return parentSpringboard; }
    }



    public Block getBoard() { return board; }
}
