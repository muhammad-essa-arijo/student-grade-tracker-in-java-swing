package view;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import model.GradeBook;
import model.Student;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(33, 99, 166);
    private static final Color SECONDARY_COLOR = new Color(70, 130, 180);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color LIGHT_BG = new Color(240, 248, 255);
    private static final Color DARK_TEXT = new Color(33, 33, 33);
    
    private GradeBook gradeBook;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JLabel statisticsLabel;
    private JTabbedPane tabbedPane;
    private HistogramPanel histogramPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton clearSearchButton;
    
    // Buttons
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton exportSummaryButton;
    
    // Menu items (for controller to access)
    private JMenuItem saveMenuItem;
    private JMenuItem loadMenuItem;
    private JMenuItem exportMenuItem;
    private JMenuItem exportSummaryMenuItem;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem autosaveCheckBox;
    
    public MainFrame(GradeBook gradeBook) {
        this.gradeBook = gradeBook;
        initComponents();
        setupLayout();
        setupMenuBar();
        refreshTable();
        updateStatistics();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setTitle("Student Grade Tracker");
        getContentPane().setBackground(LIGHT_BG);
    }
    
    /**
     * Initializes all GUI components.
     */
    private void initComponents() {
        // Table setup
        tableModel = new DefaultTableModel(
            new String[]{"Name", "Assignment", "Quiz", "Midterm", "Final", "Average", "Grade", "GPA"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setRowHeight(30);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studentTable.setGridColor(new Color(220, 220, 220));
        studentTable.setSelectionBackground(SECONDARY_COLOR);
        studentTable.setSelectionForeground(Color.WHITE);
        studentTable.setShowGrid(true);
        studentTable.setShowHorizontalLines(true);
        studentTable.setShowVerticalLines(true);
        studentTable.setBackground(Color.WHITE);
        studentTable.setForeground(DARK_TEXT);
        
        // Custom header renderer with modern styling
        JTableHeader header = studentTable.getTableHeader();
        header.setDefaultRenderer(new ModernTableHeaderRenderer());
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(25, 80, 140)));
        
        // Custom cell renderer for modern appearance
        TableCellRenderer cellRenderer = new ModernTableCellRenderer();
        for (int i = 0; i < studentTable.getColumnCount(); i++) {
            studentTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        // Enable sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        studentTable.setRowSorter(sorter);
        
        // Set column widths
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        for (int i = 1; i < studentTable.getColumnCount(); i++) {
            studentTable.getColumnModel().getColumn(i).setPreferredWidth(85);
        }
    }
    
    /**
     * Custom table header renderer with modern styling.
     */
    private class ModernTableHeaderRenderer extends JLabel implements TableCellRenderer {
        private static final long serialVersionUID = 1L;
        
        public ModernTableHeaderRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
            setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, new Color(200, 200, 200)));
            setFont(new Font("Segoe UI", Font.BOLD, 13));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            setBackground(PRIMARY_COLOR);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            return this;
        }
    }
    
    /**
     * Custom table cell renderer for modern appearance with alternating colors.
     */
    private class ModernTableCellRenderer extends JLabel implements TableCellRenderer {
        private static final long serialVersionUID = 1L;
        private static final Color EVEN_ROW_COLOR = new Color(249, 250, 251);
        private static final Color ODD_ROW_COLOR = Color.WHITE;
        private static final Color HOVER_COLOR = new Color(240, 248, 255);
        
        public ModernTableCellRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.PLAIN, 12));
            setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            // Set text and font
            setText(value != null ? value.toString() : "");
            setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            if (isSelected) {
                setBackground(SECONDARY_COLOR);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else {
                // Alternating row colors
                setBackground(row % 2 == 0 ? EVEN_ROW_COLOR : ODD_ROW_COLOR);
                setForeground(DARK_TEXT);
                
                // Apply special colors for specific columns
                String cellText = value != null ? value.toString() : "";
                if (column == 6) { // Grade column
                    if (cellText.equals("A") || cellText.equals("B")) {
                        setForeground(SUCCESS_COLOR);
                        setFont(new Font("Segoe UI", Font.BOLD, 12));
                    } else if (cellText.equals("C")) {
                        setForeground(WARNING_COLOR);
                        setFont(new Font("Segoe UI", Font.BOLD, 12));
                    } else if (cellText.equals("D") || cellText.equals("F")) {
                        setForeground(DANGER_COLOR);
                        setFont(new Font("Segoe UI", Font.BOLD, 12));
                    }
                }
            }
            
            setHorizontalAlignment(column == 0 ? LEFT : CENTER);
            return this;
        }
    }
    
    /**
     * Sets up the overall layout of the frame.
     */
    private void setupLayout() {
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(LIGHT_BG);
        
        // Top panel with search
        JPanel topPanel = createTopPanel();
        contentPane.add(topPanel, BorderLayout.NORTH);
        
        // Center panel with tabs (table + histogram)
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(DARK_TEXT);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Table tab
        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        tabbedPane.addTab("Students", tableScrollPane);
        
        // Histogram tab
        histogramPanel = new HistogramPanel(gradeBook);
        tabbedPane.addTab("Grade Distribution", histogramPanel);
        
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        
        // Bottom panel with statistics
        JPanel bottomPanel = createBottomPanel();
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the top panel with search and action buttons.
     * 
     * @return the configured top panel
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(LIGHT_BG);
        topPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(LIGHT_BG);
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setForeground(DARK_TEXT);
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchPanel.add(searchLabel);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(DARK_TEXT);
        searchField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR));
        searchField.setMargin(new Insets(5, 5, 5, 5));
        searchPanel.add(searchField);
        
        searchButton = createModernButton("Find", SUCCESS_COLOR, CustomIcons.createSearchIcon(16, Color.WHITE));
        clearSearchButton = createModernButton("Clear", DANGER_COLOR, CustomIcons.createClearIcon(16, Color.WHITE));
        searchPanel.add(searchButton);
        searchPanel.add(clearSearchButton);
        
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        // Button panel with modern buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        buttonPanel.setBackground(LIGHT_BG);
        
        addButton = createModernButton("Add Student", SUCCESS_COLOR, CustomIcons.createAddIcon(16, Color.WHITE));
        editButton = createModernButton("Edit", SECONDARY_COLOR, CustomIcons.createEditIcon(16, Color.WHITE));
        deleteButton = createModernButton("Delete", DANGER_COLOR, CustomIcons.createDeleteIcon(16, Color.WHITE));
        refreshButton = createModernButton("Refresh", PRIMARY_COLOR, CustomIcons.createRefreshIcon(16, Color.WHITE));
        exportSummaryButton = createModernButton("Export", WARNING_COLOR, CustomIcons.createExportIcon(16, Color.WHITE));
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(exportSummaryButton);
        
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        return topPanel;
    }
    
    /**
     * Creates a modern styled button with icon, shadow effect, and hover animation.
     * 
     * @param text the button text
     * @param color the button base color
     * @param icon the Icon to display
     * @return the modern styled JButton
     */
    private JButton createModernButton(String text, Color color, Icon icon) {
        JButton button = new JButton(text, icon);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setMargin(new Insets(8, 14, 8, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        
        // Set button size
        button.setPreferredSize(new Dimension(110, 36));
        
        // Create custom button UI for modern appearance
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        
        // Add rounded corner border and shadow effect
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, darker(color, 0.7f)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Add hover and press effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            private Color originalColor = color;
            private Color originalBorder = darker(color, 0.7f);
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(lighter(originalColor, 1.2f));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 3, 0, darker(originalColor, 0.6f)),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
                button.setMargin(new Insets(7, 14, 9, 14));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 3, 0, originalBorder),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
                button.setMargin(new Insets(8, 14, 8, 14));
            }
            
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setMargin(new Insets(9, 14, 7, 14));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, darker(originalColor, 0.6f)),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
            
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setMargin(new Insets(8, 14, 8, 14));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 3, 0, originalBorder),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });
        
        return button;
    }
    
    /**
     * Creates a styled button with custom colors.
     * 
     * @param text the button text
     * @param color the button color
     * @return the configured JButton
     */
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setMargin(new Insets(6, 12, 6, 12));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(darker(color, 0.8f));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    /**
     * Lightens a color by a specified factor.
     * 
     * @param color the original color
     * @param factor the lightening factor (>1 for lighter, <1 for darker)
     * @return the lightened color
     */
    private Color lighter(Color color, float factor) {
        return new Color(
            Math.min((int) (color.getRed() * factor), 255),
            Math.min((int) (color.getGreen() * factor), 255),
            Math.min((int) (color.getBlue() * factor), 255)
        );
    }
    
    /**
     * Darkens a color by a specified factor.
     * 
     * @param color the original color
     * @param factor the darkening factor (0-1)
     * @return the darkened color
     */
    private Color darker(Color color, float factor) {
        return new Color(
            Math.max((int) (color.getRed() * factor), 0),
            Math.max((int) (color.getGreen() * factor), 0),
            Math.max((int) (color.getBlue() * factor), 0)
        );
    }
    
    /**
     * Creates the bottom panel with statistics display.
     * 
     * @return the configured bottom panel
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 1));
        bottomPanel.setBackground(new Color(230, 240, 250));
        
        statisticsLabel = new JLabel();
        statisticsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        statisticsLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        statisticsLabel.setOpaque(false);
        statisticsLabel.setForeground(DARK_TEXT);
        
        bottomPanel.add(statisticsLabel);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        return bottomPanel;
    }
    
    /**
     * Sets up the menu bar with File, Edit, and Help menus.
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(PRIMARY_COLOR);
        menuBar.setForeground(Color.WHITE);
        
        // File menu
        JMenu fileMenu = createStyledMenu("File");
        loadMenuItem = createStyledMenuItem("📂 Load from CSV...");
        saveMenuItem = createStyledMenuItem("💾 Save to CSV...");
        exportMenuItem = createStyledMenuItem("📊 Export Statistics...");
        exportSummaryMenuItem = createStyledMenuItem("📋 Export Summary Report...");
        fileMenu.addSeparator();
        exitMenuItem = createStyledMenuItem("🚪 Exit");
        
        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(exportMenuItem);
        fileMenu.add(exportSummaryMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        
        menuBar.add(fileMenu);
        
        // Edit menu
        JMenu editMenu = createStyledMenu("Edit");
        undoMenuItem = createStyledMenuItem("↶ Undo");
        redoMenuItem = createStyledMenuItem("↷ Redo");
        editMenu.addSeparator();
        JMenuItem autoSaveItem = createStyledMenuItem("⚙️ Autosave Settings");
        autosaveCheckBox = autoSaveItem;
        
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        
        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);
        editMenu.addSeparator();
        
        menuBar.add(editMenu);
        
        // Help menu
        JMenu helpMenu = createStyledMenu("Help");
        JMenuItem aboutMenuItem = createStyledMenuItem("ℹ️ About");
        aboutMenuItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutMenuItem);
        
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Creates a styled menu with consistent formatting.
     * 
     * @param text the menu text
     * @return the styled JMenu
     */
    private JMenu createStyledMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setFont(new Font("Arial", Font.BOLD, 12));
        menu.setForeground(DARK_TEXT);
        return menu;
    }
    
    /**
     * Creates a styled menu item with icon and consistent formatting.
     * 
     * @param text the menu item text (should include emoji icon)
     * @return the styled JMenuItem
     */
    private JMenuItem createStyledMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Arial", Font.PLAIN, 11));
        item.setForeground(DARK_TEXT);
        item.setBackground(LIGHT_BG);
        return item;
    }
    
    /**
     * Displays an About dialog.
     */
    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "Student Grade Tracker v1.0\n\n" +
                "A comprehensive application for managing student grades\n" +
                "with weighted scoring, GPA calculation, and statistics.\n\n" +
                "© 2024 Student Grade Tracker Team",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Refreshes the student table with current data.
     */
    public void refreshTable() {
        // Display all students
        displaySearchResults(gradeBook.getAllStudents());
    }
    
    /**
     * Displays search results or all students in the table.
     * 
     * @param students the list of students to display
     */
    public void displaySearchResults(List<Student> students) {
        // Clear all existing rows first
        tableModel.setRowCount(0);
        
        // Add each student to the table
        for (Student student : students) {
            Object[] rowData = new Object[]{
                student.getName(),
                String.format("%.2f", student.getAssignmentGrade()),
                String.format("%.2f", student.getQuizGrade()),
                String.format("%.2f", student.getMidtermGrade()),
                String.format("%.2f", student.getFinalGrade()),
                String.format("%.2f", student.getWeightedAverage()),
                student.getLetterGrade(),
                String.format("%.2f", student.getGPA())
            };
            tableModel.addRow(rowData);
        }
        
        // Update histogram
        histogramPanel.updateHistogram();
        
        // Refresh UI components
        studentTable.revalidate();
        studentTable.repaint();
        tabbedPane.revalidate();
        tabbedPane.repaint();
    }
    
    /**
     * Updates the statistics label with current grade book statistics.
     */
    public void updateStatistics() {
        int studentCount = gradeBook.getStudentCount();
        double classAvg = gradeBook.getClassAverage();
        double highGrade = gradeBook.getHighestGrade();
        double lowGrade = gradeBook.getLowestGrade();
        String topName = gradeBook.getTopStudent() != null ? gradeBook.getTopStudent().getName() : "N/A";
        String bottomName = gradeBook.getBottomStudent() != null ? gradeBook.getBottomStudent().getName() : "N/A";
        
        String htmlStats = String.format(
            "<html><table cellspacing='15' cellpadding='5' border='0'><tr>" +
            "<td style='background-color: #E8F4F8; padding: 8px; border-radius: 4px;'><b>👥 Students:</b> <span style='color: #212166; font-weight: bold;'>%d</span></td>" +
            "<td style='background-color: #FFF3CD; padding: 8px; border-radius: 4px;'><b>📈 Avg:</b> <span style='color: #856404; font-weight: bold;'>%.2f</span></td>" +
            "<td style='background-color: #D4EDDA; padding: 8px; border-radius: 4px;'><b>⬆️ High:</b> <span style='color: #155724; font-weight: bold;'>%.2f</span></td>" +
            "<td style='background-color: #F8D7DA; padding: 8px; border-radius: 4px;'><b>⬇️ Low:</b> <span style='color: #721C24; font-weight: bold;'>%.2f</span></td>" +
            "<td style='background-color: #D1ECF1; padding: 8px; border-radius: 4px;'><b>🏆 Top:</b> <span style='color: #0C5460; font-weight: bold;'>%s</span></td>" +
            "<td style='background-color: #D1ECF1; padding: 8px; border-radius: 4px;'><b>📉 Bottom:</b> <span style='color: #0C5460; font-weight: bold;'>%s</span></td>" +
            "</tr></table></html>",
            studentCount, classAvg, highGrade, lowGrade, topName, bottomName
        );
        statisticsLabel.setText(htmlStats);
        statisticsLabel.setForeground(PRIMARY_COLOR);
    }

    /**
     * Shows a temporary status message in the statistics label.
     * 
     * @param message the message to display
     */
    public void showStatusMessage(String message) {
        String originalText = statisticsLabel.getText();
        statisticsLabel.setText(message);
        
        // Restore original text after 1.5 seconds
        Timer delay = new Timer(1500, e -> statisticsLabel.setText(originalText));
        delay.setRepeats(false);
        delay.start();
    }
    
    /**
     * Gets the currently selected row index in the student table.
     * 
     * @return the selected row index, or -1 if no row is selected
     */
    public int getSelectedStudentIndex() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            return -1;
        }
        return studentTable.convertRowIndexToModel(selectedRow);
    }
    
    /**
     * Gets the selected student from the table.
     * 
     * @return the selected Student, or null if no row is selected
     */
    public Student getSelectedStudent() {
        int index = getSelectedStudentIndex();
        if (index < 0) {
            return null;
        }
        return gradeBook.getStudent(index);
    }
    
    /**
     * Displays a file chooser for CSV file selection.
     * 
     * @param mode JFileChooser.OPEN_DIALOG or JFileChooser.SAVE_DIALOG
     * @return the selected file path, or null if cancelled
     */
    public String showFileChooser(int mode) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        
        int returnValue = (mode == JFileChooser.OPEN_DIALOG) ?
                        fileChooser.showOpenDialog(this) :
                        fileChooser.showSaveDialog(this);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            
            if (!filePath.endsWith(".csv")) {
                filePath += ".csv";
            }
            
            return filePath;
        }
        return null;
    }
    
    /**
     * Shows an error message dialog.
     * 
     * @param title the dialog title
     * @param message the error message
     */
    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Shows an information message dialog.
     * 
     * @param title the dialog title
     * @param message the information message
     */
    public void showInfo(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows a confirmation dialog.
     * 
     * @param title the dialog title
     * @param message the confirmation message
     * @return JOptionPane.YES_OPTION or JOptionPane.NO_OPTION
     */
    public int showConfirm(String title, String message) {
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
    }
    
    // Getters for buttons and menu items (used by controller)
    
    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getRefreshButton() { return refreshButton; }
    public JButton getExportSummaryButton() { return exportSummaryButton; }
    public JButton getSearchButton() { return searchButton; }
    public JButton getClearSearchButton() { return clearSearchButton; }
    
    public JMenuItem getSaveMenuItem() { return saveMenuItem; }
    public JMenuItem getLoadMenuItem() { return loadMenuItem; }
    public JMenuItem getExportMenuItem() { return exportMenuItem; }
    public JMenuItem getExportSummaryMenuItem() { return exportSummaryMenuItem; }
    public JMenuItem getUndoMenuItem() { return undoMenuItem; }
    public JMenuItem getRedoMenuItem() { return redoMenuItem; }
    public JMenuItem getExitMenuItem() { return exitMenuItem; }
    
    public String getSearchText() { return searchField.getText().trim(); }
    public void clearSearch() { searchField.setText(""); }
    
    public JTable getStudentTable() { return studentTable; }
    public GradeBook getGradeBook() { return gradeBook; }
}
