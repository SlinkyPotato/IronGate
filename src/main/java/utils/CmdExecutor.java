package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;

/**
 * @author kristopherguzman
 */
public class CmdExecutor {
    public String runCmd(String cmd) throws IOException {
        System.out.println("executing command: " + cmd);

        // line below won't work any other way...
        String[] args = new String[]{"sh", "-c", "cd / && " + cmd}; //bourne shell, work from root directory
        Process process = Runtime.getRuntime().exec(args);
        try {
            process.waitFor();
        } catch(InterruptedException e) { System.out.println("ERROR: iterrupted exception."); }

        BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream())); //efficiently reads chars
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String line;
        String error = errorReader.readLine();

        System.out.println("Below is command output: \n ");
        String result = "";
        while ((line = outputReader.readLine()) != null) {
            System.out.println("cmd output: " + line);
            result += " " + line;
        }

        while (error != null) {
            System.out.println("error output: " + error);
            error = errorReader.readLine();
        }
        outputReader.close();
        errorReader.close();
        return result;
    }
}
