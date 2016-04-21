package directory;

import javafx.collections.ObservableList;
import utils.CmdExecutor;
import utils.IronFileFilter;
import utils.OsUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This class extends the java File class and returns the filename for toString()
 */
public class IronFile extends File implements Serializable {
    private boolean isRoot = true;
    public IronFileFilter filter;
    private ArrayList<String> localTags;
    private CmdExecutor cmd;
    final private String ATTR_TYPE = "tags.";
    private UserDefinedFileAttributeView fileAttributeView;
    public enum FilterFlags { HIDE_FILE, SHOW_FILE }
    private FilterFlags flags;

    public FilterFlags getFlags() { return flags; }
    public void setFlags(FilterFlags flag) { flags = flag; }

    /**
     * Construct an IronFile that extends File
     *
     * @param pathname the path where the file is located
     */
    public IronFile(String pathname) {
        super(pathname);
        isRoot = (getParent() == null);
        localTags = new ArrayList<>();
        filter = new IronFileFilter();
        fileAttributeView = Files.getFileAttributeView(this.toPath(), UserDefinedFileAttributeView.class);
        cmd = new CmdExecutor();
        if(isDirectory()) {
            flags = FilterFlags.HIDE_FILE;
        } else { flags = FilterFlags.SHOW_FILE; }
    }

    /**
     * Construct an IronFile that extends File. This is an overloaded method
     *
     * @param file file that can be passed instead of path (string)
     */
    public IronFile(File file) {
        super(file.getPath());
        isRoot = (getParent() == null);
        localTags = new ArrayList<>();
        filter = new IronFileFilter();
        fileAttributeView = Files.getFileAttributeView(this.toPath(), UserDefinedFileAttributeView.class);
        cmd = new CmdExecutor();
        if(isDirectory()) {
            flags = FilterFlags.HIDE_FILE;
        } else { flags = FilterFlags.SHOW_FILE; }
    }

    /**
     * FileVisitor can't get the instance of IronFile with the correct tags, so there's no simple
     * way to cache all the tags at once. Instead, when user clicks on file we load the tags exactly
     * once and then use the cached list there after
     *
     * @return
     */

    @Override
    public IronFile[] listFiles() {
        return convertFiles(super.listFiles());
    }

    public String getExtension() {
        String path = getPath();
        String ext = null;
        int slashUnix = path.lastIndexOf("/");
        int slashWin = path.lastIndexOf("\\");
        int existingSlash = Math.max(slashUnix, slashWin);
        if(existingSlash > -1) {
            String fullName = path.substring(existingSlash, path.length());
            String[] split = fullName.split("\\.");
            if(split.length > 1)
                ext = split[1];

        }
        return ext;
    }

    /**
     * Return the available hard drive disks.
     */
    public static IronFile[] listRoots() {
        return convertFiles(File.listRoots());
    }

    public static IronFile[] convertFiles(File[] files) {
        IronFile[] ironFiles = new IronFile[files.length];
        for (int i = 0; i < files.length; i++) {
            ironFiles[i] = new IronFile(files[i]);
        }
        return ironFiles;
    }

    public static IronFile[] convertFiles(List<File> files) {
        IronFile[] ironFiles = new IronFile[files.size()];
        for (int i = 0; i < files.size(); i++) {
            ironFiles[i] = new IronFile(files.get(i));
        }
        return ironFiles;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    @Override
    public String toString() {
        return (isRoot) ? this.getAbsolutePath() : this.getName();
    }

    //check tags through the local list of tags instead of through the file system

//    public String getTag(String tag) {
//        try {
//            if(OsUtils.isCompatible()) {
//                return getFileAttribute(tag);
//
//            } else if(OsUtils.isMac()) {
//                return getFileAttrMac(tag);
//            }
//
//        } catch (IOException e) {
//            System.out.println("FAILED: tag could not be retrieved.");
//        }
//        if(!tags.contains(tag))
//            tags.add(tag);
//
//        return tag;
//    }

    public boolean meetsCriteria(ObservableList<String> tags) {
        for(String criteria : tags) {
            if(!getTags().contains(criteria)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<String> getTags() {
        if(localTags.isEmpty()) {
            try {
                String[] output = null;
                ArrayList<String> tags = new ArrayList<>();
                if(OsUtils.isCompatible()) {
                    output = getAllAttributes();
                } else {
                    output = getAllAttrMac();
                }
                if(output != null) {
                    for(String t : output) {
                        if(!t.isEmpty()) {
                            tags.add(t);
                            localTags.add(t);
                        }
                    }
                }
                return tags;

            } catch(Exception e) { e.printStackTrace(); }
        }
        return localTags;
    }

    /**
     * EDIT THIS so tags entered with spaces have an underscored inserted. Otherwise tags
     * with spaces are saved as two separate tags
     * @param tag
     */
    public void setTag(String tag) {
        if(!localTags.contains(tag)) {
            try {
                if (OsUtils.isCompatible()) {
                    setFileAttribute(tag);
                } else {
                    setFileAttrMac(tag);
                }
                localTags.add(tag);
            } catch (IOException e) {
                System.out.println("FAILED: tag could not be set.");
            }
        }
    }

    public void removeTag(String tag) {
        if(localTags.contains(tag)) {
            try {
                if(OsUtils.isCompatible()) {
                    removeFileAttribute(tag);
                } else {
                    removeFileAttrMac(tag);
                }
                localTags.remove(tag);
            } catch (IOException e) {
                System.out.println("FAILED: tag could not be removed.");
            }
        } else { System.out.println("file doesn't contain tag.."); }
    }

    private String setFileAttrMac(String key) throws IOException {
        String option = (isDirectory()) ? "-r" : "";
        String command = "xattr -w " + option + " '"+ key + "' 'file_tag' '" + getAbsolutePath() + "'";
        cmd.runCmd(command);
        return key;
    }

    private String getFileAttrMac(String key) throws IOException {
        String command = "xattr -p '" + key +  "' '" + getAbsolutePath() + "'"; //then append the attr command
        String output = cmd.runCmd(command);
        return output;
    }

    private String removeFileAttrMac(String key) throws IOException {
        //String option = (isDirectory()) ? "-r" : ""; //make optional in GUI
        String command = "xattr -d '" + key +  "' '" + getAbsolutePath() + "'"; //then append the attr command
        String output = cmd.runCmd(command);
        return output;
    }

    private String setFileAttribute(String tag) throws IOException {
        ByteBuffer tagByteBuffer = Charset.defaultCharset().encode(tag);
        fileAttributeView.write(ATTR_TYPE + tag, tagByteBuffer); // Set file tag attributes
        return tag;
    }

    private void removeFileAttribute(String tag) throws IOException {
        fileAttributeView.delete(tag);
    }

    private String[] getAllAttrMac() throws IOException {
        String command = "xattr '" + getAbsolutePath() + "'"; //then append the attr command
        String output = cmd.runCmd(command);
        String[] tagArr = output.split(" ");
        return tagArr;
    }

    private String[] getAllAttributes() throws IOException {
        return (String[]) fileAttributeView.list().toArray();
    }

    private String getFileAttribute(String attrName) throws IOException {
        String fullName = ATTR_TYPE + attrName;
        int capacity = fileAttributeView.size(fullName); //why is it not the size of the tag
        ByteBuffer buf = ByteBuffer.allocate(capacity);
        fileAttributeView.read(fullName , buf);
        buf.flip();
        return Charset.defaultCharset().decode(buf).toString();
    }
}