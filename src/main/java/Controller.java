package main.java;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sun.misc.Resource;
import sun.reflect.generics.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.ResourceBundle;

public class Controller{
    @FXML private MenuItem fileOpen;
    @FXML private MenuItem fileNew;
    @FXML private MenuItem fileTag;
    @FXML private MenuItem fileExit;
    @FXML private MenuItem editPreferences;
    @FXML private TreeView<IronFile> dirTree;
    @FXML private ResourceBundle resources;

    @FXML private void initialize() {
        FolderViewManager manager = new FolderViewManager(dirTree); // 2 statements in 1 line is best
//        IronFile[] hardDrives = IronFile.listRoots(); // an array of hard drives
//        manager.setRootDirectory(hardDrives);
        IronFile homeDir = new IronFile(System.getProperty("user.home")); // use this for specific directory
        manager.setRootDirectory(homeDir);
    }
}

