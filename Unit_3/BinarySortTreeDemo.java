import java.util.Random;

public class BinarySortTreeDemo {


	/**
	 * An object of type TreeNode represents one node in a binary tree of integers.
	 */
	private static class TreeNode {
		int num;      // The data in this node.
		TreeNode left;    // Pointer to left subtree.
		TreeNode right;   // Pointer to right subtree.
		TreeNode(int N) {
			// Constructor.  Make a node containing a number.
			// Note that left and right pointers are initially null.
			num = N;
		}
	}

	/**
	 * This tree is used in this program as a binary sort tree.
	 * When the tree is empty, root is null (as it is initially).
	 */
	private static TreeNode root;  // Pointer to the root node in a binary tree.

	public static void main(String[] args) {

		// Clear the root
		root = null;

		// Create a random object
		Random ran = new Random();

		// Create random 1023 nodes and insert
		for (int i = 0; i < 1023; i++) {
			insert(ran.nextInt(1024));
		}

		int leafCount = countLeaves(root);
		int sumOfDepths = sumDepth(root, 0);
		int depthMax = maxDepth(root, 0);
		double averageDepth = ((double) sumOfDepths / leafCount);

		System.out.println("Number of leaves: " + leafCount);
		System.out.println("Average depth of leaves: " + averageDepth);
		System.out.println("Maximum depth of leaves: " + depthMax);
	}

	/**
	 * Add the N to the binary sort tree to which the global variable 
	 * "root" refers.  
	 */
	private static void insert(int N) {

		if (root == null) {
			// The tree is empty, Set root to point to a new node containing
			// N.  This becomes the root node in the tree.
			root = new TreeNode(N);
			return;
		}

		// Runs down the tree to find a place for N, starting at root
		TreeNode runner = root;

		while (true) {
			if (N < runner.num) {
				// Since the new item is less than the item in runner,
				// it belongs in the left subtree of runner.  If there
				// is an open space at runner.left, add a new node there.
				// Otherwise, advance runner down one level to the left.
				if (runner.left == null) {
					runner.left = new TreeNode(N);
					return; // New N has been added to the tree
				}
				else {
					runner = runner.left;
				}
			}
			else {
				// Since the new item is greater than or equal to the item in
				// runner it belongs in the right subtree of runner.  If there
				// is an open space at runner.right, add a new node there.
				// Otherwise, advance runner down one level to the right.
				if (runner.right == null) {
					runner.right = new TreeNode(N);
					return; // New N has been added to the tree
				}
				else {
					runner = runner.right;
				}
			}
		}

	}


	/**
	 * Count the leaves(nodes) in the binary tree.
	 * @param node A pointer to the root of the tree.  A null value indicates
	 * an empty tree.
	 * @return the number of leaves in the tree to which node points.  For an
	 * empty tree, the value is zero.
	 */
	private static int countLeaves(TreeNode node) {
		if (node == null) {
			// Tree is empty, so return 0
			return 0;
		} 
		else if (node.left == null && node.right == null) {
			// If there are no left and right nodes, it is a leaf, so return 1
			return 1;
		}
		else {
			// Add up the root node and the nodes in its two subtrees.
			int leftCount = countLeaves( node.left );
			int rightCount = countLeaves( node.right );
			return leftCount + rightCount;  
		}
	}

	/**
	 * Recursively calculates the sum of depths for all leaves in the binary tree.
	 *
	 * @param node    The current node in the binary tree.
	 * @param depth   The current depth of the recursion.
	 * @return The sum of depths for all leaves in the tree.
	 */
	private static int sumDepth(TreeNode node, int depth) {
		if (node == null) {
			// Tree is empty, so return 0
			return 0;
		}
		else if (node.left == null && node.right == null) {
			// If there is no left and right node, return depth
			return depth;
		}
		else {
			int leftDepth = sumDepth(node.left, depth + 1);
			int rightDepth = sumDepth(node.right, depth + 1);
			return leftDepth + rightDepth;
		}
	}

	/**
	 * Recursively calculates the maximum depth among all leaves in the binary tree.
	 *
	 * @param node     The current node in the binary tree.
	 * @param depth    The current depth of the recursion.
	 * @return The maximum depth among all leaves in the tree.
	 */
	private static int maxDepth(TreeNode node, int depth) {
		if (node == null) {
			// Tree is empty, so return 0
			return 0;
		}
		else if (node.left == null && node.right == null) {
			// If there is no left and right node, return depth
			return depth;
		}
		else {
			int leftDepth = maxDepth(node.left, depth + 1);
			int rightDepth = maxDepth(node.right, depth + 1);
			return Math.max(leftDepth, rightDepth);
		}
	}
}
