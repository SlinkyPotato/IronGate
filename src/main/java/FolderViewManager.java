package main.java;

import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
    This class handles manipulation of the Folder View. This includes
    directory searches, displaying directories, and all things directly changing
    the Folder View.

    Additionally, this class extends SimpleFileVisitor which uses java 8.

    @author kristopherguzman
    @author Brian Patino patinobrian@gmail.com
 */
public class FolderViewManager {
    private final Image hddIcon = new Image("/main/resources/icons/hdd.png");
//    private IronFileVisitor ironVisitor; // might be used later
    private TreeView<IronFile> view;

    public FolderViewManager(TreeView<IronFile> dirTree) {
        /*ironVisitor = new IronFileVisitor(); // save this for later
        ironVisitor.setRoot(new TreeItem<>());*/ // save this for later
        view = dirTree;
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
    /**
     * Overloaded method that sets a collection of folders/files as file browser view.
     *
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
}
