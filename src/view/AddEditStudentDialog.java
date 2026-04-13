package view;

import javax.swing.*;
import java.awt.*;
import model.Student;

/**
 * Dialog for adding or editing a student's grades.
 * Provides input fields for name and all grade components.
 * Validates input before closing.
 * 
 * @author Student Grade Tracker Team
 * @version 1.0
 */
public class AddEditStudentDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private JTextField nameField;
    private JTextField assignmentField;
    private JTextField quizField;
    private JTextField midtermField;
    private JTextField finalField;
    private JButton okButton;
    private JButton cancelButton;
    private boolean confirmed = false;
    private Student student;
    
    /**
     * Constructs an AddEditStudentDialog for creating a new student.
     * 
     * @param parent the parent frame
     */
    public AddEditStudentDialog(JFrame parent) {
        this(parent, null, "Add Student");
    }
    
    /**
     * Constructs an AddEditStudentDialog for editing an existing student.
     * 
     * @param parent the parent frame
     * @param student the student to edit (null for add mode)
     * @param title the dialog title
     */
    public AddEditStudentDialog(JFrame parent, Student student, String title) {
        super(parent, title, true);
        this.student = student;
        initComponents();
        if (student != null) {
            populateFields(student);
        }
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
    }
    
    /**
     * Initializes all GUI components.
     */
    private void initComponents() {
        setSize(400, 300);
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Input fields panel
        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        // Name field
        fieldsPanel.add(createLabel("Student Name:"));
        nameField = new JTextField(15);
        fieldsPanel.add(nameField);
        
        // Assignment field
        fieldsPanel.add(createLabel("Assignment (0-15):"));
        assignmentField = new JTextField("0");
        fieldsPanel.add(assignmentField);
        
        // Quiz field
        fieldsPanel.add(createLabel("Quiz (0-15):"));
        quizField = new JTextField("0");
        fieldsPanel.add(quizField);
        
        // Midterm field
        fieldsPanel.add(createLabel("Midterm (0-30):"));
        midtermField = new JTextField("0");
        fieldsPanel.add(midtermField);
        
        // Final field
        fieldsPanel.add(createLabel("Final (0-40):"));
        finalField = new JTextField("0");
        fieldsPanel.add(finalField);
        
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        
        okButton.addActionListener(e -> onOKPressed());
        cancelButton.addActionListener(e -> onCancelPressed());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Creates a label with consistent formatting.
     * 
     * @param text the label text
     * @return a formatted JLabel
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }
    
    /**
     * Populates the form fields with existing student data.
     * 
     * @param student the student whose data to display
     */
    private void populateFields(Student student) {
        nameField.setText(student.getName());
        assignmentField.setText(String.format("%.2f", student.getAssignmentGrade()));
        quizField.setText(String.format("%.2f", student.getQuizGrade()));
        midtermField.setText(String.format("%.2f", student.getMidtermGrade()));
        finalField.setText(String.format("%.2f", student.getFinalGrade()));
    }
    
    /**
     * Handles OK button press with validation.
     */
    private void onOKPressed() {
        if (validateInput()) {
            confirmed = true;
            dispose();
        }
    }
    
    /**
     * Handles Cancel button press.
     */
    private void onCancelPressed() {
        confirmed = false;
        dispose();
    }
    
    /**
     * Validates all input fields.
     * 
     * @return true if all inputs are valid
     */
    private boolean validateInput() {
        String name = nameField.getText().trim();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Student name cannot be empty.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            double assignment = parseGrade(assignmentField.getText());
            double quiz = parseGrade(quizField.getText());
            double midterm = parseGrade(midtermField.getText());
            double finalGrade = parseGrade(finalField.getText());
            
            if (assignment < 0 || assignment > 15 ||
                quiz < 0 || quiz > 15 ||
                midterm < 0 || midterm > 30 ||
                finalGrade < 0 || finalGrade > 40) {
                throw new NumberFormatException();
            }
            
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Assignment: 0-15, Quiz: 0-15, Midterm: 0-30, Final: 0-40",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Parses a grade string to a double, handling empty values.
     * 
     * @param gradeStr the grade string
     * @return the parsed grade value
     * @throws NumberFormatException if the string cannot be parsed
     */
    private double parseGrade(String gradeStr) {
        String trimmed = gradeStr.trim();
        if (trimmed.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(trimmed);
    }
    
    /**
     * Checks if the user confirmed the dialog.
     * 
     * @return true if OK was pressed
     */
    public boolean isConfirmed() {
        return confirmed;
    }
    
    /**
     * Creates a Student object from the form data.
     * 
     * @return a new Student with entered data
     */
    public Student getStudent() {
        String name = nameField.getText().trim();
        double assignment = parseGrade(assignmentField.getText());
        double quiz = parseGrade(quizField.getText());
        double midterm = parseGrade(midtermField.getText());
        double finalGrade = parseGrade(finalField.getText());
        
        return new Student(name, assignment, quiz, midterm, finalGrade);
    }
}
