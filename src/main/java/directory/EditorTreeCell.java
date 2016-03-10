package directory;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by kristopherguzman on 3/6/16.
 */
public class EditorTreeCell extends TreeCell<String> { //only need the name of the folder, not an entire File object

    public EditorTreeCell(TreeView<String> treeView) {

        //drop folder into new parent
        setOnDragDetected(args -> {

            if(getTreeItem() != null && !getTreeItem().getValue().equals("Template Root")) {
                //sends drag event to all nodes under cursor
                ClipboardContent content = new ClipboardContent();
                Dragboard db = treeView.startDragAndDrop(TransferMode.MOVE);
                EditorViewManager.draggedItems = treeView.getSelectionModel().getSelectedItems();
                content.putString("content");
                db.setContent(content);
            } else {
                args.setDragDetect(false);
            }

            args.consume();
        });

        setOnDragEntered(args -> {

            if(getTreeItem() != null && args.getGestureSource() != this)
                setStyle( "-fx-background-color: aqua;" );
            args.consume();
        });

        setOnDragExited(args -> {
            setStyle("");
            args.consume();

        });

        setOnDragOver(args -> {
            if(args.getGestureSource() != this) {
                args.acceptTransferModes(TransferMode.MOVE);
            }
            args.consume();
        });

        setOnDragDropped(args -> {
            Dragboard db = args.getDragboard();
            TreeItem<String> folder = getTreeItem();
            ObservableList<TreeItem<String>> children = treeView.getRoot().getChildren();

            for(TreeItem<String> f : EditorViewManager.draggedItems) {
                System.out.println("value: " + f.getValue());
                System.out.println("dropped file: " + f.getValue());
                f.getParent().getChildren().remove(f);
                if (folder != null) {
                    folder.getChildren().add(f);

                } else {
                    children.add(f); //add it to template root
                }

            }

            EditorViewManager.draggedItems = null;
            args.setDropCompleted(true);
            args.consume();

        });

        setOnDragDone(args -> {
            System.out.println("Transfer Mode: " + args.getTransferMode());
            if(args.getTransferMode() == TransferMode.MOVE) {
                System.out.println("remove source; " + getTreeItem().getValue());
                treeView.getRoot().getChildren().remove(getTreeItem()); //remove source

            }
        });

    }

    @Override
    protected void updateItem(String item, boolean empty) {

        if(item == getItem()) return;

        super.updateItem(item, empty);

        if(item == null) {
            super.setText(null);

        } else {
            super.setText(item);

        }
    }

}
