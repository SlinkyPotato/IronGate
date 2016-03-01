package directory;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.CmdExecutor;
import utils.OSDetection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
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
//    private directory.IronFileVisitor ironVisitor; // might be used later
    private TreeView<IronFile> view;
    private CmdExecutor command;
    private List<TreeItem<IronFile>> selectedFiles;

    public FolderViewManager(TreeView<IronFile> dirTree) {
        /*ironVisitor = new directory.IronFileVisitor(); // save this for later
        ironVisitor.setRoot(new TreeItem<>());*/ // save this for later

        OSDetection.getOS();
        view = dirTree;
        command = new CmdExecutor();
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

    public void setSelectedFiles(List<TreeItem<IronFile>> files) {
        selectedFiles = files;
    }

    public void setFileAttrForSelected() {
        for(TreeItem<IronFile> item : selectedFiles) {
            setFileAttr(item.getValue(), "test_attr_key", "test_attr_value");
        }
    }

    public void getFileAttrForSelected() {
        for(TreeItem<IronFile> item : selectedFiles) {
            getFileAttr(item.getValue(), "test_attr_key");
        }
    }

    public void deleteFileAttrForSelected() {
        for(TreeItem<IronFile> item : selectedFiles) {
            deleteFileAttr(item.getValue(), "test_attr_key");
        }
    }

    public void setFileAttr(IronFile file, String key, String value) {
        if(OSDetection.OSType ==  OSDetection.OS.WINDOWS) {
            try {
                UserDefinedFileAttributeView view = Files.getFileAttributeView(file.toPath(), UserDefinedFileAttributeView.class);
                //might want to give unique prefix to tag keys to avoid collision with system metadata
                view.write(key, Charset.defaultCharset().encode(value));
            } catch(IOException e) {
                e.printStackTrace();
            }
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
            } catch(IOException e) {
                e.printStackTrace();
            }

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

    public void deleteFileAttr(IronFile file, String key) {
        if(OSDetection.OSType ==  OSDetection.OS.WINDOWS) {
            try {
                UserDefinedFileAttributeView view = Files.getFileAttributeView(file.toPath(), UserDefinedFileAttributeView.class);
                view.delete(key);
            } catch(IOException e) { e.printStackTrace(); }
        } else if(OSDetection.OSType == OSDetection.OS.MAC) {
            System.out.println("file path: " + file.getAbsolutePath());
            String cmd = "xattr -d " + key + " " + file.getAbsolutePath(); //then append the attr command
            try {
                String output = command.run(cmd);
            } catch(IOException e) { e.printStackTrace(); }
        }
    }
}
