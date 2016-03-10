package directory;

import utils.IronFileFilter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;

/**
 * This class extends the java File class and returns the filename for toString()
 */
public class IronFile extends File implements Serializable {
    private boolean isRoot = true;
    public IronFileFilter filter;
    private String tag;
    final private String ATTR_TYPE = "tags";
    private UserDefinedFileAttributeView fileAttributeView;

    /**
     * Construct an IronFile that extends File
     * @param pathname the path where the file is located
     * */
    public IronFile(String pathname) {
        super(pathname);
        isRoot = (getParent() == null);
        filter = new IronFileFilter();
    }
    /**
     * Construct an IronFile that extends File. This is an overloaded method
     * @param file file that can be passed instead of path (string)
     * */
    public IronFile(File file) {
        super(file.getPath());
        isRoot = (getParent() == null);
        filter = new IronFileFilter();
        fileAttributeView =  Files.getFileAttributeView(this.toPath(), UserDefinedFileAttributeView.class);
    }

    @Override
    public IronFile[] listFiles() {
        return convertFiles(super.listFiles());
    }

    /**
     * Return the available hard drive disks.
     * */
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

    public String getTag() {
        try {
            ByteBuffer buf = ByteBuffer.allocate(fileAttributeView.size(ATTR_TYPE));
            fileAttributeView.read(ATTR_TYPE, buf);
            buf.flip();
            tag = Charset.defaultCharset().decode(buf).toString();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return tag;
    }
    public void setTag(String tag) {
        System.out.println(tag);
        this.tag = tag;
        try {
            fileAttributeView.write(ATTR_TYPE, Charset.defaultCharset().encode(tag)); // Set file tag attributes
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}