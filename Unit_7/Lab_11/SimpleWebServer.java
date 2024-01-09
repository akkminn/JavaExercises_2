import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


/**
 * SimpleWebServer is a basic HTTP server that handles GET requests, serves files,
 * and provides directory listings. It listens on a specified port and supports
 * basic error handling.
 */
public class SimpleWebServer {

	/**
	 * The server listens on this port.  Note that the port number must
	 * be greater than 1024 and lest than 65535.
	 */
	private final static int LISTENING_PORT = 50505;

	/**
	 * The root directory from which the server serves files.
	 * Set this to the desired directory on your system.
	 */
	private final static String ROOT_DIRECTORY = ""; 
	// You can use any directory that you want as your root directory

	/**
	 * The main method that starts the web server.
	 *
	 * @param args Command line arguments (not used).
	 */
	public static void main(String[] args) {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(LISTENING_PORT);
		}
		catch (Exception e) {
			System.out.println("Failed to create listening socket.");
			return;
		}
		System.out.println("Listening on port " + LISTENING_PORT);
		try {
			while (true) {
				Socket connection = serverSocket.accept();
				System.out.println("\nConnection from " 
						+ connection.getRemoteSocketAddress());
				ConnectionThread thread = new ConnectionThread(connection);
				thread.start();
			}
		}
		catch (Exception e) {
			System.out.println("Server socket shut down unexpectedly!");
			System.out.println("Error: " + e);
			System.out.println("Exiting.");
		}
	}

	/**
	 * Determines the MIME type of a file based on its extension.
	 *
	 * @param fileName The name of the file.
	 * @return The MIME type associated with the file extension.
	 */
	private static String getMimeType(String fileName) {
		int pos = fileName.lastIndexOf('.');
		if (pos < 0)  // no file extension in name
			return "x-application/x-unknown";
		String ext = fileName.substring(pos+1).toLowerCase();
		if (ext.equals("txt")) return "text/plain";
		else if (ext.equals("html")) return "text/html";
		else if (ext.equals("htm")) return "text/html";
		else if (ext.equals("css")) return "text/css";
		else if (ext.equals("js")) return "text/javascript";
		else if (ext.equals("java")) return "text/x-java";
		else if (ext.equals("jpeg")) return "image/jpeg";
		else if (ext.equals("jpg")) return "image/jpeg";
		else if (ext.equals("png")) return "image/png";
		else if (ext.equals("gif")) return "image/gif"; 
		else if (ext.equals("ico")) return "image/x-icon";
		else if (ext.equals("class")) return "application/java-vm";
		else if (ext.equals("jar")) return "application/java-archive";
		else if (ext.equals("zip")) return "application/zip";
		else if (ext.equals("xml")) return "application/xml";
		else if (ext.equals("xhtml")) return"application/xhtml+xml";
		else return "x-application/x-unknown";
		// Note:  x-application/x-unknown  is something made up;
		// it will probably make the browser offer to save the file.
	}

	/**
	 * Sends the contents of a file to the provided output stream.
	 *
	 * @param file      The file to be sent.
	 * @param socketOut The output stream to send the file contents.
	 * @throws IOException If an I/O error occurs.
	 */
	private static void sendFile(File file, OutputStream socketOut) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		OutputStream out = new BufferedOutputStream(socketOut);
		while (true) {
			int x = in.read(); // read one byte from file
			if (x < 0)
				break; // end of file reached
			out.write(x);  // write the byte to the socket
		}
		out.flush();
	}

	/**
	 * Sends an HTTP error response with the specified status code and description.
	 *
	 * @param errorCode  The HTTP error status code.
	 * @param socketOut  The output stream to send the error response.
	 */
	static void sendErrorResponse(int errorCode, OutputStream socketOut) {

		String token = "HTTP/1.1";
		String statusCode = "";
		String statusDescription = "";

		switch (errorCode) {
		case 400:
			statusCode += "400 Bad Request";
			statusDescription += "The syntax of the request is bad.";
			break;
		case 403:
			statusCode += "403 Forbidden";
			statusDescription += "You don't have permission to read the file.";
			break;
		case 404:
			statusCode += "404 Not Found";
			statusDescription += "The resource that you requested does not exist on this server.";
			break;
		case 500:
			statusCode += "500 Internal Server Error";
			statusDescription += "There has some unexpected error in handling the connection.";
			break;
		case 501:
			statusCode += "501 Not Implemented";
			statusDescription += "The method has not been implemented yet.";
			break;
		default:
			statusCode += "500 Internal Server Error";
			statusDescription += "There has some unexpected error in handling the connection.";
			break;
		}

		try {
			PrintWriter out = new PrintWriter(socketOut);

			out.print(token + statusDescription + "\r\n");
			out.print("Connection: close\r\n");
			out.print("Content-Type: text/html\r\n");
			out.print("\r\n");
			out.print("<html><head><title>Error</title></head><body>\r\n");
			out.print("<h2>Error:" + statusCode + "</h2>\r\n");
			out.print("<p>" + statusDescription + "</p>\r\n");
			out.print("</body></html>\r\n");
			out.flush();

			out.close();
		} catch (Exception e) {
			// Nothing to do if error occurs while attempting to send error message.
		}		

	}


	/**
	 * Lists the contents of a directory and its subdirectories and show them as table.
	 *
	 * @param dir      The {@code File} object representing the directory to be listed.
	 * @param outgoing The {@code PrintWriter} for sending the directory listing to the client.
	 */
	public static void getFileList(File dir, PrintWriter outgoing) {
		File[] fileList = dir.listFiles();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		outgoing.print("HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n");
		outgoing.print("<html><head><title>Directory Listing</title></head><body>\r\n");
		outgoing.print("<h1>Directory Listing</h1><h3>" + dir.getPath() + "</h3>");
		outgoing.print("<table><tr><td>Filename</td><td>Size</td><td>Last Modified</td></tr>");

		if (dir.getParentFile() != null) {
			outgoing.print("<tr><td><a href=\"../\">../</a></td><td></td><td></td></tr>");
		}

		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];

			String link = (file.isDirectory()) ? file.getPath() + "/" : file.getName();
			String size = (file.isDirectory()) ? "-" : formatSize(file.length());
			String lastModified = dateFormat.format(new Date(file.lastModified()));

			outgoing.print("<tr><td><a href=\"" + link + "\" \">" + file.getName() + "</a></td>");
			outgoing.print("<td align=\"right\">" + size + "</td><td>" + lastModified + "</td></tr>");
		}

		outgoing.print("</table><hr></body></html>\r\n");
		outgoing.flush();
	}

	/**
	 * Formats the size of a file in a user-friendly form.
	 *
	 * @param bytes The size of the file in bytes.
	 * @return A formatted string representing the file size.
	 */
	private static String formatSize(long bytes) {
		if (bytes < 1024) {
			return bytes + " B";
		} else if (bytes < 1024 * 1024) {
			return (bytes / 1024) + " KB";
		} else {
			return String.format("%.2f MB", (double) bytes / (1024 * 1024));
		}
	}

	/**
	 * Represents a thread for handling a single client connection.
	 */
	private static class ConnectionThread extends Thread {
		Socket connection;
		ConnectionThread(Socket connection) {
			this.connection = connection;
		}
		public void run() {
			handleConnection(connection);
		}
	}

	/**
	 * Handles the communication with the client for a single connection.
	 *
	 * @param connection The client socket connection.
	 */
	private static void handleConnection(Socket connection) {

		// Input and output streams for communication with the client
		Scanner in;
		PrintWriter outgoing;
		OutputStream out;

		// Variables to store information about the request and response
		String method;
		String token;
		String pathToFile;
		String type;
		String status;

		try {
			// Set up input and output streams for communication with the client
			in = new Scanner(connection.getInputStream());
			outgoing = new PrintWriter(connection.getOutputStream());
			out = connection.getOutputStream();

			// Read the HTTP request method
			method = in.next();

			// Check if the request method is supported (only supports GET)
			if (!method.equalsIgnoreCase("GET")) {
				System.out.print("ERROR! Not supported method.");
				sendErrorResponse(501, out);
			} else {

				// Process GET request
				pathToFile = in.next();
				token = in.next();

				// Check if the HTTP version is supported
				if (!token.equalsIgnoreCase("HTTP/1.1") && 
						!token.equalsIgnoreCase("HTTP/1.0")) {
					System.out.print("ERROR: Bad request.  Not HTTP/1.1 or HTTP/1.0.");
					sendErrorResponse(400, out);

				} else {

					// Create a File object for the requested resource
					File file = new File(ROOT_DIRECTORY + pathToFile);

					// Check if the resource is a directory
					if (file.isDirectory()) {
						// If it's a directory, send a directory listing to the client
						getFileList(file, outgoing);
					} else if (file.exists() && file.canRead()) {
						// If it's a file and can be read, send the file content to the client
						status = token + " 200 OK\r\n";
						outgoing.print(status);
						outgoing.print("Connection: close\r\n");

						// Determine the content type of the file
						type = getMimeType(file.getName());
						outgoing.print("Content-type: " + type);
						// Include content length in the response
						long fileLength = file.length();
						outgoing.print("Content-Length: " + fileLength + "\r\n");
						outgoing.print("\r\n");
						outgoing.flush();

						// Send the file content
						sendFile(file, out);
					} else {
						if (file.exists() && !file.canRead()) {
							// Handle cases where the file doesn't exist or can't be read
							System.out.println("ERROR: Permission to read file denied.");
							sendErrorResponse(403, out);
						} else if (!file.exists()) {
							System.out.println("ERROR: File does not exist on this server.");
							sendErrorResponse(404, out);
						}
						outgoing.flush();
					}
				}

			}

		}
		catch (Exception e) {
			// Handle exceptions that may occur during communication with the client
			System.out.println("Error while communicating with client: " + e);
			try {
				// Attempt to send an Internal Server Error response to the client
				OutputStream newOut = connection.getOutputStream();
				sendErrorResponse(500, newOut);
			} catch (Exception error) {
				System.out.println("Error sending Internal Server Error response.");
			}
		}
		finally {  
			// make SURE connection is closed before returning!
			try {
				connection.close();
			}
			catch (Exception e) {
				// Ignore any exceptions that may occur while closing the connection
			}
			System.out.println("Connection closed.");
		}
	}
}
