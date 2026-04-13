package util;

import java.util.Stack;
import model.GradeBook;
import model.Student;

/**
 * Manages undo and redo operations using the Command Pattern.
 * Maintains stacks of commands to reverse and replay user actions.
 * 
 * @author Student Grade Tracker Team
 * @version 1.0
 */
public class UndoRedoManager {
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;
    
    /**
     * Constructs a new UndoRedoManager with empty stacks.
     */
    public UndoRedoManager() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }
    
    /**
     * Executes a command and adds it to the undo stack.
     * Clears the redo stack when a new command is performed.
     * 
     * @param command the command to execute
     */
    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Clear redo stack on new action
    }
    
    /**
     * Undoes the last command if available.
     * Moves the command to the redo stack.
     * 
     * @return true if undo was performed, false if undo stack is empty
     */
    public boolean undo() {
        if (undoStack.isEmpty()) {
            return false;
        }
        Command command = undoStack.pop();
        command.undo();
        redoStack.push(command);
        return true;
    }
    
    /**
     * Redoes the last undone command if available.
     * Moves the command back to the undo stack.
     * 
     * @return true if redo was performed, false if redo stack is empty
     */
    public boolean redo() {
        if (redoStack.isEmpty()) {
            return false;
        }
        Command command = redoStack.pop();
        command.execute();
        undoStack.push(command);
        return true;
    }
    
    /**
     * Checks if undo operation is available.
     * 
     * @return true if there are commands to undo
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }
    
    /**
     * Checks if redo operation is available.
     * 
     * @return true if there are commands to redo
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    
    /**
     * Clears all undo and redo history.
     */
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
    
    /**
     * Abstract Command interface for implementing undoable operations.
     */
    public static abstract class Command {
        public abstract void execute();
        public abstract void undo();
    }
    
    /**
     * Command to add a student to the grade book.
     */
    public static class AddStudentCommand extends Command {
        private GradeBook gradeBook;
        private Student student;
        
        public AddStudentCommand(GradeBook gradeBook, Student student) {
            this.gradeBook = gradeBook;
            this.student = student;
        }
        
        @Override
        public void execute() {
            gradeBook.addStudent(student);
        }
        
        @Override
        public void undo() {
            gradeBook.removeStudent(gradeBook.getStudentCount() - 1);
        }
    }
    
    /**
     * Command to remove a student from the grade book.
     */
    public static class RemoveStudentCommand extends Command {
        private GradeBook gradeBook;
        private Student student;
        private int index;
        
        public RemoveStudentCommand(GradeBook gradeBook, int index) {
            this.gradeBook = gradeBook;
            this.index = index;
            this.student = (Student) gradeBook.getStudent(index).clone();
        }
        
        @Override
        public void execute() {
            gradeBook.removeStudent(index);
        }
        
        @Override
        public void undo() {
            gradeBook.addStudent(student);
        }
    }
    
    /**
     * Command to edit a student's grades.
     */
    public static class EditStudentCommand extends Command {
        private Student student;
        private double oldAssignment, oldQuiz, oldMidterm, oldFinal;
        private double newAssignment, newQuiz, newMidterm, newFinal;
        
        public EditStudentCommand(Student student,
                                double newAssignment, double newQuiz,
                                double newMidterm, double newFinal) {
            this.student = student;
            this.oldAssignment = student.getAssignmentGrade();
            this.oldQuiz = student.getQuizGrade();
            this.oldMidterm = student.getMidtermGrade();
            this.oldFinal = student.getFinalGrade();
            this.newAssignment = newAssignment;
            this.newQuiz = newQuiz;
            this.newMidterm = newMidterm;
            this.newFinal = newFinal;
        }
        
        @Override
        public void execute() {
            student.setAssignmentGrade(newAssignment);
            student.setQuizGrade(newQuiz);
            student.setMidtermGrade(newMidterm);
            student.setFinalGrade(newFinal);
        }
        
        @Override
        public void undo() {
            student.setAssignmentGrade(oldAssignment);
            student.setQuizGrade(oldQuiz);
            student.setMidtermGrade(oldMidterm);
            student.setFinalGrade(oldFinal);
        }
    }
}
