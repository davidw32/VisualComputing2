package Controller;

import Model.GraphicScene;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ElementBarController {


    @FXML
    private Circle ballDummy;
    @FXML
    private Rectangle blockDummy;

    private GraphicScene graphicScene;


    public void initialize() {


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
    }





    public void setGraphicScene(GraphicScene graphicScene) {
        this.graphicScene = graphicScene;
    }


}

