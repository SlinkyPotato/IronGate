package launcher;

import directory.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

import java.util.ArrayList;
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
    @FXML private MenuItem toolsDeleteTags;
    @FXML private MenuItem toolsTagFiles;
    @FXML private ResourceBundle resources;
    @FXML private TreeView<IronFile> dirTree;
    @FXML private TreeView<String> editorView;
    @FXML private ListView<IronFile> templateListView;
    @FXML private TextField txtAddTag;
    @FXML private TextField txtSearchTag;
    @FXML private TextField txtNewFolderName;
    @FXML private TextField txtRenameFolder;
    @FXML private MenuBar menubar;
    @FXML private Label dragHereLabel;
    @FXML private ListView<IronFile> viewTags;
    @FXML private ListView<String> viewExistTags;
    private FolderViewManager manager;
    private EditorViewManager templateEditor;

    @FXML private void initialize() {
        dirTree.setCellFactory(FileViewTreeCell::new); //use custom tree cell for drag and drop
        editorView.setCellFactory(EditorTreeCell::new);
        menubar.setUseSystemMenuBar(true); //allows use of native menu bars, luckily an easy 1 liner
        templateEditor = new EditorViewManager(editorView);
        manager = new FolderViewManager(dirTree); // 2 statements in 1 line is best


        //sets the text of the renameFolder field to the name of the selected folder
        editorView.setOnMouseClicked(args -> {
            ObservableList<TreeItem<String>> selected = templateEditor.getSelected();
            if(selected.size() == 1 && selected.get(0).getValue() != null) { //fill in folder name if only one is selected
                txtRenameFolder.setText(selected.get(0).getValue());
            }

        });

    }
    /**
     * Initialize the drag and Drop event and other scene events
    * */
    public void initializeSceneEvents(Scene scene) {

        scene.setOnDragOver(args -> {
           args.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            args.consume();
        });

        scene.setOnDragDropped(args -> {

            Dragboard db = args.getDragboard();
            args.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            System.out.println("scene drop");
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

    /**
     * Events for creating templates
     * */

    @FXML private void onAddFolderClick(MouseEvent event) {
        TreeItem<String> folderItem = new TreeItem<>(txtNewFolderName.getText());
        List<TreeItem<String>> list = new ArrayList<>();
        list.add(folderItem);
        templateEditor.addItemsToSelected(list);
    }

    @FXML private void onDeleteFolderClick(MouseEvent event) {
        templateEditor.deleteSelectedItems();
    }

    @FXML private void onSetFolderNameClick(MouseEvent event) { //ironically this is giving problems
        List<TreeItem<String>> selected = templateEditor.getSelected();
        if(!txtRenameFolder.getText().equals("")) {
            for(TreeItem<String> item : selected) {
                System.out.println("renamed folder to " + txtRenameFolder.getText());
                item.setValue(txtRenameFolder.getText());

            }
        }
    }

}

