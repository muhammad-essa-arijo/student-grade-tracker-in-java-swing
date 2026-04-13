@echo off
REM Student Grade Tracker - Windows Build Script
REM This script compiles all Java source files and prepares the application

echo ========================================
echo Student Grade Tracker - Build Script
echo ========================================
echo.

REM Check if bin directory exists, create if not
if not exist "bin" (
    echo Creating bin directory...
    mkdir bin
)

REM Compile all Java files
echo Compiling Java source files...
javac -d bin src\*.java src\model\*.java src\view\*.java src\controller\*.java src\util\*.java

if errorlevel 1 (
    echo.
    echo ERROR: Compilation failed!
    echo Please check your Java installation and source files.
    pause
    exit /b 1
)

REM Copy resources to bin
echo Copying resource files...
if not exist "bin\resources" (
    mkdir bin\resources
)
xcopy resources bin\resources /E /I /Y > nul

if errorlevel 1 (
    echo WARNING: Could not copy all resource files, some features may not work
)

echo.
echo ========================================
echo Build completed successfully!
echo.
echo To run the application, use: run.bat
echo ========================================
pause
