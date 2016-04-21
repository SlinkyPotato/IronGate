package directory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class extends TreeItem and generates child files dynamically for the file browser.
 *
 * @author Brian Patino patinobrian@gmail.com
 * @see <a href="https://docs.oracle.com/javafx/2/api/javafx/scene/control/TreeItem.html">Tree Item</a>
 */
public class FileTreeItem extends TreeItem<IronFile> implements Serializable {
    private boolean isLeaf;
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private ObservableList<String> searchCriteria;

    public FileTreeItem(IronFile rootFile) {
        super(rootFile);
        searchCriteria = FXCollections.emptyObservableList();
    }
    public FileTreeItem(IronFile rootFile, ObservableList<String> tags) {
        super(rootFile);
        if(tags == null) {
            searchCriteria = FXCollections.emptyObservableList();
        } else {
            searchCriteria = tags;
        }

    }

    public static List<FileTreeItem> convertFromTreeItem(List<TreeItem<IronFile>> li) {
        ArrayList<FileTreeItem> newList = new ArrayList<>();
        for (TreeItem<IronFile> item : li) {
            FileTreeItem i = new FileTreeItem(item.getValue());
            System.out.println("converted item value: " + i.getValue());
            newList.add(i);
        }
        return newList;
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
        if (f != null && f.isDirectory()) {
            IronFile[] files = f.listFiles();
            if (files != null) {
                ObservableList<FileTreeItem> children = FXCollections.observableArrayList();
                for (IronFile childFile : files) {
                    if(!searchCriteria.isEmpty()) {
                        System.out.println(searchCriteria + " VERSUS " + childFile.getTags());
                        for(String tag : searchCriteria) {
                            if(childFile.getTags().contains(tag)) {
                                children.add(new FileTreeItem(childFile));
                            }
                        }
                    } else {
                        children.add(new FileTreeItem(childFile));
                    }
                }
                return children;
            }
        }
        return FXCollections.emptyObservableList();
    }
}
