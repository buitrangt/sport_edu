@echo off
echo ===== FIXING CONSTRUCTOR ERROR =====
echo Building project...
cd /d "C:\Users\ACER\Desktop\be"
call gradlew clean build -x test
if %ERRORLEVEL% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo ===== BUILD SUCCESSFUL =====
echo Starting application...
call gradlew bootRun
