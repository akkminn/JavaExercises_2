import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;

import javax.swing.JFileChooser;

/**
 * The SpellChecker class performs spell checking on a given input file using a dictionary
 * and suggests corrections for misspelled words.
 * !!The first input file is for dictionary!!
 * !!The second is for the file you want to check the spelling!!
 */
public class SpellChecker {

	/**
     * The main method is the entry point of the program.
     * It allows the user to select a dictionary file and an input file,
     * reads the dictionary, processes the input file, and suggests corrections.
     *
     * @param args Command line arguments (not used in this program).
     * @throws FileNotFoundException If a selected file is not found.
     */
	public static void main (String[] args) throws FileNotFoundException {

		// Get input file for dictionary
		File dictFile = getInputFileNameFromUser("Select File for Dictionary");	
		
		// Get input file to check the spelling
		File inputFile = getInputFileNameFromUser("Select File for Input");

		try {
			// Read the dictionary
			Dictionary dict = new Dictionary(dictFile);
			dict.loadDictionary(); // Load the content of the dictionary
			HashSet<String> wordDictionary = dict.getDictionary();

			// Set for the incorrect word to remove duplication
			HashSet<String> badWordSet = new HashSet<>();

			// Read a selected file skipping any non-letter characters
			Scanner filein = new Scanner(inputFile).useDelimiter("[^a-zA-Z]+");;

			while (filein.hasNext()) {
				String word = filein.next();
				// If the word is not in dictionary
				if (!wordDictionary.contains(word.toLowerCase())) {
					// Add to the badWordSet
					badWordSet.add(word.toLowerCase());
				}
			}

			// Suggest for corrections
			for (String badWord: badWordSet) {
				// Get the suggestions 
				TreeSet<String> suggestions = corrections(badWord, wordDictionary);

				// If there is no suggestion
				if (suggestions.size() == 0) {
					System.out.println(badWord  + ": (no suggestions)");
						// Print no suggestion
				}
				else {
					System.out.print(badWord + ": ");
					// Get the first word
					String firstWord = suggestions.first();
					// Print the first word with no comma
					System.out.print(firstWord);
					// Processing the remaining words
					for (String suggestWord : suggestions.tailSet(firstWord + 1)) {
						System.out.print(", " + suggestWord);
					}
					System.out.println();
				}
			}
			// Close the scanner
			filein.close();
		}
		catch (Exception e) {
			System.out.println("Error " + e.getMessage());
		}
	}

	/**
    * Generates a TreeSet of possible corrections for a misspelled word.
    *
    * @param badWord The misspelled word.
    * @param dictionary The HashSet representing the dictionary.
    * @return A TreeSet of possible corrections for the misspelled word.
    */
	static TreeSet<String> corrections(String badWord, HashSet<String> dictionary) {

		// A tree set for possible suggestion
		TreeSet<String> possibleCorrections = new TreeSet<>();

		// Delete any one of the letters
		for (int i = 0; i < badWord.length(); i++) {
			String correction = badWord.substring(0, i) + badWord.substring(i + 1);
			if (dictionary.contains(correction)) {
				possibleCorrections.add(correction);
			}
		}

		// Change any letter to any other letter
		for (int i = 0; i < badWord.length(); i++) {
			for (char ch = 'a'; ch <= 'z'; ch++) {
				String correction = badWord.substring(0, i) + ch + badWord.substring(i + 1);
				if (dictionary.contains(correction)) {
					possibleCorrections.add(correction);
				}
			}
		}

		// Insert any letter at any point
		for (int i = 0; i <= badWord.length(); i++) {
			for (char ch = 'a'; ch <= 'z'; ch++) {
				String correction = badWord.substring(0, i) + ch + badWord.substring(i);
				if (dictionary.contains(correction)) {
					possibleCorrections.add(correction);
				}
			}
		}

		// Swap any two neighboring characters
		for (int i = 0; i < badWord.length() - 1; i++) {
			String correction = badWord.substring(0, i) + badWord.charAt(i + 1) + 
					badWord.charAt(i) + badWord.substring(i + 2);
			if (dictionary.contains(correction)) {
				possibleCorrections.add(correction);
			}
		}

		// Insert a space at any point (check both resulting words are in the dictionary)
		for (int i = 1; i < badWord.length(); i++) {
			String firstPart = badWord.substring(0, i);
			String secondPart = badWord.substring(i);
			if (dictionary.contains(firstPart) && dictionary.contains(secondPart)) {
				possibleCorrections.add(firstPart + " " + secondPart);
			}
		}

		return possibleCorrections;
	}


	/**
	 * Lets the user select an input file using a standard file
	 * selection dialog box.  If the user cancels the dialog without
	 * selecting a file, the return value is null.
	 */
	static File getInputFileNameFromUser(String dialog) {
		JFileChooser fileDialog = new JFileChooser();
		fileDialog.setDialogTitle(dialog);
		int option = fileDialog.showOpenDialog(null);
		if (option != JFileChooser.APPROVE_OPTION)
			return null;
		else
			return fileDialog.getSelectedFile();
	}
}

/**
 * The Dictionary class represents a dictionary of words.
 */
class Dictionary {

	// Creating a HashSet for the dictionary
	private HashSet<String> wordDictionary = new HashSet<>();
	private File file;

	public Dictionary(File dictFile) {
		this.file = dictFile;
	}
	
	/**
	 * Gets the HashSet representing the dictionary.
	 *
	 * @return The HashSet representing the dictionary.
	 */
	public HashSet<String> getDictionary() {
		return wordDictionary;
	}

	/**
     * Gets the size of the dictionary.
     *
     * @return The size of the dictionary.
     */
	public int getSize() {
		return wordDictionary.size();
	}

	/**
     * Reads words from the dictionary file and adds them to the dictionary HashSet.
     * Converts each word to lower case before adding to ensure case-insensitivity.
     */
	public void loadDictionary() {
		try (Scanner filein = new Scanner(file)) {
			while (filein.hasNext()) {
				String tk = filein.next();
				wordDictionary.add(tk.toLowerCase());
				// Add the words in lowerCase to the dictionary
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		}
	}
}