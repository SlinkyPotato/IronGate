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
    private TreeItem<File> root = new TreeItem<>();
    private final Image hddIcon = new Image("/main/resources/icons/hdd.png");
    @FXML private MenuItem fileOpen;
    @FXML private MenuItem fileNew;
    @FXML private MenuItem fileTag;
    @FXML private MenuItem fileExit;
    @FXML private MenuItem editPreferences;
    @FXML private TreeView<File> dirTree;
    @FXML private ResourceBundle resources;

    @FXML private void initialize() {
        createTree();
        // FolderViewManager.java calls are done here
    }

    // Create the directory Tree
    private void createTree() {
        // Get the hard disk drives and 2 level down
        File[] roots = File.listRoots();
        for (File hdd : roots) {
            TreeItem<File> parentTreeItem = new TreeItem<>(hdd);
            parentTreeItem.setGraphic(new ImageView(hddIcon));
//            setChildOfParent(parentTreeItem);
            parentTreeItem.getChildren().add(new TreeItem<>(new File("Users"))); // test
            root.getChildren().add(parentTreeItem); // set hdd to root

        }
        dirTree.setRoot(root);
        dirTree.setShowRoot(false);

        /*dirTree.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
            @Override
            public TreeCell<File> call(TreeView<File> param) {
                return new TreeFieldImpl();
            }
        });*/
    }

    private void setChildOfParent(TreeItem<File> parent) {
//        String rootPath = parent.getPath();
        try {
            // Loop through all of the files/folders from parent at depth 2
            Files.walkFileTree(Paths.get(parent.getValue().getAbsolutePath()), EnumSet.of(FileVisitOption.FOLLOW_LINKS), 2, new FolderViewManager()).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    File childFile = filePath.toFile(); // create a file from working path
                    TreeItem<File> itemChild = new TreeItem<>(childFile); // create a tree item from child file
                    parent.getChildren().add(itemChild);
                    itemChild.setExpanded(false);
                    System.out.println(filePath);
                }
            });
            /*Files.walk(Paths.get("C:\\Users\\Brian")).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
//                    System.out.println(filePath);
                }
            });*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class TreeFieldImpl extends TreeCell<File> {
        public TreeFieldImpl() {
//            MenuItem
        }
    }
}

