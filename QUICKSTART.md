# QUICKSTART GUIDE - Student Grade Tracker

## 5-Minute Setup

### Step 1: Build the Application
**Windows:**
```
Double-click: build.bat
```

**Linux/Mac:**
```bash
chmod +x build.sh
./build.sh
```

### Step 2: Run the Application
**Windows:**
```
Double-click: run.bat
```

**Linux/Mac:**
```bash
./run.sh
```

The application window should open in a few seconds.

---

## Basic Operations

### Add Your First 3 Students

1. **Click "+ Add"** button
2. **Example 1 - Add Student "Alice Johnson"**
   - Name: `Alice Johnson`
   - Assignment: `95`
   - Quiz: `88`
   - Midterm: `92`
   - Final: `94`
   - Click **OK**

3. **Example 2 - Add Student "Bob Smith"**
   - Name: `Bob Smith`
   - Assignment: `78`
   - Quiz: `82`
   - Midterm: `75`
   - Final: `80`
   - Click **OK**

4. **Example 3 - Add Student "Carol Williams"**
   - Name: `Carol Williams`
   - Assignment: `88`
   - Quiz: `90`
   - Midterm: `85`
   - Final: `87`
   - Click **OK**

✅ You should now see 3 students in the table!

---

## Try Key Features

### Feature 1: View Statistics
- **Bottom of window shows:**
  - Total Students: 3
  - Class Average: ~87.50
  - Highest Grade: ~91.20
  - Lowest Grade: ~78.75
  - Top Student: Alice Johnson
  - Bottom Student: Bob Smith

### Feature 2: Grade Distribution
- **Click "Grade Distribution" tab** (at top of table)
- You'll see a **histogram chart** showing grade distribution

### Feature 3: Edit a Grade
- **Select "Bob Smith"** in the table
- **Click "✎ Edit"** button
- **Change Quiz from 82 to 92**
- **Click OK**
- Notice the statistics update automatically!

### Feature 4: Search
- **In the search box**, type: `Carol`
- **Click "Find"**
- A popup shows the matching student
- **Click "Clear"** to show all students again

### Feature 5: Undo/Redo
- **Edit → Undo** (or Ctrl+Z)
- Bob Smith's quiz reverts to 82
- **Edit → Redo** (or Ctrl+Y)
- Bob Smith's quiz returns to 92

### Feature 6: Save & Load
**Save:**
- **File → Save to CSV**
- Choose a location
- Click Save

**Load:**
- **File → Load from CSV**
- Select the CSV file you just saved
- Click Open
- Students are reloaded!

### Feature 7: Export Statistics
- **File → Export Statistics**
- Creates a report file with detailed statistics
- Open the file in a text editor to view

### Feature 8: Sort Table
- **Click "Average" column header** to sort by grade
- **Click again** to reverse sort (highest to lowest)
- **Click "Name"** to sort alphabetically

---

## Sample CSV Format

If you want to manually create a CSV file with student data:

**students.csv:**
```
Name,Assignment,Quiz,Midterm,Final
Alice Johnson,95,88,92,94
Bob Smith,78,82,75,80
Carol Williams,88,90,85,87
David Brown,92,91,94,93
Eve Davis,65,70,68,72
```

Then load it using **File → Load from CSV**

---

## Understanding the Grades

### How Grades Are Calculated

```
Final Grade = (Assignment × 0.20) + (Quiz × 0.20) + 
              (Midterm × 0.30) + (Final × 0.30)
```

**Example for Alice Johnson:**
- Assignment: 95 × 0.20 = 19.00
- Quiz: 88 × 0.20 = 17.60
- Midterm: 92 × 0.30 = 27.60
- Final: 94 × 0.30 = 28.20
- **Total: 92.40** ✅ Great!

### Letter Grades

| Final Grade | Letter | GPA |
|-------------|--------|-----|
| 92.40 | A | 4.0 |
| 80.25 | B | 3.0 |
| 75.15 | C | 2.0 |
| 70.00 | D | 1.0 |
| <60 | F | 0.0 |

---

## Common Tasks

### Delete a Student
1. **Select the student** in the table
2. **Click "✕ Delete"** button
3. **Click "Yes"** in the confirmation dialog
4. Student is removed

### Delete All and Start Fresh
1. Delete students one by one, OR
2. **File → Load from CSV** (with empty or different file)

### Add Grades for Many Students
Use **File → Load from CSV** with a pre-made CSV file instead of manually adding each student.

---

## Troubleshooting Quick Fixes

| Problem | Solution |
|---------|----------|
| Nothing happens when I click buttons | Click the "Refresh" button |
| Table is empty but I added students | Did you close a dialog? Check for error messages |
| CSV file won't load | Make sure it's in format: Name,Assignment,Quiz,Midterm,Final |
| Can't find undo button | It's in the **Edit** menu |
| Numbers look wrong in table | Make sure grades are 0-100 |

---

## Next Steps

1. **Read the full README.md** for detailed information
2. **Explore ARCHITECTURE.md** to understand how the code is organized
3. **Review the source code** comments in the src/ folder
4. **Try the advanced features** like autosave and statistics export

---

## Tips for Best Results

✅ **Valid Grade Ranges**: Always use 0-100 for all grade components  
✅ **Clear Names**: Use full names like "John Smith" (not "JS")  
✅ **Regular Saves**: Use File → Save to CSV frequently  
✅ **Check Statistics**: Monitor class performance in the statistics panel  
✅ **Use Search**: Find students quickly by name  

---

## Demo Data Script

Want to quickly populate with 10 sample students? Create **students_demo.csv**:

```
Name,Assignment,Quiz,Midterm,Final
Alex Martinez,92,88,94,91
Bella Chen,85,84,86,87
Carlos Rodriguez,78,80,76,79
Diana Prince,95,97,96,98
Ethan Hunt,70,72,68,71
Fiona Green,88,90,87,89
Gabriel White,82,81,83,84
Hannah Black,93,92,95,94
Isaac Newton,91,93,92,90
Julia Roberts,79,78,80,81
```

Then load with **File → Load from CSV**

---

## Keyboard Shortcuts Summary

| Shortcut | Action |
|----------|--------|
| Ctrl+Z | Undo last action |
| Ctrl+Y | Redo last action |
| Tab | Move to next field in dialog |
| Enter | Submit dialog |
| Escape | Cancel dialog |
| Click Column Header | Sort by that column |

---

**Congratulations!** You now have a fully functional Student Grade Tracker! 🎓

For more help, see **README.md** in the project folder.
