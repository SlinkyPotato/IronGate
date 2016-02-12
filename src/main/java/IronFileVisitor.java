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
}
