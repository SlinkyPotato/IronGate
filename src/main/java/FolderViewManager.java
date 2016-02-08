package main.java;

import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;

/**
 * Created by kristopherguzman on 2/7/16.
 */

/**
    This class handles manipulation of the Folder View. This includes
    directory searches, displaying directories, and all things directly changing
    the Folder View.
 */
public class FolderViewManager {

    public static TreeView<String> treeView;
    private static TreeItem<String> root;
    private static int NEST_COUNT = 0; //tracks number of nested calls when searching through files, TEMPORARY

    private FolderViewManager() { }

    public static void setRootDirectory(File file) {

        root = new TreeItem<String>(file.getName());
        createCellsFromRoot(file, root);
        treeView.setRoot(root);
    }

    private static void createCellsFromRoot(File rootFile, TreeItem<String> rootNode) {

        NEST_COUNT++;

        if(NEST_COUNT > 6000) { //fixed value, TEMPORARY, must come up with better way to optimize load time of files

            return;
        }

        for(File f : rootFile.listFiles()) {

            if(!f.getName().startsWith(".")) { //don't show files that start with dot (ex: .filename .pythonfile)

                TreeItem<String> fileNode = new TreeItem<String>(f.getName());
                rootNode.getChildren().add(fileNode);

                if (f.isDirectory()) {

                    createCellsFromRoot(f, fileNode);

                }
            }

        }

    }

}
