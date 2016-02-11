package main.java;

import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
    This class handles manipulation of the Folder View. This includes
    directory searches, displaying directories, and all things directly changing
    the Folder View.

    Additionally, this class extends SimpleFileVisitor which uses java 8.

    @author kristopherguzman
    @author Brian Patino
 */
public class FolderViewManager extends SimpleFileVisitor<Path>{
//    public static TreeView<String> treeView;
//    private static TreeItem<String> root;
//    private static int NEST_COUNT = 0; //tracks number of nested calls when searching through files, TEMPORARY

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//        System.out.printf("Visiting file %s\n", file);
        return FileVisitResult.CONTINUE;
    }
    /**
     * {@inheritDoc}
     * This method must be overridden so that walking the tree can continue.
     * */
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
        System.err.printf("Visiting failed for %s\n", file);
        return FileVisitResult.SKIP_SUBTREE;
    }
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//        System.out.printf("About to visit directory %s\n", dir);
        return FileVisitResult.CONTINUE;
    }

    /*public static void setRootDirectory(File file) {
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
*/}
