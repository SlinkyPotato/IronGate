package directory;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import org.json.JSONObject;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kristopherguzman on 3/6/16.
 */
public class EditorTreeCell extends TreeCell<String> { //only need the name of the folder, not an entire File object

    private static DataFormat dataFormat = new DataFormat("TREE_DRAG");

    public EditorTreeCell(TreeView<String> treeView) {

        //drop folder into new parent
        setOnDragDetected(args -> {

            if(getTreeItem() != null && !getTreeItem().getValue().equals(treeView.getRoot().getValue())) {
                //sends drag event to all nodes under cursor
                ClipboardContent content = new ClipboardContent();
                Dragboard db = treeView.startDragAndDrop(TransferMode.MOVE);
                EditorViewManager.draggedItems = treeView.getSelectionModel().getSelectedItems();
                content.put(dataFormat, "TREE_DRAG");
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
                args.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            args.consume();

        });

        setOnDragDropped(args -> {
            Dragboard db = args.getDragboard();
            if(db.hasContent(dataFormat)) { //if content exists, then we are dragging a list item, not tree item
                TreeItem<String> folder = getTreeItem();
                ObservableList<TreeItem<String>> children = treeView.getRoot().getChildren();
                for (TreeItem<String> f : EditorViewManager.draggedItems) {
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

            } else if(db.hasContent(TemplateListCell.dataFormat)){ //string list item is in content so we are dragging a list item
                String selectedTemplate = (String) db.getContent(TemplateListCell.dataFormat);
                System.out.println("drag template with key: " + selectedTemplate);
                JSONObject template = (JSONObject) EditorViewManager.json.get(selectedTemplate);

                //build tree item from template in json object
                generateTemplateItems(template, getTreeItem());
            }

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

    private void generateTemplateItems(JSONObject json, TreeItem<String> target) {
        Set<String> keys = json.keySet();
        for(String key : keys) {
            TreeItem<String> folder = new TreeItem<>(key);
            target.getChildren().add(folder);
            if(!json.get(key).equals(JSONObject.NULL)) {
                JSONObject subFolders = (JSONObject) json.get(key);
                generateTemplateItems(subFolders, folder);
            }
        }
    }

    @Override
    protected void updateItem(String item, boolean empty) {

        if(item != null && item.equals(getItem())) return;

        super.updateItem(item, empty);

        if(item != null && item.equals("")) {
            super.setText("");

        } else {
            super.setText(item);

        }
    }

}
