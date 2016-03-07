package launcher;

import directory.FolderViewManager;
import directory.IronFile;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller{
    @FXML private MenuItem fileOpen;
    @FXML private MenuItem fileNew;
    @FXML private MenuItem fileTag;
    @FXML private MenuItem fileExit;
    @FXML private MenuItem editPreferences;
    @FXML private MenuItem fileDropboxSignin;
    @FXML private MenuItem toolsDeleteTags;
    @FXML private MenuItem toolsTagFiles;
    @FXML private ResourceBundle resources;
    @FXML private TreeView<IronFile> dirTree;
    @FXML private TextField txtAddTag;
    @FXML private TextField txtSearchTag;
    @FXML private MenuBar menubar;
    @FXML private Label dragHereLabel;
    @FXML private ListView<IronFile> viewTags;
    @FXML private ListView<String> viewExistTags;
    private FolderViewManager manager;
    private FolderViewManager templateEditor;

    @FXML private void initialize() {
        manager = new FolderViewManager(dirTree); // 2 statements in 1 line is best
//        IronFile[] hardDrives = IronFile.listRoots(); // an array of hard drives
        menubar.setUseSystemMenuBar(true); //allows use of native menu bars, luckily an easy 1 liner
//        manager.setRootDirectory(hardDrives); //Ideally only show tree view of files the user drags in
    }
    /**
     * Initialize the drag and Drop event and other scene events
    * */
    public void initializeSceneEvents(Scene scene) {
        scene.setOnDragDropped(args -> {
            Dragboard db = args.getDragboard();
            args.acceptTransferModes(TransferMode.COPY);
            boolean success = false;
            if(db.hasFiles()) {
                IronFile[] roots = IronFile.convertFiles(db.getFiles());
                manager.setRootDirectory(roots);
                success = true;
                dragHereLabel.setText("");
                dragHereLabel.setMaxWidth(0);
            }
            args.setDropCompleted(success);
        });
    }
    /**
     * Action event triggered when user clicks. This method will add tag directly to IronFile
     * Method name must match StartPage.fxml assigned `on Action`
     * */
    @FXML private void eventAddTag() {
        ObservableList<TreeItem<IronFile>> treeIronFileList = dirTree.getSelectionModel().getSelectedItems();
        ObservableList<IronFile> selectedIronFiles = FXCollections.observableArrayList();
        /* The following line converts ObservableList<TreeItem<IronFile> into ObservableList<IronFile> which is needed to display just the names.*/
        selectedIronFiles.addAll(treeIronFileList.stream().map(TreeItem::getValue).collect(Collectors.toList()));
        manager.setTags(selectedIronFiles, txtAddTag.getText());
        ObservableList<String> tagsList = FXCollections.observableArrayList(txtAddTag.getText());
        tagsList.addAll(viewExistTags.getItems());
        viewExistTags.setItems(tagsList);
    }
    @FXML private void eventRemoveTag() {
        ObservableList<String> allTags = viewExistTags.getItems();
        ObservableList<String> selectedTagsList = viewExistTags.getSelectionModel().getSelectedItems();
        ObservableList<String> cleanTagList = FXCollections.observableArrayList();
        manager.deleteTags(selectedTagsList);
        /* Create list that does not contain removed tags */
        cleanTagList.addAll(allTags.stream().filter(tag -> !selectedTagsList.contains(tag)).collect(Collectors.toList())); // checkout java 8 .stream() and .collect()
        viewExistTags.setItems(cleanTagList);
    }
    /**
     * On Click event that will search and display files based on entered tag
     * */
    @FXML private void eventSearchTag() {
        ObservableList<IronFile> taggedItems = manager.getTaggedItems(txtSearchTag.getText());
        viewTags.setItems(taggedItems);
    }

    @FXML private void eventSearchRemoveTag() {
//        ObservableList<TreeItem<IronFile>> treeIronFile
    }
}

