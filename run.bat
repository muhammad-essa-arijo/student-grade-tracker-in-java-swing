@echo off
REM Student Grade Tracker - Windows Run Script

echo ========================================
echo Student Grade Tracker
echo Starting application...
echo ========================================
echo.

REM Check if bin directory exists
if not exist "bin" (
    echo ERROR: bin directory not found!
    echo Please run build.bat first.
    pause
    exit /b 1
)

REM Run the application
cd bin
java StudentGradeTrackerApp
cd ..

pause
