package Model;

import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ElementEditorModel {
    // Eventhandler der Textfelder die hinzugefuegt werden bei einem Objekt,welches angeklickt wird

    private ChangeListener<Object> changeTranslateX;
    private ChangeListener<Object> changeTranslateY;
    private ChangeListener<Object> changeTranslateZ;
    private ChangeListener<Object> changeScaleX;
    private ChangeListener<Object> changeScaleY;
    private ChangeListener<Object> changeScaleZ;
    private ChangeListener<Object> changeWeight;
    private ChangeListener<Object> changeAccelerationX;
    private ChangeListener<Object> changeAccelerationY;
    private ChangeListener<Object> changeVelocityX;
    private ChangeListener<Object> changeVelocityY;
    private ChangeListener<Object> changeRotateX;
    private ChangeListener<Object> changeRotateY;
    private ChangeListener<Object> changeRotateZ;

    private EventHandler<KeyEvent> eventRotate;
    private EventHandler eventTranslate;
    private EventHandler eventScale;

    //Ober GridPane des ganzen Programmes
    private GridPane starterGrid;
    //grafische Szene welche fuer Animation zustaendig ist
    private Pane graphicGrid;

    //Node welches bei Keypress erscheint ( Taste R) wenn ein Node bereits ausgewaehlt (angeklickt) worden ist
    private Circle rotationNode = new Circle(100);
    //Node welches bei Keypress erscheint ( Taste S) wenn ein Node bereits ausgewaehlt (angeklickt) worden ist
    private Rectangle scaleNode = new Rectangle(100,100);

    //Beschreibt ob ToolWindow Fenster detached werden kann. ( Damit das nicht noch einmal passieren kann)
    private boolean modalOpen = false;
    private GraphicsObject currentShape;

    /**
     * Initialisierung einzelner Nodes und ihre Styles
     */
    public ElementEditorModel( )
    {
        rotationNode.setFill(Color.TRANSPARENT);
        scaleNode.setFill(Color.TRANSPARENT);
        rotationNode.setStyle("-fx-stroke: black;-fx-stroke-width: 5");
        scaleNode.setStyle("-fx-stroke: black;-fx-stroke-width: 5");
        scaleNode.setVisible(false);
        rotationNode.setVisible(false);
    }




    //public Scene getModalScene() { return modalScene; }
    //public GridPane getModalGrid() { return modalGrid; }
    //public Stage getModalStage() { return modalStage; }
   // public GraphicOb getCurrentShape() { return currentShape; }
    //ublic Timeline getToolWindow_time() { return toolWindow_time; }
    public ChangeListener<Object> getChangeAccelerationX() { return changeAccelerationX; }
    public ChangeListener<Object> getChangeAccelerationY() { return changeAccelerationY; }
    public ChangeListener<Object> getChangeRotateX() { return changeRotateX; }
    public ChangeListener<Object> getChangeRotateY() { return changeRotateY; }
    public ChangeListener<Object> getChangeRotateZ() { return changeRotateZ; }
    public ChangeListener<Object> getChangeScaleX() { return changeScaleX; }
    public ChangeListener<Object> getChangeScaleY() { return changeScaleY; }
    public ChangeListener<Object> getChangeScaleZ() { return changeScaleZ; }
    public ChangeListener<Object> getChangeTranslateX() { return changeTranslateX; }
    public ChangeListener<Object> getChangeTranslateY() { return changeTranslateY; }
    public ChangeListener<Object> getChangeTranslateZ() { return changeTranslateZ; }
    public ChangeListener<Object> getChangeVelocityX() { return changeVelocityX; }
    public ChangeListener<Object> getChangeVelocityY() { return changeVelocityY; }
    public ChangeListener<Object> getChangeWeight() { return changeWeight; }
    public EventHandler<KeyEvent> getEventRotate() { return eventRotate; }
    public EventHandler getEventScale() { return eventScale; }
    public EventHandler getEventTranslate() { return eventTranslate; }
    public GridPane getStarterGrid() { return starterGrid; }
    public Pane getGraphicGrid() { return graphicGrid; }
    //public Stage getMainStage() { return mainStage; }
    public Circle getRotationNode() { return rotationNode; }
    public Rectangle getScaleNode() { return scaleNode; }
    public boolean isModalOpen() { return modalOpen; }


    public void setModalOpen(boolean modalOpen) { this.modalOpen = modalOpen; }
    public void setEventRotate(EventHandler<KeyEvent> eventRotate) { this.eventRotate = eventRotate; }
    public void setEventScale(EventHandler eventScale) { this.eventScale = eventScale; }
    public void setEventTranslate(EventHandler eventTranslate) { this.eventTranslate = eventTranslate; }
    public void setCurrentShape(GraphicsObject currentShape) { this.currentShape = currentShape; }


    public void setChangeTranslateX(ChangeListener<Object> changeTranslateX) { this.changeTranslateX = changeTranslateX; }
    public void setChangeTranslateY(ChangeListener<Object> changeTranslateY) { this.changeTranslateY = changeTranslateY; }
    public void setChangeTranslateZ(ChangeListener<Object> changeTranslateZ) { this.changeTranslateZ = changeTranslateZ; }
    public void setChangeAccelerationX(ChangeListener<Object> changeAccelerationX) { this.changeAccelerationX = changeAccelerationX; }
    public void setChangeAccelerationY(ChangeListener<Object> changeAccelerationY) { this.changeAccelerationY = changeAccelerationY; }
    public void setChangeRotateX(ChangeListener<Object> changeRotateX) { this.changeRotateX = changeRotateX; }
    public void setChangeRotateY(ChangeListener<Object> changeRotateY) { this.changeRotateY = changeRotateY; }
    public void setChangeRotateZ(ChangeListener<Object> changeRotateZ) { this.changeRotateZ = changeRotateZ; }
    public void setChangeScaleX(ChangeListener<Object> changeScaleX) { this.changeScaleX = changeScaleX; }
    public void setChangeScaleY(ChangeListener<Object> changeScaleY) { this.changeScaleY = changeScaleY; }
    public void setChangeScaleZ(ChangeListener<Object> changeScaleZ) { this.changeScaleZ = changeScaleZ; }
    public void setChangeVelocityX(ChangeListener<Object> changeVelocityX) { this.changeVelocityX = changeVelocityX; }
    public void setChangeVelocityY(ChangeListener<Object> changeVelocityY) { this.changeVelocityY = changeVelocityY; }
    public void setChangeWeight(ChangeListener<Object> changeWeight) { this.changeWeight = changeWeight; }
    public void setStarterGrid(GridPane starterGrid) { this.starterGrid = starterGrid; }
    public void setGraphicGrid(Pane graphicGrid) { this.graphicGrid = graphicGrid; }
    //public void setMainStage(Stage mainStage) { this.mainStage = mainStage; }
    //public void setModalStage(Stage modalStage) { this.modalStage = modalStage; }
    //public void setModalGrid(GridPane modalGrid) { this.modalGrid = modalGrid; }
    //public void setModalScene(Scene modalScene) { this.modalScene = modalScene; }
}
