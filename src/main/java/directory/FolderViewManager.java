package directory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

/**
    This class handles manipulation of the Folder View. This includes
    directory searches, displaying directories, and all things directly changing
    the Folder View.

    Additionally, this class extends SimpleFileVisitor which uses java 8.

    @author Kristopher Guzman kristopherguz@gmail.com
    @author Brian Patino patinobrian@gmail.com
 */
public class FolderViewManager {
    private final Image hddIcon = new Image("/icons/hdd.png");
    private TreeView<IronFile> view;
    private ObservableList<IronFile> taggedItems = FXCollections.observableArrayList();
    public static List<TreeItem<IronFile>> draggedItems; //use this to store items being dragged, because ClipBoard is a pain in the ass

    /**
     * Folder View Manager constructor, initializes the view for the file browser
     * */
    public FolderViewManager(TreeView<IronFile> dirTree) {
        view = dirTree;
        view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // enable multi-select
    }
    /**
     * Sets the root directory of the file browser to the specified folder.
     * @param file root folder/file to start browser view
     **/
    public void setRootDirectory(IronFile file) {
        FileTreeItem rootItem = new FileTreeItem(file);
        view.setRoot(rootItem);
    }

    public boolean hasRoot() { return (view.getRoot() != null); }
    /**
     * Overloaded method that sets a collection of folders/files as file browser view.
     * @param hardDrives a collection of hard drives to being from root
     * */
    public void setRootDirectory(IronFile[] hardDrives) {
        view.setRoot(new FileTreeItem(new IronFile(""))); // needs a blank file as root
        for (IronFile hdd : hardDrives) {
            FileTreeItem diskTreeItem = new FileTreeItem(hdd);
            diskTreeItem.setGraphic(new ImageView(hddIcon));
            view.getRoot().getChildren().add(diskTreeItem);
        }
        view.setShowRoot(false); // hide the blank file
    }

    public ObservableList<TreeItem<IronFile>> getSelected() { return view.getSelectionModel().getSelectedItems(); }

    public void deleteSelectedItems() {

        ObservableList<TreeItem<IronFile>> selectedItems = view.getSelectionModel().getSelectedItems();
        ObservableList<TreeItem<IronFile>> children = view.getRoot().getChildren();

        for(TreeItem<IronFile> item : selectedItems) {
            if(item != null && !item.getValue().getName().equals("Template Root")) {
                children.remove(item);
            }
        }

    }

    public void setTags(ObservableList<IronFile> selectedItems, String tag) {
        for (IronFile selectedIronFile : selectedItems) {
            selectedIronFile.setTag(tag);
            taggedItems.add(selectedIronFile); // add tagged item to list
        }
    }

    public void deleteTags(ObservableList<String> listTags) {
        for (IronFile taggedFile : taggedItems) {
            if (listTags.contains(taggedFile.getTag())) {
                taggedFile.setTag("");
            }
        }
    }

    /**
     * Retrieves an Observable list of TreeItem<IronFile> of tagged items.
     * */
    public ObservableList<IronFile> getTaggedItems(String searchTag) {
        ObservableList<IronFile> listTagFiles = FXCollections.observableArrayList();
        for (IronFile taggedIronFile : taggedItems) {
            if (taggedIronFile.getTag().equals(searchTag)) {
                listTagFiles.add(taggedIronFile);
            }
        }
        return listTagFiles;
    }
}
