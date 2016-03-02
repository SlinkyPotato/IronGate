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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import sun.misc.Resource;
import sun.reflect.generics.tree.Tree;
import com.dropbox.core.*;
import com.dropbox.core.v2.*;
import webapp.DropboxController;

import java.util.List;
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
    @FXML private Button btnAddTag;
    @FXML private TextField txtAddTag;
    @FXML private TextField txtTagSearch;
    @FXML private Button btnSearchTag;
    @FXML private ListView<TreeItem<IronFile>> viewTags;

    @FXML private void initialize() {
        final FolderViewManager manager = new FolderViewManager(dirTree); // 2 statements in 1 line is best
        IronFile[] hardDrives = IronFile.listRoots(); // an array of hard drives
        manager.setRootDirectory(hardDrives);
        setEvents(manager);
    }

    private void setEvents(FolderViewManager manager) {
        /*dirTree.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent args) {
                ObservableList<TreeItem<IronFile>> selectedItems = dirTree.getSelectionModel().getSelectedItems();
                manager.setSelectedFiles(selectedItems);
            }
        });*/

        btnAddTag.setOnAction((event) -> { // new java 8 set mouse event
            ObservableList<TreeItem<IronFile>> selectedItems = dirTree.getSelectionModel().getSelectedItems(); // get list of selected files
//            manager.setSelectedFiles(selectedItems);
            manager.setTags(selectedItems, txtAddTag.getText());
        });
        btnSearchTag.setOnAction(event -> {
            ObservableList<TreeItem<IronFile>> taggedItems = manager.getTagedItems(txtTagSearch.getText());
            viewTags.setItems(taggedItems);
//            manager.displayTagedFiles(txtTagSearch.getText());
        });
        toolsDeleteTags.setOnAction(event -> {
            ObservableList<TreeItem<IronFile>> selectedItems = dirTree.getSelectionModel().getSelectedItems(); // get list of selected files
            manager.deleteAllTags(selectedItems);
//            manager.deleteFileAttrForSelected();
        });
    }
}

