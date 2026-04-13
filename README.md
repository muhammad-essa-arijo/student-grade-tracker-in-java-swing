# Student Grade Tracker - Complete Documentation

## Project Overview

**Student Grade Tracker** is a professional-grade Java desktop application for managing student grades with advanced features including weighted scoring, GPA calculation, statistics analysis, and data persistence.

**Version:** 1.0  
**Author:** Student Grade Tracker Team  
**Built with:** Java Swing + AWT

---

## Features

### Core Features
✅ **Student Management**
- Add new students with weighted grade components
- Edit existing student grades
- Delete student records
- Search students by name (case-insensitive)

✅ **Grade Calculation**
- Weighted average (Assignment 20%, Quiz 20%, Midterm 30%, Final 30%)
- Letter grades (A, B, C, D, F)
- GPA calculation (4.0 scale)

✅ **Statistics & Analysis**
- Class average, highest, and lowest grades
- Top and bottom performing students
- Grade distribution histogram with visual representation
- Sort by name or grade using table sorting

### Advanced Features
✅ **Data Persistence**
- CSV import/export functionality
- Export statistics to CSV
- Load previous records from files

✅ **Undo/Redo**
- Command Pattern implementation
- Undo/Redo for Add, Edit, Delete operations
- Keyboard shortcuts (Ctrl+Z, Ctrl+Y)

✅ **Autosave**
- Automatic saving every 2 minutes
- Configurable autosave settings
- Prevents data loss

✅ **User Interface**
- Modern Swing/AWT interface
- Tabbed interface (Students / Grade Distribution)
- Responsive table with sorting
- Intuitive dialog windows
- Professional layout with menus

✅ **Code Quality**
- MVC Architecture (clean separation of concerns)
- Comprehensive Javadoc comments
- Input validation
- Exception handling
- i18n Support (Internationalization ready)

---

## Project Structure

```
Student Grade Tracker/
│
├── src/
│   ├── model/
│   │   ├── Student.java                 # Student model with weighted grades
│   │   └── GradeBook.java              # Grade collection and statistics
│   │
│   ├── view/
│   │   ├── MainFrame.java              # Main application window
│   │   ├── AddEditStudentDialog.java   # Student add/edit dialog
│   │   └── HistogramPanel.java         # Grade distribution chart
│   │
│   ├── controller/
│   │   └── StudentGradeController.java # MVC Controller - handles interactions
│   │
│   ├── util/
│   │   ├── CSVService.java             # CSV I/O operations
│   │   ├── UndoRedoManager.java        # Undo/Redo and Command Pattern
│   │   └── ResourceBundleManager.java  # Internationalization support
│   │
│   └── StudentGradeTrackerApp.java     # Application entry point
│
├── resources/
│   └── messages_en.properties          # English language strings (i18n)
│
└── README.md                           # This file
```

---

## Architecture

### Model-View-Controller Pattern

**Model Layer**
- `Student.java`: Represents individual student with all grade components
- `GradeBook.java`: Manages collection of students and calculations

**View Layer**
- `MainFrame.java`: Primary GUI window with table and controls
- `AddEditStudentDialog.java`: Modal dialog for student data entry
- `HistogramPanel.java`: Custom AWT component for grade distribution chart

**Controller Layer**
- `StudentGradeController.java`: Orchestrates Model and View interactions
  - Handles button/menu clicks
  - Validates user input
  - Updates Model and refreshes View
  - Manages file operations and autosave

### Design Patterns Used

1. **MVC Pattern**: Clear separation between data, presentation, and logic
2. **Command Pattern**: Implements Undo/Redo functionality
3. **Singleton Pattern**: Resource bundle manager for i18n
4. **Observer Pattern**: Event listeners for user interactions

---

## Compilation & Execution

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Text editor or IDE (Eclipse, IntelliJ IDEA, NetBeans)

### Compilation

**Option 1: From Terminal**
```bash
# Navigate to project directory
cd "Student Grade Tracker"

# Create bin directory for compiled files
mkdir bin

# Compile all Java files
javac -d bin src/**/*.java src/*.java

# Copy resources to bin directory
xcopy resources bin\resources /E /I
```

**Option 2: In IDE**
1. Create a new Java project
2. Copy the `src` folder to the project
3. Copy the `resources` folder to the project/bin folder
4. Set build path to include resources
5. Run as Java Application

### Execution

**From Terminal**
```bash
cd bin
java StudentGradeTrackerApp
```

**From IDE**
- Right-click `StudentGradeTrackerApp.java` → Run As → Java Application

---

## Usage Guide

### Adding a Student
1. Click the **"+ Add"** button
2. Enter student name
3. Enter grades for:
   - Assignment (0-100)
   - Quiz (0-100)
   - Midterm (0-100)
   - Final (0-100)
4. Click **OK** to add

### Editing a Student
1. Select a student from the table
2. Click **"✎ Edit"** button
3. Modify grades as needed
4. Click **OK** to save

### Deleting a Student
1. Select a student from the table
2. Click **"✕ Delete"** button
3. Confirm deletion in popup dialog

### Searching for Students
1. Type a student name (or partial name) in the search box
2. Click **"Find"** button
3. View results in a dialog window
4. Click **"Clear"** to show all students again

### Saving/Loading Data
**Save to CSV:**
- File → Save to CSV
- Choose location and filename
- Data saved in format: Name,Assignment,Quiz,Midterm,Final

**Load from CSV:**
- File → Load from CSV
- Select CSV file
- All existing students replaced with file contents

**Export Statistics:**
- File → Export Statistics
- Generates detailed report with individual student stats

### Using Undo/Redo
- **Undo**: Edit → Undo or Ctrl+Z
- **Redo**: Edit → Redo or Ctrl+Y
- Works for Add, Edit, Delete operations

### Sorting Table
- Click on any column header to sort by that column
- Click again to reverse sort direction

### Viewing Statistics
- **Statistics Panel** at bottom shows:
  - Total student count
  - Class average
  - Highest and lowest grades
  - Top and bottom performing students

### Grade Distribution Histogram
- Click the **"Grade Distribution"** tab
- Visual representation of student grade distribution
- Shows frequency of grades in 10-point ranges (0-9, 10-19, etc.)

---

## Grading System

### Weighted Calculation
```
Weighted Average = (Assignment × 0.20) + (Quiz × 0.20) + 
                   (Midterm × 0.30) + (Final × 0.30)
```

### Letter Grades
| Range | Letter | GPA |
|-------|--------|-----|
| 90-100 | A | 4.0 |
| 80-89 | B | 3.0 |
| 70-79 | C | 2.0 |
| 60-69 | D | 1.0 |
| 0-59 | F | 0.0 |

---

## CSV File Format

### Import/Export Format
```
Name,Assignment,Quiz,Midterm,Final
John Doe,85.5,90.0,88.0,92.5
Jane Smith,92.0,88.0,95.0,90.0
Bob Johnson,78.0,82.0,75.0,80.0
```

### Statistics Export Format
```
Grade Book Statistics
Course: Computer Science 101

Summary Statistics
Total Students,3
Class Average,88.25
Highest Grade,93.25
Lowest Grade,78.75

Student Details
Name,Average,Letter Grade,GPA
John Doe,89.60,A,4.00
Jane Smith,91.50,A,4.00
Bob Johnson,79.10,C,2.00
```

---

## Input Validation

The application validates all user input:

| Field | Rules |
|-------|-------|
| Name | Cannot be empty |
| Assignment Grade | Must be numeric, 0-100 |
| Quiz Grade | Must be numeric, 0-100 |
| Midterm Grade | Must be numeric, 0-100 |
| Final Grade | Must be numeric, 0-100 |

Error messages clearly indicate what needs to be fixed.

---

## Customization & Extension

### Modifying Weights
To change grade component weights, edit `Student.java`:

```java
private double assignmentWeight = 0.20;  // Change this to 0.30 for 30%
private double quizWeight = 0.20;        // Change as needed
private double midtermWeight = 0.30;
private double finalWeight = 0.30;
```

Ensure weights sum to 1.0 (100%).

### Changing Default Course Title
In `StudentGradeTrackerApp.java`:
```java
GradeBook gradeBook = new GradeBook("Your Course Name");
```

### Adding More Grade Components
1. Add fields to `Student.java`
2. Add corresponding input fields in `AddEditStudentDialog.java`
3. Update table columns in `MainFrame.java`
4. Update CSV format in `CSVService.java`
5. Update grading calculation in `Student.getWeightedAverage()`

### Implementing New Languages
1. Create `messages_es.properties` for Spanish
2. Create `messages_fr.properties` for French
3. Add UI to select language
4. Use `ResourceBundleManager.setLocale()` to switch

### Customizing Look & Feel
In `StudentGradeTrackerApp.java`, change:
```java
UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
```

Options:
- `"com.sun.java.swing.plaf.motif.MotifLookAndFeel"`
- `"com.sun.java.swing.plaf.metal.MetalLookAndFeel"`
- `"com.sun.java.swing.plaf.windows.WindowsLookAndFeel"`

---

## Keyboard Shortcuts

| Shortcut | Action |
|----------|--------|
| Ctrl+Z | Undo |
| Ctrl+Y | Redo |
| Tab | Move between fields in dialog |
| Enter | Confirm dialog |
| Escape | Cancel dialog |

---

## Technical Details

### Swing Components Used
- `JFrame`: Main application window
- `JTable` + `DefaultTableModel`: Student list display
- `JDialog`: Modal dialogs
- `JPanel`, `JLabel`, `JTextField`: Form components
- `JMenuBar`, `JMenu`, `JMenuItem`: Menu system
- `JFileChooser`: File selection
- `JOptionPane`: Message dialogs
- `JTabbedPane`: Tab interface
- `JScrollPane`: Scrollable table
- `TableRowSorter`: Table sorting

### AWT Components Used
- `Graphics2D`: Custom histogram drawing
- `Color`, `Font`: Styling
- `Rectangle2D`: Shape drawing for histogram bars
- `BorderLayout`, `GridLayout`, `FlowLayout`: Layout managers

### Threading
- `SwingUtilities.invokeLater()`: UI thread safety
- `Timer` + `TimerTask`: Autosave scheduling

### Data Persistence
- FileWriter/FileReader for CSV
- BufferedReader/PrintWriter for efficient I/O
- Exception handling for file operations

---

## Troubleshooting

### "Class not found" Error
**Solution**: Ensure resources folder is in classpath. Copy resources to bin directory.

### Table Not Showing Data
**Solution**: Check that students were added successfully. Use "Refresh" button.

### CSV Load Fails
**Solution**: Verify CSV format matches specification. Check for special characters in names.

### Undo/Redo Disabled
**Solution**: Undo/Redo buttons enable automatically when actions are available.

### Autosave Not Working
**Cause**: Autosave requires a file to have been saved first.
**Solution**: Save to CSV file first, then autosave will work.

---

## Performance Considerations

- Application handles 100+ students efficiently
- Table sorting uses built-in Swing RowSorter (fast)
- Histogram rendering optimized with Graphics2D
- Autosave uses background timer thread (non-blocking)
- File I/O uses buffered streams (efficient)

---

## Security Notes

- Application has no network connectivity (local use only)
- CSV files are plain text (no encryption)
- For sensitive data, encrypt files separately
- Input validation prevents injection attacks

---

## Future Enhancement Ideas

1. **Database Integration**: Replace CSV with database (MySQL, SQLite)
2. **Chart Library**: Use JFreeChart for advanced visualizations
3. **Multi-Course Support**: Manage grades for multiple courses
4. **Student Photos**: Display student pictures in UI
5. **Email Integration**: Email grade reports to students
6. **Web Dashboard**: Web-based grade viewer
7. **Mobile App**: Mobile version for iOS/Android
8. **Gradebook Analytics**: Advanced statistical analysis
9. **Attendance Tracking**: Link attendance to grades
10. **File Encryption**: Secure sensitive student data

---

## Code Quality Standards

This project follows Java best practices:

✅ **Style**: Consistent naming, proper indentation  
✅ **Documentation**: Comprehensive Javadoc comments  
✅ **Error Handling**: Try-catch blocks for all I/O operations  
✅ **Validation**: Input validation before processing  
✅ **Architecture**: Clean MVC pattern separation  
✅ **Encapsulation**: Private fields, controlled access  
✅ **Reusability**: Utility classes for common operations  
✅ **Testing**: Validation logic tested during usage  

---

## License & Credits

**License**: Open Source (Free to use and modify)

**Credits**:
- Student Grade Tracker Team
- Java Swing/AWT Documentation
- Community Java developers

---

## Support & Contact

For bugs, questions, or suggestions:
1. Check this documentation first
2. Review the inline Javadoc comments in source code
3. Make sure all files are in correct directories
4. Verify Java version compatibility

---

## Changelog

### Version 1.0 (Initial Release)
- Complete student grade management system
- Weighted grade calculation
- GPA and letter grade system
- Grade distribution histogram
- CSV import/export
- Undo/Redo functionality
- Autosave capability
- Student search
- Table sorting
- Comprehensive UI with menus
- Internationalization ready
- Full documentation

---

**Happy Grade Tracking!** 🎓

For more information on Java Swing development, visit:
- https://docs.oracle.com/javase/tutorial/uiswing/
- https://docs.oracle.com/javase/8/docs/api/javax/swing/package-summary.html
