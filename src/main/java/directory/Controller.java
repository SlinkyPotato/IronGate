package directory;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeView;

import java.util.ResourceBundle;

public class Controller{
    @FXML private MenuItem fileOpen;
    @FXML private MenuItem fileNew;
    @FXML private MenuItem fileTag;
    @FXML private MenuItem fileExit;
    @FXML private MenuItem editPreferences;
    @FXML private TreeView<IronFile> dirTree;
    @FXML private ResourceBundle resources;

    @FXML private void initialize() {
        FolderViewManager manager = new FolderViewManager(dirTree); // 2 statements in 1 line is best
        IronFile[] hardDrives = IronFile.listRoots(); // an array of hard drives
        manager.setRootDirectory(hardDrives);
//        directory.IronFile homeDir = new directory.IronFile(System.getProperty("user.home")); // use this for specific directory
//        manager.setRootDirectory(homeDir);
    }
}

