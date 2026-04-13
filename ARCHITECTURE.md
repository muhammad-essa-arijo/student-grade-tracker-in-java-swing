# ARCHITECTURE DOCUMENTATION

## System Architecture Overview

The Student Grade Tracker follows the **Model-View-Controller (MVC)** architectural pattern, which provides clean separation of concerns and makes the codebase maintainable and extensible.

---

## MVC Pattern Explanation

### What is MVC?

MVC divides the application into three interconnected components:

```
┌─────────────────────────────────────────────────────────┐
│                   USER INTERACTIONS                       │
│            (Clicks, Typing, Menu Selections)             │
└──────────────┬────────────────────────────╯──────────────┘
               │
               │ Events
               ▼
┌─────────────────────────────────────────────────────────┐
│               CONTROLLER                                  │
│         StudentGradeController.java                       │
│  - Handles user events                                   │
│  - Validates input                                       │
│  - Updates Model                                         │
│  - Refreshes View                                        │
└──────────────┬─────────────────────────────┬─────────────┘
               │                             │
        Updates │                             │ Updates
               ▼                             ▼
┌──────────────────────────┐  ┌──────────────────────────────┐
│      MODEL               │  │         VIEW                 │
│    Data & Logic          │  │    Presentation Layer        │
│                          │  │                              │
│  - GradeBook.java        │  │  - MainFrame.java            │
│  - Student.java          │  │  - AddEditStudentDialog.java │
│                          │  │  - HistogramPanel.java       │
│                          │  │                              │
│  Responsibilities:       │  │  Responsibilities:           │
│  ✓ Store student data    │  │  ✓ Display UI components     │
│  ✓ Calculate grades      │  │  ✓ Show data to user         │
│  ✓ Statistics            │  │  ✓ Capture user input        │
│  ✓ Validation            │  │  ✓ Refresh from Model        │
└──────────────────────────┘  └──────────────────────────────┘
```

---

## Component Breakdown

### 1. MODEL LAYER (`model/`)

**Purpose**: Encapsulates data and business logic

#### `Student.java` - Individual Student Entity
```
Key Responsibilities:
├─ Store student information
│  ├─ Name
│  ├─ Assignment grade (0-100)
│  ├─ Quiz grade (0-100)
│  ├─ Midterm grade (0-100)
│  └─ Final grade (0-100)
├─ Calculate derived values
│  ├─ Weighted average
│  ├─ Letter grade (A-F)
│  └─ GPA (4.0 scale)
└─ Validate grade data
   └─ Ensure 0-100 range

Key Methods:
└─ getWeightedAverage()      → Calculated grade
   getLetterGrade()           → A, B, C, D, or F
   getGPA()                   → 0.0 to 4.0
   Getters/Setters            → Access grades
   clone()                    → Deep copy for undo
```

**Weight Distribution:**
```
Final Grade = (Assignment × 0.20) + (Quiz × 0.20) + 
              (Midterm × 0.30) + (Final × 0.30)
```

#### `GradeBook.java` - Collection Manager
```
Key Responsibilities:
├─ Manage student collection (ArrayList)
├─ CRUD operations
│  ├─ addStudent()           → Add new student
│  ├─ removeStudent()        → Remove by index
│  ├─ getStudent()           → Retrieve student
│  └─ getAllStudents()       → Get all students
├─ Search functionality
│  ├─ findStudentByName()    → Find specific student
│  └─ searchStudents()       → Find by name fragment
├─ Calculate class statistics
│  ├─ getClassAverage()      → Mean of all grades
│  ├─ getHighestGrade()      → Maximum grade
│  └─ getLowestGrade()       → Minimum grade
└─ Advanced operations
   ├─ getTopStudent()        → Highest performer
   ├─ getBottomStudent()     → Lowest performer
   └─ getGradeDistribution() → Histogram data

Data Structure:
└─ ArrayList<Student> students
   └─ Dynamic array of student records
```

**No UI Code**: Student and GradeBook contain zero GUI dependencies!

---

### 2. VIEW LAYER (`view/`)

**Purpose**: Presents data to user and captures input

#### `MainFrame.java` - Primary Application Window
```
Extends: JFrame (Swing window)

GUI Components:
├─ Menu Bar
│  ├─ File Menu
│  │  ├─ Load from CSV
│  │  ├─ Save to CSV
│  │  ├─ Export Statistics
│  │  └─ Exit
│  ├─ Edit Menu
│  │  ├─ Undo (Ctrl+Z)
│  │  ├─ Redo (Ctrl+Y)
│  │  └─ Autosave Settings
│  └─ Help Menu
│     └─ About
├─ Top Panel
│  ├─ Search box
│  ├─ Find button
│  ├─ Clear button
│  └─ Action buttons
│     ├─ + Add
│     ├─ ✎ Edit
│     ├─ ✕ Delete
│     └─ ⟲ Refresh
├─ Center Panel (Tabbed)
│  ├─ Students Tab
│  │  └─ JTable with 8 columns
│  │     ├─ Name
│  │     ├─ Assignment
│  │     ├─ Quiz
│  │     ├─ Midterm
│  │     ├─ Final
│  │     ├─ Average
│  │     ├─ Grade
│  │     └─ GPA
│  └─ Grade Distribution Tab
│     └─ HistogramPanel
└─ Bottom Panel
   └─ Statistics display
      ├─ Total students
      ├─ Class average
      ├─ Highest/Lowest grades
      └─ Top/Bottom students

Key Methods:
├─ refreshTable()            → Populate JTable from Model
├─ updateStatistics()        → Update statistics label
├─ getSelectedStudent()      → Get table selection
├─ showFileChooser()         → File open/save dialog
├─ showError/Info/Confirm()  → Message dialogs
└─ getters for components    → Access for Controller
```

**Layout Managers Used:**
- BorderLayout (main frame)
- FlowLayout (buttons)
- GridLayout (form fields)

#### `AddEditStudentDialog.java` - Input Dialog
```
Extends: JDialog (Modal dialog)

Purpose:
├─ Add new student
├─ Edit existing student
└─ Validate input before submission

Components:
├─ JTextField nameField
├─ JTextField assignmentField
├─ JTextField quizField
├─ JTextField midtermField
├─ JTextField finalField
├─ JButton okButton
└─ JButton cancelButton

Responsibilities:
├─ Capture user input
├─ Validate all entries
│  └─ Name not empty
│  └─ Grades numeric
│  └─ Grades 0-100
├─ Return Student object
└─ Provide confirmation status (isConfirmed)
```

#### `HistogramPanel.java` - Grade Distribution Chart
```
Extends: JPanel (Swing component)

Purpose:
└─ Visualize grade distribution

Features:
├─ Custom AWT Graphics2D rendering
├─ 10 grade bins (0-9, 10-19, ..., 90-100)
├─ Bar chart display
├─ Frequency labels
├─ Axis labels
└─ Empty state message

Rendering:
└─ paintComponent()          → Draw histogram
   ├─ Draw axes
   ├─ Draw bars
   ├─ Label bins
   └─ Show frequencies
```

---

### 3. CONTROLLER LAYER (`controller/`)

**Purpose**: Orchestrate interactions between Model and View

#### `StudentGradeController.java` - Main Controller
```
Coordinates:
├─ View (MainFrame)
├─ Model (GradeBook)
├─ Utilities (CSV, UndoRedo, i18n)
└─ File I/O operations

Event Handlers:
├─ Button Events
│  ├─ handleAddStudent()      → Add new student dialog
│  ├─ handleEditStudent()     → Edit selected student
│  ├─ handleDeleteStudent()   → Remove student
│  ├─ handleSearch()          → Find students
│  └─ handleClearSearch()     → Reset search
├─ Menu Events
│  ├─ handleSaveCSV()         → File save dialog
│  ├─ handleLoadCSV()         → File load dialog
│  ├─ handleExportStatistics()→ Export stats CSV
│  ├─ handleUndo()            → Undo operation
│  └─ handleRedo()            → Redo operation
└─ Autosave
   └─ performAutosave()       → Save to file

Workflow for Add Student:
1. User clicks "+ Add" button
2. Controller calls handleAddStudent()
3. Show AddEditStudentDialog
4. Wait for user confirmation
5. Create AddStudentCommand
6. Execute through UndoRedoManager
7. Model updated with new student
8. refreshView() updates table and stats
9. performAutosave() saves to file
```

---

### 4. UTILITY LAYER (`util/`)

**Purpose**: Reusable utilities for common operations

#### `CSVService.java` - File I/O
```
Static Methods:
├─ loadFromCSV(filePath, gradeBook)
│  └─ Read CSV → Parse → Populate GradeBook
├─ saveToCSV(filePath, gradeBook)
│  └─ Read GradeBook → Format → Write CSV
├─ exportStatistics(filePath, gradeBook)
│  └─ Calculate stats → Generate report CSV
└─ Helper methods
   ├─ parseStudentFromCSV()   → Parse line
   ├─ formatStudentToCSV()    → Format line
   ├─ parseGrade()            → Parse number
   └─ isValidCSVFile()        → Validate extension

CSV Format:
┌─────────────────────────────────────────┐
│ Name,Assignment,Quiz,Midterm,Final      │
│ John Doe,85.5,90.0,88.0,92.5            │
│ Jane Smith,92.0,88.0,95.0,90.0          │
└─────────────────────────────────────────┘
```

#### `UndoRedoManager.java` - Command Pattern
```
Implements Command Pattern:
├─ Stack<Command> undoStack
├─ Stack<Command> redoStack
└─ Command interface (abstract)
   ├─ execute()  → Perform action
   └─ undo()     → Reverse action

Command Implementations:
├─ AddStudentCommand
│  ├─ execute() → gradeBook.addStudent(student)
│  └─ undo()    → gradeBook.removeStudent()
├─ RemoveStudentCommand
│  ├─ execute() → gradeBook.removeStudent()
│  └─ undo()    → gradeBook.addStudent(student)
└─ EditStudentCommand
   ├─ execute() → Update all grade fields
   └─ undo()    → Restore previous grades

Undo/Redo Flow:
Action Performed
    ↓
Command created
    ↓
executeCommand(command)
    ├─ command.execute() → Update model
    └─ Push to undoStack
    ↓
On Undo:
    ├─ Pop from undoStack
    ├─ command.undo() → Revert model
    └─ Push to redoStack
    ↓
On Redo:
    ├─ Pop from redoStack
    ├─ command.execute() → Re-apply
    └─ Push to undoStack
```

#### `ResourceBundleManager.java` - Internationalization
```
Purpose:
└─ Load translated strings for any language

Methods:
├─ getString(key)           → Get string resource
├─ getString(key, params)   → Format with parameters
├─ setLocale(locale)        → Switch language
├─ getCurrentLocale()       → Get current language
└─ containsKey(key)         → Check if key exists

Usage Example:
├─ ResourceBundleManager.getString("menu.file")
│  └─ Returns "File" (English) or translation
└─ Supports multiple .properties files
   ├─ messages_en.properties (English)
   ├─ messages_es.properties (Spanish)
   └─ messages_fr.properties (French)
```

---

## Data Flow Diagrams

### Adding a Student
```
User Action
    ↓
[View] User clicks "+ Add" 
    ↓
[Controller] handleAddStudent()
    ↓
[View] Show AddEditStudentDialog
    ↓
User enters data & clicks OK
    ↓
[Dialog] validateInput()
    ├─ Check name not empty
    ├─ Check grades numeric
    └─ Check grades 0-100
    ↓
[Dialog] getStudent() → returns Student object
    ↓
[Controller] Create AddStudentCommand
    ↓
[UndoRedoManager] executeCommand(command)
    ├─ command.execute()
    │  └─ [Model] gradeBook.addStudent(student)
    └─ undoStack.push(command)
    ↓
[Controller] refreshView()
    ├─ [View] refreshTable() → Reload JTable
    └─ [View] updateStatistics() → Update stats
    ↓
[Controller] performAutosave() → Save to CSV
    ↓
✓ Data displayed in table
```

### Loading from CSV
```
User Action
    ↓
[View] File → Load from CSV
    ↓
[View] showFileChooser(OPEN_DIALOG)
    ↓
User selects file
    ↓
[Controller] handleLoadCSV()
    ├─ Get file path
    └─ Call CSVService.loadFromCSV()
    ↓
[CSVService] 
├─ Open file
├─ Read line by line
├─ parseStudentFromCSV() for each line
│  └─ Parse: Name,Assignment,Quiz,Midterm,Final
└─ Return populated data
    ↓
[Model] GradeBook.clearAllStudents()
    ↓
[Model] For each student → addStudent()
    ↓
[UndoRedoManager] Clear history
    ↓
[Controller] refreshView()
    ↓
[View] Table displays all students
    ↓
✓ Data loaded and displayed
```

### Undo Operation
```
User Action
    ↓
[View] Edit → Undo (or Ctrl+Z)
    ↓
[Controller] handleUndo()
    ↓
[UndoRedoManager]
├─ Check canUndo()
├─ Pop from undoStack → command
├─ command.undo() → revert model
└─ Push to redoStack
    ↓
[Controller] refreshView()
    ├─ [View] Update display
    └─ [View] Update statistics
    ↓
[Controller] updateUndoRedoMenuItems()
    ├─ Enable/disable Undo
    └─ Enable/disable Redo
    ↓
✓ Previous state restored
```

---

## Separation of Concerns

### What Model DOESN'T Know
```
Student & GradeBook are INDEPENDENT of:
❌ GUI (no JFrame, JTable, JButton, etc.)
❌ File I/O (no FileWriter, BufferedReader, etc.)
❌ Controller logic (no event listeners)
❌ User interactions (no events)
```

### What View DOESN'T Know
```
MainFrame & Dialogs don't directly:
❌ Perform calculations
❌ Handle file I/O
❌ Manage undo/redo
❌ Store data
```

### What Controller Knows
```
StudentGradeController coordinates:
✅ View (MainFrame, Dialogs)
✅ Model (GradeBook, Student)
✅ Utilities (CSV, UndoRedo, i18n)
✅ User interactions
```

---

## Class Relationships

```
                    StudentGradeTrackerApp
                            │
                            ├─→ GradeBook (Model)
                            │       ├─→ ArrayList<Student>
                            │       └─→ Statistics calculations
                            │
                            ├─→ MainFrame (View)
                            │       ├─→ JTable
                            │       ├─→ JMenuBar
                            │       ├─→ JButton, JTextField
                            │       ├─→ AddEditStudentDialog
                            │       └─→ HistogramPanel
                            │
                            └─→ StudentGradeController
                                    ├─→ References both Model & View
                                    ├─→ UndoRedoManager
                                    ├─→ CSVService
                                    └─→ ResourceBundleManager
```

---

## Design Patterns Used

### 1. Model-View-Controller (MVC)
**Location**: Entire application  
**Purpose**: Separate concerns into three layers  
**Benefit**: Easier to maintain, test, and extend

### 2. Command Pattern
**Location**: `UndoRedoManager.java`  
**Purpose**: Encapsulate actions as objects  
**Commands**:
- AddStudentCommand
- RemoveStudentCommand
- EditStudentCommand
**Benefit**: Enables Undo/Redo functionality

### 3. Singleton Pattern
**Location**: `ResourceBundleManager.java`  
**Purpose**: Single resource bundle instance  
**Benefit**: Consistent translation throughout app

### 4. Observer Pattern
**Location**: Swing components  
**Purpose**: Event listeners trigger actions  
**Example**: Button clicks → Action listeners → Controller methods

---

## Threading Model

```
Main Thread (Event Dispatch Thread)
    │
    ├─ All UI operations → SwingUtilities.invokeLater()
    │
    ├─ User interactions → Event listeners
    │
    ├─ Controller business logic
    │
    ├─ Model operations (synchronous)
    │
    └─ Autosave Timer Thread
         └─ Runs every 2 minutes (non-blocking)
```

**Thread Safety**: 
- UI operations always on Event Dispatch Thread
- Model operations not concurrent (single-threaded)
- Autosave uses separate timer thread

---

## Extension Points

### Adding New Features

**1. New Grade Component**
```
Modify:
├─ Student.java
│  └─ Add new field (e.g., assignmentWeight)
├─ GradeBook.java
│  └─ Update calculation logic
├─ AddEditStudentDialog.java
│  └─ Add UI field
├─ MainFrame.java
│  └─ Add table column
└─ CSVService.java
   └─ Update format
```

**2. New Command for Undo/Redo**
```
Create:
└─ UndoRedoManager.YourCommand extends Command
   ├─ execute()  → Perform action
   └─ undo()     → Reverse action

Use:
└─ UndoRedoManager.executeCommand(new YourCommand(...))
```

**3. New Language Support**
```
Create:
└─ resources/messages_xx.properties

Use:
└─ ResourceBundleManager.setLocale(new Locale("xx"))
```

**4. Database Integration**
```
Replace:
└─ CSVService.java
   └─ loadFromCSV/saveToCSV with database calls

Benefits:
├─ Persistent storage
├─ Concurrent access
└─ Advanced queries
```

---

## Performance Considerations

| Operation | Time Complexity | Notes |
|-----------|-----------------|-------|
| Add Student | O(1) | ArrayList append |
| Remove Student | O(n) | ArrayList removal |
| Find Student by Name | O(n) | Linear search |
| Calculate Average | O(n) | Sum all grades |
| Sort Table | O(n log n) | TableRowSorter |
| Generate Histogram | O(n) | Single pass |
| Save to CSV | O(n) | File I/O |

**Optimization Tips:**
1. For large datasets (1000+ students), consider database
2. Caching histogram data (only recalculate on changes)
3. Lazy loading of statistics
4. Background file operations (threading)

---

## Summary

The Student Grade Tracker demonstrates professional Java application architecture with:

✅ **Clean MVC Pattern**: Proper separation of concerns  
✅ **Extensibility**: Easy to add features  
✅ **Maintainability**: Well-organized code  
✅ **Reusability**: Utility classes for common tasks  
✅ **Robustness**: Exception handling and validation  
✅ **User-Friendly**: Intuitive GUI with helpful features  

This architecture makes it easy for developers to:
- Understand the codebase
- Add new features
- Fix bugs
- Test components independently
- Scale the application

---

**Next**: See [README.md](README.md) for usage guide or [QUICKSTART.md](QUICKSTART.md) for quick setup.
