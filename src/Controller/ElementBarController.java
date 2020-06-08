package Controller;

import Model.GraphicScene;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ElementBarController {


    public Text springboardDummy;
    public Text spinnerDummy;
    public Text seasawDummy;
    @FXML
    private Circle ballDummy;
    @FXML
    private Rectangle blockDummy;
    @FXML private VBox elementBar;

    private GraphicScene graphicScene;


    public void initialize() {
        System.out.println(" init ElementBarController");

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
        seasawDummy.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Dragboard db = seasawDummy.startDragAndDrop(TransferMode.COPY);

                ClipboardContent content = new ClipboardContent();
                content.putString(seasawDummy.getId());
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

    public VBox getElementBar() {
        return elementBar;
    }

    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }


}

