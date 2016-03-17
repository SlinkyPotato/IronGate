package utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.nio.file.Files;

/**
 * @author kristopherguzman
 */
public class IronFileFilter implements FilenameFilter, Serializable {

    public boolean accept(File file, String name) {
        boolean hidden = false;
        boolean isSymbolic = false;
        boolean isRegularFile = false;

        try {
            hidden = Files.isHidden(file.toPath());
            isSymbolic = Files.isSymbolicLink(file.toPath());
            isRegularFile = Files.isRegularFile(file.toPath());
        } catch (Exception e) {
            System.out.println(e);
        }
        return !(hidden || isSymbolic || (!isRegularFile && !file.isDirectory()));
    }
}
