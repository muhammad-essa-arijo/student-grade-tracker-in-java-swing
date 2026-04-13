package controller;

import model.GradeBook;
import model.Student;
import view.MainFrame;
import view.AddEditStudentDialog;
import util.CSVService;
import util.UndoRedoManager;
import util.UndoRedoManager.Command;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller class that manages interactions between Model and View.
 * Implements the MVC architectural pattern by handling user actions
 * and updating the model and view accordingly.
 * 
 * Responsibilities:
 * - Listen to user actions from the View
 * - Execute business logic using the Model
 * - Update the View with new data
 * - Handle file I/O and data persistence
 * - Manage undo/redo operations
 * - Handle autosave functionality
 * 
 * @author Student Grade Tracker Team
 * @version 1.0
 */
public class StudentGradeController {
    private MainFrame view;
    private GradeBook model;
    private UndoRedoManager undoRedoManager;
    private Timer autosaveTimer;
    private String lastSavedFile;
    private String autosaveFilePath;
    private boolean autosaveEnabled = false;
    
    /**
     * Constructs the controller with references to View and Model.
     * 
     * @param view the MainFrame (View)
     * @param model the GradeBook (Model)
     */
    public StudentGradeController(MainFrame view, GradeBook model) {
        this.view = view;
        this.model = model;
        this.undoRedoManager = new UndoRedoManager();
        initializeAutosave();
        attachListeners();
    }
    
    /**
     * Attaches event listeners to all View components.
     */
    private void attachListeners() {
        // Button listeners
        view.getAddButton().addActionListener(e -> handleAddStudent());
        view.getEditButton().addActionListener(e -> handleEditStudent());
        view.getDeleteButton().addActionListener(e -> handleDeleteStudent());
        view.getRefreshButton().addActionListener(e -> refreshView());
        view.getSearchButton().addActionListener(e -> handleSearch());
        view.getClearSearchButton().addActionListener(e -> handleClearSearch());
        view.getExportSummaryButton().addActionListener(e -> handleExportSummaryReport());
        
        // Menu listeners
        view.getSaveMenuItem().addActionListener(e -> handleSaveCSV());
        view.getLoadMenuItem().addActionListener(e -> handleLoadCSV());
        view.getExportMenuItem().addActionListener(e -> handleExportStatistics());
        view.getExportSummaryMenuItem().addActionListener(e -> handleExportSummaryReport());
        view.getUndoMenuItem().addActionListener(e -> handleUndo());
        view.getRedoMenuItem().addActionListener(e -> handleRedo());
        view.getExitMenuItem().addActionListener(e -> System.exit(0));
        
        // Enable/disable undo/redo based on available operations
        updateUndoRedoMenuItems();
    }
    
    /**
     * Handles the Add Student action.
     */
    private void handleAddStudent() {
        AddEditStudentDialog dialog = new AddEditStudentDialog(view);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Student newStudent = dialog.getStudent();
            Command command = new UndoRedoManager.AddStudentCommand(model, newStudent);
            undoRedoManager.executeCommand(command);
            refreshView();
            performAutosave();
        }
    }
    
    /**
     * Handles the Edit Student action.
     */
    private void handleEditStudent() {
        Student selectedStudent = view.getSelectedStudent();
        if (selectedStudent == null) {
            view.showError("Selection Error", "Please select a student to edit.");
            return;
        }
        
        AddEditStudentDialog dialog = new AddEditStudentDialog(
            view,
            selectedStudent,
            "Edit Student"
        );
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Student updatedStudent = dialog.getStudent();
            Command command = new UndoRedoManager.EditStudentCommand(
                selectedStudent,
                updatedStudent.getAssignmentGrade(),
                updatedStudent.getQuizGrade(),
                updatedStudent.getMidtermGrade(),
                updatedStudent.getFinalGrade()
            );
            undoRedoManager.executeCommand(command);
            refreshView();
            performAutosave();
        }
    }
    
    /**
     * Handles the Delete Student action.
     */
    private void handleDeleteStudent() {
        int selectedIndex = view.getSelectedStudentIndex();
        if (selectedIndex < 0) {
            view.showError("Selection Error", "Please select a student to delete.");
            return;
        }
        
        Student selectedStudent = model.getStudent(selectedIndex);
        int response = view.showConfirm(
            "Confirm Delete",
            "Are you sure you want to delete " + selectedStudent.getName() + "?"
        );
        
        if (response == JOptionPane.YES_OPTION) {
            Command command = new UndoRedoManager.RemoveStudentCommand(model, selectedIndex);
            undoRedoManager.executeCommand(command);
            refreshView();
            performAutosave();
        }
    }
    
    /**
     * Handles the Undo action.
     */
    private void handleUndo() {
        if (undoRedoManager.undo()) {
            refreshView();
            updateUndoRedoMenuItems();
            performAutosave();
        }
    }
    
    /**
     * Handles the Redo action.
     */
    private void handleRedo() {
        if (undoRedoManager.redo()) {
            refreshView();
            updateUndoRedoMenuItems();
            performAutosave();
        }
    }
    
    /**
     * Handles the Search action to find students by name.
     */
    private void handleSearch() {
        String query = view.getSearchText();
        if (query.isEmpty()) {
            view.showError("Search Error", "Please enter a search term.");
            return;
        }
        
        List<Student> results = model.searchStudents(query);
        if (results.isEmpty()) {
            view.showInfo("Search Results", "No students found matching: " + query);
            view.displaySearchResults(new ArrayList<>());
        } else {
            view.displaySearchResults(results);
            view.showInfo("Search Results", "Found " + results.size() + " student(s) matching: " + query);
        }
    }
    
    /**
     * Handles the Clear Search action.
     */
    private void handleClearSearch() {
        view.clearSearch();
        view.displaySearchResults(model.getAllStudents());
        refreshView();
    }
    
    /**
     * Handles Save to CSV action.
     */
    private void handleSaveCSV() {
        String filePath = view.showFileChooser(JFileChooser.SAVE_DIALOG);
        if (filePath == null) {
            return; // User cancelled
        }
        
        try {
            CSVService.saveToCSV(filePath, model);
            lastSavedFile = filePath;
            view.showInfo("Save Success", "Data saved to: " + new java.io.File(filePath).getName());
        } catch (IOException e) {
            view.showError("Save Error", "Failed to save file: " + e.getMessage());
        }
    }
    
    /**
     * Handles Load from CSV action.
     */
    private void handleLoadCSV() {
        String filePath = view.showFileChooser(JFileChooser.OPEN_DIALOG);
        if (filePath == null) {
            return; // User cancelled
        }
        
        try {
            CSVService.loadFromCSV(filePath, model);
            lastSavedFile = filePath;
            undoRedoManager.clear(); // Clear undo/redo history
            refreshView();
            view.showInfo("Load Success", "Data loaded from: " + new java.io.File(filePath).getName());
        } catch (IOException e) {
            view.showError("Load Error", "Failed to load file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            view.showError("Load Error", "Invalid CSV format: " + e.getMessage());
        }
    }
    
    /**
     * Handles Export Statistics action.
     */
    private void handleExportStatistics() {
        String filePath = view.showFileChooser(JFileChooser.SAVE_DIALOG);
        if (filePath == null) {
            return; // User cancelled
        }
        
        // Force .csv extension for statistics export
        if (!filePath.endsWith("_stats.csv")) {
            filePath = filePath.replace(".csv", "") + "_stats.csv";
        }
        
        try {
            CSVService.exportStatistics(filePath, model);
            view.showInfo("Export Success", "Statistics exported to: " + new java.io.File(filePath).getName());
        } catch (IOException e) {
            view.showError("Export Error", "Failed to export statistics: " + e.getMessage());
        }
    }

    /**
     * Handles Export Summary Report action.
     */
    private void handleExportSummaryReport() {
        String filePath = view.showFileChooser(JFileChooser.SAVE_DIALOG);
        if (filePath == null) {
            return; // User cancelled
        }
        
        // Force .csv extension for summary report export
        if (!filePath.endsWith("_summary.csv")) {
            filePath = filePath.replace(".csv", "") + "_summary.csv";
        }
        
        try {
            CSVService.exportSummaryReport(filePath, model);
            view.showInfo("Export Success", "Summary report exported to: " + new java.io.File(filePath).getName());
        } catch (IOException e) {
            view.showError("Export Error", "Failed to export summary report: " + e.getMessage());
        }
    }
    
    /**
     * Initializes the autosave timer.
     * Autosaves every 2 minutes if enabled and a file has been saved.
     */
    private void initializeAutosave() {
        autosaveTimer = new Timer(true);
        long autosaveInterval = 2 * 60 * 1000; // 2 minutes
        
        autosaveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (autosaveEnabled && autosaveFilePath != null) {
                    performAutosave();
                }
            }
        }, autosaveInterval, autosaveInterval);
    }
    
    /**
     * Performs autosave to the configured file path.
     */
    private void performAutosave() {
        if (autosaveEnabled && autosaveFilePath != null) {
            try {
                CSVService.saveToCSV(autosaveFilePath, model);
                System.out.println("Autosave completed: " + autosaveFilePath);
            } catch (IOException e) {
                System.err.println("Autosave failed: " + e.getMessage());
            }
        }
    }
    
    /**
     * Enables or disables autosave functionality.
     * 
     * @param enabled true to enable autosave
     * @param filePath the file path to autosave to
     */
    public void setAutosave(boolean enabled, String filePath) {
        this.autosaveEnabled = enabled;
        this.autosaveFilePath = filePath;
    }
    
    /**
     * Updates the enabled state of Undo/Redo menu items.
     */
    private void updateUndoRedoMenuItems() {
        view.getUndoMenuItem().setEnabled(undoRedoManager.canUndo());
        view.getRedoMenuItem().setEnabled(undoRedoManager.canRedo());
    }
    
    /**
     * Refreshes the View with the current Model state.
     */
    private void refreshView() {
        int studentCount = model.getStudentCount();
        System.out.println("Refreshing view... Total students: " + studentCount);
        
        view.refreshTable();
        view.updateStatistics();
        updateUndoRedoMenuItems();
        view.showStatusMessage("✓ Refreshed " + studentCount + " student(s)!");
        
        System.out.println("✓ View refreshed successfully!");
    }
    
    /**
     * Shuts down the controller and cleanup resources.
     */
    public void shutdown() {
        if (autosaveTimer != null) {
            autosaveTimer.cancel();
        }
    }
}
