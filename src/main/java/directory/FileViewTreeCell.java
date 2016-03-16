package directory;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * Created by kristopherguzman on 3/10/16.
 */
public class FileViewTreeCell extends TreeCell<IronFile> {

    public FileViewTreeCell(TreeView<IronFile> treeView) {



    }

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
