package directory;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author kristopherguzman
 */
public class EditorViewManager {
    public static List<TreeItem<String>> draggedItems; //use this to store items being dragged, because Clipboard is a pain in the ass
    private TreeView<String> view;
    private ListView<String> listView;
    public static JSONObject json;
    private String jsonPath;

    /**
     * Folder View Manager constructor, initializes the view for the file browser
     */
    public EditorViewManager(TreeView<String> tree, ListView<String> lv) {
        view = tree;
        listView = lv;
        view.setRoot(new TreeItem<>("My Template"));
        view.getRoot().setExpanded(true);
        view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // enable multi-select
        loadTemplatesToList();
    }

    public boolean hasRoot() {
        return (view.getRoot() != null);
    }

    //adds tree items to selected cells
    public void addItemsToSelected(List<TreeItem<String>> newItems) {
        ObservableList<TreeItem<String>> selectedItems = view.getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            view.getRoot().getChildren().addAll(newItems);
        }

        for (TreeItem<String> item : selectedItems) { //loop through selected items
            item.getChildren().addAll(newItems); //add new items to each selected item
        }
    }

    public ObservableList<TreeItem<String>> getSelected() {
        return view.getSelectionModel().getSelectedItems();
    }

    public boolean isEmpty() {
        return view.getRoot().getChildren().size() == 0;
    }

    public void deleteSelectedItems() {
        ObservableList<TreeItem<String>> selectedItems = view.getSelectionModel().getSelectedItems();
        ObservableList<TreeItem<String>> children = view.getRoot().getChildren();

        for (TreeItem<String> item : selectedItems) {
            if ((item != null) && !item.getValue().equals("Template Root")) {
                children.remove(item);
            }
        }
    }

    public void saveToJSON() {
        //convert tree items to json representation
        try {
            //create json entry
            TreeItem<String> rootItem = view.getRoot();
            JSONObject subFolders = new JSONObject();
            json.put(rootItem.getValue(), subFolders);
            traverseTree(rootItem, subFolders);

            FileWriter writer = new FileWriter(jsonPath);
            json.write(writer);
            writer.close();

            if (existsInList(rootItem.getValue())) {//add template to list if it is a new one
                System.out.println("adding new template to list");
                listView.getItems().add(rootItem.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean existsInList(String templateName) {
        ObservableList<String> listItems = listView.getItems();
        for (String i : listItems) {
            if (i.equals(view.getRoot().getValue())) {
                return false;
            }
        }
        return true;
    }

    //this must be called BEFORE saving templates
    private void loadTemplatesToList() {
        try {
            jsonPath = getClass().getClassLoader().getResource("userData/user_templates.json").getPath();
            BufferedReader reader = new BufferedReader(new FileReader(jsonPath));
            String jsonString = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                jsonString += line + "\n";
            }
            reader.close();
            json = new JSONObject(jsonString);
            System.out.println("****************json string***************** \n" + json.toString());

            Set<String> keys = json.keySet();
            for (String key : keys) {
                System.out.println(key);
                listView.getItems().add(key);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void traverseTree(TreeItem<String> item, JSONObject json) {
        for (TreeItem<String> child : item.getChildren()) {
            if (!child.isLeaf()) {
                JSONObject nestedFolder = new JSONObject();
                json.put(child.getValue(), nestedFolder);
                traverseTree(child, nestedFolder);

            } else {
                json.put(child.getValue(), JSONObject.NULL);
            }
        }
    }

}
