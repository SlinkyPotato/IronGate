package main.java;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.ResourceBundle;

public class Controller{
    @FXML private MenuItem fileOpen;
    @FXML private MenuItem fileNew;
    @FXML private MenuItem fileTag;
    @FXML private MenuItem fileExit;
    @FXML private MenuItem editPreferences;
    @FXML private TreeView<String> dirTree;

    @FXML private ResourceBundle resources;

    @FXML
    private void initialize() {
//        createTree();
    }

    // Create the directory Tree
    private void createTree() {
        TreeItem<String> root = new TreeItem<String>("Root");
        TreeItem<String> itemChild = new TreeItem<String>("Child");
        itemChild.setExpanded(false);
        root.getChildren().add(itemChild);
        dirTree.setRoot(root);
    }

}

