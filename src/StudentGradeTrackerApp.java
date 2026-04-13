import model.GradeBook;
import view.MainFrame;
import controller.StudentGradeController;
import javax.swing.*;

public class StudentGradeTrackerApp {
    public static void main(String[] args) {
        // Schedule UI initialization on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system default
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Failed to set look and feel: " + e.getMessage());
            }
            
            // Create the Model
            GradeBook gradeBook = new GradeBook("Computer Science 101");
            
            // Create the View
            MainFrame mainFrame = new MainFrame(gradeBook);
            
            // Create and attach the Controller
            StudentGradeController controller = new StudentGradeController(mainFrame, gradeBook);
            
            // Add shutdown hook to cleanup resources
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                controller.shutdown();
            }));
            
            // Display the main frame
            mainFrame.setVisible(true);
            
            System.out.println("Student Grade Tracker started successfully!");
        });
    }
}
