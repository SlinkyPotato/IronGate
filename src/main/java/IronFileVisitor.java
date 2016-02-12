package main.java;

import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.File;

/**
 * Created by kristopherguzman on 2/11/16.
 */
public class IronFileVisitor extends SimpleFileVisitor<Path> {

    private TreeItem<String> currentDirectory;

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

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
        System.out.printf("About to visit directory %s\n", dir);

        currentDirectory = new TreeItem<String>(dir.getFileName().toString());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
        System.out.printf("About to visit directory %s\n", dir);


        return FileVisitResult.CONTINUE;
    }

}
