package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        Parent root = FXMLLoader.load(getClass().getResource("/StartPage.fxml"));
        primaryStage.setTitle("Iron-gate!");
        primaryStage.setScene(new Scene(root, 990, 700));
        Scene scene = primaryStage.getScene(); // we get the scene from above
        scene.getStylesheets().clear(); // clear any styles
//        scene.getStylesheets().add("/main/resources/mainStyle.css"); // absolute path
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
