package Model;

import javafx.beans.value.ChangeListener;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.LinkedList;

import static Helpers.Config.GRAVITY;

/**
 * @author: Patrick Pavlenko
 * individuelles Objekt: Sprungbrett
 */

public class Springboard extends Block
{

    private double startHeight;
    private boolean down = false;
    private boolean activated = false;
    private double s = 0;
    private boolean collided = false;
    private boolean boarddragged = false;
    private boolean featherdragged = false;

    private ChangeListener changeX;
    private ChangeListener changeY;

    private Board board;

    public Springboard(double initialX, double initialY)
    {
        super(initialX, initialY);

        ((Rectangle)elementView).setHeight(100);
        ((Rectangle)elementView).setWidth(100);
        ((Rectangle)elementView).setStroke(Color.TRANSPARENT);
        board = new Board(0,0,this);
        initOutlines();
        updateOutlines();
        board.initOutlines();
        board.updateOutlines();

        startHeight = getHeight();
        Image img = new Image(getClass().getClassLoader().getResourceAsStream("img/Model_IMG/springboard.png"));
        elementView.setFill(new ImagePattern(img));

        changeX = (observable, oldValue, newValue) -> {
            featherdragged = true;
            if(oldValue != newValue && !boarddragged )
            {
                board.setXPosition(getXPosition() );
            }
            featherdragged = false;
        };

        changeY = (observable, oldValue, newValue) -> {
            featherdragged = true;
            if(oldValue != newValue && !boarddragged )
            {
                board.setYPosition(getYPosition() - board.getHeight() );
            }
            featherdragged = false;
        };

        xPosition.addListener(changeX);
        yPosition.addListener(changeY);

        //hier ändert sich die Farbe wenn das Objekt angeklickt wird
        isSelectedProperty().addListener((observable, oldValue, newValue) -> {
            setIsSelectedColor();
            board.setIsSelectedColor();
        });

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

        public void moveElement(LinkedList<GraphicsObject> obj)
        {

            boolean verifier = false;
            for(GraphicsObject o : obj)
            {
                if(o instanceof Ball)
                {
                    if(((Ball) o).springboardCollision)
                    {
                        verifier = true;
                        break;
                    }
                }
            }
            collided = verifier;
            if(collided) return;
            if (getHeight() < startHeight)
            {
                setYVelocity(getYVelocity() - getYAcceleration() * time);
                ((Rectangle) elementView).setHeight(getHeight() + getYVelocity() * time);
                setYPosition(getYPosition() - getYVelocity() * time);
                board.setYPosition(getYPosition() - board.getHeight() );
            }
            else
            {
                setYPosition(startY);
                setHeight(startHeight);
                activated = false;
                down = false;
            }

            this.initOutlines();
            this.updateOutlines();
            board.initOutlines();
            board.updateOutlines();
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
                if(getHeight() <= 15)
                {
                    setHeight(15);
                    ((Rectangle) elementView).setHeight(15);
                    setYPosition(getYPosition() + difference);
                    board.setYPosition(getYPosition() - board.getHeight() );
                    ball.setYPosition(board.getYPosition() - ball.getRadius() );
                    down = true;
                    setYVelocity(0);
                }
                else if (getHeight() > startHeight - s && getYVelocity() > 0)
                {
                    setYVelocity(getYVelocity() + getYAcceleration() * time);
                    if(getYVelocity() < 0)
                    {
                        setYVelocity(0);
                    }
                    ((Rectangle) elementView).setHeight(getHeight() - difference);
                    setYPosition(getYPosition() + difference);
                    board.setYPosition(getYPosition() - board.getHeight() );
                }
                else
                {
                    down = true;
                    ball.setYPosition(board.getYPosition()-ball.getRadius() );
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
                    ball.setYPosition(board.getYPosition()-ball.radius());
                    ball.setSpringboardCollision(false);
                }

            }
        }

    @Override
    public void resetElement() {
        super.resetElement();
        this.activated = false;
        this.down = false;
        collided = false;
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

        private ChangeListener changeXBoard;
        private ChangeListener changeYBoard;


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

            changeXBoard = (observable, oldValue, newValue) -> {
                boarddragged = true;
                if(oldValue != newValue && !featherdragged)
                {
                    parentSpringboard.setXPosition(getXPosition() );
                }
                boarddragged = false;
            };

            changeYBoard = (observable, oldValue, newValue) -> {
                boarddragged = true;
                if(oldValue != newValue && !featherdragged)
                {
                    parentSpringboard.setYPosition(getYPosition() + getHeight() );
                }
                boarddragged = false;
            };

            xPosition.addListener(changeXBoard);
            yPosition.addListener(changeYBoard);

            //hier ändert sich die Farbe wenn das Objekt angeklickt wird
            isSelectedProperty().addListener((observable, oldValue, newValue) -> {
                setIsSelectedColor();
                parentSpringboard.setIsSelectedColor();
            });
        }

        public Springboard getParentSpringboard() { return parentSpringboard; }
    }



    public Block getBoard() { return board; }
}
