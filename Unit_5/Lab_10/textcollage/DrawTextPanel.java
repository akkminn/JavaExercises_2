package textcollage;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A panel that contains a large drawing area where strings
 * can be drawn.  The strings are represented by objects of
 * type DrawTextItem.  An input box under the panel allows
 * the user to specify what string will be drawn when the
 * user clicks on the drawing area.
 */
public class DrawTextPanel extends JPanel  {

	// As it now stands, this class can only show one string at at
	// a time!  The data for that string is in the DrawTextItem object
	// named theString.  (If it's null, nothing is shown.  This
	// variable should be replaced by a variable of type
	// ArrayList<DrawStringItem> that can store multiple items.

	private ArrayList<DrawTextItem> strings = new ArrayList<>();  // change to an ArrayList<DrawTextItem> !


	private Color currentTextColor = Color.BLACK;  // Color applied to new strings.

	private Canvas canvas;  // the drawing area.
	private JTextField input;  // where the user inputs the string that will be added to the canvas
	private SimpleFileChooser fileChooser;  // for letting the user select files
	private JMenuBar menuBar; // a menu bar with command that affect this panel
	private MenuHandler menuHandler; // a listener that responds whenever the user selects a menu command
	private JMenuItem undoMenuItem;  // the "Remove Item" command from the edit menu
	private JSlider transparencySlider; // the transparency slider
	private JLabel transparencyValue;

	/**
	 * An object of type Canvas is used for the drawing area.
	 * The canvas simply displays all the DrawTextItems that
	 * are stored in the ArrayList, strings.
	 */
	private class Canvas extends JPanel {
		Canvas() {
			setPreferredSize( new Dimension(800,600) );
			setBackground(Color.LIGHT_GRAY);
			setFont( new Font( "Serif", Font.BOLD, 24 ));
		}
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			if (strings != null) {
				for (DrawTextItem string: strings) {
					string.draw(g);
				}
			}
		}
	}

	/**
	 * An object of type MenuHandler is registered as the ActionListener
	 * for all the commands in the menu bar.  The MenuHandler object
	 * simply calls doMenuCommand() when the user selects a command
	 * from the menu.
	 */
	private class MenuHandler implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			doMenuCommand( evt.getActionCommand());
		}
	}

	/**
	 * Creates a DrawTextPanel.  The panel has a large drawing area and
	 * a text input box where the user can specify a string.  When the
	 * user clicks the drawing area, the string is added to the drawing
	 * area at the point where the user clicked.
	 */
	public DrawTextPanel() {
		fileChooser = new SimpleFileChooser();
		undoMenuItem = new JMenuItem("Remove Item");
		undoMenuItem.setEnabled(false);
		menuHandler = new MenuHandler();
		setLayout(new BorderLayout(3,3));
		setBackground(Color.BLACK);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		canvas = new Canvas();
		add(canvas, BorderLayout.CENTER);
		JPanel bottom = new JPanel();
		bottom.add(new JLabel("Text to add: "));
		input = new JTextField("Hello World!", 40);
		bottom.add(input);
		add(bottom, BorderLayout.SOUTH);

		JPanel textTransparency = new JPanel();
		textTransparency.add(new JLabel("Text transparency:"));
		transparencySlider = new JSlider(0, 10, 0); // Range from 0 to 10, initial value 1
		textTransparency.add(transparencySlider);
		transparencyValue = new JLabel("Current value:" + 0);
		textTransparency.add(transparencyValue, BorderLayout.NORTH);
		add(textTransparency, BorderLayout.NORTH);
		transparencySlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateDisplay();
			}
		});


		canvas.addMouseListener( new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				doMousePress( e );
			}
		} );
	}

	/**
	 * This method is called when the user clicks the drawing area.
	 * A new string is added to the drawing area.  The center of
	 * the string is at the point where the user clicked.
	 * @param e the mouse event that was generated when the user clicked
	 */
	public void doMousePress( MouseEvent e ) {
		String text = input.getText().trim();
		if (text.length() == 0) {
			input.setText("Hello World!");
			text = "Hello World!";
		}
		DrawTextItem s = new DrawTextItem( text, e.getX(), e.getY() );
		s.setTextColor(currentTextColor);  // Default is null, meaning default color of the canvas (black).

		//   SOME OTHER OPTIONS THAT CAN BE APPLIED TO TEXT ITEMS:
		//		s.setFont( new Font( "Serif", Font.ITALIC + Font.BOLD, 12 ));  // Default is null, meaning font of canvas.
		//		s.setMagnification(3);  // Default is 1, meaning no magnification.
		//		s.setBorder(true);  // Default is false, meaning don't draw a border.
		//		s.setRotationAngle(25);  // Default is 0, meaning no rotation.
		s.setTextTransparency(getTextTransparency()); // Default is 0, meaning text is not at all transparent.
		//		s.setBackground(Color.BLUE);  // Default is null, meaning don't draw a background area.
		//		s.setBackgroundTransparency(0.7);  // Default is 0, meaning background is not transparent.

		strings.add(s);  // Set this string as the ONLY string to be drawn on the canvas!
		undoMenuItem.setEnabled(true);
		canvas.repaint();
	}

	private double getTextTransparency() {
		double transparency = transparencySlider.getValue() / 10.0;
		updateDisplay();
		return transparency;
	}

	private void updateDisplay() {
		double transparency = transparencySlider.getValue() / 10.0;
		transparencyValue.setText("Current Value: " + String.format("%.1f", transparency));
	}

	/**
	 * Returns a menu bar containing commands that affect this panel.  The menu
	 * bar is meant to appear in the same window that contains this panel.
	 */
	public JMenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();

			String commandKey; // for making keyboard accelerators for menu commands
			if (System.getProperty("mrj.version") == null)
				commandKey = "control ";  // command key for non-Mac OS
			else
				commandKey = "meta ";  // command key for Mac OS

			JMenu fileMenu = new JMenu("File");
			menuBar.add(fileMenu);
			JMenuItem saveItem = new JMenuItem("Save...");
			saveItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "N"));
			saveItem.addActionListener(menuHandler);
			fileMenu.add(saveItem);
			JMenuItem openItem = new JMenuItem("Open...");
			openItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "O"));
			openItem.addActionListener(menuHandler);
			fileMenu.add(openItem);
			fileMenu.addSeparator();
			JMenuItem saveImageItem = new JMenuItem("Save Image...");
			saveImageItem.addActionListener(menuHandler);
			fileMenu.add(saveImageItem);

			JMenu editMenu = new JMenu("Edit");
			menuBar.add(editMenu);
			undoMenuItem.addActionListener(menuHandler); // undoItem was created in the constructor
			undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "Z"));
			editMenu.add(undoMenuItem);
			editMenu.addSeparator();
			JMenuItem clearItem = new JMenuItem("Clear");
			clearItem.addActionListener(menuHandler);
			editMenu.add(clearItem);

			JMenu optionsMenu = new JMenu("Options");
			menuBar.add(optionsMenu);
			JMenuItem colorItem = new JMenuItem("Set Text Color...");
			colorItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "T"));
			colorItem.addActionListener(menuHandler);
			optionsMenu.add(colorItem);
			JMenuItem bgColorItem = new JMenuItem("Set Background Color...");
			bgColorItem.addActionListener(menuHandler);
			optionsMenu.add(bgColorItem);

		}
		return menuBar;
	}

	/**
     * Saves the current state of the panel to a file.
     */
	private void saveFile() { 
		// Get the file user want to save
		File file = fileChooser.getOutputFile(this, "Select File Name", "collage.txt");

		if (file == null) {
			return;	// if selected file is null, return
		}

		PrintWriter out; // Create a PrintWriter to write a file

		try {
			FileOutputStream stream = new FileOutputStream(file);
			out = new PrintWriter(stream);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Sorry, but an error occurred while trying to open the file:\n" + e);
			return;
		}

		try {

			out.println("TextCollage");

			Color bg = canvas.getBackground(); // The background color of the image
			// Print the background color
			out.println("Background-color: " + bg.getRed() + " " +
					bg.getGreen()+ " " + bg.getBlue());


			// Print all the strings
			if (strings != null) {
				for (DrawTextItem string: strings) {
					out.println();
					out.println("Start_of_string");

					out.println("Text " + string.getString());
					Color textColor = string.getTextColor();
					out.println("Text-color " + textColor.getRed() + " " +
							textColor.getGreen()+ " " + textColor.getBlue());

					out.println("Coordinates " + string.getX() + " " + string.getY());

					out.println("Text-transparency " + string.getTextTransparency());

					out.println("End_of_string");
				}

				out.close();
				if (out.checkError()) {
					throw new IOException("Output error");
				}

			}


		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Sorry, but an error occurred while trying to write the file:\n" + e);
			return;
		}

	}


	/**
     * Opens a file and reconstructs the list of strings based on the file content.
     */
	private void openFile() {
		// Get the file the user wants to open
		File file = fileChooser.getInputFile(this, "Select File Name");

		if (file == null) {
			return; // If the selected file is null, return
		}

		Scanner scanner = null; // Initialize the scanner outside the try block

		try {
			FileInputStream input = new FileInputStream(file);
			scanner = new Scanner(input);

			if (!scanner.hasNext() || !"TextCollage".equals(scanner.next())) {
				throw new IOException("Invalid file format or empty file.");
			}

			// Get the background Color
			if (scanner.hasNext() && "Background-color:".equalsIgnoreCase(scanner.next())) {
				int red = scanner.nextInt();
				int green = scanner.nextInt();
				int blue = scanner.nextInt();
				Color newBg = new Color(red, green, blue);
				canvas.setBackground(newBg);
			}

			ArrayList<DrawTextItem> newStrings = new ArrayList<>();

			while (scanner.hasNext()) {
				String token = scanner.next();
				if (token.equalsIgnoreCase("Start_of_string")) {
					scanner.nextLine(); // Move to the next line
					String content = scanner.next();

					if (content.equalsIgnoreCase("Text")) {
						String text = scanner.nextLine();
						DrawTextItem item = new DrawTextItem(text);
						while (scanner.hasNext()) {
							String itemContent = scanner.next();
							if (itemContent.equalsIgnoreCase("End_of_string")) {
								break;
							}
							switch (itemContent.toLowerCase()) {
							case "text-color":
								int red = scanner.nextInt();
								int green = scanner.nextInt();
								int blue = scanner.nextInt();
								item.setTextColor(new Color(red, green, blue));
								break;
							case "coordinates":
								int x = scanner.nextInt();
								int y = scanner.nextInt();
								item.setX(x);
								item.setY(y);
								break;
							case "text-transparency":
								double value = scanner.nextDouble();
								item.setTextTransparency(value);
								break;
							default:
								throw new IOException("Unknown term in input during strings.");
							}
						}
						newStrings.add(item);
					}

				}
			}
			strings = newStrings;
			canvas.repaint();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error reading the file:\n" + e.getMessage());
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}


	/**
	 * Carry out one of the commands from the menu bar.
	 * @param command the text of the menu command.
	 */
	private void doMenuCommand(String command) {
		if (command.equals("Save...")) { // save all the string info to a file
			saveFile();
		}
		else if (command.equals("Open...")) { // read a previously saved file, and reconstruct the list of strings
			openFile();
		}
		else if (command.equals("Clear")) {  // remove all strings
			strings .clear();   // Remove all the strings
			undoMenuItem.setEnabled(false);
			canvas.repaint();
		}
		else if (command.equals("Remove Item")) { // remove the most recently added string
			strings.remove(strings.size()-1);   // Remove the last string
			if(strings.isEmpty()) {
				// if ArrayList is empty disable these
				undoMenuItem.setEnabled(false);
			} else {
				undoMenuItem.setEnabled(true);
			}
			canvas.repaint();
		}
		else if (command.equals("Set Text Color...")) {
			Color c = JColorChooser.showDialog(this, "Select Text Color", currentTextColor);
			if (c != null)
				currentTextColor = c;
		}
		else if (command.equals("Set Background Color...")) {
			Color c = JColorChooser.showDialog(this, "Select Background Color", canvas.getBackground());
			if (c != null) {
				canvas.setBackground(c);
				canvas.repaint();
			}
		}
		else if (command.equals("Save Image...")) {  // save a PNG image of the drawing area
			File imageFile = fileChooser.getOutputFile(this, "Select Image File Name", "textimage.png");
			if (imageFile == null)
				return;
			try {
				// Because the image is not available, I will make a new BufferedImage and
				// draw the same data to the BufferedImage as is shown in the panel.
				// A BufferedImage is an image that is stored in memory, not on the screen.
				// There is a convenient method for writing a BufferedImage to a file.
				BufferedImage image = new BufferedImage(canvas.getWidth(),canvas.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				Graphics g = image.getGraphics();
				g.setFont(canvas.getFont());
				canvas.paintComponent(g);  // draws the canvas onto the BufferedImage, not the screen!
				boolean ok = ImageIO.write(image, "PNG", imageFile); // write to the file
				if (ok == false)
					throw new Exception("PNG format not supported (this shouldn't happen!).");
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(this, 
						"Sorry, an error occurred while trying to save the image:\n" + e);
			}
		}
	}


}