/**
 * A simple implementation of a hash table with separate chaining using linked lists.
 */
public class HashTable {

	private static int size = 10; // Default size of the hash table.

	private LinkedList[] table; // The array representing the hash table.

	private int count = 0;  // The number of nodes in the hash table.

	/**
     * Constructs a hash table with the default size.
     */
	public HashTable() { 
		table = new LinkedList[size];
		for (int i = 0; i < size; i++) {
			table[i] = new LinkedList();
		}
	}

	/**
     * Constructs a hash table with a specified size.
     *
     * @param length The size of the hash table.
     * @throws IllegalArgumentException if the specified size is not a positive integer.
     */
	public HashTable(int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("Table size must be a positive integer");
		}
		table = new LinkedList[length];
		for (int i = 0; i < length; i++) {
			table[i] = new LinkedList();
		}
	}

	private int hash(Object key) {
		return (Math.abs(key.hashCode())) % table.length;
	}

	private static class Node {
		private String key;
		private String value;
		private Node next;

		public Node(String key, String value) {
			this.key = key;
			this.value = value;
			this.next = null;
		}
	}

	public int size() {
		return count;
	}

	/**
     * Inserts a key-value pair into the hash table.
     *
     * @param key   The key to be inserted.
     * @param value The value associated with the key.
     * @throws IllegalArgumentException if the key is null.
     */
	public void put(String key, String value) {
		int index = hash(key);
		if (key == null) {
			throw new IllegalArgumentException("Key cannot be null");
		}
		if (table[index].search(key) == null) {
			table[index].insert(key, value);
			count++;
		}
	}

	 /**
     * Retrieves the value associated with the specified key from the hash table.
     *
     * @param key The key to search for.
     * @return The value associated with the key, or null if the key is not found.
     * @throws IllegalArgumentException if the key is null.
     */
	public String get(String key) {
		if (key == null) {
			throw new IllegalArgumentException("Key cannot be null");
		}
		int index = hash(key);
		return table[index].search(key);
	}

	/**
     * Removes the key-value pair with the specified key from the hash table.
     *
     * @param key The key to be removed.
     * @throws IllegalArgumentException if the key is null.
     */
	public void remove(String key) {
		if (key == null) {
			throw new IllegalArgumentException("Key cannot be null");
		}
		int index = hash(key);
		if (table[index].search(key) != null) {
			table[index].delete(key);
			count--;
		}
	}

	/**
     * Checks if the hash table contains the specified key.
     *
     * @param key The key to check for.
     * @return true if the key is found, false otherwise.
     * @throws IllegalArgumentException if the key is null.
     */
	public boolean containsKey(String key) {
		int index = hash(key);
		if (table[index].search(key) != null) {
			return true;
		}
		return false;
	}

	/**
     * Prints the elements of the hash table.
     */
	public void printTable() {
		for (int i = 0; i < table.length; i++) {
			LinkedList list = table[i];
			Node current = list.head;

			System.out.print("Index " + i + ": ");
			while (current != null) {
				System.out.print("(" + current.key + ", " + current.value + ") ");
				current = current.next;
			}
			System.out.println(); // Move to the next line for the next index
		}
	}

	private class LinkedList {
		private Node head;

		public void insert(String key, String value) {
			Node newNode = new Node(key, value);
			if (head == null) {
				head = newNode;
			} else {
				newNode.next = head;
				head = newNode;
			}
		}

		public String search(String key) {
			Node current = head;
			while (current != null) {
				if (current.key.equals(key)){
					return current.value;
				}
				current = current.next;
			}
			return null;
		}

		public void delete(String key) {
			Node current = head;
			Node prev = null;

			while (current != null && !current.key.equals(key)) {
				prev = current;
				current = current.next;
			}

			if (current != null) {
				if (prev != null) {
					prev.next = current.next;
				} else {
					head = current.next;
				}
			}
		}
	}


}

