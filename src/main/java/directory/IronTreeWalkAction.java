package directory;

import javafx.scene.control.TreeItem;

/**
 * Created by kristopherguzman on 4/7/16.
 *
 * Used to perform actions when first visiting a folder, and when returning back up to this folder
 */
public interface IronTreeWalkAction {
    void visitChild(TreeItem<IronFile> child);
    void preVisitDirectory(TreeItem<IronFile> folder);
    void postVisitDirectory(TreeItem<IronFile> folder);
}
