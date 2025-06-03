@echo off
echo ===== FIXING TOURNAMENT API =====
echo Building project...
call gradlew clean build -x test
if %ERRORLEVEL% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo ===== BUILD SUCCESSFUL =====
echo Starting application...
call gradlew bootRun
