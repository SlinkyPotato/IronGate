package utils;

import java.io.FilenameFilter;
import java.io.File;
import java.nio.file.Files;

/**
 * Created by kristopherguzman on 2/19/16.
 */
public class IronFileFilter implements FilenameFilter {

    public boolean accept(File file, String name) {
        boolean hidden = false;
        boolean isSymbolic = false;
        boolean isRegularFile = false;

        try {
            hidden = Files.isHidden(file.toPath());
            isSymbolic = Files.isSymbolicLink(file.toPath());
            isRegularFile = Files.isRegularFile(file.toPath());
        } catch(Exception e) {

        }

        if(hidden || isSymbolic || (!isRegularFile && !file.isDirectory())) {
            //System.out.println(name + " is hidden or a symbolic link, or just not a regular file");
            return false;
        }
        return true;
    }
}
