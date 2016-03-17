package directory;

import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * This class handles the searching algorithm for tags in files.
 * <p>
 * Additionally, this class extends SimpleFileVisitor which uses java 8.
 *
 * @author Brian Patino
 * @author kristopherguzman
 */
public class IronFileVisitor extends SimpleFileVisitor<Path> {
    private TreeItem<IronFile> root;

    @Override
    public FileVisitResult visitFile(Path pathFile, BasicFileAttributes attrs) throws IOException {
        addExistTag(pathFile);
        System.out.printf("Visiting file %s\n", pathFile);
        return FileVisitResult.CONTINUE;
    }

    /**
     * {@inheritDoc}
     * This method must be overridden so that walking the tree can continue.
     */

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
        System.err.printf("Visiting failed for %s\n", file);
        return FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        IronFile currentFile = new IronFile(dir.toAbsolutePath().toString());
        if (currentFile.getName().startsWith(".")) { // Skip hidden folders
            System.out.printf("Skipped directory: %s\n", dir.getFileName());
            return FileVisitResult.SKIP_SUBTREE;
        } else {
            System.out.printf("About to visit directory: %s\n", dir.getFileName());
            return FileVisitResult.CONTINUE;
        }
    }

    @Override
    public FileVisitResult postVisitDirectory(Path pathFile, IOException e) throws IOException {
        addExistTag(pathFile);
        return FileVisitResult.CONTINUE;
    }

    private void addExistTag(Path pathFile) {
        IronFile current = new IronFile(pathFile.toFile());
        if (!current.getTag().isEmpty()) {
            FolderViewManager.taggedItems.add(current);
            FolderViewManager.availableTags.add(current.getTag());
        }
    }

    public TreeItem<IronFile> getRoot() {
        return root;
    }

    public void setRoot(TreeItem<IronFile> root) {
        this.root = root;
    }
}
