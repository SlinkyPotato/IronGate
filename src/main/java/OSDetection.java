package main.java;

/**
 * Created by kristopherguzman on 2/19/16.
 *
 * This is a static utlity class to detect the OS. Program methods may have to adjust to the OS.
 */
public class OSDetection {

    private OSDetection() {}

    public enum OS { WINDOWS, MAC, UNIX }
    public static OS OSType;

    public static void getOS() { //utility method to get OS

        String os = System.getProperty("os.name").toLowerCase();

        if(os.contains("win")) {

            OSType = OS.WINDOWS;
            System.out.println("OS: Windows");

        } else if(os.contains("nix") || os.contains("ux") || os.contains("aix")) {

            OSType = OS.UNIX;
            System.out.println("OS: Unix/Linux");

        } else if(os.contains("mac")) {

            OSType = OS.MAC;
            System.out.println("OS: Mac");

        } else {

            System.out.println("Could not detect OS");

        }

    }

}
