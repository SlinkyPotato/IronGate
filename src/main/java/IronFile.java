
package main.java;

import java.io.File;

/**
 * This class extends the java File class and returns the filename for toString()
 */
public class IronFile extends File {
    private boolean isRoot = true;

    public IronFile(String pathname) {
        super(pathname);
        isRoot = (getParent() == null);
    }
    public IronFile(File file) {
        super(file.getPath());
        isRoot = (getParent() == null);
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

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    @Override
    public String toString() {
        if (isRoot) {
            return this.getAbsolutePath();
        } else {
            return this.getName();
        }
    }
}