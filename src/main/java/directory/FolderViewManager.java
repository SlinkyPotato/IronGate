package directory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.OsUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * This class handles manipulation of the Folder View. This includes
 * directory searches, displaying directories, and all things directly changing
 * the Folder View.
 * <p>
 * Additionally, this class extends SimpleFileVisitor which uses java 8.
 * </p>
 */
public class FolderViewManager {
    private final Image hddIcon = new Image("/icons/hdd.png");
    private TreeView<IronFile> view;
    private IronFile[] roots;
    //public static ObservableList<IronFile> taggedItems = FXCollections.observableArrayList();
    public static List<TreeItem<IronFile>> draggedItems; //use this to store items being dragged, because ClipBoard is a pain in the ass
    //public static ObservableList<String> availableTags = FXCollections.observableArrayList();

    /**
     * Folder View Manager constructor, initializes the view for the file browser
     */
    public FolderViewManager(TreeView<IronFile> dirTree) {
        view = dirTree;
        view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // enable multi-select
    }

    /**
     * Sets the root directory of the file browser to the specified folder.
     *
     * @param file root folder/file to start browser view
     **/
    public void setRootDirectory(IronFile file) {
        FileTreeItem rootItem = new FileTreeItem(file);
        view.setRoot(rootItem);
    }

    public boolean hasRoot() {
        return (view.getRoot() != null);
    }

    /**
     * Overloaded method that sets a collection of folders/files as file browser view.
     *
     * @param roots a collection of root folders to being from root
     */
    public void setRootDirectory(IronFile[] roots, ObservableList<String> tags) {
        System.out.println("setting roots");
        this.roots = roots;
        view.setRoot(new FileTreeItem(new IronFile(""))); // needs a blank file as root
        for (IronFile hdd : roots) {
            FileTreeItem diskTreeItem = new FileTreeItem(hdd, tags);
            diskTreeItem.setGraphic(new ImageView(hddIcon));
            view.getRoot().getChildren().add(diskTreeItem);
        }
        view.setShowRoot(false); // hide the blank file
    }

    public ObservableList<TreeItem<IronFile>> getSelected() {
        return view.getSelectionModel().getSelectedItems();
    }

    public void setTags(ObservableList<TreeItem<IronFile>> selectedItems, String tag) {
        for (TreeItem<IronFile> selectedItem : selectedItems) {
            selectedItem.getValue().setTag(tag);
        }
    }

    public void deleteTags(ObservableList<TreeItem<IronFile>> selectedItems, ObservableList<String> listTags) {
        for(String tag : listTags) {
            for(TreeItem<IronFile> selectedItem : selectedItems) {
                selectedItem.getValue().removeTag(tag);
            }
        }
    }

    public void filterTreeView(ObservableList<String> tags) {
        ObservableList<TreeItem<IronFile>> roots = view.getRoot().getChildren();
        IronFile[] fileRoots = new IronFile[view.getRoot().getChildren().size()];
        for(int i = 0; i < fileRoots.length; i++) { //convert to standard array
            fileRoots[i] = roots.get(i).getValue();
        }
        setRootDirectory(fileRoots, tags);
    }

    private void walkTreeView(TreeItem<IronFile> root, ObservableList<String> tags) {
        for(TreeItem<IronFile> child : root.getChildren()) {
            for(String tag : tags) {
                if(!child.getValue().getTags().contains(tag)) { //if file does NOT contain a search tag
                    root.getChildren().remove(child);
                }
            }
            if(!child.isLeaf()) {
                walkTreeView(child, tags);
            }
        }
    }

    /**
     * Check tags of files that are dropped or shown in the ListView
     *
     * @param roots root folders/hdd
     */
//    public void checkTags(IronFile[] roots) {
//       // if (OsUtils.isCompatible()) { // check for compatibility
//            for (IronFile file : roots) {
//                // Visit each file in root
//                IronFileVisitor tagVisit = new IronFileVisitor();
//                try {
//                    Files.walkFileTree(file.toPath(), tagVisit);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//       // }
//    }

    /**
     * Retrieves an Observable list of TreeItem<IronFile> of tagged items.
     */
//    public ObservableList<IronFile> getTaggedItems(String searchTag) {
//        ObservableList<IronFile> listTagFiles = FXCollections.observableArrayList();
//        for (IronFile taggedIronFile : taggedItems) {
//            if (taggedIronFile.getTags().contains(searchTag)) {
//                listTagFiles.add(taggedIronFile);
//            }
//        }
//        return listTagFiles;
//    }

}
