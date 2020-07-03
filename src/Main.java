import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Die Main Klasse für unser Programm
 */
public class Main extends Application {

    //Hauptszene des Programms
    private Scene mainScene;
    //Primaere Stage
    private Stage primaryStage;

    // Die GridPane der Starter Klasse ( Bzw die Ober- GridPane)
    private BorderPane mainPane;

    // der Zustaendige FXMLLoader dafuer
    private FXMLLoader mainPaneLoader = new FXMLLoader();


    @Override
    /**
     * FXMLloader laed View in GridPane und initalisiert Controller
     * GridPane wird dann in Szene gepackt
     */
    public void init() throws Exception {
        super.init();

        mainPane = (BorderPane)initFXML(mainPaneLoader,"./View/mainView.fxml", mainPane);
        mainScene = new Scene(mainPane,1700,980);
        initCSS(mainScene,"./css/style.css");
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;
        primaryStage.setTitle("Rolling Stones");
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param fxmlPath FXMLDatei die hinzugefuegt wird ( Dateiname reicht aus)
     * @param pane Eine x-beliebige Pane die der View bzw FXML Datei weidergegeben wird
     */
    private Pane initFXML(FXMLLoader loader, String fxmlPath, Pane pane)
    {
        try
        {
            pane = loader.load(getClass().getResource("./View/mainView.fxml"));
        }
        catch(Exception e)
        {
            System.out.println(e);
            System.out.println("----------- FXML DATEI NICHT GELADEN-------");
            System.out.println(fxmlPath);
        }
        return pane;
    }


    /**
     * @param scene die Hauptszene
     * @param cssFile Dateiname der zu hinzufuegende CSS Datei in die Szene ( bsp: style.css)
     * Momentan noch nicht implementiert.
     */
    private void initCSS(Scene scene,String cssFile)
    {
        try
        {
            scene.getStylesheets().add(Main.class.getClassLoader().getResource(cssFile).toExternalForm());
        }
        catch(Exception e)
        {
            System.out.println(e);
            System.out.println("-----------CSS DATEI NICHT GELADEN-------");
            System.out.println(cssFile);
        }
    }
}
