import java.io.File;
import java.util.Scanner;

/**
 * This program lists the files in a directory specified by
 * the user.  The user is asked to type in a directory name.
 * If the name entered by the user is not a directory, a
 * message is printed and the program ends.
 */
public class DirectoryList {


    public static void main(String[] args) {

        String directoryName;  // Directory name entered by the user.
        File directory;        // File object referring to the directory.
        String[] files;        // Array of file names in the directory.
        Scanner scanner;       // For reading a line of input from the user.

        scanner = new Scanner(System.in);  // scanner reads from standard input.

        System.out.print("Enter a directory name: ");
        directoryName = scanner.nextLine().trim();
        directory = new File(directoryName);

        if (directory.isDirectory() == false) {
            if (directory.exists() == false)
                System.out.println("There is no such directory!");
            else
                System.out.println("That file is not a directory.");
        }
        else {
            getDirectory(directory, "");
        }

    } // end main()
    
    /**
     * Recursively lists the contents of a directory and its subdirectories.
     *
     * @param dir    The {@code File} object representing the directory to be listed.
     * @param indent The current indentation level for formatting the output.
     */
    public static void getDirectory(File dir, String indent) {
    	String[] fileList = dir.list();
    	System.out.println(indent + "Directory \"" + dir.getName() + "\":");
    	indent += "   ";  // Increase the indentation for listing the contents.
    	for (String file: fileList) {
    		File f = new File(dir, file);
    		if (f.isDirectory()) {
    			getDirectory(f, indent);
    		} else {
    			System.out.println(indent + file);
    		}
    	}
    }

} // end class DirectoryList