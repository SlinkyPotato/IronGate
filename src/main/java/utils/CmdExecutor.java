package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by kristopherguzman on 2/19/16.
 */
public class CmdExecutor {

    public CmdExecutor() { }

    public String run(String cmd) throws IOException {
        System.out.println("executing command: " + cmd);
        String[] args = new String[] {"sh", "-c", "cd / && " + cmd};
        Process process = Runtime.getRuntime().exec(args);
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream())); //efficiently reads chars
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String line = outputReader.readLine();
        String error = errorReader.readLine();

        System.out.println("Below is command output: \n ");
        while(line != null) {
            System.out.println("cmd output: " + line);
            line = outputReader.readLine();
        }

        while(error != null) {
            System.out.println("error output: " + error);
            error = errorReader.readLine();

        }
        outputReader.close();
        errorReader.close();
        return null;
    }
}
