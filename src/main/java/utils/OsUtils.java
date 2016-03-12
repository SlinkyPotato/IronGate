package utils;


/**
 *
 * This is a static utility class to detect the OS.
 * Class has been updated to make only one call for the lifetime
 * of the application.
 *
 * @author kristopherguzman
 * @author Brian Patino
 */
public final class OsUtils {
    private static String OS = null;

    public static String getOsName() {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS;
    }

    public static boolean isWindows() {
        return getOsName().contains("Win");
    }

    public static boolean isUnix() {
        return getOsName().contains("nix") || getOsName().contains("ux") || getOsName().contains("aix");
    }

    public static boolean isMac() {
        return getOsName().contains("mac");
    }

    public static boolean isCompatible() {
        if (isWindows() || isUnix()) {
            return true;
        } else {
            System.out.println("macs..");
            return false;
        }
    }
}
