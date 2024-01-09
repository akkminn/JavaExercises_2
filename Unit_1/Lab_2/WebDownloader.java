import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * A simple program that fetches data from a specified URL and saves it to a file.
 * The user is prompted to enter the URL and the output file name.
 * Uses exception handling to deal with I/O errors and invalid URLs.
 */
public class WebDownloader {

	private static void copyStream(InputStream in, OutputStream out) throws IOException {
		int oneByte = in.read();
		while (oneByte >= 0) { // negative value indicates end-of-stream
			out.write(oneByte);
			oneByte = in.read();
		}
	}

	public static void main(String[] args) throws URISyntaxException {

		InputStream in = null;
		OutputStream out = null;
		Scanner scanner = new Scanner(System.in);
		
		try {
			// Read URL from the user
			System.out.print("Enter the URL: ");
			String url = scanner.nextLine();

			// Read file name from the user
			System.out.print("Enter the output file name: ");
			String outputFile = scanner.nextLine();

			// Fetch data from the URL
			in = new URI(url).toURL().openStream();

			// Save data to a file
			out = new FileOutputStream(outputFile);

			copyStream(in, out);

			System.out.println("Data downloaded and saved successfully.");
		} catch (MalformedURLException e) {
			System.out.println("Invalid URL. Please provide a valid URL."); 
		} catch (FileNotFoundException e) {
			System.out.println("File not found. Please provide a valid file name.");
		} catch (IOException e) {
			System.out.println("An error occurred: " + e.getMessage());
		} finally {
			scanner.close();
			// Close InputStream and OutputStream if they were opened
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				System.out.println("An error occurred: " + e.getMessage());
			}
		}

	}
}
