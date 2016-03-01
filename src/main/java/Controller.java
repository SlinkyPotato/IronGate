package main.java;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
    @FXML private MenuBar menubar;
    @FXML private TreeView<IronFile> dirTree;
    @FXML private MenuItem toolsTagFiles;
    @FXML private MenuItem toolsDeleteTags;
    @FXML private ResourceBundle resources;
    @FXML private Label dragHereLabel;

    FolderViewManager manager;

    @FXML private void initialize() {

        manager  = new FolderViewManager(dirTree);

        IronFile[] hardDrives = IronFile.listRoots(); // an array of hard drives
       // manager.setRootDirectory(hardDrives);

        menubar.setUseSystemMenuBar(true); //allows use of native menu bars, luckily an easy 1 liner

        /**
         * Tell the manager that new files have been selected
         */
        dirTree.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent args) {

                ObservableList<TreeItem<IronFile>> selectedItems = dirTree.getSelectionModel().getSelectedItems();

                manager.setSelectedFiles(selectedItems);

            }

        });


        toolsTagFiles.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                manager.setFileAttrForSelected();

            }

        });

        toolsDeleteTags.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                manager.deleteFileAttrForSelected();

            }

        });

    }

    public void initializeSceneEvents() {

        Scene scene = dirTree.getScene();

        scene.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent args) {

                Dragboard db = args.getDragboard();
                //System.out.println("dragging over");

                if(db.hasFiles()) {

                    args.acceptTransferModes(TransferMode.COPY);

                } else { args.consume(); }
            }

        });


        scene.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent args) {

                Dragboard db = args.getDragboard();
                args.acceptTransferModes(TransferMode.COPY);

                boolean success = false;

                if(db.hasFiles()) {

                    System.out.println("dropped file(s)");

                    IronFile[] roots = IronFile.convertFiles(db.getFiles());
                    manager.setRootDirectory(roots);
                    success = true;
                    dragHereLabel.setText("");
                    dragHereLabel.setMaxWidth(0);

                }

                args.setDropCompleted(success);

            }

        });

    }
}

