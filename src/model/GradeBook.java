package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a grade book that manages a collection of students and their grades.
 * Provides statistical calculations such as average, highest, and lowest scores,
 * as well as operations to add, remove, and retrieve students.
 * 
 * @author Student Grade Tracker Team
 * @version 1.0
 */
public class GradeBook implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private ArrayList<Student> students;
    private String courseTitle;
    
    /**
     * Constructs an empty GradeBook with a specified course title.
     * 
     * @param courseTitle the name/title of the course
     */
    public GradeBook(String courseTitle) {
        this.courseTitle = courseTitle;
        this.students = new ArrayList<>();
    }
    
    /**
     * Adds a student to the grade book.
     * 
     * @param student the student to add
     * @throws IllegalArgumentException if student is null
     */
    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        students.add(student);
    }
    
    /**
     * Removes a student from the grade book by index.
     * 
     * @param index the index of the student to remove
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public void removeStudent(int index) {
        if (index < 0 || index >= students.size()) {
            throw new IndexOutOfBoundsException("Invalid student index: " + index);
        }
        students.remove(index);
    }
    
    /**
     * Retrieves a student by index.
     * 
     * @param index the index of the student
     * @return the student at the specified index
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public Student getStudent(int index) {
        if (index < 0 || index >= students.size()) {
            throw new IndexOutOfBoundsException("Invalid student index: " + index);
        }
        return students.get(index);
    }
    
    /**
     * Finds a student by name (case-insensitive).
     * 
     * @param name the name to search for
     * @return the first student with matching name, or null if not found
     */
    public Student findStudentByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        String searchName = name.toLowerCase().trim();
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(searchName)) {
                return student;
            }
        }
        return null;
    }
    
    /**
     * Finds all students whose name contains the search string (case-insensitive).
     * 
     * @param nameFragment a substring to search for
     * @return a list of matching students
     */
    public List<Student> searchStudents(String nameFragment) {
        List<Student> results = new ArrayList<>();
        if (nameFragment == null || nameFragment.trim().isEmpty()) {
            return results;
        }
        String searchTerm = nameFragment.toLowerCase();
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(searchTerm)) {
                results.add(student);
            }
        }
        return results;
    }
    
    /**
     * Returns the number of students in the grade book.
     * 
     * @return the number of students
     */
    public int getStudentCount() {
        return students.size();
    }
    
    /**
     * Gets all students as an unmodifiable list.
     * 
     * @return a list of all students
     */
    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(students);
    }
    
    /**
     * Calculates the class average of weighted grades.
     * 
     * @return the average weighted grade, or 0 if no students
     */
    public double getClassAverage() {
        if (students.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Student student : students) {
            sum += student.getWeightedAverage();
        }
        return sum / students.size();
    }
    
    /**
     * Finds the highest weighted grade in the class.
     * 
     * @return the highest grade, or 0 if no students
     */
    public double getHighestGrade() {
        if (students.isEmpty()) {
            return 0.0;
        }
        double max = 0.0;
        for (Student student : students) {
            double grade = student.getWeightedAverage();
            if (grade > max) {
                max = grade;
            }
        }
        return max;
    }
    
    /**
     * Finds the lowest weighted grade in the class.
     * 
     * @return the lowest grade, or 0 if no students
     */
    public double getLowestGrade() {
        if (students.isEmpty()) {
            return 0.0;
        }
        double min = 100.0;
        for (Student student : students) {
            double grade = student.getWeightedAverage();
            if (grade < min) {
                min = grade;
            }
        }
        return min;
    }
    
    /**
     * Finds the student with the highest grade.
     * 
     * @return the top student, or null if no students
     */
    public Student getTopStudent() {
        if (students.isEmpty()) {
            return null;
        }
        Student topStudent = students.get(0);
        for (Student student : students) {
            if (student.getWeightedAverage() > topStudent.getWeightedAverage()) {
                topStudent = student;
            }
        }
        return topStudent;
    }
    
    /**
     * Finds the student with the lowest grade.
     * 
     * @return the lowest performing student, or null if no students
     */
    public Student getBottomStudent() {
        if (students.isEmpty()) {
            return null;
        }
        Student bottomStudent = students.get(0);
        for (Student student : students) {
            if (student.getWeightedAverage() < bottomStudent.getWeightedAverage()) {
                bottomStudent = student;
            }
        }
        return bottomStudent;
    }
    
    /**
     * Calculates the distribution of grades into bins for histogram generation.
     * Returns an array of 10 elements, each representing a 10-point range:
     * [0]: 0-9, [1]: 10-19, ..., [9]: 90-100
     * 
     * @return an array of grade distribution counts
     */
    public int[] getGradeDistribution() {
        int[] distribution = new int[10];
        for (Student student : students) {
            int gradeRange = (int) (student.getWeightedAverage() / 10);
            if (gradeRange == 10) gradeRange = 9; // Handle edge case of exactly 100
            distribution[gradeRange]++;
        }
        return distribution;
    }
    
    /**
     * Clears all students from the grade book.
     */
    public void clearAllStudents() {
        students.clear();
    }
    
    public String getCourseTitle() {
        return courseTitle;
    }
    
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
    
    @Override
    public String toString() {
        return String.format("GradeBook[%s] - %d students, Average: %.2f",
                           courseTitle, students.size(), getClassAverage());
    }
}
