package main.java;

import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
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
    private final Image hddIcon = new Image("/main/resources/icons/hdd.png");
    private int NEST_COUNT = 0; //tracks number of nested calls when searching through files, TEMPORARY
    private IronFileVisitor ironVisitor;
    private TreeView<IronFile> view;

    public FolderViewManager(TreeView<IronFile> dirTree) {
        ironVisitor = new IronFileVisitor();
        ironVisitor.setRoot(new TreeItem<>());
        view = dirTree;
    }

    /**
    * Original method to set a specified directory as root.
    * */
    public void setRootDirectory(IronFile file) {
        ironVisitor.setRoot(new TreeItem<>(file));
//        createCellsFromRoot(file, root);
        createTree(new TreeItem<>(file));
    }
    /**
     * Overloaded method to set multiple hard drives as root.
     * */
    public void setRootDirectory(IronFile[] hardDrives) {
        for (IronFile hdd : hardDrives) { // loop through all hard drives
            TreeItem<IronFile> hddTreeItem = new TreeItem<>(hdd);
            hddTreeItem.setGraphic(new ImageView(hddIcon));
            createTree(hddTreeItem);
//            createCellsFromRoot(hdd, parentTreeItem);
//            ironVisitor.getRoot().getChildren().add(parentTreeItem); // set hdd to root
        }
    }

    private void createCellsFromRoot(File rootFile, TreeItem<IronFile> rootNode) {
        NEST_COUNT++;
        if(NEST_COUNT <= 6000) { //fixed value, TEMPORARY optimization
            for(IronFile file : IronFile.convertFiles(rootFile.listFiles())) {
                if(!file.getName().startsWith(".") && !file.getName().startsWith("$")) { //don't show system files that start with dot (ex: .filename .pythonfile)
                    TreeItem<IronFile> fileNode = new TreeItem<>(file);
                    rootNode.getChildren().add(fileNode);
                    if (file.isDirectory()) {
                        createCellsFromRoot(file, fileNode);
                    }
                }
            }
        }
    }

    private void createTree(TreeItem<IronFile> rootItem) {
        Path pathDir = Paths.get(rootItem.getValue().getAbsolutePath()); // get the path of the TreeItem
        try {
            Files.walkFileTree(pathDir, EnumSet.of(FileVisitOption.FOLLOW_LINKS), 2, ironVisitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        view.setRoot(ironVisitor.getRoot()); // set multiple hard drives as root
        view.setShowRoot(false); // do not show blank
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
