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
    This class handles manipulation of the Folder View. This includes
    directory searches, displaying directories, and all things directly changing
    the Folder View.

    Additionally, this class extends SimpleFileVisitor which uses java 8.
 */
public class FolderViewManager {
    private final Image hddIcon = new Image("/icons/hdd.png");
    private TreeView<IronFile> view;
    public static ObservableList<IronFile> taggedItems = FXCollections.observableArrayList();
    public static List<TreeItem<IronFile>> draggedItems; //use this to store items being dragged, because ClipBoard is a pain in the ass
    public static ObservableList<String> availableTags = FXCollections.observableArrayList();

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
        System.out.println("setting roots");
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
        if (OsUtils.isCompatible()) { // check for compatibility
            for (IronFile selectedIronFile : selectedItems) {
                selectedIronFile.setTag(tag);
                taggedItems.add(selectedIronFile); // add tagged item to list
            }
        }
    }

    public void deleteTags(ObservableList<String> listTags) {
        if (OsUtils.isCompatible()) { // check for compatibility
            for (IronFile taggedFile : taggedItems) {
                if (listTags.contains(taggedFile.getTag())) {
                    taggedFile.setTag("");
                }
            }
        }
    }
    /**
     * Check tags of files that are dropped or shown in the ListView
     *
     * @param roots root folders/hdd
     * */
    public void checkTags(IronFile[] roots) {
        if (OsUtils.isCompatible()) { // check for compatibility
            for (IronFile file : roots) {
                // Visit each file in root
                IronFileVisitor tagVisit = new IronFileVisitor();
                try {
                    Files.walkFileTree(file.toPath(), tagVisit);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    /**
     * Mac Specific code
     *
     * */

    /*public void setFileAttrForSelected() {
        for(TreeItem<IronFile> item : selectedFiles) {
            setFileAttr(item.getValue(), "test_attr_key", "test_attr_value");
        }
    }
    public void getFileAttrForSelected() {
        for(TreeItem<IronFile> item : selectedFiles) {
            getFileAttr(item.getValue(), "test_attr_key");
        }
    }
    public void setFileAttr(IronFile file, String key, String value) {
        if(OSDetection.OSType ==  OSDetection.OS.WINDOWS) {
            try {
                UserDefinedFileAttributeView view = Files.getFileAttributeView(file.toPath(), UserDefinedFileAttributeView.class);
                //might want to give unique prefix to tag keys to avoid collision with system metadata
                view.write(key, Charset.defaultCharset().encode(value));
            } catch(IOException e) { e.printStackTrace(); }
        } else if(OSDetection.OSType == OSDetection.OS.MAC) {
            String option = "";
            if(file.isDirectory()) {
                option = "-r";
            }
            String cmd = "xattr -w " + option + " " + key + " " + value + " " + file.getAbsolutePath();
            try {
                String output = command.run(cmd);
            } catch(IOException e) { e.printStackTrace(); }
        }
    }

    public String getFileAttr(IronFile file, String key) {
        if(OSDetection.OSType ==  OSDetection.OS.WINDOWS) {
            try {
                UserDefinedFileAttributeView view = Files.getFileAttributeView(file.toPath(), UserDefinedFileAttributeView.class);

                ByteBuffer buf = ByteBuffer.allocate(view.size(key));
                view.read(key, buf);
                buf.flip();
                return Charset.defaultCharset().decode(buf).toString();
            } catch(IOException e) { e.printStackTrace(); }
        } else if(OSDetection.OSType == OSDetection.OS.MAC) {
            System.out.println("file path: " + file.getAbsolutePath());
            String option = "";
            if(file.isDirectory()) {
                //option = "-r";
            }
            String cmd = "xattr -p " + option + " " + key + " " + file.getAbsolutePath(); //then append the attr command
            try {
                String output = command.run(cmd);
            } catch(IOException e) { e.printStackTrace(); }
        }
        return null;
    }
    public String removeFileAttr(IronFile file, String key) {
        if(OSDetection.OSType ==  OSDetection.OS.WINDOWS) {
            try {
                return (String) Files.getAttribute(file.toPath(), key);
            } catch(IOException e) { e.printStackTrace(); }
        } else if(OSDetection.OSType == OSDetection.OS.MAC) {
            System.out.println("file path: " + file.getAbsolutePath());
            String option = "";
            if (file.isDirectory()) {
                //option = "-r";
            }
            String cmd = "xattr -p " + option + " " + key + " " + file.getAbsolutePath(); //then append the attr command
            try {
                String output = command.run(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }*/
}
