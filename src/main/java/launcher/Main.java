package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("/SimpleGUI.fxml").openStream());
        Controller controller = loader.getController();
        primaryStage.setTitle("Iron-gate!");
        Scene scene = new Scene(root, 990, 700);
        primaryStage.setScene(scene);
//        System.out.println(scene);
        controller.initializeSceneEvents(scene); // called after primary stage has been set
        scene.getStylesheets().clear(); // clear any styles
//        scene.getStylesheets().add("/main/resources/mainStyle.css"); // absolute path
        primaryStage.show(); // show the initialized stage
    }

    public static void main(String[] args) {
        launch(args);
    }
}
