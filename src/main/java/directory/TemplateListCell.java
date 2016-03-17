package directory;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * @author kristopherguzman
 */
public class TemplateListCell extends ListCell<String> {

    public static DataFormat dataFormat = new DataFormat("LIST_DRAG");

    public TemplateListCell(ListView<String> view) {

        setOnDragDetected(args -> {

            String template = view.getSelectionModel().getSelectedItem();
            System.out.println("dragged template: " + template);
            ClipboardContent content = new ClipboardContent();
            content.put(dataFormat, template);
            Dragboard db = view.startDragAndDrop(TransferMode.COPY);
            db.setContent(content);

        });

    }

    @Override
    protected void updateItem(String item, boolean empty) {

        if (item != null && item.equals(getItem())) return;

        super.updateItem(item, empty);

        if (item != null && item.equals("")) {
            super.setText("");

        } else {
            super.setText(item);

        }
    }

}
