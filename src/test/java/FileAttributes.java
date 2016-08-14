import directory.IronFile;
import org.junit.Test;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileAttributes {
    IronFile linuxFile = new IronFile("/home/brian/Downloads/anotherFile.txt");
    Path linuxFilePath = linuxFile.toPath();

    @Test
    public void testLinuxXAttr() {
        try {
            FileStore store = Files.getFileStore(linuxFilePath);
            String errMsg = "UserDefinedFileAttributeView not supported on store " + store;
            assertTrue(errMsg,store.supportsFileAttributeView("xattr"));
            UserDefinedFileAttributeView view = Files.getFileAttributeView(linuxFilePath, UserDefinedFileAttributeView.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetTags() {
        ArrayList<String> tags = linuxFile.getTags();
    }
}
