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

        setOnDragEntered(args -> {
            if(args.getGestureSource().getClass().getName().equals("String")) {
                setStyle("-fx-background-color: aqua;");
            }
            args.consume();
        });

        setOnDragExited(args -> {
            setStyle("");
            args.consume();
        });

        setOnDragOver(args -> {
            if(args.getGestureSource().getClass().getName().equals("String")) {
                args.acceptTransferModes(TransferMode.COPY);
            }
            args.consume();
        });

        setOnDragDropped(args -> {

        });
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
