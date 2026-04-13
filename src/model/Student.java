package model;

import java.io.Serializable;

/**
 * Represents a student with weighted grades.
 * This class encapsulates student information including name and individual grade components
 * that are used to Calculate weighted average GPA and letter grades.
 * 
 * @author Student Grade Tracker Team
 * @version 1.0
 */
public class Student implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private double assignmentGrade;      // Weighted component 1
    private double quizGrade;             // Weighted component 2
    private double midtermGrade;          // Weighted component 3
    private double finalGrade;            // Weighted component 4
    
    // Default weights (can be customized)
    private double assignmentWeight = 0.15;  // 15%
    private double quizWeight = 0.15;        // 15%
    private double midtermWeight = 0.30;     // 30%
    private double finalWeight = 0.40;       // 40%
    
    /**
     * Constructs a Student with initial zero grades.
     * 
     * @param name the student's name
     */
    public Student(String name) {
        this.name = name;
        this.assignmentGrade = 0;
        this.quizGrade = 0;
        this.midtermGrade = 0;
        this.finalGrade = 0;
    }
    
    /**
     * Constructs a Student with specified grades.
     * 
     * @param name the student's name
     * @param assignmentGrade assignment component grade (0-15)
     * @param quizGrade quiz component grade (0-15)
     * @param midtermGrade midterm component grade (0-30)
     * @param finalGrade final exam grade (0-40)
     */
    public Student(String name, double assignmentGrade, double quizGrade,
                   double midtermGrade, double finalGrade) {
        this.name = name;
        this.assignmentGrade = validateAssignmentGrade(assignmentGrade);
        this.quizGrade = validateQuizGrade(quizGrade);
        this.midtermGrade = validateMidtermGrade(midtermGrade);
        this.finalGrade = validateFinalGrade(finalGrade);
    }
    
    /**
     * Validates that assignment grade is within [0, 15].
     * 
     * @param grade the grade to validate
     * @return the validated grade
     * @throws IllegalArgumentException if grade is outside [0, 15]
     */
    private static double validateAssignmentGrade(double grade) {
        if (grade < 0 || grade > 15) {
            throw new IllegalArgumentException("Assignment grade must be between 0 and 15");
        }
        return grade;
    }

    /**
     * Validates that quiz grade is within [0, 15].
     * 
     * @param grade the grade to validate
     * @return the validated grade
     * @throws IllegalArgumentException if grade is outside [0, 15]
     */
    private static double validateQuizGrade(double grade) {
        if (grade < 0 || grade > 15) {
            throw new IllegalArgumentException("Quiz grade must be between 0 and 15");
        }
        return grade;
    }

    /**
     * Validates that midterm grade is within [0, 30].
     * 
     * @param grade the grade to validate
     * @return the validated grade
     * @throws IllegalArgumentException if grade is outside [0, 30]
     */
    private static double validateMidtermGrade(double grade) {
        if (grade < 0 || grade > 30) {
            throw new IllegalArgumentException("Midterm grade must be between 0 and 30");
        }
        return grade;
    }

    /**
     * Validates that final grade is within [0, 40].
     * 
     * @param grade the grade to validate
     * @return the validated grade
     * @throws IllegalArgumentException if grade is outside [0, 40]
     */
    private static double validateFinalGrade(double grade) {
        if (grade < 0 || grade > 40) {
            throw new IllegalArgumentException("Final grade must be between 0 and 40");
        }
        return grade;
    }
    
    /**
     * Calculates the weighted average grade out of 100.
     * Since grades are on different scales (0-15, 0-15, 0-30, 0-40) that total to 100,
     * the weighted average is simply the sum of all grade components.
     * 
     * @return the total grade out of 100
     */
    public double getWeightedAverage() {
        return assignmentGrade + quizGrade + midtermGrade + finalGrade;
    }
    
    /**
     * Converts a numeric grade to a letter grade (A, B, C, D, F).
     * Uses standard grading scale:
     * A: 90-100, B: 80-89, C: 70-79, D: 60-69, F: 0-59
     * 
     * @return the letter grade
     */
    public String getLetterGrade() {
        double average = getWeightedAverage();
        if (average >= 90) return "A+";
        if (average >= 80) return "A";
        if (average >= 70) return "B+";
        if (average >= 60) return "B";
        if (average >= 50) return "B";
        return "F";
    }
    
    /**
     * Converts a letter grade to GPA value (4.0 scale).
     * A: 4.0, B: 3.0, C: 2.0, D: 1.0, F: 0.0
     * 
     * @return the GPA value
     */
    public double getGPA() {
        String letterGrade = getLetterGrade();
        switch (letterGrade) {
            case "A+": return 4.0;
            case "A": return 3.5;
            case "B+": return 3.0;
            case "B": return 2.5;
            case "C": return 2.0;
            default: return 0.0;
        }
    }
    
    // Getters and Setters
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }
    
    public double getAssignmentGrade() {
        return assignmentGrade;
    }
    
    public void setAssignmentGrade(double assignmentGrade) {
        this.assignmentGrade = validateAssignmentGrade(assignmentGrade);
    }
    
    public double getQuizGrade() {
        return quizGrade;
    }
    
    public void setQuizGrade(double quizGrade) {
        this.quizGrade = validateQuizGrade(quizGrade);
    }
    
    public double getMidtermGrade() {
        return midtermGrade;
    }
    
    public void setMidtermGrade(double midtermGrade) {
        this.midtermGrade = validateMidtermGrade(midtermGrade);
    }
    
    public double getFinalGrade() {
        return finalGrade;
    }
    
    public void setFinalGrade(double finalGrade) {
        this.finalGrade = validateFinalGrade(finalGrade);
    }
    
    public double getAssignmentWeight() {
        return assignmentWeight;
    }
    
    public void setAssignmentWeight(double weight) {
        this.assignmentWeight = weight;
    }
    
    public double getQuizWeight() {
        return quizWeight;
    }
    
    public void setQuizWeight(double weight) {
        this.quizWeight = weight;
    }
    
    public double getMidtermWeight() {
        return midtermWeight;
    }
    
    public void setMidtermWeight(double weight) {
        this.midtermWeight = weight;
    }
    
    public double getFinalWeight() {
        return finalWeight;
    }
    
    public void setFinalWeight(double weight) {
        this.finalWeight = weight;
    }
    
    /**
     * Creates a deep copy of this Student object.
     * 
     * @return a clone of this Student
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new Student(this.name, this.assignmentGrade, this.quizGrade,
                             this.midtermGrade, this.finalGrade);
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s (%.2f, Grade: %c, GPA: %.2f)",
                           name, getWeightedAverage(), getLetterGrade(), getGPA());
    }
}
