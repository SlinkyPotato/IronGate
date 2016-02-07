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

        setUserAgentStylesheet(STYLESHEET_CASPIAN);
       /* Button btn = new Button();
        btn.setText("This is a test");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.print("Bingo you did something!");
            }
        });*/

        Parent root = FXMLLoader.load(getClass().getResource("StartPage.fxml"));
        Scene scene = primaryStage.getScene();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        //scene.getStylesheets().add("style_main.css"); //Error here...

        primaryStage.setTitle("Iron-gate!");
        primaryStage.setScene(new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
