package view;

import javax.swing.Icon;
import java.awt.*;

/**
 * Custom icon implementations using AWT Graphics2D for a professional appearance.
 */
public class CustomIcons {
    
    /**
     * Plus icon for Add button.
     */
    public static Icon createAddIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int margin = 4;
                int centerX = x + size / 2;
                int centerY = y + size / 2;
                int halfSize = (size - margin * 2) / 2;
                
                // Horizontal line
                g2d.drawLine(centerX - halfSize, centerY, centerX + halfSize, centerY);
                // Vertical line
                g2d.drawLine(centerX, centerY - halfSize, centerX, centerY + halfSize);
            }
            
            @Override
            public int getIconWidth() { return size; }
            
            @Override
            public int getIconHeight() { return size; }
        };
    }
    
    /**
     * Pencil icon for Edit button.
     */
    public static Icon createEditIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                
                int margin = 3;
                int px = x + margin;
                int py = y + margin;
                int sz = size - 2 * margin;
                
                // Pencil body
                int[] pencilX = {px + sz - 2, px + sz, px + 3, px + 1};
                int[] pencilY = {py + 1, py + 3, py + sz - 2, py + sz};
                g2d.fillPolygon(pencilX, pencilY, 4);
                
                // Pencil tip (eraser)
                g2d.setColor(new Color(200, 200, 200));
                g2d.fillRect(px, py + sz - 3, 3, 3);
            }
            
            @Override
            public int getIconWidth() { return size; }
            
            @Override
            public int getIconHeight() { return size; }
        };
    }
    
    /**
     * Trash can icon for Delete button.
     */
    public static Icon createDeleteIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(1.5f));
                
                int margin = 2;
                int px = x + margin;
                int py = y + margin;
                int sz = size - 2 * margin;
                
                // Can body
                int canX = px + sz / 4;
                int canY = py + sz / 3;
                int canW = sz / 2;
                int canH = 2 * sz / 3;
                g2d.fillRect(canX, canY, canW, canH);
                
                // Can lid
                g2d.drawRect(canX - 2, canY - 3, canW + 4, 3);
                
                // Handle
                g2d.drawArc(canX - 2, canY - 5, canW + 4, 4, 0, 180);
                
                // Lines inside can
                g2d.drawLine(canX + 2, canY + 2, canX + 2, canY + canH - 2);
                g2d.drawLine(canX + canW / 2, canY + 2, canX + canW / 2, canY + canH - 2);
                g2d.drawLine(canX + canW - 2, canY + 2, canX + canW - 2, canY + canH - 2);
            }
            
            @Override
            public int getIconWidth() { return size; }
            
            @Override
            public int getIconHeight() { return size; }
        };
    }
    
    /**
     * Refresh/Reload icon for Refresh button.
     */
    public static Icon createRefreshIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int centerX = x + size / 2;
                int centerY = y + size / 2;
                int radius = size / 3;
                
                // Arc (3/4 circle)
                g2d.drawArc(centerX - radius, centerY - radius, 2 * radius, 2 * radius, 45, 270);
                
                // Arrow head
                int[] arrowX = {centerX + radius + 1, centerX + radius + 4, centerX + radius - 1};
                int[] arrowY = {centerY - radius - 1, centerY - radius + 3, centerY - radius + 4};
                g2d.fillPolygon(arrowX, arrowY, 3);
            }
            
            @Override
            public int getIconWidth() { return size; }
            
            @Override
            public int getIconHeight() { return size; }
        };
    }
    
    /**
     * Download/Export icon for Export button.
     */
    public static Icon createExportIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int margin = 3;
                int px = x + margin;
                int py = y + margin;
                int sz = size - 2 * margin;
                
                // Download box outline
                g2d.drawRect(px, py + sz / 2 - 2, sz, sz / 2 + 2);
                
                // Download indicator line
                g2d.drawLine(px + sz / 2, py + 2, px + sz / 2, py + sz / 2);
                
                // Arrow down
                int[] arrowX = {px + sz / 2 - 2, px + sz / 2, px + sz / 2 + 2};
                int[] arrowY = {py + sz / 2 - 2, py + sz / 2 + 1, py + sz / 2 - 2};
                g2d.fillPolygon(arrowX, arrowY, 3);
            }
            
            @Override
            public int getIconWidth() { return size; }
            
            @Override
            public int getIconHeight() { return size; }
        };
    }
    
    /**
     * Magnifying glass icon for Find button.
     */
    public static Icon createSearchIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int margin = 2;
                int px = x + margin;
                int py = y + margin;
                int sz = size - 2 * margin;
                
                // Circle (lens)
                int circleSize = (int) (sz * 0.6);
                g2d.drawOval(px + 1, py + 1, circleSize, circleSize);
                
                // Handle
                int handleStart = px + circleSize / 2 + 2;
                int handleEnd = px + sz - 1;
                g2d.drawLine(handleStart, handleStart, handleEnd, handleEnd);
            }
            
            @Override
            public int getIconWidth() { return size; }
            
            @Override
            public int getIconHeight() { return size; }
        };
    }
    
    /**
     * X/Clear icon for Clear button.
     */
    public static Icon createClearIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int margin = 4;
                int px = x + margin;
                int py = y + margin;
                int sz = size - 2 * margin;
                
                // X shape
                g2d.drawLine(px, py, px + sz, py + sz);
                g2d.drawLine(px + sz, py, px, py + sz);
            }
            
            @Override
            public int getIconWidth() { return size; }
            
            @Override
            public int getIconHeight() { return size; }
        };
    }
}
