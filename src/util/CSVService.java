package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.GradeBook;
import model.Student;

/**
 * Service class for handling CSV file operations.
 * Supports loading and saving student grade data from/to CSV files.
 * 
 * CSV Format: Name,Assignment,Quiz,Midterm,Final
 * 
 * @author Student Grade Tracker Team
 * @version 1.0
 */
public class CSVService {
    public static final String CSV_EXTENSION = ".csv";
    private static final String DELIMITER = ",";
    
    /**
     * Loads student data from a CSV file into a GradeBook.
     * Expects CSV format: Name,Assignment,Quiz,Midterm,Final
     * 
     * @param filePath the path to the CSV file
     * @param gradeBook the GradeBook to populate
     * @throws IOException if the file cannot be read
     * @throws IllegalArgumentException if CSV format is invalid
     */
    public static void loadFromCSV(String filePath, GradeBook gradeBook) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        
        gradeBook.clearAllStudents();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    Student student = parseStudentFromCSV(line);
                    if (student != null) {
                        gradeBook.addStudent(student);
                    }
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Error on line " + lineNumber + ": " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Saves all students from a GradeBook to a CSV file.
     * 
     * @param filePath the path where the CSV file will be saved
     * @param gradeBook the GradeBook to save
     * @throws IOException if the file cannot be written
     */
    public static void saveToCSV(String filePath, GradeBook gradeBook) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Write header
            writer.println("Name,Assignment,Quiz,Midterm,Final");
            
            // Write student data
            for (Student student : gradeBook.getAllStudents()) {
                writer.println(formatStudentToCSV(student));
            }
            
            // Ensure all data is written to file
            writer.flush();
        }
    }
    
    /**
     * Parses a single CSV line into a Student object.
     * 
     * @param line the CSV line to parse
     * @return a Student object, or null if line is empty
     * @throws IllegalArgumentException if the line format is invalid
     */
    private static Student parseStudentFromCSV(String line) {
        String[] parts = line.split(DELIMITER);
        
        if (parts.length < 1) {
            return null;
        }
        
        try {
            String name = parts[0].trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Student name cannot be empty");
            }
            
            double assignment = parts.length > 1 ? parseGrade(parts[1]) : 0.0;
            double quiz = parts.length > 2 ? parseGrade(parts[2]) : 0.0;
            double midterm = parts.length > 3 ? parseGrade(parts[3]) : 0.0;
            double finalGrade = parts.length > 4 ? parseGrade(parts[4]) : 0.0;
            
            return new Student(name, assignment, quiz, midterm, finalGrade);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid grade format: " + e.getMessage());
        }
    }
    
    /**
     * Formats a Student object as a CSV line.
     * 
     * @param student the student to format
     * @return a CSV line string
     */
    private static String formatStudentToCSV(Student student) {
        return String.format("%s,%.2f,%.2f,%.2f,%.2f",
                            student.getName(),
                            student.getAssignmentGrade(),
                            student.getQuizGrade(),
                            student.getMidtermGrade(),
                            student.getFinalGrade());
    }
    
    /**
     * Parses a grade from string format, handling empty values.
     * 
     * @param gradeStr the grade string to parse
     * @return the parsed grade value
     * @throws NumberFormatException if the string cannot be parsed as a number
     */
    private static double parseGrade(String gradeStr) {
        String trimmed = gradeStr.trim();
        if (trimmed.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(trimmed);
    }
    
    /**
     * Validates that a file path has a CSV extension.
     * 
     * @param filePath the path to validate
     * @return true if the file has a .csv extension
     */
    public static boolean isValidCSVFile(String filePath) {
        return filePath != null && filePath.toLowerCase().endsWith(CSV_EXTENSION);
    }
    
    /**
     * Exports grade statistics to a CSV file.
     * 
     * @param filePath the path where the statistics will be saved
     * @param gradeBook the GradeBook to export statistics from
     * @throws IOException if the file cannot be written
     */
    public static void exportStatistics(String filePath, GradeBook gradeBook) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("Grade Book Statistics");
            writer.println("Course: " + gradeBook.getCourseTitle());
            writer.println();
            writer.println("Summary Statistics");
            writer.println("Total Students," + gradeBook.getStudentCount());
            writer.println("Class Average," + String.format("%.2f", gradeBook.getClassAverage()));
            writer.println("Highest Grade," + String.format("%.2f", gradeBook.getHighestGrade()));
            writer.println("Lowest Grade," + String.format("%.2f", gradeBook.getLowestGrade()));
            writer.println();
            writer.println("Student Details");
            writer.println("Name,Average,Letter Grade,GPA");
            for (Student student : gradeBook.getAllStudents()) {
                writer.printf("%s,%.2f,%c,%.2f%n",
                            student.getName(),
                            student.getWeightedAverage(),
                            student.getLetterGrade(),
                            student.getGPA());
            }
            
            // Ensure all data is written to file
            writer.flush();
        }
    }

    /**
     * Exports a comprehensive summary report of all students to a CSV file.
     * Includes all grade components, weighted average, letter grade, and GPA.
     * 
     * @param filePath the path where the summary report will be saved
     * @param gradeBook the GradeBook to export from
     * @throws IOException if the file cannot be written
     */
    public static void exportSummaryReport(String filePath, GradeBook gradeBook) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Header with course title
            writer.println("Student Grade Summary Report");
            writer.println("Course: " + gradeBook.getCourseTitle());
            writer.println("======================================");
            writer.println();
            
            // Grading weights explanation
            writer.println("Grade Weights:");
            writer.println("Assignment: 15%, Quiz: 15%, Midterm: 30%, Final: 40%");
            writer.println();
            
            // Column headers
            writer.println("Student Summary");
            writer.println("Name,Assignment,Quiz,Midterm,Final,Weighted Average,Letter Grade,GPA");
            
            // Write each student's data
            for (Student student : gradeBook.getAllStudents()) {
                writer.printf("%s,%.2f,%.2f,%.2f,%.2f,%.2f,%c,%.2f%n",
                            student.getName(),
                            student.getAssignmentGrade(),
                            student.getQuizGrade(),
                            student.getMidtermGrade(),
                            student.getFinalGrade(),
                            student.getWeightedAverage(),
                            student.getLetterGrade(),
                            student.getGPA());
            }
            
            // Summary statistics at the end
            writer.println();
            writer.println("Class Statistics");
            writer.println("Total Students," + gradeBook.getStudentCount());
            writer.println("Class Average," + String.format("%.2f", gradeBook.getClassAverage()));
            writer.println("Highest Grade," + String.format("%.2f", gradeBook.getHighestGrade()));
            writer.println("Lowest Grade," + String.format("%.2f", gradeBook.getLowestGrade()));
            
            if (gradeBook.getTopStudent() != null) {
                writer.println("Top Student," + gradeBook.getTopStudent().getName() + 
                            " (" + String.format("%.2f", gradeBook.getTopStudent().getWeightedAverage()) + ")");
            }
            
            if (gradeBook.getBottomStudent() != null) {
                writer.println("Bottom Student," + gradeBook.getBottomStudent().getName() + 
                            " (" + String.format("%.2f", gradeBook.getBottomStudent().getWeightedAverage()) + ")");
            }
            
            // Ensure all data is written to file
            writer.flush();
        }
    }
}
