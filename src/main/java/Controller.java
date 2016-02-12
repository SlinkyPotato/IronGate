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
    private TreeItem<IronFile> root = new TreeItem<>();
    private final Image hddIcon = new Image("/main/resources/icons/hdd.png");
    @FXML private MenuItem fileOpen;
    @FXML private MenuItem fileNew;
    @FXML private MenuItem fileTag;
    @FXML private MenuItem fileExit;
    @FXML private MenuItem editPreferences;
    @FXML private TreeView<String> dirTree;
    @FXML private ResourceBundle resources;

    @FXML private void initialize() {
        //createTree();
        // FolderViewManager.java calls are done here

        FolderViewManager.view = dirTree;
        File homeDir = new File(System.getProperty("user.home"));
        FolderViewManager.setRootDirectory(homeDir);
    }

    // Create the directory Tree
   /* private void createTree() {
        // Get the hard disk drives and 2 levels down
        IronFile[] roots = IronFile.listRoots();
        for (IronFile hdd : roots) {
            TreeItem<IronFile> parentTreeItem = new TreeItem<>(hdd);
            parentTreeItem.setGraphic(new ImageView(hddIcon));
            setChildOfParent(hdd, parentTreeItem);
            root.getChildren().add(parentTreeItem); // set hdd to root

        }
        dirTree.setRoot(root);
        dirTree.setShowRoot(false);

        /*dirTree.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
            @Override
            public TreeCell<File> call(TreeView<File> param) {
                return new TreeFieldImpl();
            }
        });
    }*/
    /**
     * Set the child of a given parent TreeItem
     *
     * @param parent file of the parent node
     * @param parentTreeItem TreeItem of the parent node
     * */
    @SuppressWarnings("ConstantConditions")
    private void setChildOfParent(final File parent, TreeItem<IronFile> parentTreeItem) {
        for (final IronFile file : IronFile.convertFiles(parent.listFiles())) {
            if (file.isDirectory()) {
                System.out.println("This is a directory");
            } else {
                parentTreeItem.getChildren().add(new TreeItem<>(file));
                System.out.println(file.getName());
            }
        }
    }

    private class TreeFieldImpl extends TreeCell<File> {
        public TreeFieldImpl() {
//            MenuItem
        }
    }
}

