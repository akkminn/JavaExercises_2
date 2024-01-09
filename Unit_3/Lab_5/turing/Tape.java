package turing;

/**
 * This class represent TuringMachine's Tape
 * 
 * A Turing machine works on a "tape" that is used for both input 
 * and output. The tape is made up of little squares called cells 
 * lined up in a horizontal row that stretches, conceptually, 
 * off to infinity in both directions. Each cell can hold one character. 
 * Initially, the content of a cell is a blank space.
 * One cell on the tape is considered to be the current cell. 
 * This is the cell where the machine is located. As a Turing machine computes,
 * it moves back and forth along the tape, and the current cell changes.
 */
public class Tape {
	
	private Cell currentCell;
	
	/**
	 * A constructor that creates a tape with a single cell containing a blank space.
	 * The current cell pointer is initially set to this cell.
	 */
	public Tape() {
		Cell cell = new Cell();
		cell.content = ' '; 
		cell.next = null;
		cell.prev = null;
		currentCell = cell; 
	}

	/**
     * Returns the reference to the current cell.
     *
     * @return The reference to the current cell.
     */
	public Cell getCurrentCell() {
		return currentCell;
	}
	
	/**
    * Returns the character from the current cell.
    *
    * @return The character from the current cell.
    */
	public char getContent() {
		return currentCell.content;
	}
	
	/**
     * Changes the character in the current cell to the specified value.
     *
     * @param ch The new character value.
     */
	public void setContent(char ch) {
		currentCell.content = ch;
	}
	
	/**
	 * moveLeft method moves the current cell one position to 
	 * the left along the tape. If the current cell is the leftmost cell 
	 * that exists, then a new cell must be created and 
	 * added to the tape at the left of the current cell, and then 
	 * the current cell pointer can be moved to point to the new cell. 
	 * The content of the new cell should be a blank space.
	 */
	public void moveLeft() {
		
		if (currentCell.prev != null) { // If the previous to current cell is not null
			currentCell = currentCell.prev;
		}
		else { // Else, create a new cell
			Cell newCell = new Cell(); 
			currentCell.prev = newCell; // Added to the left of the current cell
			newCell.next = currentCell;
			newCell.content = ' ';   // Content of the new cell
			currentCell = newCell;
		}
	}
	
	/**
	 * moveRight method moves the current cell one position to 
	 * the right along the tape. If the current cell is the rightmost cell 
	 * that exists, then a new cell must be created and 
	 * added to the tape at the right of the current cell, and then 
	 * the current cell pointer can be moved to point to the new cell. 
	 * The content of the new cell should be a blank space.
	 */
	public void moveRight() {
		
		if (currentCell.next != null) { // If the next to current cell is not null
			currentCell = currentCell.next;
		}
		else { // Else, create a new cell
			Cell newCell = new Cell(); 
			currentCell.next = newCell; // Added to the left of the current cell
			newCell.prev = currentCell;
			newCell.content = ' ';   // Content of the new cell
			currentCell = newCell;
		}
	}
	
	/**
    * Returns a string consisting of the characters from all the cells on the
    * tape, read from left to right, except that leading or trailing blank
    * characters should be discarded. It uses StringBuilder to efficiently
    * concatenate and manipulate strings without creating new string objects for
    * every operation.
    *
    * @return The contents of the tape as a string.
    */
	public String getTapeContents() {
		
		// Create a different pointer to move
		Cell runner;
		
		// Point to the currentCell
		runner = currentCell;
		
		while (runner.prev != null) { // If there is previous cell to the current one
			runner = runner.prev; 	// Move to the leftmost cell
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		
		while (runner != null) {
			stringBuilder.append(runner.content);
			runner = runner.next;
		}
		
		return stringBuilder.toString().trim();
	}
}
