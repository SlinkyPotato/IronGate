package directory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * This class extends TreeItem and generates child files dynamically for the file browser.
 *
 * @author Brian Patino patinobrian@gmail.com
 * @see <a href="https://docs.oracle.com/javafx/2/api/javafx/scene/control/TreeItem.html">Tree Item</a>
 */
public class FileTreeItem extends TreeItem<IronFile> {
    private boolean isLeaf;
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;

    public FileTreeItem(IronFile rootFile) {
        super(rootFile);
    }

    @Override
    public ObservableList<TreeItem<IronFile>> getChildren() {
        if (isFirstTimeChildren) { //if children of node have not been created, then do so
            isFirstTimeChildren = false;
            super.getChildren().setAll(buildChildren(this));
        }
        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        if (isFirstTimeLeaf) {
            isFirstTimeLeaf = false;
            IronFile f = getValue();
            isLeaf = f.isFile();
        }
        return isLeaf;
    }
    private ObservableList<FileTreeItem> buildChildren(TreeItem<IronFile> ironTreeItem) {
        IronFile f = ironTreeItem.getValue();

        System.out.println("file's parent: " + f.getParent());

        if (f != null && f.isDirectory()) {
            IronFile[] files = f.listFiles();
            if (files != null) {
                ObservableList<FileTreeItem> children = FXCollections.observableArrayList();

                for (IronFile childFile : files) {
                    if(childFile.filter.accept(childFile, childFile.getName())) {
                        children.add(new FileTreeItem(childFile));
                    }
                }
                return children;
            }
        }
        return FXCollections.emptyObservableList();
    }
}
