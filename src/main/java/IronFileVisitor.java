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
public class IronFileVisitor extends SimpleFileVisitor<Path>{
    private TreeItem<IronFile> root;
    @Override
    public FileVisitResult visitFile(Path pathFile, BasicFileAttributes attrs) throws IOException {
        IronFile current = new IronFile(pathFile.toFile());
//        System.out.println(current.getAbsolutePath());
//        root.getChildren().add(new TreeItem<>(new IronFile(current.getAbsolutePath())));
        System.out.printf("Visiting file %s\n", pathFile);
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
        IronFile currentFile = new IronFile(dir.toAbsolutePath().toString());
        if (currentFile.getName().startsWith(".")) {
            System.out.printf("Skipped directory: %s\n", dir.getFileName());
            return FileVisitResult.SKIP_SUBTREE;
        } else {
            System.out.printf("About to visit directory: %s\n", dir.getFileName());
            root.getChildren().add(new TreeItem<>(currentFile));
            return FileVisitResult.CONTINUE;
        }
    }

    public TreeItem<IronFile> getRoot() {
        return root;
    }

    public void setRoot(TreeItem<IronFile> root) {
        this.root = root;
    }
}
