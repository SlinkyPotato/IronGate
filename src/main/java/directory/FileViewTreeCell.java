package directory;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import org.json.JSONObject;

import java.util.Set;

/**
 * Created by kristopherguzman on 3/10/16.
 */
public class FileViewTreeCell extends TreeCell<IronFile> {

    public FileViewTreeCell(TreeView<IronFile> treeView) {

        setOnDragEntered(args -> {
            Dragboard db = args.getDragboard();
            if (!db.hasFiles() && getTreeItem() != null) {
                setStyle("-fx-background-color: aqua;");
            }
            args.consume();
        });

        setOnDragExited(args -> {
            setStyle("");
            args.consume();
        });

        setOnDragOver(args -> {
            args.acceptTransferModes(TransferMode.COPY);
            args.consume();
        });

        setOnDragDropped(args -> {
            Dragboard db = args.getDragboard();
            if (db.hasContent(TemplateListCell.dataFormat)) { //only handle dropping of template
                String template = (String) db.getContent(TemplateListCell.dataFormat);
                JSONObject jsonObject = EditorViewManager.json.getJSONObject(template);
                TreeItem<IronFile> target = getTreeItem();
                if (target != null && target.getValue().isDirectory()) {
                    generateTreeItems(jsonObject, target);
                }
                args.consume();
            }
        });
    }

    private void generateTreeItems(JSONObject json, TreeItem<IronFile> target) {
        Set<String> keys = json.keySet();
        for (String key : keys) {
            String path = target.getValue().getPath();
            System.out.println(path + "/" + key);
            System.out.println("abs path: " + target.getValue().getAbsolutePath());
            IronFile ironFolder = new IronFile(path + "/" + key);
            ironFolder.mkdir();
            TreeItem<IronFile> folder = new TreeItem<>(ironFolder);
            target.getChildren().add(folder);
            if (!json.get(key).equals(JSONObject.NULL)) {
                JSONObject subFolders = (JSONObject) json.get(key);
                generateTreeItems(subFolders, folder);
            }
        }
    }

    @Override
    protected void updateItem(IronFile item, boolean empty) {
        if (item != null && item.equals(getItem())) return;

        super.updateItem(item, empty);

        if (item != null) {
            super.setText(item.getName());
        } else {
            super.setText("");
        }
    }
}
