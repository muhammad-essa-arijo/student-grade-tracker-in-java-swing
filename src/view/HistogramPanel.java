package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import model.GradeBook;

/**
 * Panel that displays a histogram of grade distribution.
 * Shows the frequency of grades in 10-point bins (0-9, 10-19, etc.).
 * Uses AWT Graphics2D for custom drawing.
 * 
 * @author Student Grade Tracker Team
 * @version 1.0
 */
public class HistogramPanel extends JPanel {
    private GradeBook gradeBook;
    private static final int BAR_HEIGHT_SCALE = 20;
    private static final int MARGIN = 40;
    private static final int BIN_WIDTH = 40;
    
    /**
     * Constructs a HistogramPanel for the given GradeBook.
     * 
     * @param gradeBook the GradeBook to display statistics for
     */
    public HistogramPanel(GradeBook gradeBook) {
        this.gradeBook = gradeBook;
        setPreferredSize(new Dimension(600, 350));
        setBackground(Color.WHITE);
    }
    
    /**
     * Updates the displayed data and refreshes the histogram.
     * Call this method after grade data has changed.
     */
    public void updateHistogram() {
        revalidate();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        if (gradeBook.getStudentCount() == 0) {
            drawEmptyMessage(g2d, width, height);
            return;
        }
        
        // Draw title
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(new Color(33, 99, 166));
        g2d.drawString("Grade Distribution Histogram", MARGIN, 25);
        
        // Draw axes
        drawAxes(g2d, width, height);
        
        // Get grade distribution
        int[] distribution = gradeBook.getGradeDistribution();
        
        // Find max value for scaling
        int maxCount = 0;
        for (int count : distribution) {
            if (count > maxCount) {
                maxCount = count;
            }
        }
        if (maxCount == 0) maxCount = 1; // Avoid division by zero
        
        // Draw bars with gradient colors
        int plotAreaHeight = height - 2 * MARGIN;
        Color[] barColors = new Color[]{
            new Color(220, 53, 69),    // Red for low grades
            new Color(255, 193, 7),   // Yellow for middle grades
            new Color(40, 167, 69)    // Green for high grades
        };
        
        for (int i = 0; i < distribution.length; i++) {
            int barHeight = (int) ((distribution[i] / (double) maxCount) * plotAreaHeight);
            int x = MARGIN + (i * BIN_WIDTH) + 5;
            int y = height - MARGIN - barHeight;
            
            // Choose color based on grade range (red for low, yellow for middle, green for high)
            Color barColor = i < 3 ? barColors[0] : (i < 6 ? barColors[1] : barColors[2]);
            
            // Draw bar with color
            g2d.setColor(barColor);
            g2d.fillRect(x, y, BIN_WIDTH - 10, barHeight);
            
            // Draw border
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRect(x, y, BIN_WIDTH - 10, barHeight);
            
            // Draw label
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            String label = (i * 10) + "-" + ((i + 1) * 10 - 1);
            FontMetrics fm = g2d.getFontMetrics();
            int labelX = x + (BIN_WIDTH - 10 - fm.stringWidth(label)) / 2;
            g2d.setColor(Color.BLACK);
            g2d.drawString(label, labelX, height - MARGIN + 20);
            g2d.drawString(label, labelX, height - MARGIN + 20);
            
            // Draw count on top of bar
            String countLabel = String.valueOf(distribution[i]);
            int countX = x + (BIN_WIDTH - 10 - fm.stringWidth(countLabel)) / 2;
            g2d.drawString(countLabel, countX, y - 5);
            
            g2d.setColor(new Color(70, 130, 180));
        }
    }
    
    /**
     * Draws the coordinate axes (X and Y).
     * 
     * @param g2d the Graphics2D object
     * @param width the panel width
     * @param height the panel height
     */
    private void drawAxes(Graphics2D g2d, int width, int height) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        
        // Y-axis
        g2d.drawLine(MARGIN, MARGIN, MARGIN, height - MARGIN);
        
        // X-axis
        g2d.drawLine(MARGIN, height - MARGIN, width - MARGIN, height - MARGIN);
        
        // Labels
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Grade Range", (width / 2) - 40, height - 10);
        
        // Y-axis label (rotated)
        g2d.rotate(-Math.PI / 2);
        g2d.drawString("Frequency", -height / 2, 15);
        g2d.rotate(Math.PI / 2);
    }
    
    /**
     * Draws a message when no students are in the grade book.
     * 
     * @param g2d the Graphics2D object
     * @param width the panel width
     * @param height the panel height
     */
    private void drawEmptyMessage(Graphics2D g2d, int width, int height) {
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.setColor(Color.GRAY);
        String message = "No students to display. Add students to generate histogram.";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(message)) / 2;
        int y = height / 2;
        g2d.drawString(message, x, y);
    }
}
