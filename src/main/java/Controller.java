package main.java;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller{
    @FXML private MenuItem fileOpen;
    @FXML private MenuItem fileNew;
    @FXML private MenuItem fileTag;
    @FXML private MenuItem fileExit;
    @FXML private MenuItem editPreferences;
    @FXML private TreeView<File> dirTree;

    @FXML private ResourceBundle resources;

    @FXML private void initialize() {
        createTree();
    }

    // Create the directory Tree
    private void createTree() {
        dirTree.setRoot(new TreeItem<>());
        dirTree.setShowRoot(false);
        File[] roots = File.listRoots();
        for (File root : roots) {
            TreeItem<File> itemChild = new TreeItem<>(root);
            itemChild.setExpanded(false);
            dirTree.getRoot().getChildren().add(itemChild);
        }
        /*dirTree.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
            @Override
            public TreeCell<File> call(TreeView<File> param) {
                return new TreeFieldImpl();
            }
        });*/
    }

    private class TreeFieldImpl extends TreeCell<File> {
        public TreeFieldImpl() {
//            MenuItem
        }
    }
}

