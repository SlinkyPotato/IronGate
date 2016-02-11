package main.java;

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
       /* Button btn = new Button();
        btn.setText("This is a test");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.print("Bingo you did something!");
            }
        });*/
        Parent root = FXMLLoader.load(getClass().getResource("/main/resources/StartPage.fxml"));
//        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        primaryStage.setTitle("Iron-gate!");
        primaryStage.setScene(new Scene(root, 990, 700));

        Scene scene = primaryStage.getScene(); // we get the scene from above
        scene.getStylesheets().clear(); // clear any styles
//        scene.getStylesheets().add("/main/resources/mainStyle.css"); // absolute path

        /**
         * Temporally omitted, needs to be transfered to the Controller Class
         */
        /*try { //try to set the tree view in the FolderViewManager class

            FolderViewManager.treeView = (TreeView<String>) scene.lookup("#folder-view-pane");
            File homeDir = new File(System.getProperty("user.home"));

            FolderViewManager.setRootDirectory(homeDir);
            System.out.println(homeDir.getName() + " set as root directory.");

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
