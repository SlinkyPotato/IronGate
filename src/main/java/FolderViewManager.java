package main.java;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    @author Kristopher Guzman kristopherguz@gmail.com
    @author Brian Patino patinobrian@gmail.com
 */
public class FolderViewManager {

    //private IronFileVisitor ironVisitor; // might be used later
    private final Image hddIcon = new Image("/main/resources/icons/hdd.png");
    private TreeView<IronFile> view;
    private CmdExecutor command;
    private List<TreeItem<IronFile>> selectedFiles;

    public FolderViewManager(TreeView<IronFile> dirTree) {
        /*ironVisitor = new IronFileVisitor(); // save this for later
        ironVisitor.setRoot(new TreeItem<>());*/ // save this for later

        OSDetection.getOS();
        view = dirTree;
        command = new CmdExecutor();

    }


    public void setRootDirectory(IronFile[] hardDrives) {
        view.setRoot(new FileTreeItem(new IronFile(""))); // needs a blank file as root
        for (IronFile hdd : hardDrives) {
            System.out.println(hdd.getName());
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

}
