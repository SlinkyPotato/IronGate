package directory;

import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.List;

/**
 * Created by kristopherguzman on 3/10/16.
 */
public class EditorViewManager {
    public static List<TreeItem<String>> draggedItems; //use this to store items being dragged, because ClipBoard is a pain in the ass
    private TreeView<String> view;

    /**
     * Folder View Manager constructor, initializes the view for the file browser
     * */
    public EditorViewManager(TreeView<String> tree) {
        view = tree;
        view.setRoot(new TreeItem<>("My Template"));
        view.getRoot().setExpanded(true);
        view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // enable multi-select
    }

    public boolean hasRoot() { return (view.getRoot() != null); }

    //adds tree items to selected cells
    public void addItemsToSelected(List<TreeItem<String>> newItems) {
        ObservableList<TreeItem<String>> selectedItems = view.getSelectionModel().getSelectedItems();
        if(selectedItems.size() == 0) {
            view.getRoot().getChildren().addAll(newItems);
        }

        for(TreeItem<String> item : selectedItems) { //loop through selected items
            item.getChildren().addAll(newItems); //add new items to each selected item
        }
    }

    public ObservableList<TreeItem<String>> getSelected() { return view.getSelectionModel().getSelectedItems(); }

    public void deleteSelectedItems() {

        ObservableList<TreeItem<String>> selectedItems = view.getSelectionModel().getSelectedItems();
        ObservableList<TreeItem<String>> children = view.getRoot().getChildren();
        for(TreeItem<String> item : selectedItems) {
            if((item != null) && !item.getValue().equals("Template Root")) {
                children.remove(item);
            }
        }
    }
}
