package directory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileReader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import utils.IronFileFilter;

/**
 * This class handles manipulation of the Folder View. This includes
 * directory searches, displaying directories, and all things directly changing
 * the Folder View.
 * <p>
 * Additionally, this class extends SimpleFileVisitor which uses java 8.
 * </p>
 */
public class FolderViewManager {
    private final Image hddIcon = new Image("/icons/hdd.png");
    private TreeView<IronFile> view;
    private IronFile[] roots;
    public static List<TreeItem<IronFile>> draggedItems; //use this to store items being dragged, because ClipBoard is a pain in the ass

    /**
     * Folder View Manager constructor, initializes the view for the file browser
     */
    public FolderViewManager(TreeView<IronFile> dirTree) {
        view = dirTree;
        view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // enable multi-select
    }

    /**
     * Sets the root directory of the file browser to the specified folder.
     *
     * @param file root folder/file to start browser view
     **/
    public void setRootDirectory(IronFile file) {
        FileTreeItem rootItem = new FileTreeItem(file);
        view.setRoot(rootItem);
    }

    public ObservableList<String> getFilteredList(ObservableList<String> list) {
        if(list != null && list.size() > 0) {
            ObservableList<String> cleanTagList = FXCollections.observableArrayList();
            cleanTagList.addAll(list.stream().filter(tag -> !list.contains(tag)).collect(Collectors.toList())); // checkout java 8 .stream() and .collect()
            return cleanTagList;
        }
        return null;
    }

    /**
     * Overloaded method that sets a collection of folders/files as file browser view.
     *
     * @param roots a collection of root folders to being from root
     */
    public void setRootDirectory(IronFile[] roots, ObservableList<String> tags) {
        this.roots = roots;
        view.setRoot(new FileTreeItem(new IronFile(""))); // needs a blank file as root
        for (IronFile hdd : roots) {
            FileTreeItem diskTreeItem = new FileTreeItem(hdd, tags);
            diskTreeItem.setGraphic(new ImageView(hddIcon));
            view.getRoot().getChildren().add(diskTreeItem);
        }
        view.setShowRoot(false); // hide the blank file
    }

    public ObservableList<TreeItem<IronFile>> getSelected() {
        return view.getSelectionModel().getSelectedItems();
    }

    public void setTags(ObservableList<TreeItem<IronFile>> selectedItems, String tag) {
        for (TreeItem<IronFile> selectedItem : selectedItems) {
            selectedItem.getValue().setTag(tag);
        }
    }

    public void deleteTags(ObservableList<TreeItem<IronFile>> selectedItems, ObservableList<String> listTags) {
        for(String tag : listTags) {
            for(TreeItem<IronFile> selectedItem : selectedItems) {
                selectedItem.getValue().removeTag(tag);
            }
        }
    }

    public void filterTreeView(ObservableList<String> tags) {
        ObservableList<TreeItem<IronFile>> roots = view.getRoot().getChildren();
        if(roots != null && roots.size() > 0) {
            IronFile[] fileRoots = new IronFile[view.getRoot().getChildren().size()];
            for(int i = 0; i < fileRoots.length; i++) { //convert to standard array
                fileRoots[i] = roots.get(i).getValue();
            }
            setRootDirectory(fileRoots, tags);
        }
    }

    /**
     *
     * "Pseudo Deletes" files selected by user based on tags. Instead of actually deleting
     * files, they are thrown in a junk folder. The user can delete that folder themselves through
     * the file system. Java deletion is irreversible
     *
     * @param roots folder/files to start deletion from
     * @param tags delete files with tags
     */
    public void deleteFiles(ObservableList<TreeItem<IronFile>> roots, ObservableList<String> tags, ListView<String> errorList) {
        if(tags.size() > 0 && roots.size() > 0) {
            File junkDir = new File(getClass().getClassLoader().getResource("userData/junkFiles").getPath());

            IronTreeWalkAction deleteAction = new IronTreeWalkAction() {

                private ArrayList<TreeItem<IronFile>> itemsToDelete = new ArrayList<>();

                @Override
                public void visitChild(TreeItem<IronFile> child) {
                    IronFile file = child.getValue();
                    for(String t : tags) {
                        if(file.getTags().contains(t)) {
                            try {
                                if(file.isDirectory()) {
                                    FileUtils.copyDirectoryToDirectory(file, junkDir);
                                    FileUtils.deleteDirectory(file);

                                } else {
                                    FileUtils.copyFileToDirectory(file, junkDir);
                                    FileUtils.forceDelete(file);
                                }
                                itemsToDelete.add(child);
                            } catch(Exception e){
                                System.out.println("CANNOT MOVE TO JUNK FOLDER");
                                errorList.getItems().add(file.getName());
                            }
                            break;
                        }
                    }
                }

                @Override
                public void preVisitDirectory(TreeItem<IronFile> folder) {

                }

                @Override
                public void postVisitDirectory(TreeItem<IronFile> folder) {
                    folder.getChildren().removeAll(itemsToDelete);
                }
            };

            IronFileFilter filter = new IronFileFilter();

            for(TreeItem<IronFile> root : roots) {
                walkTreeView(root, deleteAction, filter);
            }
        }
    }

    /**
     * Automatically tags selected folders and their descendants based on the names of their parents
     * @param selectedItems collection of selected folders/files
     */
    public void autoTagSelected(ObservableList<TreeItem<IronFile>> selectedItems) {
        long startTime = System.currentTimeMillis() / 1000;
        IronTreeWalkAction autoTagAction = new IronTreeWalkAction() {
            private Stack<String> tagStack = new Stack<>();

            @Override
            public void visitChild(TreeItem<IronFile> item) {
                Iterator<String> tagIterator = tagStack.iterator();
                IronFile file = item.getValue();
                file.setTag(file.getName());
                while (tagIterator.hasNext()) {
                    file.setTag(tagIterator.next());
                }

                //use extension to get further related tags
                if(item.isLeaf()) {
                    String extension = file.getExtension();
                    ExtensionMatcher matcher = new ExtensionMatcher();
                    List<String> related = matcher.getRelated(extension);
                    if(related != null && related.size() > 0) {
                        for(String tag : related) {
                            file.setTag(tag);
                        }
                    }
                }
            }

            @Override
            public void preVisitDirectory(TreeItem<IronFile> folder) {
                IronFile f = folder.getValue();
                tagStack.push(f.getName());
                f.setTag(f.getName());
            }

            @Override
            public void postVisitDirectory(TreeItem<IronFile> folder) {
                tagStack.pop();
            }
        };

        IronFileFilter filter = new IronFileFilter();
        for(TreeItem<IronFile> roots : selectedItems) {
            walkTreeView(roots, autoTagAction, filter);
        }

        System.out.println("AUTO TAG TIME: " + ((System.currentTimeMillis() / 1000) - startTime) + " seconds");
    }

    /**
     *
     * @param root the folder/file to start the traversal from
     * @param walkAction a class that performs custom actions during the tree traversal
     */
    private void walkTreeView(TreeItem<IronFile> root, IronTreeWalkAction walkAction, IronFileFilter filter) {
        walkAction.preVisitDirectory(root);
        if(!root.isLeaf()) {
            for (TreeItem<IronFile> child : root.getChildren()) {
                walkAction.visitChild(child);
                if (!child.isLeaf() && filter.accept(child.getValue(), child.getValue().getName())) {
                    walkTreeView(child, walkAction, filter);
                }
            }
        } else {
            walkAction.visitChild(root);
        }
        walkAction.postVisitDirectory(root);

    }

}
