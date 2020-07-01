package Controller;

import Model.GraphicScene;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ElementBarController {


    public VBox springboardDummy;
    @FXML
    private VBox spinnerDummy;
    @FXML
    private VBox seesawDummy;
    @FXML
    private Circle ballDummy;
    @FXML
    private Rectangle blockDummy;
    @FXML
    private VBox elementBar;

    private GraphicScene graphicScene;


    public void initialize() {
        //System.out.println("Init ElementBarController");
        // addListeners(ballDummy);
        ballDummy.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Dragboard db = ballDummy.startDragAndDrop(TransferMode.COPY);

                ClipboardContent content = new ClipboardContent();
                content.putString(ballDummy.getId());
                db.setContent(content);

                event.consume();
            }
        });


        blockDummy.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Dragboard db = blockDummy.startDragAndDrop(TransferMode.COPY);

                ClipboardContent content = new ClipboardContent();
                content.putString(blockDummy.getId());
                db.setContent(content);

                event.consume();
            }
        });
        springboardDummy.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Dragboard db = springboardDummy.startDragAndDrop(TransferMode.COPY);

                ClipboardContent content = new ClipboardContent();
                content.putString(springboardDummy.getId());
                db.setContent(content);

                event.consume();
            }
        });
        seesawDummy.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Dragboard db = seesawDummy.startDragAndDrop(TransferMode.COPY);

                ClipboardContent content = new ClipboardContent();
                content.putString(seesawDummy.getId());
                db.setContent(content);

                event.consume();
            }
        });
        spinnerDummy.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Dragboard db = spinnerDummy.startDragAndDrop(TransferMode.COPY);

                ClipboardContent content = new ClipboardContent();
                content.putString(spinnerDummy.getId());
                db.setContent(content);

                event.consume();
            }
        });

    }

    private void addListeners(Node node) {

        node.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Dragboard db = node.startDragAndDrop(TransferMode.COPY);

                ClipboardContent content = new ClipboardContent();
                content.putString(node.getId());
                db.setContent(content);

                event.consume();
            }
        });

    }


    public VBox getElementBar() {
        return elementBar;
    }

    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }


}

