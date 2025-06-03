@echo off
echo ===== QUICK BUILD AND TEST =====
echo Stopping any running process on port 8080...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8080" ^| find "LISTENING"') do taskkill /f /pid %%a 2>nul

echo Building project...
call gradlew clean build -x test --info
if %ERRORLEVEL% neq 0 (
    echo Build failed! Check the output above.
    pause
    exit /b 1
)

echo ===== BUILD SUCCESSFUL =====
echo Starting application on port 8080...
start cmd /k "call gradlew bootRun"

echo ===== APPLICATION STARTING =====
echo Wait for 10 seconds for the app to start...
timeout /t 10 /nobreak

echo Testing tournament endpoint...
curl -X GET http://localhost:8080/api/tournaments
echo.
echo.
echo ===== READY FOR TESTING =====
echo Backend is running on http://localhost:8080
echo You can now test the frontend!
pause
