package launcher;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import directory.FolderViewManager;
import directory.IronFile;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import sun.misc.Resource;
import sun.reflect.generics.tree.Tree;
import com.dropbox.core.*;
import com.dropbox.core.v2.*;
import webapp.DropboxController;

import java.util.ResourceBundle;

public class Controller{
    @FXML private MenuItem fileOpen;
    @FXML private MenuItem fileNew;
    @FXML private MenuItem fileTag;
    @FXML private MenuItem fileExit;
    @FXML private MenuItem editPreferences;
    @FXML private MenuItem fileDropboxSignin;
    @FXML private TreeView<IronFile> dirTree;
    @FXML private MenuItem toolsTagFiles;
    @FXML private MenuItem toolsDeleteTags;
    @FXML private ResourceBundle resources;

    @FXML private void initialize() {
        final FolderViewManager manager = new FolderViewManager(dirTree); // 2 statements in 1 line is best
        IronFile[] hardDrives = IronFile.listRoots(); // an array of hard drives
        manager.setRootDirectory(hardDrives);

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

        DropboxController dbManager = new DropboxController();
        try {
            dbManager.test();
        } catch (DbxException e) {
            e.printStackTrace();
        }
//        directory.IronFile homeDir = new directory.IronFile(System.getProperty("user.home")); // use this for specific directory
//        manager.setRootDirectory(homeDir);
    }
}

