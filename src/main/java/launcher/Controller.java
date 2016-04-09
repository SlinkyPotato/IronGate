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
    @FXML private TabPane tabView;
    @FXML private TextField txtAddTag;
    @FXML private TextField txtSearchTag;
    @FXML private Button btnSearchRemoveTag;
    @FXML private TextField txtNewFolderName;
    @FXML private TextField txtRenameFolder;
    @FXML private MenuBar menubar;
    @FXML private Label dragHereLabel;
    @FXML private ListView<String> searchTags;
    @FXML private ListView<String> viewExistTags;
    @FXML private ListView<String> templateListView;
    @FXML private Button saveTemplateButton;
    private FolderViewManager manager;
    private EditorViewManager templateEditor;

    @FXML private void initialize() {
        dirTree.setCellFactory(FileViewTreeCell::new); //use custom tree cell for drag and drop
        editorView.setCellFactory(EditorTreeCell::new);
        templateListView.setCellFactory(TemplateListCell::new);
        menubar.setUseSystemMenuBar(true); //allows use of native menu bars, luckily an easy 1 liner
        templateEditor = new EditorViewManager(editorView, templateListView);
        manager = new FolderViewManager(dirTree); // 2 statements in 1 line is best

        //sets the text of the renameFolder field to the name of the selected folder
        editorView.setOnMouseClicked(args -> {
            ObservableList<TreeItem<String>> selected = templateEditor.getSelected();
            if(selected.size() == 1 && selected.get(0).getValue() != null) { //fill in folder name if only one is selected
                txtRenameFolder.setText(selected.get(0).getValue());
            }
        });

        dirTree.setOnMouseClicked(args -> {
            ObservableList<TreeItem<IronFile>> selected = manager.getSelected();
            if(selected.size() == 1 && selected.get(0).getValue() != null) { //if we are selecting 1 valid file
                System.out.println(selected.get(0).getValue().getTags());
                ObservableList<String> tags = FXCollections.observableArrayList(selected.get(0).getValue().getTags());
                System.out.println("# of tags: " + tags.size());
                viewExistTags.setItems(tags);
            }
        });

        //when Editor is closed, remove "save template" button, no need for it to be visible
        saveTemplateButton.setVisible(false);
        saveTemplateButton.setManaged(false);
        tabView.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {

            int index = tabView.getSelectionModel().getSelectedIndex();
            if(index == 0) {
                saveTemplateButton.setVisible(false);
                saveTemplateButton.setManaged(false);
            } else {
                saveTemplateButton.setVisible(true);
                saveTemplateButton.setManaged(true);
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
            boolean success = false;
            if(db.hasFiles()) {
                IronFile[] roots = IronFile.convertFiles(db.getFiles()); // multi-folder drag and drop
                manager.setRootDirectory(roots, null);
                //manager.checkTags(roots);
                //viewExistTags.setItems(FolderViewManager.availableTags);
                success = true;
                dragHereLabel.setText("");
                dragHereLabel.setMaxWidth(0); //get rid of prompt text once user has dragged in files
            }
            args.setDropCompleted(success);
        });
    }

    /**
     * Action event triggered when user clicks. This method will add tag directly to IronFile
     * Method name must match SimpleGUI.fxml assigned `on Action`
     * */
    @FXML private void eventAddTag() {
        manager.setTags(dirTree.getSelectionModel().getSelectedItems(), txtAddTag.getText());
        viewExistTags.getItems().add(txtAddTag.getText());
    }
    @FXML private void eventRemoveTag() {
        ObservableList<String> allTags = viewExistTags.getItems();
        ObservableList<String> selectedTagsList = viewExistTags.getSelectionModel().getSelectedItems();
        manager.deleteTags(dirTree.getSelectionModel().getSelectedItems(), selectedTagsList);
        /* Create list that does not contain removed tags */
        ObservableList<String> cleanTagList = FXCollections.observableArrayList();
        cleanTagList.addAll(allTags.stream().filter(tag -> !selectedTagsList.contains(tag)).collect(Collectors.toList())); // checkout java 8 .stream() and .collect()
        viewExistTags.setItems(cleanTagList);
    }

    @FXML private void onAutoTagClick() {
        ObservableList<TreeItem<IronFile>> selected = dirTree.getSelectionModel().getSelectedItems();
        if(selected.size() > 0) {
            manager.autoTagSelected(selected);
        }
    }
    /**
     * On Click event that will search and display files based on entered tag
     * */
    @FXML private void eventSearchTag() {
        searchTags.getItems().add(txtSearchTag.getText());
        ObservableList<String> tags = searchTags.getItems();
        manager.filterTreeView(tags);
    }

    @FXML private void eventSearchRemoveTag() {
        ObservableList<String> tags = searchTags.getSelectionModel().getSelectedItems();

        if(tags != null && tags.size() > 0) {
            ObservableList<String> cleanTagList = FXCollections.observableArrayList();
            cleanTagList.addAll(tags.stream().filter(tag -> !tags.contains(tag)).collect(Collectors.toList())); // checkout java 8 .stream() and .collect()
            searchTags.setItems(cleanTagList);
        }

        manager.filterTreeView(searchTags.getItems());
    }

    /**
     * Events for creating templates
     * */

    @FXML private void onAddFolderClick(MouseEvent event) {

        if(!txtNewFolderName.getText().equals("")) {

            TreeItem<String> folderItem = new TreeItem<>(txtNewFolderName.getText());
            List<TreeItem<String>> list = new ArrayList<>();
            list.add(folderItem);
            templateEditor.addItemsToSelected(list);

        }
    }

    @FXML private void onDeleteFolderClick(MouseEvent event) {
        templateEditor.deleteSelectedItems();
    }

    @FXML private void onSetFolderNameClick(MouseEvent event) {
        List<TreeItem<String>> selected = templateEditor.getSelected();
        if(!txtRenameFolder.getText().equals("")) {
            for(TreeItem<String> item : selected) {
                System.out.println("renamed folder to " + txtRenameFolder.getText());
                item.setValue(txtRenameFolder.getText());

            }
        }
    }

    @FXML private void onTemplateSave(MouseEvent event) {
        if(!templateEditor.isEmpty()) {
            templateEditor.saveToJSON();
        }
    }

}

