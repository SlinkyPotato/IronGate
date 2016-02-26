package launcher;

import directory.FolderViewManager;
import directory.IronFile;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeView;
import com.dropbox.core.*;
import com.dropbox.core.v2.*;
import webapp.DropboxController;

import java.util.ResourceBundle;

public class Controller{
    @FXML private MenuItem fileOpen;
    @FXML private MenuItem fileNew;
    @FXML private MenuItem fileTag;
    @FXML private MenuItem fileExit;
    @FXML private MenuItem editPreferences;
    @FXML private MenuItem fileDropboxSignin;
    @FXML private TreeView<IronFile> dirTree;
    @FXML private ResourceBundle resources;



    @FXML private void initialize() {
        FolderViewManager manager = new FolderViewManager(dirTree); // 2 statements in 1 line is best
        IronFile[] hardDrives = IronFile.listRoots(); // an array of hard drives
        manager.setRootDirectory(hardDrives);
        DropboxController dbManager = new DropboxController();
        try {
            dbManager.test();
        } catch (DbxException e) {
            e.printStackTrace();
        }
//        directory.IronFile homeDir = new directory.IronFile(System.getProperty("user.home")); // use this for specific directory
//        manager.setRootDirectory(homeDir);
    }
}

