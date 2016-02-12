package main.java;

import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutionException;

/**
    This class handles manipulation of the Folder View. This includes
    directory searches, displaying directories, and all things directly changing
    the Folder View.

    Additionally, this class extends SimpleFileVisitor which uses java 8.

    @author kristopherguzman
    @author Brian Patino
 */
public class FolderViewManager {

    public static TreeView<String> view;
    private static TreeItem<String> root;
    private static int NEST_COUNT = 0; //tracks number of nested calls when searching through files, TEMPORARY

    private FolderViewManager() { }

    public static void setRootDirectory(File file) {

        root = new TreeItem<String>(file.getName());
        view.setRoot(root);

        createCellsFromRoot(file, root);
    }


    private static void createCellsFromRoot(File rootFile, TreeItem<String> rootNode) {

        NEST_COUNT++;
        if(NEST_COUNT > 6000) { //fixed value, TEMPORARY optimization
            return;
        }

        for(File f : rootFile.listFiles()) {
            if(!f.getName().startsWith(".")) { //don't show system files that start with dot (ex: .filename .pythonfile)
                TreeItem<String> fileNode = new TreeItem<String>(f.getName());
                rootNode.getChildren().add(fileNode);
                if (f.isDirectory()) {
                    createCellsFromRoot(f, fileNode);
                }
            }
        }
    }
}
