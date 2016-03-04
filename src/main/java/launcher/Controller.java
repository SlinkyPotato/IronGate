package launcher;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import directory.FolderViewManager;
import directory.IronFile;
import javafx.fxml.FXML;
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
import com.dropbox.core.*;
import com.dropbox.core.v2.*;
import webapp.DropboxController;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    @FXML private TextField txtSearchTag;
    @FXML private Button btnSearchTag;
    @FXML private ListView<IronFile> viewTags;
    private FolderViewManager manager;
    @FXML private MenuBar menubar;
    @FXML private Label dragHereLabel;

    @FXML private void initialize() {
        manager = new FolderViewManager(dirTree); // 2 statements in 1 line is best
        IronFile[] hardDrives = IronFile.listRoots(); // an array of hard drives
        menubar.setUseSystemMenuBar(true); //allows use of native menu bars, luckily an easy 1 liner
//        manager.setRootDirectory(hardDrives); //Ideally only show tree view of files the user drags in
//        initializeSceneEvents(); // initialize Drag and Drop feature
    }
    /**
     * Action event triggered when user clicks. This method will add tag directly to IronFile
     * Method name must match StartPage.fxml assigned `on Action`
     * */
    @FXML private void eventAddTag() {
        ObservableList<TreeItem<IronFile>> treeIronFileList = dirTree.getSelectionModel().getSelectedItems();
        ObservableList<IronFile> selectedIronFiles = FXCollections.observableArrayList();
        /** The following line converts ObservableList<TreeItem<IronFile> into ObservableList<IronFile> which is needed to display just the names.**/
        selectedIronFiles.addAll(treeIronFileList.stream().map(TreeItem::getValue).collect(Collectors.toList()));
        manager.setTags(selectedIronFiles, txtAddTag.getText());
    }
    /**
     * On Click event that will search and display files based on entered tag
     * */
    @FXML private void eventSearchTag() {
        ObservableList<IronFile> taggedItems = manager.getTaggedItems(txtSearchTag.getText());
        viewTags.setItems(taggedItems);
    }
    /**
    * Action on click event that will delete all tag information from selected files.
    * */
    @FXML private void eventDeleteTags() {
        ObservableList<TreeItem<IronFile>> selectedItems = dirTree.getSelectionModel().getSelectedItems(); // get list of selected files
        manager.deleteAllTags(selectedItems);
    }
    public void initializeSceneEvents() {
        Scene scene = dirTree.getScene();
        scene.setOnDragOver(args -> {
            Dragboard db = args.getDragboard();
            //System.out.println("dragging over");
            if(db.hasFiles()) {
                args.acceptTransferModes(TransferMode.COPY);
            } else { args.consume(); }
        });
        scene.setOnDragDropped(args -> {
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
        });
    }

/*    public void setScene(Scene scene) {
        this.scene = scene;
    }*/
}

