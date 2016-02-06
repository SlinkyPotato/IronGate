package gui;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import java.util.ResourceBundle;

public class Controller{
    @FXML
    private MenuItem menuOpen;

    @FXML private ResourceBundle resources;

    /*@FXML
    private void initialize() {
        menuOpen.setOnAction(openMenu());
    }

    private EventHandler<ActionEvent> openMenu() {
        EventHandler<ActionEvent> evenHand = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                menuOpen.setText("Somewhat right...");
            }
        };
        return evenHand;
    }*/

}

