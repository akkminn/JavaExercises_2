import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InputCanvas extends JPanel implements MouseListener, MouseMotionListener {

	private Point[] points;
	private int pointCount;
	private int selectedPointIndex = -1;
	private DisplayCanvas displayCanvas;

	public InputCanvas(DisplayCanvas displayCanvas) {
		this.displayCanvas = displayCanvas;
		setPreferredSize(new Dimension(500, 500));
		setBackground(Color.WHITE);
		addMouseListener(this);
		addMouseMotionListener(this);
		initializePoints();
	}

	private void initializePoints() {

		int numPoints = pointCount; 
		int distanceBetweenPoints = 30;

		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;

		points = new Point[numPoints];

		for (int i = 0; i < numPoints; i++) {
			int x = centerX - (numPoints / 2 - i) * distanceBetweenPoints;
			int y = centerY;
			points[i] = new Point(x, y);
		}

	}

	public void setPointCount(int pointCount) {
		this.pointCount = pointCount;
		int numPoints = pointCount; 
		int distanceBetweenPoints = 30;
		int centerX = 250;
		int centerY = 250;

		points = new Point[numPoints];

		for (int i = 0; i < numPoints; i++) {
			int x = centerX - (numPoints / 2 - i) * distanceBetweenPoints;
			int y = centerY;
			points[i] = new Point(x, y);
		}
		repaint();
		displayCanvas.setPoints(points);
		displayCanvas.repaint();
	}

	public void install(int[] coordinates) {

		int numPoints = coordinates.length / 2;
		points = new Point[numPoints];

		for (int i = 0; i < numPoints; i++) {
			int x = coordinates[i * 2];
			int y = coordinates[i * 2 + 1];
			points[i] = new Point(x, y);
		}

		repaint();
		displayCanvas.setPoints(points);
		displayCanvas.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int numPoints = pointCount; 

		// Draw lines connecting the points
		g.setColor(Color.BLACK);

		for (int i = 0; i < points.length - 1; i++) {

			g.drawLine(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y);

		}

		// Draw points
		for (int i = 0; i < numPoints; i++) {
			Color pointColor = (i == 0 || i == points.length - 1) ? Color.BLUE : Color.RED;
			g.setColor(pointColor);
			g.fillOval(points[i].x - 5, points[i].y - 5, 10, 10);

		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Not needed for dragging
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Check if a point is clicked
		for (int i = 0; i < points.length; i++) {
			if (points[i].distance(e.getPoint()) < 5) {
				selectedPointIndex = i;
				break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Reset the selected point index when the mouse is released
		selectedPointIndex = -1;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// Update the position of the selected point while dragging
		if (selectedPointIndex != -1) {
			points[selectedPointIndex] = e.getPoint();
			repaint();
			displayCanvas.setPoints(points);
			displayCanvas.repaint();
		}
	}

	// Other mouse events not used in this example
	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}
}
