package directory;

import utils.IronFileFilter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
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
     *
     * @param pathname the path where the file is located
     */
    public IronFile(String pathname) {
        super(pathname);
        isRoot = (getParent() == null);
        filter = new IronFileFilter();
        fileAttributeView = Files.getFileAttributeView(this.toPath(), UserDefinedFileAttributeView.class);
    }

    /**
     * Construct an IronFile that extends File. This is an overloaded method
     *
     * @param file file that can be passed instead of path (string)
     */
    public IronFile(File file) {
        super(file.getPath());
        isRoot = (getParent() == null);
        filter = new IronFileFilter();
        fileAttributeView = Files.getFileAttributeView(this.toPath(), UserDefinedFileAttributeView.class);
    }

    @Override
    public IronFile[] listFiles() {
        return convertFiles(super.listFiles());
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

    public String getTag() {
        try {
            tag = getFileAttribute();
        } catch (IOException e) {
            setTag(""); // set empty tag if none exist
        }
        return tag;
    }

    public void setTag(String tag) {
        System.out.println(tag);
        this.tag = tag;
        try {
            setFileAttribute(tag);
        } catch (IOException e) {
            try {
                setFileAttribute(tag, "user." + ATTR_TYPE);
            } catch (IOException insideError) {
                System.out.println("File System does not support extended attributes");
//                insideError.printStackTrace();
            }
        }
    }

    private void setFileAttribute(String tag) throws IOException {
        ByteBuffer tagByteBuffer = Charset.defaultCharset().encode(tag);
        fileAttributeView.write(ATTR_TYPE, tagByteBuffer); // Set file tag attributes
    }

    private void setFileAttribute(String tag, String attributeType) throws IOException {
        ByteBuffer tagByteBuffer = Charset.defaultCharset().encode(tag);
        fileAttributeView.write(attributeType, tagByteBuffer); // Set file tag attributes
    }

    private String getFileAttribute() throws IOException {
        int capacity = fileAttributeView.size(ATTR_TYPE);
        ByteBuffer buf = ByteBuffer.allocate(capacity);
        fileAttributeView.read(ATTR_TYPE, buf);
        buf.flip();
        return Charset.defaultCharset().decode(buf).toString();
    }
}