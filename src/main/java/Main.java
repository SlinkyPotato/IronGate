package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

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

        Parent root = FXMLLoader.load(getClass().getResource("StartPage.fxml"));
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setTitle("Iron-gate!");
        primaryStage.setScene(new Scene(root, screenBounds.getWidth(), screenBounds.getHeight())); // a scene is created here
        Scene scene = primaryStage.getScene(); // we get the scene from above
        scene.getStylesheets().clear(); // clear any styles
        scene.getStylesheets().add("/main/resources/mainStyle.css"); // absolute path
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
