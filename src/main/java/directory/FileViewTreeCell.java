package directory;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;

/**
 * @author kristopherguzman
 */
public class FileViewTreeCell extends TreeCell<IronFile> {
    public FileViewTreeCell(TreeView<IronFile> treeView) {}

    @Override
    protected void updateItem(IronFile item, boolean empty) {
        if(item == getItem()) return;
        super.updateItem(item, empty);
        if(item == null) {
            super.setText(null);
        } else {
            super.setText(item.getName());
        }
    }
}
