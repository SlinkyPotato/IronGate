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
    private TreeView<IronFile> view;
    private TreeItem<IronFile> root;
    private int NEST_COUNT = 0; //tracks number of nested calls when searching through files, TEMPORARY

    public FolderViewManager(TreeItem<IronFile> root, TreeView<IronFile> dirTree) {
        this.root = root;
        this.view = dirTree;
    }

    public void setRootDirectory(IronFile file) {
        root = new TreeItem<>(file);
        view.setRoot(root);
        createCellsFromRoot(file, root);
    }

    private void createCellsFromRoot(File rootFile, TreeItem<IronFile> rootNode) {
        NEST_COUNT++;
        if(NEST_COUNT > 1000) { //fixed value, TEMPORARY optimization
            return;
        }

        for(IronFile file : IronFile.convertFiles(rootFile.listFiles())) {
            if(!file.getName().startsWith(".")) { //don't show system files that start with dot (ex: .filename .pythonfile)
                TreeItem<IronFile> fileNode = new TreeItem<>(file);
                rootNode.getChildren().add(fileNode);
                if (file.isDirectory()) {
                    createCellsFromRoot(file, fileNode);
                }
            }
        }
    }
    /**
     * Set the child of a given parent TreeItem
     *
     * @param parent file of the parent node
     * @param parentTreeItem TreeItem of the parent node
     * */
    /*@SuppressWarnings("ConstantConditions")
    private void setChildOfParent(final File parent, TreeItem<IronFile> parentTreeItem) {
        for (final IronFile file : IronFile.convertFiles(parent.listFiles())) {
            if (file.isDirectory()) {
                System.out.println("This is a directory");
            } else {
                parentTreeItem.getChildren().add(new TreeItem<>(file));
                System.out.println(file.getName());
            }
        }
    }*/
}
